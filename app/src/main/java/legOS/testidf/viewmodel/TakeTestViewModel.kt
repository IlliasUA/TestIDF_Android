package legOS.testidf.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.example.quizapp.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.delay
import legOS.testidf.data.UserSession
import legOS.testidf.data.UserSession.groupId
import java.util.UUID

data class TakeTestUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val questions: List<Question> = emptyList(),
    val timeLimit: Int = 15
)

class TakeTestViewModel(private val sessionId: String) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(TakeTestUiState())
    val uiState: StateFlow<TakeTestUiState> = _uiState

    private val _currentQuestionIndex = mutableIntStateOf(0)
    val currentQuestionIndex: State<Int> = _currentQuestionIndex

    private val _timeRemaining = mutableIntStateOf(0)
    val timeRemaining: State<Int> = _timeRemaining

    private val _answers = mutableStateListOf<String?>()
    val answers: List<String?> = _answers

    private val _showQuitConfirmation = mutableStateOf(false)
    val showQuitConfirmation: State<Boolean> = _showQuitConfirmation

    private val _isSubmitting = mutableStateOf(false)
    val isSubmitting: State<Boolean> = _isSubmitting

    private val _submitResultFlow = MutableSharedFlow<Boolean>()
    val submitResultFlow: SharedFlow<Boolean> = _submitResultFlow

    private var resultListener: ListenerRegistration? = null

    fun loadTestQuestions() {
        viewModelScope.launch {
            _uiState.value = TakeTestUiState(isLoading = true)

            try {
                val sessionDoc = firestore.collection("test_sessions")
                    .document(sessionId)
                    .get()
                    .await()

                val questionRefs = sessionDoc.get("questionRefs") as? List<Map<String, Any>> ?: emptyList()
                val timeLimit = (sessionDoc.getLong("timeLimit") ?: 15).toInt()

                val questions = questionRefs.mapNotNull { ref ->
                    val name = ref["name"] as? String ?: return@mapNotNull null
                    val category = ref["category"] as? String ?: return@mapNotNull null
                    val imagePath = ref["imagePath"] as? String

                    Log.d("TakeTestVM", "=== Processing question ===")
                    Log.d("TakeTestVM", "Name: $name")
                    Log.d("TakeTestVM", "Category: $category")
                    Log.d("TakeTestVM", "ImagePath from server: $imagePath")

                    val question = findQuestionByNameAndCategory(name, category)

                    if (question == null) {
                        Log.e("TakeTestVM", "Question NOT FOUND for name=$name, category=$category")
                        return@mapNotNull null
                    }

                    Log.d("TakeTestVM", "Original question.image: ${question.image}")

                    // ИСПРАВЛЕНИЕ: Добавляем папку категории к пути с сервера
                    val finalQuestion = if (imagePath != null && imagePath.isNotEmpty()) {
                        val imageFolder = getImageFolder(category)
                        val fullPath = "$imageFolder/$imagePath"
                        Log.d("TakeTestVM", "Server imagePath: $imagePath")
                        Log.d("TakeTestVM", "Image folder: $imageFolder")
                        Log.d("TakeTestVM", "Full path: $fullPath")
                        question.copy(image = fullPath)
                    } else {
                        Log.d("TakeTestVM", "Using original question.image: ${question.image}")
                        question
                    }

                    Log.d("TakeTestVM", "Final question.image: ${finalQuestion.image}")
                    Log.d("TakeTestVM", "========================")

                    finalQuestion
                }

                _uiState.value = TakeTestUiState(
                    questions = questions.shuffled(),
                    timeLimit = timeLimit
                )

                _answers.clear()
                _answers.addAll(MutableList(questions.size) { null })
                _timeRemaining.intValue = timeLimit

                Log.d("TakeTestVM", "Loaded ${questions.size} questions for session $sessionId")
                questions.forEachIndexed { index, q ->
                    Log.d("TakeTestVM", "  $index. ${q.correct} - ${q.image}")
                }

                startTimer()
            } catch (e: Exception) {
                Log.e("TakeTestVM", "Error loading questions", e)
                _uiState.value = TakeTestUiState(
                    error = "Erreur de chargement: ${e.message}"
                )
            }
        }
    }

    fun selectAnswer(answer: String) {
        if (_isSubmitting.value) return
        _answers[_currentQuestionIndex.intValue] = answer
        Log.d("TakeTestVM", "User answer: $answer at index ${_currentQuestionIndex.intValue}")
        if (_currentQuestionIndex.intValue < _uiState.value.questions.size - 1) {
            _currentQuestionIndex.intValue++
            _timeRemaining.intValue = _uiState.value.timeLimit
        } else {
            submitTestResults()
        }
    }

    fun startTimer() {
        viewModelScope.launch {
            while (_timeRemaining.intValue > 0 && !_isSubmitting.value && _currentQuestionIndex.intValue < _uiState.value.questions.size) {
                kotlinx.coroutines.delay(1000L)
                _timeRemaining.intValue--
                if (_timeRemaining.intValue <= 0) {
                    _answers[_currentQuestionIndex.intValue] = null
                    Log.d("TakeTestVM", "Auto answer: null at index ${_currentQuestionIndex.intValue}")
                    if (_currentQuestionIndex.intValue < _uiState.value.questions.size - 1) {
                        _currentQuestionIndex.intValue++
                        _timeRemaining.intValue = _uiState.value.timeLimit
                    } else {
                        submitTestResults()
                    }
                }
            }
        }
    }

    fun submitTestResults() {
        if (_isSubmitting.value) return
        _isSubmitting.value = true
        val resultId = UUID.randomUUID().toString()
        Log.d("TakeTestVM", "Starting submission for resultId: $resultId, sessionId: $sessionId")

        viewModelScope.launch {
            try {
                val userId = UserSession.userId ?: throw Exception("User not logged in")
                val userName = UserSession.userName ?: "Anonyme"

                val questions = _uiState.value.questions
                var score = 0

                val detailedAnswers = questions.mapIndexed { index, question ->
                    val userAnswer = _answers.getOrNull(index) ?: "Aucune réponse"
                    val correctAnswer = question.correct
                    val isCorrect = userAnswer == correctAnswer

                    if (isCorrect) score++

                    hashMapOf(
                        "questionText" to correctAnswer,
                        "userAnswer" to userAnswer,
                        "correctAnswer" to correctAnswer,
                        "isCorrect" to isCorrect
                    )
                }

                Log.d("TakeTestVM", "Prepared ${detailedAnswers.size} detailed answers, score: $score")

                val resultData = hashMapOf(
                    "resultId" to resultId,
                    "sessionId" to sessionId,
                    "groupId" to groupId,
                    "participantId" to userId,
                    "participantName" to userName,
                    "answers" to detailedAnswers,
                    "score" to score,
                    "totalQuestions" to questions.size,
                    "completedAt" to Timestamp.now(),
                    "timeSpent" to 0
                )

                firestore.collection("test_results")
                    .document(resultId)
                    .set(resultData)
                    .addOnSuccessListener {
                        Log.d("TakeTestVM", "Local write success, starting server sync monitor")
                        monitorServerSync(resultId)
                    }
                    .addOnFailureListener { e ->
                        Log.e("TakeTestVM", "Write failure", e)
                        _isSubmitting.value = false
                        _submitResultFlow.tryEmit(false)
                    }
            } catch (e: Exception) {
                Log.e("TakeTestVM", "Error preparing submission", e)
                _isSubmitting.value = false
                _submitResultFlow.tryEmit(false)
            }
        }
    }

    private fun monitorServerSync(resultId: String) {
        resultListener = firestore.collection("test_results")
            .document(resultId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("TakeTestVM", "Sync listener error", error)
                    viewModelScope.launch {
                        delay(5000L)
                        if (_isSubmitting.value) {
                            Log.d("TakeTestVM", "Retrying sync...")
                        }
                    }
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("TakeTestVM", "Server sync confirmed for $resultId")
                    resultListener?.remove()
                    _isSubmitting.value = false
                    viewModelScope.launch {
                        try {
                            val userId = UserSession.userId ?: return@launch
                            firestore.collection("notifications")
                                .whereEqualTo("sessionId", sessionId)
                                .whereEqualTo("recipientId", userId)
                                .get()
                                .await()
                                .documents
                                .forEach { doc ->
                                    doc.reference.update("isRead", true).await()
                                }
                            _submitResultFlow.emit(true)
                        } catch (e: Exception) {
                            Log.e("TakeTestVM", "Error updating notifications", e)
                            _submitResultFlow.emit(false)
                        }
                    }
                }
            }
    }

    fun showQuitDialog(show: Boolean) {
        _showQuitConfirmation.value = show
    }

    override fun onCleared() {
        super.onCleared()
        resultListener?.remove()
    }

    private fun findQuestionByNameAndCategory(name: String, category: String): Question? {
        return when (category) {
            "Chars" -> Test_Data.QUESTION.find { it.correct == name }
            "Artillerie" -> Art_Data.QUESTION.find { it.correct == name }
            "Aviation" -> Air_Data.QUESTION.find { it.correct == name }
            "Génie" -> Genie_Data.QUESTION.find { it.correct == name }
            "Reconnaissance" -> Recon_Data.QUESTION.find { it.correct == name }
            "Militaire" -> Test_bm2.QUESTION.find { it.correct == name }
            else -> null
        }
    }

    private fun getImageFolder(category: String): String {
        return when (category) {
            "Chars" -> "tank_images"
            "Artillerie" -> "artillery_images"
            "Aviation" -> "air_images"
            "Génie" -> "genie_images"
            "Reconnaissance" -> "recon_images"
            "Militaire" -> "bm2_images"
            else -> "tank_images"
        }
    }
}