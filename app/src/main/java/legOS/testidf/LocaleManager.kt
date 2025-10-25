package legOS.testidf

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * Менеджер локализации приложения
 * Управляет сменой языка в приложении
 */
object LocaleManager {

    private const val PREFS_NAME = "app_preferences"
    private const val KEY_LANGUAGE = "selected_language"

    /**
     * Доступные языки
     */
    enum class Language(val code: String, val displayName: String) {
        FRENCH("fr", "Français"),
        ENGLISH("en", "English"),
        RUSSIAN("ru", "Русский")
    }

    /**
     * Получить текущий язык из настроек
     */
    fun getCurrentLanguage(context: Context): Language {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANGUAGE, Language.FRENCH.code) ?: Language.FRENCH.code
        return Language.values().find { it.code == code } ?: Language.FRENCH
    }

    /**
     * Установить язык приложения
     */
    fun setLanguage(context: Context, language: Language): Context {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()

        return updateResources(context, language.code)
    }

    /**
     * Обновить ресурсы приложения с новым языком
     */
    private fun updateResources(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
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
     * Применить сохраненный язык при запуске
     */
    fun applyLanguage(context: Context): Context {
        val language = getCurrentLanguage(context)
        return updateResources(context, language.code)
    }
}

/**
 * CompositionLocal для доступа к текущему языку
 */
val LocalLanguage = staticCompositionLocalOf { LocaleManager.Language.FRENCH }

/**
 * Composable-обертка для применения языка
 */
@Composable
fun ProvideLanguage(
    language: LocaleManager.Language = LocaleManager.Language.FRENCH,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalLanguage provides language) {
        content()
    }
}

/**
 * Хук для получения текущего языка
 */
@Composable
fun rememberCurrentLanguage(): LocaleManager.Language {
    val context = LocalContext.current
    return LocaleManager.getCurrentLanguage(context)
}