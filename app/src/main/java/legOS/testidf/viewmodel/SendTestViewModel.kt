package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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
    val notifiedParticipants: Set<String> = emptySet(),
    val groupCode: String = ""
)

class SendTestViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private var sessionListener: ListenerRegistration? = null

    private val _uiState = MutableStateFlow(SendTestUiState())
    val uiState: StateFlow<SendTestUiState> = _uiState

    fun loadSession(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                Log.d("SendTestVM", "Setting up real-time listener for session: $sessionId")

                // Устанавливаем слушатель на изменения документа сессии
                sessionListener = firestore.collection("test_sessions")
                    .document(sessionId)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e("SendTestVM", "Error listening to session", error)
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = "Erreur: ${error.message}"
                            )
                            return@addSnapshotListener
                        }

                        if (snapshot != null && snapshot.exists()) {
                            viewModelScope.launch {
                                processSessionUpdate(snapshot.data ?: emptyMap())
                            }
                        } else {
                            Log.e("SendTestVM", "Session document doesn't exist")
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = "Session introuvable"
                            )
                        }
                    }

            } catch (e: Exception) {
                Log.e("SendTestVM", "Error setting up session listener", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erreur de chargement: ${e.message}"
                )
            }
        }
    }

    private suspend fun processSessionUpdate(data: Map<String, Any>) {
        try {
            val title = data["title"] as? String ?: "Test"
            val questionRefs = data["questionRefs"] as? List<*> ?: emptyList<Any>()
            val timeLimit = (data["timeLimit"] as? Long ?: 15).toInt()
            val participantIds = data["participantIds"] as? List<String> ?: emptyList()
            val groupId = data["groupId"] as? String ?: ""

            Log.d("SendTestVM", "Session updated - Title: $title, Participants: ${participantIds.size}")

            // Загружаем код группы
            var groupCode = ""
            if (groupId.isNotEmpty()) {
                try {
                    val groupDoc = firestore.collection("groups")
                        .document(groupId)
                        .get()
                        .await()
                    groupCode = groupDoc.getString("groupCode") ?: ""
                    Log.d("SendTestVM", "Group code loaded: $groupCode")
                } catch (e: Exception) {
                    Log.e("SendTestVM", "Error loading group code", e)
                }
            }

            // Загружаем участников
            val participants = mutableListOf<Participant>()
            for (participantId in participantIds) {
                try {
                    val userDoc = firestore.collection("users")
                        .document(participantId)
                        .get()
                        .await()

                    if (userDoc.exists()) {
                        val participantName = userDoc.getString("name") ?: "Anonyme"
                        participants.add(
                            Participant(
                                userId = participantId,
                                name = participantName
                            )
                        )
                        Log.d("SendTestVM", "Loaded participant: $participantName ($participantId)")
                    } else {
                        Log.w("SendTestVM", "Participant document not found: $participantId")
                    }
                } catch (e: Exception) {
                    Log.e("SendTestVM", "Error loading participant $participantId", e)
                }
            }

            Log.d("SendTestVM", "Total participants loaded: ${participants.size}")

            _uiState.value = SendTestUiState(
                sessionTitle = title,
                questionCount = questionRefs.size,
                timeLimit = timeLimit,
                participants = participants,
                groupCode = groupCode,
                notifiedParticipants = _uiState.value.notifiedParticipants
            )

        } catch (e: Exception) {
            Log.e("SendTestVM", "Error processing session update", e)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Erreur: ${e.message}"
            )
        }
    }

    fun sendTestToParticipants(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)

            try {
                val participants = _uiState.value.participants

                if (participants.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        error = "Aucun participant dans la session"
                    )
                    return@launch
                }

                val notified = mutableSetOf<String>()

                // Создаем уведомления для каждого участника
                for (participant in participants) {
                    try {
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
                    } catch (e: Exception) {
                        Log.e("SendTestVM", "Error sending notification to ${participant.name}", e)
                    }
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

                Log.d("SendTestVM", "Test sent to ${notified.size} participants")

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

    override fun onCleared() {
        super.onCleared()
        sessionListener?.remove()
        Log.d("SendTestVM", "Session listener removed")
    }
}