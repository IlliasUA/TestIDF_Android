package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class Participant(
    val userId: String = "",
    val name: String = ""
)

data class SendTestUiState(
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val sessionTitle: String = "",
    val questionCount: Int = 0,
    val timeLimit: Int = 0,
    val participants: List<Participant> = emptyList(),
    val notifiedParticipants: Set<String> = emptySet()
)

class SendTestViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(SendTestUiState())
    val uiState: StateFlow<SendTestUiState> = _uiState

    fun loadSession(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Загружаем сессию
                val sessionDoc = firestore.collection("test_sessions")
                    .document(sessionId)
                    .get()
                    .await()

                val title = sessionDoc.getString("title") ?: "Test"
                val questionRefs = sessionDoc.get("questionRefs") as? List<*> ?: emptyList<Any>()
                val timeLimit = (sessionDoc.getLong("timeLimit") ?: 15).toInt()
                val participantIds = sessionDoc.get("participantIds") as? List<String> ?: emptyList()

                // Загружаем данные участников
                val participants = mutableListOf<Participant>()
                for (participantId in participantIds) {
                    val userDoc = firestore.collection("users")
                        .document(participantId)
                        .get()
                        .await()

                    if (userDoc.exists()) {
                        participants.add(
                            Participant(
                                userId = participantId,
                                name = userDoc.getString("name") ?: "Anonyme"
                            )
                        )
                    }
                }

                _uiState.value = SendTestUiState(
                    sessionTitle = title,
                    questionCount = questionRefs.size,
                    timeLimit = timeLimit,
                    participants = participants
                )

                Log.d("SendTestVM", "Session loaded: $title, ${participants.size} participants")

            } catch (e: Exception) {
                Log.e("SendTestVM", "Error loading session", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erreur de chargement: ${e.message}"
                )
            }
        }
    }

    fun sendTestToParticipants(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)

            try {
                val participants = _uiState.value.participants
                val notified = mutableSetOf<String>()

                // Создаем уведомления для каждого участника
                for (participant in participants) {
                    val notificationId = UUID.randomUUID().toString()
                    val notificationData = hashMapOf(
                        "notificationId" to notificationId,
                        "type" to "test_available",
                        "sessionId" to sessionId,
                        "recipientId" to participant.userId,
                        "title" to "Nouveau test disponible",
                        "message" to "Le chef a envoyé un nouveau test: ${_uiState.value.sessionTitle}",
                        "isRead" to false,
                        "createdAt" to Timestamp.now()
                    )

                    firestore.collection("notifications")
                        .document(notificationId)
                        .set(notificationData)
                        .await()

                    notified.add(participant.userId)
                    Log.d("SendTestVM", "Notification sent to ${participant.name}")
                }

                // Обновляем статус сессии
                firestore.collection("test_sessions")
                    .document(sessionId)
                    .update("status", "active")
                    .await()

                _uiState.value = _uiState.value.copy(
                    isSending = false,
                    notifiedParticipants = notified,
                    successMessage = "Test envoyé à ${notified.size} participant(s)!"
                )

            } catch (e: Exception) {
                Log.e("SendTestVM", "Error sending test", e)
                _uiState.value = _uiState.value.copy(
                    isSending = false,
                    error = "Erreur d'envoi: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}