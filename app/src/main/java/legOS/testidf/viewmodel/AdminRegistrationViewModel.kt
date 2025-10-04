package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession
import java.util.UUID

data class AdminRegistrationUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val groupCode: String? = null  // ДОБАВЛЕНО
)

class AdminRegistrationViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(AdminRegistrationUiState())
    val uiState: StateFlow<AdminRegistrationUiState> = _uiState

    fun registerAdmin(
        email: String,
        password: String,
        name: String,
        groupName: String,
        onComplete: (Boolean, String?, String?, String?) -> Unit  // Добавили groupCode
    ) {
        viewModelScope.launch {
            _uiState.value = AdminRegistrationUiState(isLoading = true)

            try {
                // 1. Создаем пользователя в Firebase Authentication
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = authResult.user?.uid ?: throw Exception("User ID is null")

                Log.d("AdminRegVM", "User created in Auth: $userId")

                // 2. Создаем документ пользователя в Firestore
                val userData = hashMapOf(
                    "userId" to userId,
                    "email" to email,
                    "name" to name,
                    "role" to "admin",
                    "createdAt" to Timestamp.now()
                )

                firestore.collection("users")
                    .document(userId)
                    .set(userData)
                    .await()

                Log.d("AdminRegVM", "User document created in Firestore")

                // 3. Генерируем код группы
                val groupCode = generateGroupCode()

                // 4. Создаем группу
                val groupId = UUID.randomUUID().toString()
                val groupData = hashMapOf(
                    "groupId" to groupId,
                    "adminId" to userId,
                    "name" to groupName,
                    "groupCode" to groupCode,  // ДОБАВЛЕНО
                    "participantIds" to emptyList<String>(),
                    "createdAt" to Timestamp.now()
                )

                firestore.collection("groups")
                    .document(groupId)
                    .set(groupData)
                    .await()

                Log.d("AdminRegVM", "Group created: $groupId with code: $groupCode")

                // 5. Сохраняем сессию
                UserSession.setAdminSession(
                    userId = userId,
                    groupId = groupId,
                    name = name,
                    email = email
                )

                // 6. Успех
                _uiState.value = AdminRegistrationUiState(
                    successMessage = "Inscription réussie!",
                    groupCode = groupCode  // ДОБАВЛЕНО
                )

                onComplete(true, userId, groupId, groupCode)

            } catch (e: Exception) {
                Log.e("AdminRegVM", "Registration error", e)

                val errorMessage = when {
                    e.message?.contains("email address is already in use") == true ->
                        "Cet email est déjà utilisé"
                    e.message?.contains("network") == true ->
                        "Erreur réseau. Vérifiez votre connexion"
                    else ->
                        "Erreur: ${e.message}"
                }

                _uiState.value = AdminRegistrationUiState(error = errorMessage)
                onComplete(false, null, null, null)
            }
        }
    }

    /**
     * Генерирует 6-значный код группы
     * Использует только легко различимые символы
     */
    private fun generateGroupCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // Без I, O, 0, 1
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }
}