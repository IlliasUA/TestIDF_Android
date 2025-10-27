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
import java.text.SimpleDateFormat
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

                // Группируем по временным интервалам (30 секунд)
                val groupedByTimeWindow = mutableMapOf<Long, MutableList<ParticipantResult>>()

                resultsWithTimestamps.forEach { (timestamp, result, docId) ->
                    // Округляем до 30-секундного окна
                    val timeWindow = (timestamp / 30000) * 30000

                    Log.d("SessionResultsVM", "Mapping $docId to window: ${Date(timeWindow)}")

                    if (!groupedByTimeWindow.containsKey(timeWindow)) {
                        groupedByTimeWindow[timeWindow] = mutableListOf()
                    }
                    groupedByTimeWindow[timeWindow]!!.add(result)
                }

                Log.d("SessionResultsVM", "Created ${groupedByTimeWindow.size} time windows")

                // Создаем группы сессий
                val sessionGroups = groupedByTimeWindow.entries
                    .sortedBy { it.key }
                    .map { (timeWindow, participants) ->
                        Log.d("SessionResultsVM", "Processing window ${Date(timeWindow)} with ${participants.size} participants")

                        // Сортируем по баллам и присваиваем ранги
                        val sortedResults = participants
                            .sortedByDescending { it.percentage }
                            .mapIndexed { index, result ->
                                result.copy(rank = index + 1)
                            }

                        val averageScore = if (sortedResults.isNotEmpty()) {
                            sortedResults.map { it.percentage }.average().toInt()
                        } else 0

                        val bestScore = sortedResults.firstOrNull()?.percentage ?: 0

                        val adjustedTime = timeWindow + (2 * 60 * 60 * 1000) // +2 часа
                        val timeString = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                            .format(Date(adjustedTime))

                        Log.d("SessionResultsVM", "Created group: $timeString, ${sortedResults.size} participants, avg: $averageScore%, best: $bestScore%")

                        SessionResultGroup(
                            timestamp = timeString,
                            results = sortedResults,
                            averageScore = averageScore,
                            bestScore = bestScore
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