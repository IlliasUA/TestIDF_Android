package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession

data class ParticipantRegistrationUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class ParticipantRegistrationViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(ParticipantRegistrationUiState())
    val uiState: StateFlow<ParticipantRegistrationUiState> = _uiState

    fun joinGroup(
        groupCode: String,
        participantName: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                Log.d("ParticipantRegistrationVM", "==============================================")
                Log.d("ParticipantRegistrationVM", "🔐 Attempting to join group with code: $groupCode")

                // ИСПРАВЛЕНИЕ: Проверяем аутентификацию и создаем пользователя если нужно
                var currentUserId = auth.currentUser?.uid

                if (currentUserId == null) {
                    Log.d("ParticipantRegistrationVM", "⚠️ User not authenticated - creating anonymous user")

                    try {
                        val authResult = auth.signInAnonymously().await()
                        currentUserId = authResult.user?.uid

                        Log.d("ParticipantRegistrationVM", "✅ Anonymous user created: $currentUserId")

                        // Создаем документ пользователя в Firestore
                        if (currentUserId != null) {
                            val userData = hashMapOf(
                                "name" to participantName,
                                "role" to "participant",
                                "createdAt" to com.google.firebase.Timestamp.now()
                            )

                            firestore.collection("users")
                                .document(currentUserId)
                                .set(userData)
                                .await()

                            Log.d("ParticipantRegistrationVM", "✅ User document created in Firestore")
                        }
                    } catch (e: Exception) {
                        Log.e("ParticipantRegistrationVM", "❌ Failed to create anonymous user", e)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Erreur d'authentification: ${e.message}"
                        )
                        onComplete(false, null)
                        return@launch
                    }
                }

                if (currentUserId == null) {
                    Log.e("ParticipantRegistrationVM", "❌ User ID is still null after authentication")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Impossible de créer un utilisateur"
                    )
                    onComplete(false, null)
                    return@launch
                }

                Log.d("ParticipantRegistrationVM", "Current user ID: $currentUserId")

                // Обновляем имя пользователя в Firestore (на случай если документ уже существует)
                try {
                    firestore.collection("users")
                        .document(currentUserId)
                        .update("name", participantName)
                        .await()

                    Log.d("ParticipantRegistrationVM", "✅ User name updated to: $participantName")
                } catch (e: Exception) {
                    // Если документ не существует, создаем его
                    Log.w("ParticipantRegistrationVM", "⚠️ User document doesn't exist, creating...")

                    val userData = hashMapOf(
                        "name" to participantName,
                        "role" to "participant",
                        "createdAt" to com.google.firebase.Timestamp.now()
                    )

                    firestore.collection("users")
                        .document(currentUserId)
                        .set(userData)
                        .await()

                    Log.d("ParticipantRegistrationVM", "✅ User document created")
                }

                // Ищем группу по коду
                val groupQuery = firestore.collection("groups")
                    .whereEqualTo("groupCode", groupCode)
                    .limit(1)
                    .get()
                    .await()

                if (groupQuery.isEmpty) {
                    Log.w("ParticipantRegistrationVM", "⚠️ Group not found with code: $groupCode")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Code de groupe invalide"
                    )
                    onComplete(false, null)
                    return@launch
                }

                val groupDoc = groupQuery.documents[0]
                val groupId = groupDoc.id
                val groupName = groupDoc.getString("name") ?: "Groupe"

                if (groupDoc.getBoolean("isActive") == false) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Ce groupe n'est plus actif"
                    )
                    onComplete(false, null)
                    return@launch
                }

                Log.d("ParticipantRegistrationVM", "✅ Found group: $groupId")
                Log.d("ParticipantRegistrationVM", "Group name: $groupName")

                // The transaction retries if the admin closes the group at the
                // same moment. arrayUnion also prevents concurrent joins from
                // overwriting each other's participant IDs.
                val groupReference = firestore.collection("groups").document(groupId)
                firestore.runTransaction { transaction ->
                    val currentGroup = transaction.get(groupReference)
                    check(currentGroup.exists() && currentGroup.getBoolean("isActive") != false) {
                        "Ce groupe n'est plus actif"
                    }
                    transaction.update(
                        groupReference,
                        "participantIds",
                        FieldValue.arrayUnion(currentUserId)
                    )
                }.await()

                Log.d("ParticipantRegistrationVM", "✅ Participant membership confirmed")

                UserSession.setParticipantSession(
                    userId = currentUserId,
                    name = participantName,
                    email = "",
                    groupId = groupId
                )

                Log.d("ParticipantRegistrationVM", "==============================================")
                Log.d("ParticipantRegistrationVM", "✅ UserSession updated:")
                Log.d("ParticipantRegistrationVM", "  userId = ${UserSession.userId}")
                Log.d("ParticipantRegistrationVM", "  userName = ${UserSession.userName}")
                Log.d("ParticipantRegistrationVM", "  userRole = ${UserSession.userRole}")
                Log.d("ParticipantRegistrationVM", "  groupId = ${UserSession.groupId}")
                Log.d("ParticipantRegistrationVM", "==============================================")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null,
                    successMessage = "Vous avez rejoint $groupName!"
                )

                onComplete(true, groupId)

            } catch (e: Exception) {
                Log.e("ParticipantRegistrationVM", "❌ Error joining group", e)
                Log.e("ParticipantRegistrationVM", "Error type: ${e.javaClass.simpleName}")
                Log.e("ParticipantRegistrationVM", "Error message: ${e.message}")
                e.printStackTrace()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erreur: ${e.message}"
                )
                onComplete(false, null)
            }
        }
    }

    // Выход из группы
    fun leaveGroup(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val groupId = UserSession.groupId
                val userId = UserSession.userId

                Log.d("ParticipantRegistrationVM", "==============================================")
                Log.d("ParticipantRegistrationVM", "🚪 Attempting to leave group")
                Log.d("ParticipantRegistrationVM", "  userId = $userId")
                Log.d("ParticipantRegistrationVM", "  groupId = $groupId")

                if (groupId != null && userId != null) {
                    // arrayRemove is atomic and safe even when the ID is absent.
                    firestore.collection("groups")
                        .document(groupId)
                        .update("participantIds", FieldValue.arrayRemove(userId))
                        .await()

                    Log.d("ParticipantRegistrationVM", "✅ Successfully removed from group")

                    // Очищаем локальные данные в любом случае
                    UserSession.clearGroupData()
                    Log.d("ParticipantRegistrationVM", "✅ Local session cleared")
                } else {
                    Log.w("ParticipantRegistrationVM", "⚠️ groupId or userId is null")
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )

                Log.d("ParticipantRegistrationVM", "✅ Leave group completed")
                Log.d("ParticipantRegistrationVM", "==============================================")

                onComplete(true)

            } catch (e: Exception) {
                Log.e("ParticipantRegistrationVM", "❌ Error leaving group", e)
                Log.e("ParticipantRegistrationVM", "Error type: ${e.javaClass.simpleName}")
                Log.e("ParticipantRegistrationVM", "Error message: ${e.message}")

                // Даже при ошибке очищаем данные и считаем успехом
                UserSession.clearGroupData()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )

                Log.d("ParticipantRegistrationVM", "Treated as success - group likely deleted")
                onComplete(true)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}
