package legOS.testidf.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import legOS.testidf.utils.ChatMessage
import legOS.testidf.utils.GeminiApiClient
import legOS.testidf.utils.NetworkUnavailableException
import android.util.Log

/**
 * ViewModel для AI Assistant экрана
 * Сохраняет состояние чата при изменении конфигурации (rotation)
 */
class AIAssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val geminiClient = GeminiApiClient(application.applicationContext)

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
            } catch (e: NetworkUnavailableException) {
                // ✅ НОВОЕ: Специальная обработка отсутствия сети
                Log.w(TAG, "Network unavailable", e)

                // Добавляем offline сообщение от Major напрямую в чат
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + ChatMessage(
                        text = e.message ?: geminiClient.getRandomOfflineMessage(),
                        isUser = false
                    ),
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