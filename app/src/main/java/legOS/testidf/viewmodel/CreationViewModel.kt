package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import legOS.testidf.data.UserSession
import legOS.testidf.repository.FirebaseRepository
import legOS.testidf.screens.CreationItem


data class CreationUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessionId: String? = null,
    val successMessage: String? = null
)

class CreationViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _uiState = MutableStateFlow(CreationUiState())
    val uiState: StateFlow<CreationUiState> = _uiState

    /**
     * Создает тестовую сессию из выбранных элементов
     */
    fun createTestSession(
        selectedItems: List<CreationItem>,
        timeLimit: Int = 15,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = CreationUiState(isLoading = true)

            try {
                // Проверяем авторизацию
                val userId = UserSession.userId
                val groupId = UserSession.groupId

                if (userId == null || groupId == null) {
                    _uiState.value = CreationUiState(
                        error = "Utilisateur non connecté. Veuillez vous reconnecter."
                    )
                    Log.e("CreationVM", "User not logged in")
                    return@launch
                }

                // Проверяем количество вопросов
                if (selectedItems.size < 4) {
                    _uiState.value = CreationUiState(
                        error = "Minimum 4 éléments requis pour créer un test"
                    )
                    return@launch
                }

                Log.d("CreationVM", "Creating session with ${selectedItems.size} items")

                // Создаем сессию
                val result = repository.createTestSessionFromCreation(
                    adminId = userId,
                    groupId = groupId,
                    title = "Test personnalisé - ${UserSession.userName}",
                    selectedItems = selectedItems,
                    timeLimit = timeLimit
                )

                if (result.isSuccess) {
                    val sessionId = result.getOrNull()
                    if (sessionId != null) {
                        _uiState.value = CreationUiState(
                            sessionId = sessionId,
                            successMessage = "Test créé avec succès!"
                        )
                        onSuccess(sessionId)
                        Log.d("CreationVM", "✅ Session created: $sessionId")
                    }
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Erreur inconnue"
                    _uiState.value = CreationUiState(
                        error = "Échec de création: $errorMsg"
                    )
                    Log.e("CreationVM", "Failed to create session: $errorMsg")
                }

            } catch (e: Exception) {
                _uiState.value = CreationUiState(
                    error = "Erreur: ${e.message}"
                )
                Log.e("CreationVM", "Error creating session", e)
            }
        }
    }

    /**
     * Сбрасывает состояние ошибки
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}