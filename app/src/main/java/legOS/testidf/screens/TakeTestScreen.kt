package legOS.testidf.screens

import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quizapp.*
import kotlinx.coroutines.delay
import legOS.testidf.loadImageFromAssets
import legOS.testidf.viewmodel.TakeTestViewModel
import java.io.IOException

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TakeTestScreen(
    navController: NavController,
    sessionId: String,
    viewModel: TakeTestViewModel = viewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val uiState by viewModel.uiState.collectAsState()

    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var timeRemaining by rememberSaveable { mutableIntStateOf(0) }
    var answers by rememberSaveable { mutableStateOf(mutableListOf<String?>()) }
    var showQuitConfirmation by rememberSaveable { mutableStateOf(false) }
    var isSubmitting by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(sessionId) {
        viewModel.loadTestQuestions(sessionId)
    }

    LaunchedEffect(uiState.questions.isNotEmpty(), currentQuestionIndex) {
        if (timeRemaining == 0 && uiState.timeLimit > 0) {
            timeRemaining = uiState.timeLimit
        }
    }

    LaunchedEffect(currentQuestionIndex, uiState.questions.size, isSubmitting) {
        if (uiState.questions.isEmpty() || isSubmitting) return@LaunchedEffect

        while (timeRemaining > 0 && currentQuestionIndex < uiState.questions.size && !isSubmitting) {
            delay(1000L)
            timeRemaining--
        }

        if (timeRemaining <= 0 && currentQuestionIndex < uiState.questions.size && !isSubmitting) {
            answers.add(null)
            Log.d("TakeTestScreen", "Auto answer: null at index $currentQuestionIndex")

            if (currentQuestionIndex < uiState.questions.size - 1) {
                currentQuestionIndex++
                timeRemaining = uiState.timeLimit
            } else {
                if (!isSubmitting) {
                    isSubmitting = true
                    submitTest(viewModel, sessionId, answers, navController)
                }
            }
        }
    }

    val backgroundImage = remember {
        try {
            context.assets.open("images/background_6.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            null
        }
    }

    if (uiState.isLoading || isSubmitting) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                if (isSubmitting) {
                    Spacer(Modifier.height(16.dp))
                    Text("Envoi des résultats...")
                }
            }
        }
        return
    }

    if (uiState.error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Erreur: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = { navController.navigateUp() }) {
                    Text("Retour")
                }
            }
        }
        return
    }

    if (uiState.questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Aucune question disponible")
        }
        return
    }

    val currentQuestion = uiState.questions[currentQuestionIndex]

    Box(modifier = Modifier.fillMaxSize()) {
        backgroundImage?.let { image ->
            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            if (isLandscape) {
                TakeTestLandscapeLayout(
                    currentQuestion = currentQuestion,
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = uiState.questions.size,
                    timeRemaining = timeRemaining,
                    initialTimeLimit = uiState.timeLimit,
                    showQuitConfirmation = showQuitConfirmation,
                    isSubmitting = isSubmitting,
                    onAnswer = { answer ->
                        if (isSubmitting) return@TakeTestLandscapeLayout

                        answers.add(answer)
                        Log.d("TakeTestScreen", "User answer: $answer at index $currentQuestionIndex")

                        if (currentQuestionIndex < uiState.questions.size - 1) {
                            currentQuestionIndex++
                            timeRemaining = uiState.timeLimit
                        } else {
                            if (!isSubmitting) {
                                isSubmitting = true
                                submitTest(viewModel, sessionId, answers, navController)
                            }
                        }
                    },
                    onQuit = { showQuitConfirmation = true },
                    onConfirmQuit = {
                        showQuitConfirmation = false
                        navController.navigate("participant_waiting") {
                            popUpTo("participant_waiting") { inclusive = true }
                        }
                    },
                    onDismissQuit = { showQuitConfirmation = false }
                )
            } else {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        TakeTestCompactLayout(
                            currentQuestion = currentQuestion,
                            currentQuestionIndex = currentQuestionIndex,
                            totalQuestions = uiState.questions.size,
                            timeRemaining = timeRemaining,
                            initialTimeLimit = uiState.timeLimit,
                            showQuitConfirmation = showQuitConfirmation,
                            isSubmitting = isSubmitting,
                            onAnswer = { answer ->
                                if (isSubmitting) return@TakeTestCompactLayout

                                answers.add(answer)

                                if (currentQuestionIndex < uiState.questions.size - 1) {
                                    currentQuestionIndex++
                                    timeRemaining = uiState.timeLimit
                                } else {
                                    if (!isSubmitting) {
                                        isSubmitting = true
                                        submitTest(viewModel, sessionId, answers, navController)
                                    }
                                }
                            },
                            onQuit = { showQuitConfirmation = true },
                            onConfirmQuit = {
                                showQuitConfirmation = false
                                navController.navigate("participant_waiting") {
                                    popUpTo("participant_waiting") { inclusive = true }
                                }
                            },
                            onDismissQuit = { showQuitConfirmation = false }
                        )
                    }
                    else -> {
                        TakeTestLargeLayout(
                            currentQuestion = currentQuestion,
                            currentQuestionIndex = currentQuestionIndex,
                            totalQuestions = uiState.questions.size,
                            timeRemaining = timeRemaining,
                            initialTimeLimit = uiState.timeLimit,
                            showQuitConfirmation = showQuitConfirmation,
                            isSubmitting = isSubmitting,
                            onAnswer = { answer ->
                                if (isSubmitting) return@TakeTestLargeLayout

                                answers.add(answer)

                                if (currentQuestionIndex < uiState.questions.size - 1) {
                                    currentQuestionIndex++
                                    timeRemaining = uiState.timeLimit
                                } else {
                                    if (!isSubmitting) {
                                        isSubmitting = true
                                        submitTest(viewModel, sessionId, answers, navController)
                                    }
                                }
                            },
                            onQuit = { showQuitConfirmation = true },
                            onConfirmQuit = {
                                showQuitConfirmation = false
                                navController.navigate("participant_waiting") {
                                    popUpTo("participant_waiting") { inclusive = true }
                                }
                            },
                            onDismissQuit = { showQuitConfirmation = false }
                        )
                    }
                }
            }
        }
    }
}

private fun submitTest(
    viewModel: TakeTestViewModel,
    sessionId: String,
    answers: List<String?>,
    navController: NavController
) {
    viewModel.submitTestResults(
        sessionId = sessionId,
        answers = answers
    ) { success ->
        if (success) {
            navController.navigate("test_completed") {
                popUpTo("participant_waiting") { inclusive = true }
            }
        }
    }
}

@Composable
private fun TakeTestLandscapeLayout(
    currentQuestion: Question,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    timeRemaining: Int,
    initialTimeLimit: Int,
    showQuitConfirmation: Boolean,
    isSubmitting: Boolean,
    onAnswer: (String) -> Unit,
    onQuit: () -> Unit,
    onConfirmQuit: () -> Unit,
    onDismissQuit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            QuestionImage(currentQuestion)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { timeRemaining.toFloat() / initialTimeLimit.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .padding(vertical = 8.dp),
                color = if (timeRemaining <= 3) Color(0xFF8B0000) else Color(0xFF32CD32)
            )

            Text(
                text = "${currentQuestionIndex + 1}/$totalQuestions",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentQuestion.options.take((currentQuestion.options.size + 1) / 2).size) { index ->
                        AnswerButton(
                            text = currentQuestion.options[index],
                            onClick = { onAnswer(currentQuestion.options[index]) },
                            enabled = !isSubmitting
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currentQuestion.options.drop((currentQuestion.options.size + 1) / 2).size) { index ->
                        val realIndex = index + (currentQuestion.options.size + 1) / 2
                        AnswerButton(
                            text = currentQuestion.options[realIndex],
                            onClick = { onAnswer(currentQuestion.options[realIndex]) },
                            enabled = !isSubmitting
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            QuitButton(onClick = onQuit, enabled = !isSubmitting)
        }
    }

    QuitDialog(
        show = showQuitConfirmation,
        onConfirm = onConfirmQuit,
        onDismiss = onDismissQuit
    )
}

@Composable
private fun TakeTestCompactLayout(
    currentQuestion: Question,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    timeRemaining: Int,
    initialTimeLimit: Int,
    showQuitConfirmation: Boolean,
    isSubmitting: Boolean,
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
            LinearProgressIndicator(
                progress = { timeRemaining.toFloat() / initialTimeLimit.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .padding(vertical = 8.dp),
                color = if (timeRemaining <= 3) Color(0xFF8B0000) else Color(0xFF32CD32)
            )

            Text(
                text = "${currentQuestionIndex + 1}/$totalQuestions",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            QuestionImage(currentQuestion)

            Spacer(Modifier.height(12.dp))

            currentQuestion.options.forEach { option ->
                AnswerButton(
                    text = option,
                    onClick = { onAnswer(option) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 4.dp),
                    enabled = !isSubmitting
                )
            }

            Spacer(Modifier.height(12.dp))

            QuitButton(
                onClick = onQuit,
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = !isSubmitting
            )
        }
    }

    QuitDialog(
        show = showQuitConfirmation,
        onConfirm = onConfirmQuit,
        onDismiss = onDismissQuit
    )
}

@Composable
private fun TakeTestLargeLayout(
    currentQuestion: Question,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    timeRemaining: Int,
    initialTimeLimit: Int,
    showQuitConfirmation: Boolean,
    isSubmitting: Boolean,
    onAnswer: (String) -> Unit,
    onQuit: () -> Unit,
    onConfirmQuit: () -> Unit,
    onDismissQuit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { timeRemaining.toFloat() / initialTimeLimit.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(vertical = 12.dp),
                color = if (timeRemaining <= 3) Color(0xFF8B0000) else Color(0xFF32CD32)
            )

            Text(
                text = "${currentQuestionIndex + 1}/$totalQuestions",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            QuestionImage(currentQuestion, size = 530.dp to 390.dp)

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    currentQuestion.options.take(currentQuestion.options.size / 2).forEach { option ->
                        AnswerButton(
                            text = option,
                            onClick = { onAnswer(option) },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(56.dp),
                            enabled = !isSubmitting
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    currentQuestion.options.drop(currentQuestion.options.size / 2).forEach { option ->
                        AnswerButton(
                            text = option,
                            onClick = { onAnswer(option) },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(56.dp),
                            enabled = !isSubmitting
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            QuitButton(
                onClick = onQuit,
                modifier = Modifier.fillMaxWidth(0.6f),
                enabled = !isSubmitting
            )
        }
    }

    QuitDialog(
        show = showQuitConfirmation,
        onConfirm = onConfirmQuit,
        onDismiss = onDismissQuit
    )
}

@Composable
private fun QuestionImage(
    question: Question,
    size: Pair<androidx.compose.ui.unit.Dp, androidx.compose.ui.unit.Dp> = 430.dp to 350.dp
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var isGestureActive by remember { mutableStateOf(false) }

    val animatedScale by animateFloatAsState(scale, tween(300))
    val animatedOffsetX by animateFloatAsState(offsetX, tween(300))
    val animatedOffsetY by animateFloatAsState(offsetY, tween(300))

    val context = LocalContext.current
    val imagePath = remember(question) { question.image }
    val bitmap = loadImageFromAssets(context, imagePath)

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Question Image",
            modifier = Modifier
                .size(size.first, size.second)
                .padding(16.dp)
                .graphicsLayer(
                    scaleX = animatedScale,
                    scaleY = animatedScale,
                    translationX = animatedOffsetX,
                    translationY = animatedOffsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        isGestureActive = true
                        scale = (scale * zoom).coerceIn(1f, 3f)
                        if (scale > 1f) {
                            offsetX += pan.x
                            offsetY += pan.y
                            val maxOffsetX = (size.first.value * (scale - 1f)) / 2
                            val maxOffsetY = (size.second.value * (scale - 1f)) / 2
                            offsetX = offsetX.coerceIn(-maxOffsetX, maxOffsetX)
                            offsetY = offsetY.coerceIn(-maxOffsetY, maxOffsetY)
                        }
                    }
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Release && isGestureActive) {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                                isGestureActive = false
                            }
                        }
                    }
                }
        )
    } ?: Text(
        text = "Image non disponible",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
private fun AnswerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun QuitButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        Text("Quitter", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun QuitDialog(
    show: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirmation") },
            text = { Text("Êtes-vous sûr?", textAlign = TextAlign.Center) },
            confirmButton = {
                TextButton(onClick = onConfirm) { Text("Oui") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Non") }
            }
        )
    }
}