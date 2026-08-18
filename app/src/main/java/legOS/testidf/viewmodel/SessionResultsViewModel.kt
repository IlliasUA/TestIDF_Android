package legOS.testidf.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.R
import legOS.testidf.screens.Answer
import legOS.testidf.screens.ParticipantResult
import legOS.testidf.screens.SessionResultGroup
import java.util.*

data class SessionResultsUiState(
    val isLoading: Boolean = false,
    val sessionTitle: String = "",
    val totalQuestions: Int = 0,
    val timeLimit: Int = 15,
    val sessionGroups: List<SessionResultGroup> = emptyList()
)

class SessionResultsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(SessionResultsUiState())
    val uiState: StateFlow<SessionResultsUiState> = _uiState

    fun loadResults(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                Log.d("SessionResultsVM", "=== Loading results for session: $sessionId ===")

                // Загружаем информацию о сессии
                val sessionDoc = firestore.collection("test_sessions")
                    .document(sessionId)
                    .get()
                    .await()

                val sessionTitle = sessionDoc.getString("title") ?: "Test"
                val questionRefs = sessionDoc.get("questionRefs") as? List<*> ?: emptyList<Any>()
                val timeLimit = sessionDoc.getLong("timeLimit")?.toInt() ?: 15

                Log.d("SessionResultsVM", "Session: $sessionTitle, Questions: ${questionRefs.size}")

                // Загружаем ВСЕ результаты для этой сессии
                val resultsSnapshot = firestore.collection("test_results")
                    .whereEqualTo("sessionId", sessionId)
                    .get()
                    .await()

                Log.d("SessionResultsVM", "Total documents found: ${resultsSnapshot.documents.size}")

                // Парсим результаты с временными метками
                val resultsWithTimestamps = resultsSnapshot.documents.mapNotNull { doc ->
                    try {
                        val participantId = doc.getString("participantId") ?: return@mapNotNull null
                        val participantName = doc.getString("participantName") ?: "Inconnu"
                        val score = doc.getLong("score")?.toInt() ?: 0
                        val totalQuestions = doc.getLong("totalQuestions")?.toInt() ?: questionRefs.size
                        val completedAtTimestamp = doc.getTimestamp("completedAt")

                        if (completedAtTimestamp == null) {
                            Log.w("SessionResultsVM", "Document ${doc.id} has no completedAt timestamp")
                            return@mapNotNull null
                        }

                        val completedAtMillis = completedAtTimestamp.seconds * 1000

                        Log.d("SessionResultsVM", "Result: $participantName at ${Date(completedAtMillis)}")

                        val answers = (doc.get("answers") as? List<Map<String, Any>>)?.map { answerMap ->
                            Answer(
                                questionText = answerMap["questionText"] as? String ?: "",
                                userAnswer = answerMap["userAnswer"] as? String ?: "",
                                correctAnswer = answerMap["correctAnswer"] as? String ?: "",
                                isCorrect = answerMap["isCorrect"] as? Boolean ?: false
                            )
                        } ?: emptyList()

                        val result = ParticipantResult(
                            participantId = participantId,
                            participantName = participantName,
                            score = score,
                            totalQuestions = totalQuestions,
                            percentage = if (totalQuestions > 0) (score * 100) / totalQuestions else 0,
                            answers = answers
                        )

                        Triple(completedAtMillis, result, doc.id)
                    } catch (e: Exception) {
                        Log.e("SessionResultsVM", "Error parsing document ${doc.id}", e)
                        null
                    }
                }

                Log.d("SessionResultsVM", "Successfully parsed: ${resultsWithTimestamps.size} results")

                // Every completion belongs to this test, regardless of when a participant
                // finished. Build one leaderboard and use completion time as a tie-breaker.
                val sortedResults = resultsWithTimestamps
                    .sortedWith(
                        compareByDescending<Triple<Long, ParticipantResult, String>> { it.second.percentage }
                            .thenBy { it.first }
                            .thenBy { it.second.participantName }
                    )
                    .mapIndexed { index, (_, result, _) -> result.copy(rank = index + 1) }

                val sessionGroups = if (sortedResults.isEmpty()) {
                    emptyList()
                } else {
                    listOf(
                        SessionResultGroup(
                            results = sortedResults,
                            averageScore = sortedResults.map { it.percentage }.average().toInt(),
                            bestScore = sortedResults.first().percentage
                        )
                    )
                }

                Log.d("SessionResultsVM", "=== Final result: ${sessionGroups.size} groups ===")
                sessionGroups.forEachIndexed { index, group ->
                    Log.d("SessionResultsVM", "Group ${index + 1}: ${group.timestamp}, ${group.results.size} participants")
                }

                _uiState.value = SessionResultsUiState(
                    isLoading = false,
                    sessionTitle = sessionTitle,
                    totalQuestions = questionRefs.size,
                    timeLimit = timeLimit,
                    sessionGroups = sessionGroups
                )

                Log.d("SessionResultsVM", "✅ State updated with ${sessionGroups.size} session groups")

            } catch (e: Exception) {
                Log.e("SessionResultsVM", "❌ Error loading results", e)
                _uiState.value = SessionResultsUiState(
                    isLoading = false,
                    sessionGroups = emptyList()
                )
            }
        }
    }
}
