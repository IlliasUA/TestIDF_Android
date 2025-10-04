package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.quizapp.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession
import java.util.UUID

data class TakeTestUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val questions: List<Question> = emptyList(),
    val timeLimit: Int = 15
)

class TakeTestViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(TakeTestUiState())
    val uiState: StateFlow<TakeTestUiState> = _uiState

    fun loadTestQuestions(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = TakeTestUiState(isLoading = true)

            try {
                val sessionDoc = firestore.collection("test_sessions")
                    .document(sessionId)
                    .get()
                    .await()

                val questionRefs = sessionDoc.get("questionRefs") as? List<Map<String, Any>> ?: emptyList()
                val timeLimit = (sessionDoc.getLong("timeLimit") ?: 15).toInt()

                // Восстанавливаем полные вопросы из локальных данных
                val questions = questionRefs.mapNotNull { ref ->
                    val name = ref["name"] as? String ?: return@mapNotNull null
                    val category = ref["category"] as? String ?: return@mapNotNull null
                    val imagePath = ref["imagePath"] as? String  // Сохраняем полный путь

                    // Находим вопрос и обновляем путь к изображению
                    val question = findQuestionByNameAndCategory(name, category)

                    // Если imagePath передан из Firebase, используем его
                    question?.copy(image = imagePath ?: question.image)
                }

                _uiState.value = TakeTestUiState(
                    questions = questions.shuffled(),
                    timeLimit = timeLimit
                )

                Log.d("TakeTestVM", "Loaded ${questions.size} questions")

            } catch (e: Exception) {
                Log.e("TakeTestVM", "Error loading questions", e)
                _uiState.value = TakeTestUiState(
                    error = "Erreur de chargement: ${e.message}"
                )
            }
        }
    }

    fun submitTestResults(
        sessionId: String,
        answers: List<String?>,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val userId = UserSession.userId ?: throw Exception("User not logged in")
                val userName = UserSession.userName ?: "Anonyme"

                // Подсчитываем результат
                val questions = _uiState.value.questions
                var score = 0
                answers.forEachIndexed { index, answer ->
                    if (answer == questions.getOrNull(index)?.correct) {
                        score++
                    }
                }

                val resultId = UUID.randomUUID().toString()
                val resultData = hashMapOf(
                    "resultId" to resultId,
                    "sessionId" to sessionId,
                    "participantId" to userId,
                    "participantName" to userName,
                    "answers" to answers,
                    "score" to score,
                    "totalQuestions" to questions.size,
                    "completedAt" to Timestamp.now(),
                    "timeSpent" to 0 // TODO: добавить отслеживание времени
                )

                firestore.collection("test_results")
                    .document(resultId)
                    .set(resultData)
                    .await()

                // Отмечаем уведомление как прочитанное
                firestore.collection("notifications")
                    .whereEqualTo("sessionId", sessionId)
                    .whereEqualTo("recipientId", userId)
                    .get()
                    .await()
                    .documents
                    .forEach { doc ->
                        doc.reference.update("isRead", true).await()
                    }

                Log.d("TakeTestVM", "Results submitted: $score/${questions.size}")
                onComplete(true)

            } catch (e: Exception) {
                Log.e("TakeTestVM", "Error submitting results", e)
                onComplete(false)
            }
        }
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
}