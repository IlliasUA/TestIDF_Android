package legOS.testidf.screens

import legOS.testidf.withRandomImage
import legOS.testidf.getRandomImageForQuestion
import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import legOS.testidf.R
import java.io.IOException

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TestScreen(navController: NavController, category: String, timeLimit: Int) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scope = rememberCoroutineScope()

    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var initialTimeLimit by rememberSaveable { mutableIntStateOf(timeLimit) }
    var timeRemaining by rememberSaveable { mutableIntStateOf(timeLimit) }
    var answers by rememberSaveable { mutableStateOf(mutableListOf<String?>()) }
    var showQuitConfirmation by rememberSaveable { mutableStateOf(false) }

    Log.d("TestScreen", "Received category: $category, timeLimit: $timeLimit")

    val questions = rememberSaveable {
        val baseQuestions = when (category) {
            "tanks" -> Test_Data.QUESTION
            "artillery" -> Art_Data.QUESTION
            "recon" -> Recon_Data.QUESTION
            "genie" -> Genie_Data.QUESTION
            "air" -> Air_Data.QUESTION
            "bm2" -> Test_bm2.QUESTION
            "final" -> buildFinalTestQuestions()
            else -> Test_Data.QUESTION
        }

        baseQuestions
            .map { it.withRandomImage() }
            .shuffled()
            .take(if (category == "final") 40 else if (category == "bm2") 20 else 10)
    }

    val totalQuestions = questions.size
    val playerName = navController.previousBackStackEntry?.savedStateHandle?.get<String>("playerName")
        ?: navController.previousBackStackEntry?.arguments?.getString("playerName")
        ?: stringResource(R.string.test_player_name_default)

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
    ) {
        backgroundImage?.let { image: androidx.compose.ui.graphics.ImageBitmap ->
            Image(
                bitmap = image,
                contentDescription = stringResource(R.string.test_background_image),
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
                TestScreenLandscapeLayout(
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
                    questions = questions,
                    playerName = playerName,
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
            } else {
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
                            isLandscape = false,
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
                            isLandscape = false,
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

            Text(
                text = "${currentQuestionIndex + 1}/$totalQuestions",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            var scale by remember { mutableFloatStateOf(1f) }
            var offsetX by remember { mutableFloatStateOf(0f) }
            var offsetY by remember { mutableFloatStateOf(0f) }
            var isGestureActive by remember { mutableStateOf(false) }
            val animatedScale by animateFloatAsState(
                targetValue = scale,
                animationSpec = tween(durationMillis = 300),
                label = "scaleAnimation"
            )
            val animatedOffsetX by animateFloatAsState(
                targetValue = offsetX,
                animationSpec = tween(durationMillis = 300),
                label = "offsetXAnimation"
            )
            val animatedOffsetY by animateFloatAsState(
                targetValue = offsetY,
                animationSpec = tween(durationMillis = 300),
                label = "offsetYAnimation"
            )
            val imagePath = getImagePath(category, currentQuestion)
            val bitmap = loadImageFromAssets(LocalContext.current, imagePath)
            bitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = stringResource(R.string.test_question_image),
                    modifier = Modifier
                        .size(
                            width = if (isLandscape) 390.dp else 430.dp,
                            height = if (isLandscape) 260.dp else 350.dp
                        )
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
                                    val maxOffsetX = (size.width * (scale - 1f)) / 2
                                    val maxOffsetY = (size.height * (scale - 1f)) / 2
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
                text = stringResource(R.string.test_image_not_found, currentQuestion.image),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(Modifier.height(12.dp))

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
                        text = option,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        maxLines = 2
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

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
                    text = stringResource(R.string.test_quit_button),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                )
            }

            if (showQuitConfirmation) {
                AlertDialog(
                    onDismissRequest = onDismissQuit,
                    title = { Text(stringResource(R.string.test_quit_confirmation_title), style = MaterialTheme.typography.headlineSmall) },
                    text = {
                        Text(
                            stringResource(R.string.test_quit_confirmation_message),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = onConfirmQuit,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(stringResource(R.string.test_quit_yes), style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = onDismissQuit,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(stringResource(R.string.test_quit_no), style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun TestScreenLandscapeLayout(
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
    questions: List<Question>,
    playerName: String,
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
            var scale by remember { mutableFloatStateOf(1f) }
            var offsetX by remember { mutableFloatStateOf(0f) }
            var offsetY by remember { mutableFloatStateOf(0f) }
            var isGestureActive by remember { mutableStateOf(false) }

            val animatedScale by animateFloatAsState(
                targetValue = scale,
                animationSpec = tween(durationMillis = 300),
                label = "scaleAnimation"
            )
            val animatedOffsetX by animateFloatAsState(
                targetValue = offsetX,
                animationSpec = tween(durationMillis = 300),
                label = "offsetXAnimation"
            )
            val animatedOffsetY by animateFloatAsState(
                targetValue = offsetY,
                animationSpec = tween(durationMillis = 300),
                label = "offsetYAnimation"
            )
            val imagePath = getImagePath(category, currentQuestion)
            val bitmap = loadImageFromAssets(LocalContext.current, imagePath)
            bitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = stringResource(R.string.test_question_image),
                    modifier = Modifier
                        .fillMaxSize()
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
                                    val maxOffsetX = (size.width * (scale - 1f)) / 2
                                    val maxOffsetY = (size.height * (scale - 1f)) / 2
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
                        },
                    contentScale = ContentScale.Fit
                )
            } ?: Text(
                text = stringResource(R.string.test_image_not_found, currentQuestion.image),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.test_question_progress, currentQuestionIndex + 1, totalQuestions),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.test_time_remaining, timeRemaining),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    color = if (timeRemaining <= 5) Color.Red else MaterialTheme.colorScheme.onSurface
                )
                LinearProgressIndicator(
                    progress = { timeRemaining.toFloat() / initialTimeLimit },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp),
                    color = if (timeRemaining <= 5) Color.Red else MaterialTheme.colorScheme.primary
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(currentQuestion.options.size) { index ->
                    Button(
                        onClick = { onAnswer(currentQuestion.options[index]) },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = currentQuestion.options[index],
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            maxLines = 2
                        )
                    }
                }
            }

            Button(
                onClick = onQuit,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.test_quit_button),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
            }

            if (showQuitConfirmation) {
                AlertDialog(
                    onDismissRequest = onDismissQuit,
                    title = { Text(stringResource(R.string.test_quit_confirmation_title), style = MaterialTheme.typography.headlineMedium) },
                    text = {
                        Text(
                            stringResource(R.string.test_quit_confirmation_message),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = onConfirmQuit,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(stringResource(R.string.test_quit_yes), style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = onDismissQuit,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(stringResource(R.string.test_quit_no), style = MaterialTheme.typography.labelLarge)
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
    // Identique à TestScreenCompactLayout pour le moment
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
        onAnswer = onAnswer,
        onQuit = onQuit,
        onConfirmQuit = onConfirmQuit,
        onDismissQuit = onDismissQuit
    )
}