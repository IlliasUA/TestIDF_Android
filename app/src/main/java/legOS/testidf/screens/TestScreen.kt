package legOS.testidf.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import com.example.quizapp.Question
import com.example.quizapp.Test_Data
import com.example.quizapp.Air_Data
import com.example.quizapp.Art_Data
import com.example.quizapp.Genie_Data
import com.example.quizapp.Recon_Data
import com.example.quizapp.Test_bm2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import legOS.testidf.loadImageFromAssets
import legOS.testidf.getImagePath
import legOS.testidf.buildFinalTestQuestions

@Composable
fun TestScreen(navController: NavController, category: String, timeLimit: Int) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var initialTimeLimit by remember { mutableStateOf(timeLimit) }
    var timeRemaining by remember { mutableStateOf(timeLimit) }
    var answers by remember { mutableStateOf(mutableListOf<String?>()) }
    var showQuitConfirmation by remember { mutableStateOf(false) }

    Log.d("TestScreen", "Received category: $category, timeLimit: $timeLimit")

    val questions = remember {
        when (category) {
            "tanks" -> Test_Data.QUESTION
            "artillery" -> Art_Data.QUESTION
            "recon" -> Recon_Data.QUESTION
            "genie" -> Genie_Data.QUESTION
            "air" -> Air_Data.QUESTION
            "bm2" -> Test_bm2.QUESTION
            "final" -> buildFinalTestQuestions()
            else -> Test_Data.QUESTION
        }.shuffled().take(if (category == "final") 40 else if (category == "bm2") 20 else 10)
    }

    val totalQuestions = questions.size
    val playerName = navController.previousBackStackEntry?.savedStateHandle?.get<String>("playerName")
        ?: navController.previousBackStackEntry?.arguments?.getString("playerName") ?: "Anonyme"
    Log.d("TestScreen", "Retrieved player name: $playerName")

    LaunchedEffect(currentQuestionIndex) {
        if (timeRemaining <= 0) {
            Log.e("TestScreen", "Invalid timeRemaining: $timeRemaining, resetting to $initialTimeLimit")
            timeRemaining = initialTimeLimit
        }
        while (timeRemaining > 0 && currentQuestionIndex < questions.size) {
            delay(1000L)
            timeRemaining--
        }
        if (timeRemaining <= 0 && currentQuestionIndex < questions.size) {
            answers.add(null)
            Log.d("TestScreen", "Auto answer added: null at index $currentQuestionIndex")
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                timeRemaining = initialTimeLimit
            } else {
                Log.d("TestScreen", "Final answers: $answers")
                navController.currentBackStackEntry?.savedStateHandle?.set("questions", questions)
                navController.currentBackStackEntry?.savedStateHandle?.set("answers", answers)
                navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                navController.navigate("results/$category/$timeLimit?playerName=$playerName")
            }
        }
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return

    // Загрузка фонового изображения
    val backgroundImage = loadImageFromAssets(context, "images/background_3.jpg")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                backgroundImage?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it.asImageBitmap()),
                        contentScale = ContentScale.Crop
                    )
                } ?: Modifier.background(MaterialTheme.colorScheme.background)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Шкала прогресса
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.Transparent)
        ) {
            LinearProgressIndicator(
                progress = { timeRemaining.toFloat() / initialTimeLimit.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                color = if (timeRemaining <= 3 && timeRemaining > 0) Color(0xFF8B0000) else Color(0xFF32CD32),
                trackColor = Color.Transparent
            )
        }

        // Нумерация вопросов
        Text(
            text = "${currentQuestionIndex + 1}/$totalQuestions",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Состояние для масштабирования
        var scale by remember { mutableStateOf(1f) }
        var isGestureActive by remember { mutableStateOf(false) }

        val imagePath = getImagePath(category, currentQuestion)
        val bitmap = loadImageFromAssets(context, imagePath)
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Question Image",
                modifier = Modifier
                    .size(550.dp, 350.dp)
                    .padding(25.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            isGestureActive = true
                            scale = (scale * zoom).coerceIn(1f, 3f) // Ограничиваем масштаб от 1x до 3x
                        }
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                if (event.type == PointerEventType.Release && isGestureActive) {
                                    scale = 1f // Сбрасываем масштаб при отпускании
                                    isGestureActive = false
                                }
                            }
                        }
                    }
            )
        } ?: Text("Image not found: ${currentQuestion.image}", style = MaterialTheme.typography.bodyMedium)

        currentQuestion.options.forEachIndexed { index, option ->
            Button(
                onClick = {
                    scope.launch {
                        answers.add(option)
                        Log.d("TestScreen", "User answer: $option at index $currentQuestionIndex")
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            timeRemaining = initialTimeLimit
                        } else {
                            Log.d("TestScreen", "Final answers: $answers")
                            navController.currentBackStackEntry?.savedStateHandle?.set("questions", questions)
                            navController.currentBackStackEntry?.savedStateHandle?.set("answers", answers)
                            navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                            navController.navigate("results/$category/$timeLimit?playerName=$playerName")
                        }
                    }
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(75.dp)
                    .padding(8.dp)
                    .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("${index + 1}. $option", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = { showQuitConfirmation = true },
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
                .padding(8.dp)
                .background(Color.Transparent),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text("Quitter", style = MaterialTheme.typography.bodyMedium)
        }

        if (showQuitConfirmation) {
            AlertDialog(
                onDismissRequest = { showQuitConfirmation = false },
                title = { Text("Confirmation") },
                text = { Text("Êtes-vous sûr?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showQuitConfirmation = false
                            navController.navigate("test_menu")
                        }
                    ) {
                        Text("Oui")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showQuitConfirmation = false }
                    ) {
                        Text("Non")
                    }
                }
            )
        }
    }
}