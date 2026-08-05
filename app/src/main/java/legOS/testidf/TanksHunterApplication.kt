package legOS.testidf

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log

/**
 * Application класс для глобального управления локализацией
 * Гарантирует корректную работу языка во всем приложении
 */
class TanksHunterApplication : Application() {

    override fun attachBaseContext(base: Context) {
        Log.d("TanksHunterApp", "attachBaseContext called")
        // ИСПРАВЛЕНО: Используем упрощенный метод, который не обращается к applicationContext
        super.attachBaseContext(LocaleManager.applyLanguageSimple(base))
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("TanksHunterApp", "onCreate called")

        configureAppCheck()

        // Теперь можно обновить ресурсы через applicationContext
        LocaleManager.updateApplicationResources(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("TanksHunterApp", "onConfigurationChanged: ${newConfig.locales}")

        // Переприменяем сохраненный язык
        val language = LocaleManager.getCurrentLanguage(this)
        val locale = java.util.Locale.forLanguageTag(language.code)
        java.util.Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
