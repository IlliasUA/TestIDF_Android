package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession

data class Participant(
    val id: String = "",
    val name: String = ""
)

data class SendTestUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val participants: List<Participant> = emptyList(),
    val groupCode: String = "",
    val isSending: Boolean = false,
    val testSent: Boolean = false,
    val completedTestIds: Set<String> = emptySet(),
    val lastSentTestId: String? = null // ДОБАВЛЕНО
)

class SendTestViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private var groupListener: ListenerRegistration? = null
    private var resultsListener: ListenerRegistration? = null

    private val _uiState = MutableStateFlow(SendTestUiState())
    val uiState: StateFlow<SendTestUiState> = _uiState

    fun loadSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val groupId = UserSession.groupId

                if (groupId != null) {
                    val groupDoc = firestore.collection("groups")
                        .document(groupId)
                        .get()
                        .await()

                    val groupCode = groupDoc.getString("groupCode") ?: ""

                    // ДОБАВЛЕНО: Загружаем последний отправленный тест
                    val lastSentTestId = groupDoc.getString("lastSentTestId")

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        groupCode = groupCode,
                        lastSentTestId = lastSentTestId
                    )

                    startListeningToGroup(groupId)

                    startListeningToResults(groupId)

                    Log.d("SendTestVM", "Last sent test: $lastSentTestId")
                } else {
                    Log.e("SendTestVM", "GroupId is null")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Groupe non trouvé"
                    )
                }
            } catch (e: Exception) {
                Log.e("SendTestVM", "Error loading session", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erreur: ${e.message}"
                )
            }
        }
    }

    private fun startListeningToGroup(groupId: String) {
        groupListener?.remove()

        groupListener = firestore.collection("groups")
            .document(groupId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SendTestVM", "Error listening to group", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val participantIds = snapshot.get("participantIds") as? List<*> ?: emptyList<Any>()

                    // ДОБАВЛЕНО: Отслеживаем изменения lastSentTestId
                    val lastSentTestId = snapshot.getString("lastSentTestId")
                    val currentLastSent = _uiState.value.lastSentTestId

                    if (lastSentTestId != currentLastSent) {
                        Log.d("SendTestVM", "🔄 Last sent test changed: $currentLastSent -> $lastSentTestId")
                        _uiState.value = _uiState.value.copy(lastSentTestId = lastSentTestId)

                    }

                    Log.d("SendTestVM", "🔄 Group updated, participants: ${participantIds.size}")
                    loadParticipants(participantIds)
                }
            }
    }

    private fun startListeningToResults(groupId: String) {
        resultsListener?.remove()

        Log.d("SendTestVM", "📊 Listening to completed tests for group: $groupId")

        resultsListener = firestore.collection("test_results")
            .whereEqualTo("groupId", groupId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SendTestVM", "Error listening to results", error)
                    return@addSnapshotListener
                }

                val completedTestIds = snapshot?.documents
                    ?.mapNotNull { it.getString("sessionId") }
                    ?.toSet()
                    .orEmpty()
                Log.d("SendTestVM", "📊 Tests with results: ${completedTestIds.size}")

                _uiState.value = _uiState.value.copy(
                    completedTestIds = completedTestIds
                )
            }
    }

    private fun loadParticipants(participantIds: List<*>) {
        viewModelScope.launch {
            try {
                val participants = mutableListOf<Participant>()

                participantIds.forEach { participantId ->
                    try {
                        val userDoc = firestore.collection("users")
                            .document(participantId.toString())
                            .get()
                            .await()

                        val name = userDoc.getString("name") ?: "Anonyme"
                        participants.add(
                            Participant(
                                id = participantId.toString(),
                                name = name
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("SendTestVM", "Error loading participant $participantId", e)
                    }
                }

                Log.d("SendTestVM", "✅ Loaded ${participants.size} participants")

                _uiState.value = _uiState.value.copy(
                    participants = participants
                )
            } catch (e: Exception) {
                Log.e("SendTestVM", "Error loading participants", e)
            }
        }
    }

    fun sendTestToParticipants(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)

            Log.d("SendTestVM", "========================================")
            Log.d("SendTestVM", "🚀 Starting to send test: $sessionId")

            try {
                val participants = _uiState.value.participants
                val groupId = UserSession.groupId

                Log.d("SendTestVM", "Participants count: ${participants.size}")
                Log.d("SendTestVM", "Group ID: $groupId")

                if (participants.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        error = "Aucun participant dans le groupe"
                    )
                    return@launch
                }

                if (groupId == null) {
                    _uiState.value = _uiState.value.copy(
                        isSending = false,
                        error = "Groupe non trouvé"
                    )
                    return@launch
                }

                val sessionDoc = firestore.collection("test_sessions")
                    .document(sessionId)
                    .get()
                    .await()

                val testTitle = sessionDoc.getString("title") ?: "Test"
                Log.d("SendTestVM", "Test title: $testTitle")

                // Деактивируем старые уведомления
                val previousTestId = _uiState.value.lastSentTestId
                Log.d("SendTestVM", "Previous test ID: $previousTestId")
                Log.d("SendTestVM", "Current test ID: $sessionId")

                if (previousTestId != null && previousTestId != sessionId) {
                    Log.d("SendTestVM", "⚠️ Deactivating previous test: $previousTestId")

                    val oldNotifications = firestore.collection("notifications")
                        .whereEqualTo("sessionId", previousTestId)
                        .whereEqualTo("type", "test_invitation")
                        .get()
                        .await()

                    Log.d("SendTestVM", "Found ${oldNotifications.size()} old notifications to deactivate")

                    oldNotifications.documents.forEach { doc ->
                        Log.d("SendTestVM", "Deactivating notification: ${doc.id}")
                        doc.reference.update(
                            mapOf(
                                "isActive" to false,
                                "deactivatedAt" to com.google.firebase.Timestamp.now()
                            )
                        ).await()
                    }

                    Log.d("SendTestVM", "✅ Deactivated ${oldNotifications.size()} old notifications")
                } else {
                    Log.d("SendTestVM", "No previous test to deactivate")
                }

                // Отправляем новые уведомления
                Log.d("SendTestVM", "📨 Creating new notifications...")
                var notificationCount = 0

                participants.forEach { participant ->
                    Log.d("SendTestVM", "Creating notification for: ${participant.name} (${participant.id})")

                    val notificationData = hashMapOf(
                        "recipientId" to participant.id,
                        "sessionId" to sessionId,
                        "groupId" to groupId, // ДОБАВЛЕНО - теперь можно проверять через groupId
                        "title" to "Nouveau test disponible",
                        "message" to testTitle,
                        "type" to "test_invitation",
                        "isRead" to false,
                        "isActive" to true,
                        "createdAt" to com.google.firebase.Timestamp.now()
                    )

                    val docRef = firestore.collection("notifications")
                        .add(notificationData)
                        .await()

                    notificationCount++
                    Log.d("SendTestVM", "✅ Created notification: ${docRef.id}")
                }

                Log.d("SendTestVM", "✅ Created $notificationCount new notifications")

                // Обновляем lastSentTestId в группе
                Log.d("SendTestVM", "Updating group lastSentTestId...")
                firestore.collection("groups")
                    .document(groupId)
                    .update("lastSentTestId", sessionId)
                    .await()

                Log.d("SendTestVM", "✅ Updated lastSentTestId to: $sessionId")
                Log.d("SendTestVM", "✅ TEST SENT SUCCESSFULLY!")
                Log.d("SendTestVM", "========================================")

                _uiState.value = _uiState.value.copy(
                    isSending = false,
                    testSent = true,
                    successMessage = "Test envoyé à ${participants.size} participants",
                    lastSentTestId = sessionId
                )

            } catch (e: Exception) {
                Log.e("SendTestVM", "❌ ERROR sending test", e)
                Log.e("SendTestVM", "Error details: ${e.message}")
                e.printStackTrace()

                _uiState.value = _uiState.value.copy(
                    isSending = false,
                    error = "Erreur lors de l'envoi: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }

    override fun onCleared() {
        super.onCleared()
        groupListener?.remove()
        resultsListener?.remove()
        Log.d("SendTestVM", "All listeners removed")
    }
}
