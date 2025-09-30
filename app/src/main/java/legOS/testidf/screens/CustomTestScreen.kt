package legOS.testidf.screens

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import legOS.testidf.loadImageFromAssets
import java.io.IOException
import legOS.testidf.screens.TestDataHolder

data class CustomTestQuestion(
    val name: String,
    val category: String,
    val imagePath: String,
    val correctAnswer: String
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun CustomTestScreen(navController: NavController, questionCount: String, timeLimit: String) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var currentQuestionIndex by rememberSaveable { mutableStateOf(0) }
    val timeLimitInt = timeLimit.toIntOrNull() ?: 10
    var timeRemaining by rememberSaveable { mutableStateOf(timeLimitInt) }
    var answers by rememberSaveable { mutableStateOf(mutableListOf<String?>()) }
    var showQuitConfirmation by remember { mutableStateOf(false) }
    var currentAnswer by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Log.d("CustomTestScreen", "Starting custom test with $questionCount questions, $timeLimit seconds each")

    // Retrieve selected items from navigation state
    val selectedItems = remember {
        navController.previousBackStackEntry?.savedStateHandle?.get<List<CreationItem>>("selectedItems")
            ?: TestDataHolder.selectedItems
    }

// Сохраняем seed для воспроизводимого перемешивания
    val shuffleSeed by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

    val customQuestions = remember(shuffleSeed) {
        val random = kotlin.random.Random(shuffleSeed.toInt())

        selectedItems.map { item ->
            // Choose random image from main + additional images
            val allImages = listOf(item.mainImage) + item.additionalImages
            val imageRandom = kotlin.random.Random((shuffleSeed + item.name.hashCode().toLong()).toInt())
            val randomImage = allImages.random(imageRandom)

            CustomTestQuestion(
                name = item.name,
                category = item.category,
                imagePath = randomImage,
                correctAnswer = item.name
            )
        }.shuffled(random).take(questionCount.toIntOrNull() ?: selectedItems.size)
    }

    val totalQuestions = customQuestions.size

    // Timer logic - запускается только при смене вопроса
    var isTimerRunning by rememberSaveable { mutableStateOf(false) }
    var lastQuestionIndex by rememberSaveable { mutableStateOf(-1) }

    LaunchedEffect(currentQuestionIndex) {
        // Сбрасываем таймер только если это действительно новый вопрос
        if (currentQuestionIndex != lastQuestionIndex) {
            timeRemaining = timeLimitInt
            lastQuestionIndex = currentQuestionIndex
            isTimerRunning = true
        }

        // Запускаем таймер
        while (timeRemaining > 0 && currentQuestionIndex < customQuestions.size && isTimerRunning) {
            delay(1000L)
            timeRemaining--
        }

        if (timeRemaining <= 0 && currentQuestionIndex < customQuestions.size) {
            // Auto-submit current answer or null
            answers.add(currentAnswer.text.takeIf { it.isNotBlank() })
            currentAnswer = TextFieldValue("")
            isTimerRunning = false
            Log.d("CustomTestScreen", "Time up! Auto-submitted answer at index $currentQuestionIndex")

            if (currentQuestionIndex < customQuestions.size - 1) {
                currentQuestionIndex++
            } else {
                // Test finished - navigate to results
                Log.d("CustomTestScreen", "Custom test completed with ${answers.size} answers")
                navController.currentBackStackEntry?.savedStateHandle?.set("customQuestions", customQuestions)
                navController.currentBackStackEntry?.savedStateHandle?.set("customAnswers", answers)
                navController.navigate("custom_results/$questionCount/$timeLimit")
            }
        }
    }

    fun navigateToResults() {
        Log.d("CustomTestScreen", "Custom test completed with ${answers.size} answers")
        navController.currentBackStackEntry?.savedStateHandle?.set("customQuestions", customQuestions)
        navController.currentBackStackEntry?.savedStateHandle?.set("customAnswers", answers)
        navController.navigate("custom_results/$questionCount/$timeLimit")
    }

    fun submitAnswer() {
        val answer = currentAnswer.text.trim().takeIf { it.isNotBlank() }
        answers.add(answer)
        Log.d("CustomTestScreen", "User submitted answer: '$answer' at index $currentQuestionIndex")
        currentAnswer = TextFieldValue("")
        keyboardController?.hide()

        if (currentQuestionIndex < customQuestions.size - 1) {
            currentQuestionIndex++
        } else {
            navigateToResults()
        }
    }

    if (customQuestions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Aucune question trouvée",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Retour")
                }
            }
        }
        return
    }

    val currentQuestion = customQuestions.getOrNull(currentQuestionIndex) ?: return

    // Load background image
    val backgroundImage: androidx.compose.ui.graphics.ImageBitmap? = remember {
        try {
            context.assets.open("images/background_3.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("CustomTestScreen", "Error loading background_3.jpg", e)
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
        backgroundImage?.let { image ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (isLandscape) {
            // Горизонтальная ориентация - специальный layout
            CustomTestLandscapeLayout(
                navController = navController,
                currentQuestion = currentQuestion,
                currentQuestionIndex = currentQuestionIndex,
                totalQuestions = totalQuestions,
                timeRemaining = timeRemaining,
                timeLimitInt = timeLimitInt,
                currentAnswer = currentAnswer,
                showQuitConfirmation = showQuitConfirmation,
                onAnswerChange = { currentAnswer = it },
                onSubmitAnswer = { submitAnswer() },
                onQuit = { showQuitConfirmation = true },
                onConfirmQuit = {
                    showQuitConfirmation = false
                    navController.navigate("test_menu")
                },
                onDismissQuit = { showQuitConfirmation = false }
            )
        } else {
            // Вертикальная ориентация
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    CustomTestCompactLayout(
                        navController = navController,
                        currentQuestion = currentQuestion,
                        currentQuestionIndex = currentQuestionIndex,
                        totalQuestions = totalQuestions,
                        timeRemaining = timeRemaining,
                        timeLimitInt = timeLimitInt,
                        currentAnswer = currentAnswer,
                        showQuitConfirmation = showQuitConfirmation,
                        isLandscape = false,
                        onAnswerChange = { currentAnswer = it },
                        onSubmitAnswer = { submitAnswer() },
                        onQuit = { showQuitConfirmation = true },
                        onConfirmQuit = {
                            showQuitConfirmation = false
                            navController.navigate("test_menu")
                        },
                        onDismissQuit = { showQuitConfirmation = false }
                    )
                }
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                    CustomTestLargeLayout(
                        navController = navController,
                        currentQuestion = currentQuestion,
                        currentQuestionIndex = currentQuestionIndex,
                        totalQuestions = totalQuestions,
                        timeRemaining = timeRemaining,
                        timeLimitInt = timeLimitInt,
                        currentAnswer = currentAnswer,
                        showQuitConfirmation = showQuitConfirmation,
                        isLandscape = false,
                        onAnswerChange = { currentAnswer = it },
                        onSubmitAnswer = { submitAnswer() },
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

private fun CoroutineScope.navigateToResults() {
    TODO("Not yet implemented")
}

@Composable
private fun CustomTestLandscapeLayout(
    navController: NavController,
    currentQuestion: CustomTestQuestion,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    timeRemaining: Int,
    timeLimitInt: Int,
    currentAnswer: TextFieldValue,
    showQuitConfirmation: Boolean,
    onAnswerChange: (TextFieldValue) -> Unit,
    onSubmitAnswer: () -> Unit,
    onQuit: () -> Unit,
    onConfirmQuit: () -> Unit,
    onDismissQuit: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // ЛЕВАЯ ЧАСТЬ - ИЗОБРАЖЕНИЕ (55% экрана)
        Box(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
                .padding(end = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            var scale by remember { mutableStateOf(1f) }
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
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

            val bitmap = loadImageFromAssets(LocalContext.current, currentQuestion.imagePath)
            bitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "Question Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
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
                text = "Image not found: ${currentQuestion.imagePath}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        // ПРАВАЯ ЧАСТЬ - УПРАВЛЕНИЕ (45% экрана)
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Transparent)
            ) {
                LinearProgressIndicator(
                    progress = { timeRemaining.toFloat() / timeLimitInt.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp),
                    color = if (timeRemaining <= 3 && timeRemaining > 0) Color(0xFF8B0000) else Color(0xFF32CD32),
                    trackColor = Color.Transparent
                )
            }

            // Счетчик вопросов и категория
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "${currentQuestionIndex + 1}/$totalQuestions",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp)
                )
                Text(
                    text = "Catégorie: ${currentQuestion.category}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(16.dp))

            // Поле ввода ответа
            OutlinedTextField(
                value = currentAnswer,
                onValueChange = onAnswerChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = { Text("Votre réponse") },
                placeholder = { Text("Entrez le nom de l'équipement...") },
                trailingIcon = {
                    if (currentAnswer.text.isNotEmpty()) {
                        IconButton(onClick = { onAnswerChange(TextFieldValue("")) }) {
                            Icon(Icons.Default.Clear, contentDescription = "Effacer")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { onSubmitAnswer() }
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
            )

            Spacer(Modifier.height(16.dp))

            // Кнопка "Valider"
            Button(
                onClick = onSubmitAnswer,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Valider",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Кнопка "Quitter"
            Button(
                onClick = onQuit,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
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

            // Диалог подтверждения выхода
            if (showQuitConfirmation) {
                AlertDialog(
                    onDismissRequest = onDismissQuit,
                    title = { Text("Confirmation", style = MaterialTheme.typography.headlineSmall) },
                    text = {
                        Text(
                            "Êtes-vous sûr de vouloir quitter le test?",
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
private fun CustomTestCompactLayout(
    navController: NavController,
    currentQuestion: CustomTestQuestion,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    timeRemaining: Int,
    timeLimitInt: Int,
    currentAnswer: TextFieldValue,
    showQuitConfirmation: Boolean,
    isLandscape: Boolean,
    onAnswerChange: (TextFieldValue) -> Unit,
    onSubmitAnswer: () -> Unit,
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
                    progress = { timeRemaining.toFloat() / timeLimitInt.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp),
                    color = if (timeRemaining <= 3 && timeRemaining > 0) Color(0xFF8B0000) else Color(0xFF32CD32),
                    trackColor = Color.Transparent
                )
            }

            // Question number and category
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "${currentQuestionIndex + 1}/$totalQuestions",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                )
                Text(
                    text = "Catégorie: ${currentQuestion.category}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Image with scaling and panning
            var scale by remember { mutableStateOf(1f) }
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
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

            val bitmap = loadImageFromAssets(LocalContext.current, currentQuestion.imagePath)
            bitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "Question Image",
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
                text = "Image not found: ${currentQuestion.imagePath}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Answer input field
            OutlinedTextField(
                value = currentAnswer,
                onValueChange = onAnswerChange,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                label = { Text("Votre réponse") },
                placeholder = { Text("Entrez le nom de l'équipement...") },
                trailingIcon = {
                    if (currentAnswer.text.isNotEmpty()) {
                        IconButton(onClick = { onAnswerChange(TextFieldValue("")) }) {
                            Icon(Icons.Default.Clear, contentDescription = "Effacer")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { onSubmitAnswer() }
                ),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // Submit button
            Button(
                onClick = onSubmitAnswer,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Valider",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Quit button
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
                            "Êtes-vous sûr de vouloir quitter le test?",
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
private fun CustomTestLargeLayout(
    navController: NavController,
    currentQuestion: CustomTestQuestion,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    timeRemaining: Int,
    timeLimitInt: Int,
    currentAnswer: TextFieldValue,
    showQuitConfirmation: Boolean,
    isLandscape: Boolean,
    onAnswerChange: (TextFieldValue) -> Unit,
    onSubmitAnswer: () -> Unit,
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
                    progress = { timeRemaining.toFloat() / timeLimitInt.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    color = if (timeRemaining <= 3 && timeRemaining > 0) Color(0xFF8B0000) else Color(0xFF32CD32),
                    trackColor = Color.Transparent
                )
            }

            // Question number and category
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Text(
                    text = "${currentQuestionIndex + 1}/$totalQuestions",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp)
                )
                Text(
                    text = "Catégorie: ${currentQuestion.category}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Image with scaling and panning
            var scale by remember { mutableStateOf(1f) }
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
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

            val bitmap = loadImageFromAssets(LocalContext.current, currentQuestion.imagePath)
            bitmap?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "Question Image",
                    modifier = Modifier
                        .size(
                            width = if (isLandscape) 585.dp else 530.dp,
                            height = if (isLandscape) 455.dp else 390.dp
                        )
                        .padding(24.dp)
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
                text = "Image not found: ${currentQuestion.imagePath}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(24.dp)
            )

            Spacer(Modifier.height(20.dp))

            // Answer input field
            OutlinedTextField(
                value = currentAnswer,
                onValueChange = onAnswerChange,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 12.dp),
                label = { Text("Votre réponse") },
                placeholder = { Text("Entrez le nom de l'équipement...") },
                trailingIcon = {
                    if (currentAnswer.text.isNotEmpty()) {
                        IconButton(onClick = { onAnswerChange(TextFieldValue("")) }) {
                            Icon(Icons.Default.Clear, contentDescription = "Effacer")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { onSubmitAnswer() }
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Submit button
                Button(
                    onClick = onSubmitAnswer,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Valider",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                }

                // Quit button
                Button(
                    onClick = onQuit,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
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
            }

            // Quit confirmation dialog
            if (showQuitConfirmation) {
                AlertDialog(
                    onDismissRequest = onDismissQuit,
                    title = { Text("Confirmation", style = MaterialTheme.typography.headlineMedium) },
                    text = {
                        Text(
                            "Êtes-vous sûr de vouloir quitter le test?",
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
