package legOS.testidf.screens

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.Question
import legOS.testidf.R
import legOS.testidf.loadImageFromAssets
import legOS.testidf.getImagePath
import legOS.testidf.saveScore

@Composable
fun ResultsScreen(navController: NavController, category: String, timeLimit: Int) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val navBackStackEntry = navController.previousBackStackEntry ?: return
    val questions = navBackStackEntry.savedStateHandle.get<List<Question>>("questions") ?: emptyList()
    val answers = navBackStackEntry.savedStateHandle.get<MutableList<String?>>("answers") ?: mutableListOf()
    val playerName = navBackStackEntry.arguments?.getString("playerName")
        ?: navBackStackEntry.savedStateHandle.get<String>("playerName")
        ?: run {
            Log.w("ResultsScreen", "Player name not found, using default Anonyme")
            "Anonyme"
        }
    Log.d("ResultsScreen", "Using player name: $playerName")
    Log.d("ResultsScreen", "Player name from savedStateHandle: ${navBackStackEntry.savedStateHandle.get<String>("playerName")}")
    Log.d("ResultsScreen", "Player name from arguments: ${navBackStackEntry.arguments?.getString("playerName")}")

    val correctAnswers = questions.zip(answers).count { (question, answer) ->
        answer == question.correct
    }
    Log.d("ResultsScreen", "Calculated correct answers: $correctAnswers")

    var scoreSaved by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (category == "final" && !scoreSaved) {
            Log.d("ResultsScreen", "Attempting to save score for $playerName with $correctAnswers")
            saveScore(context, playerName, correctAnswers)
            scoreSaved = true
            Log.d("ResultsScreen", "Score saving completed for $playerName")
        }
    }

    // Load the background image
    val backgroundImage = loadImageFromAssets(context, "images/background_6.png")

    // Выбираем компоновку в зависимости от ориентации
    if (isLandscape) {
        ResultsLandscapeLayout(
            navController = navController,
            category = category,
            timeLimit = timeLimit,
            questions = questions,
            answers = answers,
            correctAnswers = correctAnswers,
            backgroundImage = backgroundImage,
            context = context
        )
    } else {
        ResultsPortraitLayout(
            navController = navController,
            category = category,
            timeLimit = timeLimit,
            questions = questions,
            answers = answers,
            correctAnswers = correctAnswers,
            backgroundImage = backgroundImage,
            context = context
        )
    }
}

// Новый компонент для горизонтального режима
@Composable
private fun ResultsLandscapeLayout(
    navController: NavController,
    category: String,
    timeLimit: Int,
    questions: List<Question>,
    answers: MutableList<String?>,
    correctAnswers: Int,
    backgroundImage: android.graphics.Bitmap?,
    context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .then(
                backgroundImage?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it.asImageBitmap()),
                        contentScale = ContentScale.Crop
                    )
                } ?: Modifier.background(MaterialTheme.colorScheme.background)
            )
            .systemBarsPadding() // Éviter la superposition avec la barre d'état et la caméra
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Левая часть - полные карточки с изображениями и описанием
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(questions.size) { index ->
                    val question = questions[index]
                    val userAnswer = answers.getOrNull(index)
                    val isCorrect = userAnswer == question.correct

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCorrect)
                                Color(0xFF90EE90).copy(alpha = 0.85f) // Transparence de 15% sur le fond uniquement
                            else
                                Color(0xFFFFB6C1).copy(alpha = 0.85f) // Transparence de 15% sur le fond uniquement
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Изображение
                            val imagePath = getImagePath(category, question)
                            val bitmap = loadImageFromAssets(context, imagePath)
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = stringResource(R.string.results_question_label, index + 1),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 9f)
                                        .clip(MaterialTheme.shapes.medium),
                                    contentScale = ContentScale.Crop
                                )
                            } ?: Text(
                                stringResource(R.string.results_image_not_found, question.image),
                                modifier = Modifier.padding(8.dp)
                            )

                            // Текстовая информация
                            Text(
                                stringResource(R.string.results_question_label, index + 1),
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp)
                            )
                            Text(
                                stringResource(R.string.results_correct_label, question.correct),
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                            )
                            Text(
                                stringResource(R.string.results_your_answer_label, userAnswer ?: stringResource(R.string.results_no_answer)),
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                            )

                            // Кнопка "Plus d'infos" для каждой карточки
                            if (category !in listOf("tanks", "artillery", "recon", "genie", "air")) {
                                Button(
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set("questions", questions)
                                        navController.navigate("more_info/$category/$index/$timeLimit")
                                    },
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .height(32.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        stringResource(R.string.results_more_info_button),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Правая часть - общая информация и основные кнопки навигации
        Column(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .padding(end = 32.dp, top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок со счетом
            Text(
                stringResource(R.string.results_score, correctAnswers, questions.size),
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Дополнительная информация о результатах
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(bottom = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f) // Transparence de 15% sur le fond uniquement
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val percentage = (correctAnswers.toFloat() / questions.size * 100).toInt()
                    Text(
                        stringResource(R.string.results_percentage_label, percentage),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        stringResource(R.string.results_incorrect_answers, questions.size - correctAnswers),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Основные кнопки навигации
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Button(
                    onClick = {
                        if (category == "final") {
                            // ДЛЯ FINAL TEST: Переход на PlayerNameScreen
                            Log.d("ResultsScreen", "Recommencer Final Test - navigating to player_name")
                            navController.navigate("player_name") {
                                popUpTo("test_menu") { inclusive = false }
                            }
                        } else {
                            // ДЛЯ ДРУГИХ КАТЕГОРИЙ: Переход на time_selection
                            navController.navigate("time_selection/$category")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        stringResource(R.string.results_restart_button),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                    )
                }

                Button(
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        stringResource(R.string.results_return_button),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// Старая компоновка для портретного режима
@Composable
private fun ResultsPortraitLayout(
    navController: NavController,
    category: String,
    timeLimit: Int,
    questions: List<Question>,
    answers: MutableList<String?>,
    correctAnswers: Int,
    backgroundImage: android.graphics.Bitmap?,
    context: Context
) {
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
            )
            .padding(16.dp)
            .systemBarsPadding(), // Éviter la superposition avec la barre d'état et la caméra
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp)) // Espace léger en haut pour éviter la superposition

        Text(
            stringResource(R.string.results_score, correctAnswers, questions.size),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyColumn(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questions.size) { index ->
                val question = questions[index]
                val userAnswer = answers.getOrNull(index)
                val isCorrect = userAnswer == question.correct

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect)
                            Color(0xFF90EE90).copy(alpha = 0.85f) // Transparence de 15% sur le fond uniquement
                        else
                            Color(0xFFFFB6C1).copy(alpha = 0.85f) // Transparence de 15% sur le fond uniquement
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val imagePath = getImagePath(category, question)
                        val bitmap = loadImageFromAssets(context, imagePath)
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = stringResource(R.string.results_question_label, index + 1),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f)
                                    .clip(MaterialTheme.shapes.medium)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Text(stringResource(R.string.results_image_not_found, question.image))

                        Text(
                            stringResource(R.string.results_question_label, index + 1),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            stringResource(R.string.results_correct_label, question.correct),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            stringResource(R.string.results_your_answer_label, userAnswer ?: stringResource(R.string.results_no_answer)),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (category !in listOf("tanks", "artillery", "recon", "genie", "air")) {
                            Button(
                                onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("questions", questions)
                                    navController.navigate("more_info/$category/$index/$timeLimit")
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(stringResource(R.string.results_more_info_button))
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    if (category == "final") {
                        // ДЛЯ FINAL TEST: Переход на PlayerNameScreen
                        Log.d("ResultsScreen", "Recommencer Final Test - navigating to PlayerNameScreen")
                        navController.navigate("player_name") {
                            popUpTo("test_menu") { inclusive = false }
                        }
                    } else {
                        // ДЛЯ ДРУГИХ КАТЕГОРИЙ: Переход на time_selection
                        navController.navigate("time_selection/$category")
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(stringResource(R.string.results_restart_button), style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp))
            }

            Button(
                onClick = { navController.navigate("test_menu") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(stringResource(R.string.results_return_button), style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}