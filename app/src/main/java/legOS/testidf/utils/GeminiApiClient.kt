package legOS.testidf.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import legOS.testidf.R
import kotlin.random.Random

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class GeminiApiClient(private val context: Context) {

    companion object {
        private const val TAG = "GeminiApiClient"
        private const val MODEL_NAME = "gemini-3.6-flash"
        private const val MAX_HISTORY_MESSAGES = 8

        private const val SYSTEM_PROMPT = """Tu es le Major, sous-officier vétéran de la Légion étrangère française avec plus de 30 ans de service actif.

Tu es un expert des chars de combat, de l'artillerie, des véhicules blindés, de l'aviation militaire, du génie et des tactiques militaires. Tu t'adresses à l'utilisateur comme à une jeune recrue : ton style est direct, précis, exigeant mais toujours respectueux.

Donne des réponses techniquement exactes, structurées et concises. Admets franchement quand tu ne connais pas une information. Réponds en français, sauf si l'utilisateur écrit dans une autre langue ; dans ce cas, réponds dans cette langue."""
    }

    private val model by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(MODEL_NAME)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    fun getRandomOfflineMessage(): String {
        val offlineMessageIds = listOf(
            R.string.ai_offline_message_1,
            R.string.ai_offline_message_2,
            R.string.ai_offline_message_3,
            R.string.ai_offline_message_4,
            R.string.ai_offline_message_5
        )
        return context.getString(offlineMessageIds[Random.nextInt(offlineMessageIds.size)])
    }

    suspend fun sendMessage(message: String, conversationHistory: List<ChatMessage>): String =
        withContext(Dispatchers.IO) {
            if (!isNetworkAvailable()) {
                throw NetworkUnavailableException(getRandomOfflineMessage())
            }

            try {
                val response = model.generateContent(
                    buildPrompt(message, conversationHistory)
                )
                response.text?.trim()?.takeIf { it.isNotEmpty() }
                    ?: throw AIServiceException("empty_response")
            } catch (e: NetworkUnavailableException) {
                throw e
            } catch (e: Exception) {
                // Firebase exceptions can contain request metadata. Never log their message.
                Log.e(TAG, "Firebase AI request failed: ${e.javaClass.simpleName}")
                throw AIServiceException(classifyFailure(e), e)
            }
        }

    private fun buildPrompt(message: String, conversationHistory: List<ChatMessage>): String =
        buildString {
            appendLine(SYSTEM_PROMPT)
            appendLine()
            appendLine("Conversation récente :")
            conversationHistory.takeLast(MAX_HISTORY_MESSAGES).forEach { chatMessage ->
                append(if (chatMessage.isUser) "Utilisateur : " else "Major : ")
                appendLine(chatMessage.text)
            }
            append("Utilisateur : ")
            appendLine(message)
            append("Major :")
        }

    private fun classifyFailure(error: Exception): String {
        val message = error.message.orEmpty().lowercase()
        return when {
            "429" in message || "quota" in message || "rate" in message -> "rate_limit"
            "app check" in message -> "app_check"
            "permission" in message || "403" in message || "api" in message -> "configuration"
            else -> "service_unavailable"
        }
    }
}

class NetworkUnavailableException(message: String) : Exception(message)

class AIServiceException(
    val reason: String,
    cause: Throwable? = null
) : Exception(reason, cause)
