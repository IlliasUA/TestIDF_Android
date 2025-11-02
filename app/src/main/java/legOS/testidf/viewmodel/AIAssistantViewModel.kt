package legOS.testidf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import legOS.testidf.utils.ChatMessage
import legOS.testidf.utils.GeminiApiClient
import android.util.Log

/**
 * ViewModel для AI Assistant экрана
 * Сохраняет состояние чата при изменении конфигурации (rotation)
 */
class AIAssistantViewModel : ViewModel() {

    private val geminiClient = GeminiApiClient()

    // UI State
    data class ChatUiState(
        val messages: List<ChatMessage> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "AIAssistantViewModel"
    }

    /**
     * Добавляет приветственное сообщение
     */
    fun addWelcomeMessage(message: String) {
        if (_uiState.value.messages.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                messages = listOf(ChatMessage(text = message, isUser = false))
            )
        }
    }

    /**
     * Отправляет сообщение пользователя и получает ответ от AI
     */
    fun sendMessage(userMessage: String, onError: (String) -> Unit) {
        if (userMessage.isBlank()) return

        // Добавляем сообщение пользователя
        val currentMessages = _uiState.value.messages
        _uiState.value = _uiState.value.copy(
            messages = currentMessages + ChatMessage(text = userMessage, isUser = true),
            isLoading = true,
            error = null
        )

        // Запрашиваем ответ от AI
        viewModelScope.launch {
            try {
                val aiResponse = geminiClient.sendMessage(userMessage, _uiState.value.messages)
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + ChatMessage(text = aiResponse, isUser = false),
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error getting AI response", e)
                val errorMessage = e.message ?: "Unknown error"
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = errorMessage
                )
                onError(errorMessage)
            }
        }
    }
}