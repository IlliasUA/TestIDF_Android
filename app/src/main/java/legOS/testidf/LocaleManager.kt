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
 * ИСПРАВЛЕНО v4: Полная поддержка реальных устройств
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
        return language
    }

    /**
     * КРИТИЧНО: Метод для установки языка с подготовкой к recreate()
     * Используется перед activity.recreate()
     */
    fun setLanguageAndPrepareRecreate(context: Context, language: Language) {
        // 1. СИНХРОННОЕ сохранение в SharedPreferences
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString(KEY_LANGUAGE, language.code)
        }.commit() // commit() - синхронная запись

        // 2. Устанавливаем системную локаль по умолчанию
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        // 3. КРИТИЧНО для реальных устройств: обновляем ресурсы приложения
        val appContext = context.applicationContext
        val resources = appContext.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(android.os.LocaleList(locale))
        }

        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)

        // 4. Обновляем текущий контекст
        val currentResources = context.resources
        val currentConfig = Configuration(currentResources.configuration)
        currentConfig.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentConfig.setLocales(android.os.LocaleList(locale))
        }

        @Suppress("DEPRECATION")
        currentResources.updateConfiguration(currentConfig, currentResources.displayMetrics)
    }

    /**
     * КРИТИЧНО: Применяет язык при создании Activity (для attachBaseContext)
     */
    fun applyLanguageSimple(context: Context): Context {
        val language = getCurrentLanguage(context)
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(android.os.LocaleList(locale))
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    /**
     * Обновляет Application Context (вызывается из Application.onCreate)
     */
    fun updateApplicationResources(context: Context) {
        try {
            val language = getCurrentLanguage(context)
            val locale = Locale(language.code)
            Locale.setDefault(locale)

            val resources = context.resources
            val config = Configuration(resources.configuration)
            config.setLocale(locale)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocales(android.os.LocaleList(locale))
            }

            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating application resources", e)
        }
    }

    /**
     * Применяет язык для Activity
     */
    fun applyLanguage(context: Context): Context {
        return applyLanguageSimple(context)
    }

    /**
     * КРИТИЧНО: Принудительно переприменяет язык для Activity
     */
    fun forceApplyLanguage(activity: Activity) {
        val language = getCurrentLanguage(activity)
        val locale = Locale(language.code)
        Locale.setDefault(locale)

        // Обновляем Application Context
        try {
            val appResources = activity.applicationContext.resources
            val appConfig = Configuration(appResources.configuration)
            appConfig.setLocale(locale)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                appConfig.setLocales(android.os.LocaleList(locale))
            }

            @Suppress("DEPRECATION")
            appResources.updateConfiguration(appConfig, appResources.displayMetrics)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update app context", e)
        }

        // Обновляем Activity resources
        val resources = activity.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(android.os.LocaleList(locale))
        }

        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}