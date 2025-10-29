package legOS.testidf

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import legOS.testidf.screens.*
import legOS.testidf.viewmodel.SubscriptionViewModel
import java.io.IOException

class MainActivity : ComponentActivity() {

    /**
     * КРИТИЧНО: Переопределяем attachBaseContext для применения языка
     * Этот метод вызывается ДО onCreate() и позволяет установить язык
     * до загрузки ресурсов
     */
    override fun attachBaseContext(newBase: Context) {
        Log.d("MainActivity", "========================================")
        Log.d("MainActivity", "attachBaseContext called")
        Log.d("MainActivity", "System locale: ${java.util.Locale.getDefault()}")

        // НОВОЕ: Логируем доступные локали в ресурсах
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            val locales = newBase.resources.configuration.locales
            Log.d("MainActivity", "Available locales in resources: ${locales}")
        }

        // Применяем сохраненный язык
        val context = LocaleManager.applyLanguageSimple(newBase)

        val currentLang = LocaleManager.getCurrentLanguage(context)
        Log.d("MainActivity", "Language applied in attachBaseContext: ${currentLang.code}")

        // НОВОЕ: Проверяем что ресурсы загрузились правильно
        try {
            val testString = context.getString(R.string.app_name)
            Log.d("MainActivity", "Test string loaded: $testString")
        } catch (e: Exception) {
            Log.e("MainActivity", "ERROR loading resources!", e)
        }

        super.attachBaseContext(context)
        Log.d("MainActivity", "========================================")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "========================================")
        Log.d("MainActivity", "onCreate called")

        // ✅ КРИТИЧНО: Принудительно переприменяем язык в onCreate
        // Это гарантирует корректную работу на реальных устройствах
        LocaleManager.forceApplyLanguage(this)

        val savedLanguage = LocaleManager.getCurrentLanguage(this)
        Log.d("MainActivity", "onCreate: Current language = ${savedLanguage.code}")
        Log.d("MainActivity", "Current Locale.getDefault() = ${java.util.Locale.getDefault().language}")

        // НОВОЕ: Проверяем строки после применения языка
        try {
            val menuButton = getString(R.string.menu_button)
            val quitButton = getString(R.string.quit_button)
            Log.d("MainActivity", "Strings check - Menu: $menuButton, Quit: $quitButton")
        } catch (e: Exception) {
            Log.e("MainActivity", "ERROR reading strings!", e)
        }

        // ✅ Включаем edge-to-edge для Android 15+
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        Log.d("MainActivity", "onCreate: Setup complete")
        Log.d("MainActivity", "========================================")

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d("MainActivity", "========================================")
        Log.d("MainActivity", "onResume called")

        // ✅ Переприменяем язык при возврате в приложение
        // Это важно для случаев, когда пользователь меняет системный язык
        LocaleManager.forceApplyLanguage(this)

        val currentLang = LocaleManager.getCurrentLanguage(this)
        Log.d("MainActivity", "onResume: Language = ${currentLang.code}")

        WindowCompat.setDecorFitsSystemWindows(window, false)

        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        Log.d("MainActivity", "onResume: Language and UI reapplied")
        Log.d("MainActivity", "========================================")
    }

    /**
     * НОВОЕ: Переопределяем onRestart для дополнительной надёжности
     */
    override fun onRestart() {
        super.onRestart()

        Log.d("MainActivity", "onRestart called - reapplying language")
        LocaleManager.forceApplyLanguage(this)
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        // Фоновое изображение
        val context = LocalContext.current
        val backgroundBitmap = try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            Log.e("AppNavigation", "Failed to load background image", e)
            null
        }
        backgroundBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Background Image",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.75f),
                contentScale = ContentScale.Crop
            )
        }

        // Стартуем с главного меню БЕЗ проверки подписки
        val startDestination = "main_menu"

        NavHost(navController, startDestination = startDestination) {
            // Главное меню - БЕЗ проверки подписки
            composable("main_menu") {
                MainMenuScreen(navController = navController)
            }

            // Test menu с проверкой подписки
            composable("test_menu") {
                TestMenuScreenWithSubscription(navController, viewModel())
            }

            // Экран подписки
            composable("subscription") {
                SubscriptionScreen(navController, viewModel())
            }

            // Все остальные маршруты
            composable("creation") {
                CreationScreen(
                    navController = navController,
                    mode = CreationMode.OFFLINE
                )
            }
            composable("creation_online") {
                CreationScreen(
                    navController = navController,
                    mode = CreationMode.ONLINE
                )
            }
            composable("send_test/{sessionId}") { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getString("sessionId")
                if (sessionId != null) {
                    SendTestScreen(navController, sessionId)
                } else {
                    Log.e("Navigation", "sessionId is null in send_test route")
                }
            }
            composable("chef_sessions") {
                ChefSessionsScreen(navController)
            }
            composable("session_results/{sessionId}") { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
                SessionResultsScreen(navController, sessionId)
            }
            composable("take_test/{sessionId}") { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
                TakeTestScreen(navController, sessionId)
            }
            composable("test_completed") {
                TestCompletedScreen(navController)
            }
            composable("participant_registration") {
                ParticipantRegistrationScreen(navController)
            }
            composable("participant_waiting") {
                ParticipantWaitingScreen(navController)
            }
            composable("competition") {
                CompetitionRoleScreen(navController)
            }
            composable("admin_registration") {
                AdminRegistrationScreen(navController)
            }
            composable("time_selection/{category}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                TimeSelectionScreen(navController, category)
            }
            composable("player_name") { PlayerNameScreen(navController) }
            composable(
                route = "confirmation_final_test?playerName={playerName}",
                arguments = listOf(navArgument("playerName") { defaultValue = "Anonyme" })
            ) { backStackEntry ->
                val playerName = backStackEntry.arguments?.getString("playerName") ?: "Anonyme"
                ConfirmationFinalTestScreen(navController, playerName)
            }
            composable("results/{category}/{timeLimit}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                val timeLimit = backStackEntry.arguments?.getString("timeLimit")?.toIntOrNull() ?: 60
                ResultsScreen(navController, category, timeLimit)
            }
            composable("more_info/{category}/{index}/{timeLimit}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                val timeLimit = backStackEntry.arguments?.getString("timeLimit")?.toIntOrNull() ?: 60
                MoreInfoScreen(navController, category, index, timeLimit)
            }
            composable("hall_of_fame") { HallOfFameScreen(navController) }
            composable("test/{category}/{timeLimit}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                val timeLimit = backStackEntry.arguments?.getString("timeLimit")?.toIntOrNull() ?: 60
                TestScreen(navController, category, timeLimit)
            }
            composable("info_screen") { InfoScreen(navController) }
            composable("catalog") { CatalogScreen(navController) }
            composable("custom_time_selection/{questionCount}") { backStackEntry ->
                val questionCount = backStackEntry.arguments?.getString("questionCount") ?: "0"
                CustomTimeSelectionScreen(navController = navController, questionCount = questionCount)
            }
            composable("custom_test/{questionCount}/{timeLimit}") { backStackEntry ->
                val questionCount = backStackEntry.arguments?.getString("questionCount") ?: "0"
                val timeLimit = backStackEntry.arguments?.getString("timeLimit") ?: "10"
                CustomTestScreen(navController = navController, questionCount = questionCount, timeLimit = timeLimit)
            }
            composable("custom_results/{questionCount}/{timeLimit}") { backStackEntry ->
                val questionCount = backStackEntry.arguments?.getString("questionCount") ?: "0"
                val timeLimit = backStackEntry.arguments?.getString("timeLimit") ?: "10"
                CustomResultsScreen(navController, questionCount, timeLimit)
            }
            composable("help_screen") {
                HelpScreen(navController = navController)
            }
            composable("news_screen") {
                NewsScreen(navController = navController)
            }
        }
    }
}

/**
 * Обёртка для TestMenuScreen с проверкой подписки
 */
@Composable
fun TestMenuScreenWithSubscription(
    navController: androidx.navigation.NavController,
    subscriptionViewModel: SubscriptionViewModel = viewModel()
) {
    val subscriptionState by subscriptionViewModel.uiState.collectAsState()

    Log.d("TestMenuWithSub", "State: loading=${subscriptionState.isLoading}, active=${subscriptionState.isActive}, testMode=${subscriptionState.isTestMode}")

    when {
        subscriptionState.isLoading -> {
            Log.d("TestMenuWithSub", "Showing subscription check screen")
            SubscriptionScreen(navController, subscriptionViewModel)
        }
        subscriptionState.isActive || subscriptionState.isTestMode -> {
            Log.d("TestMenuWithSub", "Subscription active, showing TestMenuScreen")
            TestMenuScreen(navController)
        }
        else -> {
            Log.d("TestMenuWithSub", "No subscription, showing subscription screen")
            SubscriptionScreen(navController, subscriptionViewModel)
        }
    }
}