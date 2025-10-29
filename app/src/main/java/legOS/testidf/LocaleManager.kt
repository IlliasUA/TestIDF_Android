package legOS.testidf

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import java.util.Locale

/**
 * Gestionnaire de localisation de l'application
 * Gère le changement de langue dans l'application
 *
 * ИСПРАВЛЕНО: Улучшенная обработка смены языка с корректным сохранением состояния
 */
object LocaleManager {

    private const val PREFS_NAME = "app_preferences"
    private const val KEY_LANGUAGE = "selected_language"
    private const val TAG = "LocaleManager"

    /**
     * Langues disponibles
     */
    enum class Language(val code: String, val displayName: String) {
        FRENCH("fr", "FR"),
        ENGLISH("en", "EN")
    }

    /**
     * Obtenir la langue actuelle depuis les paramètres
     */
    fun getCurrentLanguage(context: Context): Language {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANGUAGE, Language.FRENCH.code) ?: Language.FRENCH.code
        val language = Language.values().find { it.code == code } ?: Language.FRENCH

        Log.d(TAG, "getCurrentLanguage: $code -> $language")
        return language
    }

    /**
     * ИСПРАВЛЕНО: Улучшенный метод установки языка
     * Теперь корректно работает с recreate() Activity
     */
    fun setLanguage(context: Context, language: Language): Context {
        Log.d(TAG, "===========================================")
        Log.d(TAG, "setLanguage called: ${language.code} (${language.displayName})")

        // Сохраняем выбранный язык в SharedPreferences
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_LANGUAGE, language.code)
        val saved = editor.commit() // Используем commit для синхронного сохранения

        Log.d(TAG, "Language saved to preferences: $saved")

        // Верифицируем сохранение
        val verifyCode = prefs.getString(KEY_LANGUAGE, null)
        Log.d(TAG, "Verification - saved language: $verifyCode")

        // Обновляем ресурсы приложения
        val newContext = updateResources(context, language.code)

        Log.d(TAG, "Language set complete: ${language.code}")
        Log.d(TAG, "===========================================")

        return newContext
    }

    /**
     * ИСПРАВЛЕНО: Улучшенное обновление ресурсов с поддержкой разных версий Android
     */
    private fun updateResources(context: Context, languageCode: String): Context {
        Log.d(TAG, "updateResources: Setting locale to $languageCode")

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        Log.d(TAG, "Default locale set: ${Locale.getDefault()}")

        val resources = context.resources
        val config = Configuration(resources.configuration)

        // Устанавливаем локаль в конфигурацию
        config.setLocale(locale)

        // КРИТИЧНО: Обновляем конфигурацию для разных версий Android
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "Using createConfigurationContext (Android N+)")
            val newContext = context.createConfigurationContext(config)

            // ВАЖНО: Также обновляем ресурсы самого контекста
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)

            newContext
        } else {
            Log.d(TAG, "Using updateConfiguration (Android pre-N)")
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
            context
        }
    }

    /**
     * ИСПРАВЛЕНО: Улучшенное применение языка при запуске
     * Используется в attachBaseContext и onCreate
     */
    fun applyLanguage(context: Context): Context {
        val language = getCurrentLanguage(context)
        Log.d(TAG, "applyLanguage: Applying language ${language.code}")
        return updateResources(context, language.code)
    }
}