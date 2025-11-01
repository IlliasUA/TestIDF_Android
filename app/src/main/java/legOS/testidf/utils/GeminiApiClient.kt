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
 * Рабочий клиент для Google Gemini API
 * Использует модели, которые успешно работают в Cloud Console
 */
class GeminiApiClient {

    companion object {
        private const val TAG = "GeminiApiClient"
        private const val API_KEY = "AIzaSyBy3hssFpfdGL7IROteEnCxpDr2880alHw"

        // Используем v1beta с моделью gemini-1.5-flash-latest
        // Эта комбинация работает судя по Cloud Console
        private const val API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent"
    }

    suspend fun sendMessage(message: String, conversationHistory: List<ChatMessage>): String {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Sending message to Gemini API")
                Log.d(TAG, "URL: $API_URL")

                val url = URL("$API_URL?key=$API_KEY")
                val connection = url.openConnection() as HttpURLConnection

                connection.apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    doOutput = true
                    doInput = true
                    connectTimeout = 30000
                    readTimeout = 30000
                }

                val requestBody = buildRequest(message)
                Log.d(TAG, "Sending request...")

                connection.outputStream.use { os ->
                    OutputStreamWriter(os, "UTF-8").use { writer ->
                        writer.write(requestBody)
                        writer.flush()
                    }
                }

                val responseCode = connection.responseCode
                Log.d(TAG, "Response code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d(TAG, "Success! Parsing response...")
                    return@withContext parseResponse(response)
                } else {
                    val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                    Log.e(TAG, "Error: $errorResponse")
                    throw Exception("API Error: $responseCode")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}", e)
                throw e
            }
        }
    }

    private fun buildRequest(message: String): String {
        val request = JSONObject()

        // Формат запроса для Gemini 1.5 Flash
        val contents = JSONArray()
        contents.put(JSONObject().apply {
            put("parts", JSONArray().apply {
                put(JSONObject().apply {
                    put("text", message)
                })
            })
        })

        request.put("contents", contents)

        // Настройки генерации
        request.put("generationConfig", JSONObject().apply {
            put("temperature", 0.7)
            put("topP", 0.95)
            put("topK", 40)
            put("maxOutputTokens", 2048)
        })

        return request.toString()
    }

    private fun parseResponse(response: String): String {
        return try {
            val json = JSONObject(response)
            val candidates = json.optJSONArray("candidates")

            if (candidates != null && candidates.length() > 0) {
                val content = candidates.getJSONObject(0).optJSONObject("content")
                val parts = content?.optJSONArray("parts")

                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text", "")
                    if (text.isNotEmpty()) {
                        Log.d(TAG, "Successfully extracted response")
                        return text
                    }
                }
            }

            Log.e(TAG, "Could not extract text from response")
            "Sorry, could not process the response"

        } catch (e: Exception) {
            Log.e(TAG, "Parse error: ${e.message}")
            "Error processing response"
        }
    }
}