package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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

                // Проверяем существование пользователя
                val currentUserId = auth.currentUser?.uid
                if (currentUserId == null) {
                    Log.e("ParticipantRegistrationVM", "❌ User not authenticated")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Utilisateur non authentifié"
                    )
                    onComplete(false, null)
                    return@launch
                }

                Log.d("ParticipantRegistrationVM", "Current user ID: $currentUserId")

                // Обновляем имя пользователя в Firestore
                firestore.collection("users")
                    .document(currentUserId)
                    .update("name", participantName)
                    .await()

                Log.d("ParticipantRegistrationVM", "✅ User name updated to: $participantName")

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
                val participantIds = groupDoc.get("participantIds") as? MutableList<String>
                    ?: mutableListOf()

                Log.d("ParticipantRegistrationVM", "✅ Found group: $groupId")
                Log.d("ParticipantRegistrationVM", "Group name: $groupName")
                Log.d("ParticipantRegistrationVM", "Current participants: ${participantIds.size}")

                // Проверяем, не является ли участник уже членом группы
                if (!participantIds.contains(currentUserId)) {
                    participantIds.add(currentUserId)

                    // Обновляем список участников в группе
                    firestore.collection("groups")
                        .document(groupId)
                        .update("participantIds", participantIds)
                        .await()

                    Log.d("ParticipantRegistrationVM", "✅ Added user to group participants")
                } else {
                    Log.d("ParticipantRegistrationVM", "ℹ️ User already in group")
                }

                // КРИТИЧЕСКИ ВАЖНО: Сохраняем данные в UserSession
                UserSession.userId = currentUserId
                UserSession.userName = participantName
                UserSession.userRole = "participant"
                UserSession.groupId = groupId

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
                    // Проверяем существование группы
                    val groupDoc = firestore.collection("groups")
                        .document(groupId)
                        .get()
                        .await()

                    if (groupDoc.exists()) {
                        Log.d("ParticipantRegistrationVM", "Group exists - removing participant")

                        // Группа существует - удаляем себя из неё
                        val participantIds = groupDoc.get("participantIds") as? MutableList<String>
                            ?: mutableListOf()

                        if (participantIds.contains(userId)) {
                            participantIds.remove(userId)

                            firestore.collection("groups")
                                .document(groupId)
                                .update("participantIds", participantIds)
                                .await()

                            Log.d("ParticipantRegistrationVM", "✅ Successfully removed from group")
                        } else {
                            Log.d("ParticipantRegistrationVM", "User not in participant list")
                        }
                    } else {
                        // Группа уже не существует - это нормально
                        Log.d("ParticipantRegistrationVM", "Group already deleted - this is normal")
                    }

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