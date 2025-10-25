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
 * Gestionnaire de localisation de l'application
 * Gère le changement de langue dans l'application
 */
object LocaleManager {

    private const val PREFS_NAME = "app_preferences"
    private const val KEY_LANGUAGE = "selected_language"

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
        return Language.values().find { it.code == code } ?: Language.FRENCH
    }

    /**
     * Définir la langue de l'application
     */
    fun setLanguage(context: Context, language: Language): Context {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()

        return updateResources(context, language.code)
    }

    /**
     * Basculer vers l'autre langue
     */
    fun toggleLanguage(context: Context): Language {
        val current = getCurrentLanguage(context)
        val newLanguage = when (current) {
            Language.FRENCH -> Language.ENGLISH
            Language.ENGLISH -> Language.FRENCH
        }
        setLanguage(context, newLanguage)
        return newLanguage
    }

    /**
     * Mettre à jour les ressources de l'application avec une nouvelle langue
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
     * Appliquer la langue enregistrée au démarrage
     */
    fun applyLanguage(context: Context): Context {
        val language = getCurrentLanguage(context)
        return updateResources(context, language.code)
    }
}

/**
 * CompositionLocal pour accéder à la langue actuelle
 */
val LocalLanguage = staticCompositionLocalOf { LocaleManager.Language.FRENCH }

/**
 * Wrapper Composable pour appliquer la langue
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
 * Hook pour obtenir la langue actuelle
 */
@Composable
fun rememberCurrentLanguage(): LocaleManager.Language {
    val context = LocalContext.current
    return LocaleManager.getCurrentLanguage(context)
}