package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.screens.ParticipantResult

data class SessionResultsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessionTitle: String = "",
    val totalQuestions: Int = 0,
    val timeLimit: Int = 0,
    val results: List<ParticipantResult> = emptyList(),
    val averageScore: Int = 0,
    val bestScore: Int = 0
)

class SessionResultsViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(SessionResultsUiState())
    val uiState: StateFlow<SessionResultsUiState> = _uiState

    fun loadResults(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = SessionResultsUiState(isLoading = true)

            try {
                // Загружаем информацию о сессии
                val sessionDoc = firestore.collection("test_sessions")
                    .document(sessionId)
                    .get()
                    .await()

                val title = sessionDoc.getString("title") ?: "Test"
                val questionRefs = sessionDoc.get("questionRefs") as? List<*> ?: emptyList<Any>()
                val timeLimit = (sessionDoc.getLong("timeLimit") ?: 15).toInt()

                // Загружаем результаты
                val resultsSnapshot = firestore.collection("test_results")
                    .whereEqualTo("sessionId", sessionId)
                    .get()
                    .await()

                val results = resultsSnapshot.documents.map { doc ->
                    val score = (doc.getLong("score") ?: 0).toInt()
                    val total = (doc.getLong("totalQuestions") ?: 1).toInt()
                    val percentage = if (total > 0) (score * 100) / total else 0

                    ParticipantResult(
                        participantId = doc.getString("participantId") ?: "",
                        participantName = doc.getString("participantName") ?: "Anonyme",
                        score = score,
                        totalQuestions = total,
                        percentage = percentage
                    )
                }.sortedByDescending { it.score }

                // Добавляем ранги
                val rankedResults = results.mapIndexed { index, result ->
                    result.copy(rank = index + 1)
                }

                // Вычисляем статистику
                val avgScore = if (results.isNotEmpty()) {
                    results.map { it.percentage }.average().toInt()
                } else 0

                val bestScore = results.maxOfOrNull { it.percentage } ?: 0

                _uiState.value = SessionResultsUiState(
                    sessionTitle = title,
                    totalQuestions = questionRefs.size,
                    timeLimit = timeLimit,
                    results = rankedResults,
                    averageScore = avgScore,
                    bestScore = bestScore
                )

                Log.d("SessionResultsVM", "Loaded ${results.size} results")

            } catch (e: Exception) {
                Log.e("SessionResultsVM", "Error loading results", e)
                _uiState.value = SessionResultsUiState(
                    error = "Erreur: ${e.message}"
                )
            }
        }
    }
}