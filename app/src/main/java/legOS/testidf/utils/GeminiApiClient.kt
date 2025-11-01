package legOS.testidf.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * Модель данных для сообщения в чате
 */
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Клиент для Google Gemini API
 * Использует gemini-2.5-flash (подтверждено диагностикой)
 */
class GeminiApiClient {

    companion object {
        private const val TAG = "GeminiApiClient"

        // Получаем API ключ из BuildConfig
        private fun getApiKey(): String {
            return try {
                // Пытаемся получить из BuildConfig
                val buildConfigClass = Class.forName("legOS.testidf.BuildConfig")
                val field = buildConfigClass.getDeclaredField("GEMINI_API_KEY")
                field.get(null) as? String ?: ""
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load API key from BuildConfig: ${e.message}")
                ""
            }
        }

        // Используем актуальную модель gemini-2.5-flash (подтверждено диагностикой!)
        private const val API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent"
    }

    suspend fun sendMessage(message: String, conversationHistory: List<ChatMessage>): String {
        return withContext(Dispatchers.IO) {
            try {
                val apiKey = getApiKey()

                // Проверяем что API ключ настроен
                if (apiKey.isEmpty() || apiKey.length < 20) {
                    throw Exception(
                        "⚠️ API key not found in BuildConfig!\n\n" +
                                "Steps to fix:\n" +
                                "1. Check local.properties has: GEMINI_API_KEY=your_key\n" +
                                "2. Build → Clean Project\n" +
                                "3. Build → Rebuild Project\n" +
                                "4. Restart app\n\n" +
                                "Get key from: https://aistudio.google.com/app/apikey"
                    )
                }

                Log.d(TAG, "📤 Sending message to Gemini API")
                Log.d(TAG, "Model: gemini-2.5-flash")
                Log.d(TAG, "API Key length: ${apiKey.length}")

                val url = URL("$API_URL?key=$apiKey")
                val connection = url.openConnection() as HttpURLConnection

                connection.apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    doInput = true
                    connectTimeout = 30000
                    readTimeout = 30000
                }

                val requestBody = buildRequest(message, conversationHistory)

                connection.outputStream.use { os ->
                    OutputStreamWriter(os, "UTF-8").use { writer ->
                        writer.write(requestBody)
                        writer.flush()
                    }
                }

                val responseCode = connection.responseCode
                Log.d(TAG, "Response code: $responseCode")

                when (responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        val response = connection.inputStream.bufferedReader().use { it.readText() }
                        Log.d(TAG, "✅ Success!")
                        return@withContext parseResponse(response)
                    }

                    400 -> {
                        val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                        Log.e(TAG, "❌ Bad Request: $errorResponse")
                        throw Exception("Invalid request format.")
                    }

                    401, 403 -> {
                        val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                        Log.e(TAG, "❌ Authentication Error: $errorResponse")
                        throw Exception("API key is invalid. Get new key at aistudio.google.com/app/apikey")
                    }

                    404 -> {
                        val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                        Log.e(TAG, "❌ Model Not Found: $errorResponse")
                        throw Exception("Model not found. Run diagnostics with bug icon.")
                    }

                    429 -> {
                        throw Exception("Rate limit exceeded. Wait and try again.")
                    }

                    500, 503 -> {
                        throw Exception("Google server error. Try again later.")
                    }

                    else -> {
                        val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                        Log.e(TAG, "❌ HTTP Error $responseCode: $errorResponse")
                        throw Exception("API Error: $responseCode")
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception: ${e.message}", e)
                throw e
            }
        }
    }

    private fun buildRequest(message: String, history: List<ChatMessage>): String {
        val request = JSONObject()
        val contents = JSONArray()

        // Добавляем историю (последние 10 сообщений)
        val recentHistory = history.takeLast(10)
        for (msg in recentHistory) {
            contents.put(JSONObject().apply {
                put("role", if (msg.isUser) "user" else "model")
                put("parts", JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", msg.text)
                    })
                })
            })
        }

        // Добавляем новое сообщение
        contents.put(JSONObject().apply {
            put("role", "user")
            put("parts", JSONArray().apply {
                put(JSONObject().apply {
                    put("text", message)
                })
            })
        })

        request.put("contents", contents)

        // Настройки генерации для gemini-2.5-flash
        request.put("generationConfig", JSONObject().apply {
            put("temperature", 1.0)
            put("topK", 40)
            put("topP", 0.95)
            put("maxOutputTokens", 8192)
        })

        // Фильтры безопасности
        request.put("safetySettings", JSONArray().apply {
            for (category in listOf(
                "HARM_CATEGORY_HARASSMENT",
                "HARM_CATEGORY_HATE_SPEECH",
                "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                "HARM_CATEGORY_DANGEROUS_CONTENT"
            )) {
                put(JSONObject().apply {
                    put("category", category)
                    put("threshold", "BLOCK_MEDIUM_AND_ABOVE")
                })
            }
        })

        return request.toString()
    }

    private fun parseResponse(response: String): String {
        return try {
            val json = JSONObject(response)

            // Проверяем ошибки
            if (json.has("error")) {
                val error = json.getJSONObject("error")
                val errorMessage = error.optString("message", "Unknown error")
                throw Exception("API Error: $errorMessage")
            }

            val candidates = json.optJSONArray("candidates")

            if (candidates != null && candidates.length() > 0) {
                val content = candidates.getJSONObject(0).optJSONObject("content")
                val parts = content?.optJSONArray("parts")

                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text", "")
                    if (text.isNotEmpty()) {
                        return text
                    }
                }
            }

            Log.e(TAG, "Could not extract text from response")
            throw Exception("Empty response from API")

        } catch (e: Exception) {
            Log.e(TAG, "Parse error: ${e.message}")
            throw Exception("Failed to parse response: ${e.message}")
        }
    }
}