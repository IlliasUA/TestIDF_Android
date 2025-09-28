package legOS.testidf.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.Question
import legOS.testidf.loadImageFromAssets
import legOS.testidf.getImagePath
import legOS.testidf.saveScore

@Composable
fun ResultsScreen(navController: NavController, category: String, timeLimit: Int) {
    val context = LocalContext.current
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
                } ?: Modifier.background(MaterialTheme.colorScheme.background) // Fallback to default background if image fails to load
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Score: $correctAnswers / ${questions.size}",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyColumn(
            modifier = Modifier.weight(0.4f), // Еще больше уменьшаем weight с 0.7f до 0.4f
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(questions.size) { index ->
                val question = questions[index]
                val userAnswer = answers.getOrNull(index)
                val isCorrect = userAnswer == question.correct

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) Color(0xFF90EE90) else Color(0xFFFFB6C1)
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
                                contentDescription = "Question Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f)
                                    .clip(MaterialTheme.shapes.medium)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Text("Image not found: ${question.image}")

                        Text(
                            "Question ${index + 1}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Correct: ${question.correct}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "Votre réponse: ${userAnswer ?: "Aucune"}",
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
                                Text("Plus d'infos")
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
                onClick = { navController.navigate("time_selection/$category") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text("Recommencer", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp))
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
                Text("Retour", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}