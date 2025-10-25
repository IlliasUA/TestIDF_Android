package legOS.testidf

import android.content.Context
import android.content.res.Configuration
import android.os.Build
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