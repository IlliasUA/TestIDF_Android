package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession
import java.util.UUID

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
        participantName: String,
        groupCode: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = ParticipantRegistrationUiState(isLoading = true)

            try {
                // 1. СНАЧАЛА создаем анонимного пользователя
                val authResult = auth.signInAnonymously().await()
                val userId = authResult.user?.uid ?: throw Exception("User ID is null")

                Log.d("ParticipantVM", "Anonymous user created: $userId")

                // 2. ПОТОМ ищем группу по коду
                val groupQuery = firestore.collection("groups")
                    .whereEqualTo("groupCode", groupCode.trim().uppercase())
                    .limit(1)
                    .get()
                    .await()

                if (groupQuery.isEmpty) {
                    _uiState.value = ParticipantRegistrationUiState(
                        error = "Code invalide. Vérifiez le code avec votre chef."
                    )
                    onComplete(false)
                    return@launch
                }

                val groupDoc = groupQuery.documents[0]
                val groupId = groupDoc.id
                val groupName = groupDoc.getString("name") ?: "Groupe"

                Log.d("ParticipantVM", "Found group: $groupName ($groupId)")

                // 3. Сохраняем данные участника
                val userData = hashMapOf(
                    "userId" to userId,
                    "name" to participantName,
                    "role" to "participant",
                    "isAnonymous" to true,
                    "groupCodes" to listOf(groupCode),
                    "createdAt" to Timestamp.now()
                )

                firestore.collection("users")
                    .document(userId)
                    .set(userData)
                    .await()

                Log.d("ParticipantVM", "User data saved")

                // 4. Добавляем участника в группу
                firestore.collection("groups")
                    .document(groupId)
                    .update("participantIds", FieldValue.arrayUnion(userId))
                    .await()

                Log.d("ParticipantVM", "Participant added to group")

                // 5. Сохраняем сессию
                UserSession.setParticipantSession(
                    userId = userId,
                    name = participantName,
                    email = ""
                )

                _uiState.value = ParticipantRegistrationUiState(
                    successMessage = "Vous avez rejoint $groupName!"
                )

                onComplete(true)

            } catch (e: Exception) {
                Log.e("ParticipantVM", "Error joining group", e)
                _uiState.value = ParticipantRegistrationUiState(
                    error = "Erreur: ${e.message}"
                )
                onComplete(false)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}