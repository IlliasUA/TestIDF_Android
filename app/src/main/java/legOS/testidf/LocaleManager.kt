package legOS.testidf

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.util.Log
import java.util.Locale

/**
 * Gestionnaire de localisation de l'application
 * Gère le changement de langue dans l'application
 *
 * ИСПРАВЛЕНО v6: Полная поддержка AAB с тремя языками (EN, FR, ES)
 */
object LocaleManager {

    private const val PREFS_NAME = "app_preferences"
    private const val KEY_LANGUAGE = "selected_language"
    private const val TAG = "LocaleManager"

    /**
     * Langues disponibles
     */
    enum class Language(val code: String, val displayName: String) {
        ENGLISH("en", "EN"),
        FRENCH("fr", "FR"),
        SPANISH("es", "ES")
    }

    /**
     * Obtenir la langue actuelle depuis les paramètres
     */
    fun getCurrentLanguage(context: Context): Language {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANGUAGE, null)

        Log.d(TAG, "getCurrentLanguage: saved code = $code")

        // Если язык не сохранен, используем системный язык
        if (code == null) {
            val systemLanguage = getSystemLanguage()
            Log.d(TAG, "No saved language, using system: $systemLanguage")
            return systemLanguage
        }

        val language = Language.values().find { it.code == code } ?: Language.FRENCH
        Log.d(TAG, "getCurrentLanguage: returning $language")
        return language
    }

    /**
     * НОВОЕ: Получить системный язык с поддержкой испанского
     */
    private fun getSystemLanguage(): Language {
        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            Resources.getSystem().configuration.locale
        }

        return when (systemLocale.language) {
            "en" -> Language.ENGLISH
            "fr" -> Language.FRENCH
            "es" -> Language.SPANISH
            else -> Language.FRENCH // По умолчанию французский
        }
    }

    /**
     * КРИТИЧНО: Метод для установки языка с подготовкой к recreate()
     * Используется перед activity.recreate()
     */
    fun setLanguageAndPrepareRecreate(context: Context, language: Language) {
        Log.d(TAG, "========================================")
        Log.d(TAG, "setLanguageAndPrepareRecreate: ${language.code}")

        // 1. СИНХРОННОЕ сохранение в SharedPreferences
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val success = prefs.edit().apply {
            putString(KEY_LANGUAGE, language.code)
        }.commit() // commit() - синхронная запись

        Log.d(TAG, "Language saved to SharedPreferences: $success")

        // 2. Устанавливаем системную локаль по умолчанию
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        Log.d(TAG, "Locale.setDefault set to: ${locale.language}")

        // 3. КРИТИЧНО для AAB: обновляем ресурсы приложения ГЛОБАЛЬНО
        updateAppResources(context.applicationContext, locale)

        // 4. Обновляем текущий контекст
        updateContextResources(context, locale)

        Log.d(TAG, "setLanguageAndPrepareRecreate complete")
        Log.d(TAG, "========================================")
    }

    /**
     * КРИТИЧНО: Применяет язык при создании Activity (для attachBaseContext)
     */
    fun applyLanguageSimple(context: Context): Context {
        val language = getCurrentLanguage(context)
        val locale = Locale(language.code)

        Log.d(TAG, "applyLanguageSimple: applying ${language.code}")

        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
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

            Log.d(TAG, "updateApplicationResources: ${language.code}")

            Locale.setDefault(locale)
            updateAppResources(context, locale)

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

        Log.d(TAG, "forceApplyLanguage: ${language.code}")

        Locale.setDefault(locale)

        // Обновляем Application Context
        try {
            updateAppResources(activity.applicationContext, locale)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update app context", e)
        }

        // Обновляем Activity resources
        updateContextResources(activity, locale)
    }

    /**
     * НОВОЕ: Обновляет ресурсы контекста приложения
     */
    private fun updateAppResources(context: Context, locale: Locale) {
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        }

        // Используем createConfigurationContext для API 17+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(config)
        }

        // КРИТИЧНО: Также обновляем через updateConfiguration для старых API
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)

        Log.d(TAG, "updateAppResources: resources updated for ${locale.language}")
    }

    /**
     * НОВОЕ: Обновляет ресурсы для конкретного контекста
     */
    private fun updateContextResources(context: Context, locale: Locale) {
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        }

        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)

        Log.d(TAG, "updateContextResources: context resources updated for ${locale.language}")
    }
}