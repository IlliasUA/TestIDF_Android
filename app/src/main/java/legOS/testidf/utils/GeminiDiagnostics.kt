package legOS.testidf.utils

import android.util.Log
import legOS.testidf.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Утилита для диагностики проблем с Gemini API
 */
object GeminiDiagnostics {

    private const val TAG = "GeminiDiagnostics"

    /**
     * Тестирует API ключ и выводит доступные модели
     */
    suspend fun diagnose(): DiagnosticResult {
        return withContext(Dispatchers.IO) {
            val result = DiagnosticResult()
            val apiKey = BuildConfig.GEMINI_API_KEY

            Log.d(TAG, "=".repeat(60))
            Log.d(TAG, "GEMINI API DIAGNOSTICS")
            Log.d(TAG, "=".repeat(60))

            // 1. Проверяем API ключ
            Log.d(TAG, "1. Checking API Key...")
            result.apiKeyConfigured = apiKey.isNotEmpty() && apiKey.length > 20
            result.apiKeyLength = apiKey.length
            result.apiKeyPrefix = if (apiKey.length >= 4) apiKey.take(4) else "N/A"

            Log.d(TAG, "   API Key length: ${result.apiKeyLength}")
            Log.d(TAG, "   API Key prefix: ${result.apiKeyPrefix}")

            if (!result.apiKeyConfigured) {
                Log.e(TAG, "   ❌ API Key not properly configured!")
                return@withContext result
            }

            Log.d(TAG, "   ✅ API Key looks valid")

            // 2. Получаем список доступных моделей
            Log.d(TAG, "\n2. Fetching available models...")
            try {
                val models = listAvailableModels(apiKey)
                result.availableModels = models
                result.modelsFound = models.isNotEmpty()

                if (models.isEmpty()) {
                    Log.e(TAG, "   ❌ No models found! API key might not be activated for Gemini")
                } else {
                    Log.d(TAG, "   ✅ Found ${models.size} available models:")
                    models.forEach { model ->
                        Log.d(TAG, "      - ${model.name}")
                        Log.d(TAG, "        Methods: ${model.supportedMethods.joinToString()}")
                    }
                }
            } catch (e: Exception) {
                result.error = e.message ?: "Unknown error"
                Log.e(TAG, "   ❌ Error fetching models: ${e.message}")
            }

            // 3. Тестируем первую доступную модель
            if (result.availableModels.isNotEmpty()) {
                Log.d(TAG, "\n3. Testing first available model...")
                val firstModel = result.availableModels.first()
                try {
                    val testResult = testModel(apiKey, firstModel.name)
                    result.testSuccessful = testResult

                    if (testResult) {
                        Log.d(TAG, "   ✅ Test successful! Model ${firstModel.name} is working")
                        result.workingModel = firstModel.name
                    } else {
                        Log.e(TAG, "   ❌ Test failed for model ${firstModel.name}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "   ❌ Test error: ${e.message}")
                }
            }

            Log.d(TAG, "\n" + "=".repeat(60))
            Log.d(TAG, "DIAGNOSTIC SUMMARY:")
            Log.d(TAG, "  API Key: ${if (result.apiKeyConfigured) "✅ OK" else "❌ NOT CONFIGURED"}")
            Log.d(TAG, "  Models Found: ${if (result.modelsFound) "✅ ${result.availableModels.size}" else "❌ NONE"}")
            Log.d(TAG, "  Test: ${if (result.testSuccessful) "✅ SUCCESS" else "❌ FAILED"}")
            if (result.workingModel != null) {
                Log.d(TAG, "  Working Model: ${result.workingModel}")
            }
            if (result.error != null) {
                Log.d(TAG, "  Error: ${result.error}")
            }
            Log.d(TAG, "=".repeat(60))

            result
        }
    }

    /**
     * Получает список доступных моделей
     */
    private suspend fun listAvailableModels(apiKey: String): List<ModelInfo> {
        val url = URL("https://generativelanguage.googleapis.com/v1/models?key=$apiKey")
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                return parseModels(response)
            } else {
                val error = connection.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                throw Exception("HTTP $responseCode: $error")
            }
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Парсит список моделей из JSON
     */
    private fun parseModels(json: String): List<ModelInfo> {
        val models = mutableListOf<ModelInfo>()

        try {
            val jsonObject = JSONObject(json)
            val modelsArray = jsonObject.optJSONArray("models") ?: return emptyList()

            for (i in 0 until modelsArray.length()) {
                val modelObj = modelsArray.getJSONObject(i)
                val name = modelObj.optString("name", "")
                    .replace("models/", "") // Убираем префикс

                val methodsArray = modelObj.optJSONArray("supportedGenerationMethods")
                val methods = mutableListOf<String>()

                if (methodsArray != null) {
                    for (j in 0 until methodsArray.length()) {
                        methods.add(methodsArray.getString(j))
                    }
                }

                // Добавляем только модели с поддержкой generateContent
                if (methods.contains("generateContent")) {
                    models.add(ModelInfo(name, methods))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing models: ${e.message}")
        }

        return models
    }

    /**
     * Тестирует конкретную модель
     */
    private suspend fun testModel(apiKey: String, modelName: String): Boolean {
        return try {
            val url = URL("https://generativelanguage.googleapis.com/v1/models/$modelName:generateContent?key=$apiKey")
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val requestBody = """
                {
                    "contents": [{
                        "parts": [{"text": "Hi"}]
                    }]
                }
            """.trimIndent()

            connection.outputStream.write(requestBody.toByteArray())

            val responseCode = connection.responseCode
            connection.disconnect()

            responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            false
        }
    }

    data class ModelInfo(
        val name: String,
        val supportedMethods: List<String>
    )

    data class DiagnosticResult(
        var apiKeyConfigured: Boolean = false,
        var apiKeyLength: Int = 0,
        var apiKeyPrefix: String = "",
        var modelsFound: Boolean = false,
        var availableModels: List<ModelInfo> = emptyList(),
        var testSuccessful: Boolean = false,
        var workingModel: String? = null,
        var error: String? = null
    )
}