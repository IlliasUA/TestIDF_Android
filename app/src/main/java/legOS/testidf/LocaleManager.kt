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
 * ИСПРАВЛЕНО v2: Полностью переработанная логика для реальных устройств
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
     * НОВОЕ v2: Метод для установки языка с подготовкой к recreate()
     * Этот метод должен вызываться перед activity.recreate()
     */
    fun setLanguageAndPrepareRecreate(context: Context, language: Language) {
        Log.d(TAG, "===========================================")
        Log.d(TAG, "setLanguageAndPrepareRecreate: ${language.code}")

        // 1. Сохраняем в SharedPreferences с СИНХРОННЫМ commit
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val success = prefs.edit().apply {
            putString(KEY_LANGUAGE, language.code)
        }.commit() // commit() блокирует выполнение до сохранения

        Log.d(TAG, "Saved to SharedPreferences: $success")

        // Верификация сохранения
        val verifyCode = prefs.getString(KEY_LANGUAGE, null)
        Log.d(TAG, "Verification - saved language: $verifyCode")

        // 2. Устанавливаем системную локаль по умолчанию
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        Log.d(TAG, "Default Locale set: ${Locale.getDefault().language}")

        // 3. Обновляем Application Context (если доступен)
        try {
            val appContext = context.applicationContext
            updateContextResources(appContext, locale)
            Log.d(TAG, "Application context updated")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update application context", e)
        }

        // 4. Обновляем текущий контекст
        updateContextResources(context, locale)
        Log.d(TAG, "Current context updated")

        Log.d(TAG, "setLanguageAndPrepareRecreate complete")
        Log.d(TAG, "===========================================")
    }

    /**
     * УСТАРЕВШИЙ метод (оставлен для совместимости)
     */
    @Deprecated("Use setLanguageAndPrepareRecreate instead", ReplaceWith("setLanguageAndPrepareRecreate(context, language)"))
    fun setLanguage(context: Context, language: Language): Context {
        setLanguageAndPrepareRecreate(context, language)
        return context
    }

    /**
     * Обновляет ресурсы контекста с новой локалью
     */
    private fun updateContextResources(context: Context, locale: Locale) {
        val resources = context.resources
        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    /**
     * КРИТИЧНО: Применяет язык при создании Activity (для attachBaseContext)
     * Безопасная версия без обращения к applicationContext
     */
    fun applyLanguageSimple(context: Context): Context {
        val language = getCurrentLanguage(context)
        Log.d(TAG, "applyLanguageSimple: Applying language ${language.code}")

        val locale = Locale(language.code)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

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
            Log.d(TAG, "updateApplicationResources: Updating to ${language.code}")

            val locale = Locale(language.code)
            Locale.setDefault(locale)

            updateContextResources(context, locale)

            Log.d(TAG, "Application resources updated successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating application resources", e)
        }
    }

    /**
     * Применяет язык для Activity (вызывается в attachBaseContext и onCreate)
     */
    fun applyLanguage(context: Context): Context {
        return applyLanguageSimple(context)
    }

    /**
     * НОВОЕ v2: Принудительно переприменяет язык для Activity
     * Вызывать в onResume() и перед любым изменением UI
     */
    fun forceApplyLanguage(activity: Activity) {
        val language = getCurrentLanguage(activity)
        Log.d(TAG, "forceApplyLanguage: ${language.code}")

        val locale = Locale(language.code)
        Locale.setDefault(locale)

        // Обновляем Application Context
        try {
            updateContextResources(activity.applicationContext, locale)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update app context in forceApply", e)
        }

        // Обновляем Activity resources
        updateContextResources(activity, locale)

        Log.d(TAG, "forceApplyLanguage complete")
    }
}