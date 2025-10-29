package legOS.testidf

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import java.util.Locale

/**
 * Gestionnaire de localisation de l'application
 * Gère le changement de langue dans l'application
 *
 * ИСПРАВЛЕНО: Улучшенная обработка смены языка с корректной работой на реальных устройствах
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
     * ИСПРАВЛЕНО: Метод установки языка с принудительным обновлением конфигурации
     * Работает корректно и на эмуляторе, и на реальных устройствах
     */
    fun setLanguage(context: Context, language: Language): Context {
        Log.d(TAG, "===========================================")
        Log.d(TAG, "setLanguage called: ${language.code} (${language.displayName})")

        // Сохраняем выбранный язык в SharedPreferences
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_LANGUAGE, language.code)
            commit() // Синхронное сохранение
        }

        Log.d(TAG, "Language saved to preferences: ${language.code}")

        // Верифицируем сохранение
        val verifyCode = prefs.getString(KEY_LANGUAGE, null)
        Log.d(TAG, "Verification - saved language: $verifyCode")

        // КРИТИЧНО: Обновляем ресурсы приложения
        val newContext = updateResources(context, language.code)

        Log.d(TAG, "Language set complete: ${language.code}")
        Log.d(TAG, "===========================================")

        return newContext
    }

    /**
     * КРИТИЧНО: Обновляет конфигурацию ресурсов с новой локалью
     * Работает на всех версиях Android
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

        // КРИТИЧНО ДЛЯ РЕАЛЬНЫХ УСТРОЙСТВ:
        // Обновляем конфигурацию напрямую через resources
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)

        // Для Android N+ также создаем новый контекст
        val newContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "Creating new context for Android N+")
            context.createConfigurationContext(config)
        } else {
            Log.d(TAG, "Using existing context for Android pre-N")
            context
        }

        Log.d(TAG, "Resources updated successfully")

        return newContext
    }

    /**
     * ИСПРАВЛЕНО: Применяет язык при запуске Activity
     * Используется в attachBaseContext и onCreate
     */
    fun applyLanguage(context: Context): Context {
        val language = getCurrentLanguage(context)
        Log.d(TAG, "applyLanguage: Applying language ${language.code}")
        return updateResources(context, language.code)
    }

    /**
     * НОВОЕ: Принудительно переприменяет язык для Activity
     * Использовать перед recreate() на реальных устройствах
     */
    fun forceApplyLanguage(activity: Activity) {
        val language = getCurrentLanguage(activity)
        Log.d(TAG, "forceApplyLanguage: Force applying ${language.code}")

        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val resources = activity.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        // КРИТИЧНО: Обновляем конфигурацию напрямую
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)

        // Также обновляем application resources
        val appResources = activity.applicationContext.resources
        val appConfig = Configuration(appResources.configuration)
        appConfig.setLocale(locale)

        @Suppress("DEPRECATION")
        appResources.updateConfiguration(appConfig, appResources.displayMetrics)

        Log.d(TAG, "forceApplyLanguage: Language forcefully applied")
    }
}