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
     * НОВОЕ: Переопределяем attachBaseContext для применения языка
     */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleManager.applyLanguage(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ НОВОЕ: Включаем edge-to-edge для Android 15+
        enableEdgeToEdge()

        // ✅ НОВОЕ: Настраиваем window для правильной работы с системными панелями
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    AppNavigationWithSubscription()
                }
            }
        }
    }
}

@Composable
fun AppNavigationWithSubscription(
    subscriptionViewModel: SubscriptionViewModel = viewModel()
) {
    val navController = rememberNavController()
    val subscriptionState by subscriptionViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Фоновое изображение с прозрачностью 75%
        val context = LocalContext.current
        val backgroundBitmap = try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: IOException) {
            Log.e("AppNavigation", "Failed to load background image from images/background.png", e)
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

        // Определяем стартовый маршрут на основе статуса подписки
        val startDestination = when {
            subscriptionState.isLoading -> "subscription" // Показываем экран загрузки
            subscriptionState.isActive -> "main_menu" // Подписка активна - главное меню
            else -> "subscription" // Нет подписки - экран подписки
        }

        NavHost(navController, startDestination = startDestination) {
            // Экран подписки (должен быть доступен всегда)
            composable("subscription") {
                SubscriptionScreen(navController, subscriptionViewModel)
            }

            // Все остальные маршруты (защищены подпиской)
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
            composable("main_menu") { MainMenuScreen(navController) }
            composable("test_menu") { TestMenuScreen(navController) }
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
            // НОВЫЙ МАРШРУТ - Настройки языка
            composable("language_settings") {
                LanguageSettingsScreen(navController)
            }
        }
    }
}