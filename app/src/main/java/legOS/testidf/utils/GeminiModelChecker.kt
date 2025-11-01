package legOS.testidf.utils

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Утилита для проверки доступных моделей Gemini API
 */
class GeminiModelChecker {

    companion object {
        private const val TAG = "GeminiModelChecker"
        private const val API_KEY = "AIzaSyBy3hssFpfdGL7IROteEnCxpDr2880alHw"

        // Все возможные варианты моделей для проверки
        private val MODELS_TO_CHECK = listOf(
            "gemini-pro",
            "gemini-pro-vision",
            "gemini-1.0-pro",
            "gemini-1.0-pro-001",
            "gemini-1.0-pro-latest",
            "gemini-1.0-pro-vision-latest",
            "gemini-1.5-pro",
            "gemini-1.5-pro-latest",
            "gemini-1.5-flash",
            "gemini-1.5-flash-latest",
            "gemini-ultra",
            "text-bison-001",
            "text-bison-002",
            "chat-bison-001"
        )
    }

    /**
     * Проверяет все модели и возвращает список рабочих
     */
    suspend fun checkAllModels(): List<String> {
        return withContext(Dispatchers.IO) {
            val workingModels = mutableListOf<String>()

            Log.d(TAG, "=== CHECKING GEMINI MODELS ===")
            Log.d(TAG, "API Key: ${if (API_KEY.isNotEmpty()) "Present" else "Missing"}")

            // Сначала попробуем получить список моделей через API
            try {
                val modelsList = getModelsList()
                Log.d(TAG, "Models from API: $modelsList")
                workingModels.addAll(modelsList)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get models list: ${e.message}")
            }

            // Теперь проверяем каждую модель напрямую
            for (model in MODELS_TO_CHECK) {
                val isWorking = checkModel(model)
                if (isWorking) {
                    Log.d(TAG, "✅ Model $model is WORKING")
                    if (!workingModels.contains(model)) {
                        workingModels.add(model)
                    }
                } else {
                    Log.d(TAG, "❌ Model $model is NOT working")
                }
            }

            Log.d(TAG, "=== SUMMARY ===")
            Log.d(TAG, "Working models: $workingModels")

            workingModels
        }
    }

    /**
     * Получает список моделей через API
     */
    private suspend fun getModelsList(): List<String> {
        val url = URL("https://generativelanguage.googleapis.com/v1beta/models?key=$API_KEY")
        val connection = url.openConnection() as HttpURLConnection

        connection.apply {
            requestMethod = "GET"
            connectTimeout = 10000
            readTimeout = 10000
        }

        val responseCode = connection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            val jsonResponse = JSONObject(response)
            val modelsArray = jsonResponse.optJSONArray("models")

            val models = mutableListOf<String>()
            if (modelsArray != null) {
                for (i in 0 until modelsArray.length()) {
                    val model = modelsArray.getJSONObject(i)
                    val name = model.optString("name", "")
                    if (name.isNotEmpty()) {
                        // Извлекаем имя модели из полного пути
                        val modelName = name.replace("models/", "")

                        // Проверяем поддержку generateContent
                        val methods = model.optJSONArray("supportedGenerationMethods")
                        if (methods != null) {
                            for (j in 0 until methods.length()) {
                                if (methods.getString(j) == "generateContent") {
                                    models.add(modelName)
                                    break
                                }
                            }
                        }
                    }
                }
            }
            return models
        } else {
            throw Exception("Failed to get models list: $responseCode")
        }
    }

    /**
     * Проверяет конкретную модель
     */
    private suspend fun checkModel(model: String): Boolean {
        return try {
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$API_KEY")
            val connection = url.openConnection() as HttpURLConnection

            connection.apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                doOutput = true
                connectTimeout = 5000
                readTimeout = 5000
            }

            // Минимальный тестовый запрос
            val testRequest = """
                {
                    "contents": [{
                        "parts": [{
                            "text": "Hi"
                        }]
                    }]
                }
            """.trimIndent()

            connection.outputStream.write(testRequest.toByteArray())

            val responseCode = connection.responseCode
            responseCode == HttpURLConnection.HTTP_OK

        } catch (e: Exception) {
            false
        }
    }

    /**
     * Возвращает первую рабочую модель
     */
    suspend fun getFirstWorkingModel(): String? {
        val workingModels = checkAllModels()
        return workingModels.firstOrNull()
    }
}