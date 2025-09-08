package legOS.testidf.screens

import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import com.example.quizapp.Question
import com.example.quizapp.Test_Data
import com.example.quizapp.Air_Data
import com.example.quizapp.Art_Data
import com.example.quizapp.Genie_Data
import com.example.quizapp.Recon_Data
import com.example.quizapp.Test_bm2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import legOS.testidf.loadImageFromAssets
import legOS.testidf.getImagePath
import legOS.testidf.buildFinalTestQuestions
import java.io.IOException

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TestScreen(navController: NavController, category: String, timeLimit: Int) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
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

    // Load background image
    val backgroundImage: androidx.compose.ui.graphics.ImageBitmap? = remember {
        try {
            context.assets.open("images/background_3.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("TestScreen", "Error loading background_3.jpg", e)
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
    ) {
        // Display background image
        backgroundImage?.let { image: androidx.compose.ui.graphics.ImageBitmap ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                TestScreenCompactLayout(
                    navController = navController,
                    category = category,
                    currentQuestion = currentQuestion,
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = totalQuestions,
                    timeRemaining = timeRemaining,
                    initialTimeLimit = initialTimeLimit,
                    answers = answers,
                    showQuitConfirmation = showQuitConfirmation,
                    scope = scope,
                    isLandscape = isLandscape,
                    onAnswer = { answer ->
                        answers.add(answer)
                        Log.d("TestScreen", "User answer: $answer at index $currentQuestionIndex")
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            timeRemaining = initialTimeLimit
                        } else {
                            navController.currentBackStackEntry?.savedStateHandle?.set("questions", questions)
                            navController.currentBackStackEntry?.savedStateHandle?.set("answers", answers)
                            navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                            navController.navigate("results/$category/$timeLimit?playerName=$playerName")
                        }
                    },
                    onQuit = { showQuitConfirmation = true },
                    onConfirmQuit = {
                        showQuitConfirmation = false
                        navController.navigate("test_menu")
                    },
                    onDismissQuit = { showQuitConfirmation = false }
                )
            }
            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                TestScreenLargeLayout(
                    navController = navController,
                    category = category,
                    currentQuestion = currentQuestion,
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = totalQuestions,
                    timeRemaining = timeRemaining,
                    initialTimeLimit = initialTimeLimit,
                    answers = answers,
                    showQuitConfirmation = showQuitConfirmation,
                    scope = scope,
                    isLandscape = isLandscape,
                    onAnswer = { answer ->
                        answers.add(answer)
                        Log.d("TestScreen", "User answer: $answer at index $currentQuestionIndex")
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            timeRemaining = initialTimeLimit
                        } else {
                            navController.currentBackStackEntry?.savedStateHandle?.set("questions", questions)
                            navController.currentBackStackEntry?.savedStateHandle?.set("answers", answers)
                            navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                            navController.navigate("results/$category/$timeLimit?playerName=$playerName")
                        }
                    },
                    onQuit = { showQuitConfirmation = true },
                    onConfirmQuit = {
                        showQuitConfirmation = false
                        navController.navigate("test_menu")
                    },
                    onDismissQuit = { showQuitConfirmation = false }
                )
            }
        }
    }
}

@Composable
private fun TestScreenCompactLayout(
    navController: NavController,
    category: String,
    currentQuestion: Question,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    timeRemaining: Int,
    initialTimeLimit: Int,
    answers: MutableList<String?>,
    showQuitConfirmation: Boolean,
    scope: CoroutineScope,
    isLandscape: Boolean,
    onAnswer: (String) -> Unit,
    onQuit: () -> Unit,
    onConfirmQuit: () -> Unit,
    onDismissQuit: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Transparent)
            ) {
                LinearProgressIndicator(
                    progress = { timeRemaining.toFloat() / initialTimeLimit.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp),
                    color = if (timeRemaining <= 3 && timeRemaining > 0) Color(0xFF8B0000) else Color(0xFF32CD32),
                    trackColor = Color.Transparent
                )
            }

            // Question number
            Text(
                text = "${currentQuestionIndex + 1}/$totalQuestions",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Image with scaling
            var scale by remember { mutableStateOf(1f) }
            var isGestureActive by remember { mutableStateOf(false) }
            val imagePath = getImagePath(category, currentQuestion)
            val bitmap = loadImageFromAssets(LocalContext.current, imagePath)
            bitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "Question Image",
                    modifier = Modifier
                        .size(
                            width = if (isLandscape) 390.dp else 416.dp,
                            height = if (isLandscape) 260.dp else 286.dp
                        )
                        .padding(16.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, _, zoom, _ ->
                                isGestureActive = true
                                scale = (scale * zoom).coerceIn(1f, 3f)
                            }
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    if (event.type == PointerEventType.Release && isGestureActive) {
                                        scale = 1f
                                        isGestureActive = false
                                    }
                                }
                            }
                        }
                )
            } ?: Text(
                text = "Image not found: ${currentQuestion.image}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Answer buttons
            currentQuestion.options.forEachIndexed { index, option ->
                Button(
                    onClick = { onAnswer(option) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "${index + 1}. $option",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        maxLines = 2
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Quit button
            Button(
                onClick = onQuit,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Quitter",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                )
            }

            // Quit confirmation dialog
            if (showQuitConfirmation) {
                AlertDialog(
                    onDismissRequest = onDismissQuit,
                    title = { Text("Confirmation", style = MaterialTheme.typography.headlineSmall) },
                    text = {
                        Text(
                            "Êtes-vous sûr?",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = onConfirmQuit,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Oui", style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = onDismissQuit,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Non", style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun TestScreenLargeLayout(
    navController: NavController,
    category: String,
    currentQuestion: Question,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    timeRemaining: Int,
    initialTimeLimit: Int,
    answers: MutableList<String?>,
    showQuitConfirmation: Boolean,
    scope: CoroutineScope,
    isLandscape: Boolean,
    onAnswer: (String) -> Unit,
    onQuit: () -> Unit,
    onConfirmQuit: () -> Unit,
    onDismissQuit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = if (isLandscape) 32.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
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

            // Question number
            Text(
                text = "${currentQuestionIndex + 1}/$totalQuestions",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Image with scaling
            var scale by remember { mutableStateOf(1f) }
            var isGestureActive by remember { mutableStateOf(false) }
            val imagePath = getImagePath(category, currentQuestion)
            val bitmap = loadImageFromAssets(LocalContext.current, imagePath)
            bitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "Question Image",
                    modifier = Modifier
                        .size(
                            width = if (isLandscape) 520.dp else 585.dp,
                            height = if (isLandscape) 390.dp else 455.dp
                        )
                        .padding(24.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                        .pointerInput(Unit) {
                            detectTransformGestures { _, _, zoom, _ ->
                                isGestureActive = true
                                scale = (scale * zoom).coerceIn(1f, 3f)
                            }
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    if (event.type == PointerEventType.Release && isGestureActive) {
                                        scale = 1f
                                        isGestureActive = false
                                    }
                                }
                            }
                        }
                )
            } ?: Text(
                text = "Image not found: ${currentQuestion.image}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(24.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Answer buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    currentQuestion.options.take(currentQuestion.options.size / 2).forEachIndexed { index, option ->
                        Button(
                            onClick = { onAnswer(option) },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(56.dp)
                                .padding(vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = "${index + 1}. $option",
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                maxLines = 2
                            )
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    currentQuestion.options.drop(currentQuestion.options.size / 2).forEachIndexed { index, option ->
                        Button(
                            onClick = { onAnswer(option) },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(56.dp)
                                .padding(vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = "${index + currentQuestion.options.size / 2 + 1}. $option",
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Quit button
            Button(
                onClick = onQuit,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(56.dp)
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Quitter",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                )
            }

            // Quit confirmation dialog
            if (showQuitConfirmation) {
                AlertDialog(
                    onDismissRequest = onDismissQuit,
                    title = { Text("Confirmation", style = MaterialTheme.typography.headlineMedium) },
                    text = {
                        Text(
                            "Êtes-vous sûr?",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = onConfirmQuit,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Oui", style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = onDismissQuit,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Non", style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}