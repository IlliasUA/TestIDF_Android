package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import legOS.testidf.loadScores
import java.io.IOException

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HallOfFameScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var scores by remember { mutableStateOf(listOf<Pair<String, Int>>()) }

    // Initialize MediaPlayer safely
    val mediaPlayer = remember {
        try {
            context.assets.openFd("audio/honor_audio.mp3").use { assetFileDescriptor ->
                MediaPlayer().apply {
                    setDataSource(
                        assetFileDescriptor.fileDescriptor,
                        assetFileDescriptor.startOffset,
                        assetFileDescriptor.length
                    )
                    prepare()
                }
            }
        } catch (e: IOException) {
            Log.e("HallOfFameScreen", "Error loading honor_audio.mp3", e)
            null
        }
    }

    // Play audio when screen is entered
    LaunchedEffect(Unit) {
        mediaPlayer?.start()
    }

    // Stop and release audio when screen is exited
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    // Load scores from file
    LaunchedEffect(Unit) {
        scores = loadScores(context).take(10) // Limit to top 10 scores
    }

    // Load background image with error handling
    val backgroundImage = remember {
        try {
            context.assets.open("images/background_5.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("HallOfFameScreen", "Error loading background_5.jpg", e)
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fallback background
    ) {
        // Display background image - заполняет весь экран включая системные панели
        backgroundImage?.let { image ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Контент с безопасными отступами поверх фона
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding() // Применяем отступы только к контенту
        ) {
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    // Small screens (phones)
                    HallOfFameCompactLayout(navController, scores, isLandscape)
                }
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                    // Tablets or large screens
                    HallOfFameLargeLayout(navController, scores, isLandscape)
                }
            }
        }
    }
}

@Composable
private fun HallOfFameCompactLayout(
    navController: NavController,
    scores: List<Pair<String, Int>>,
    isLandscape: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Panthéon",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (scores.isEmpty()) {
            Text(
                text = "Aucun score disponible",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                items(scores.size) { index ->
                    ScoreCard(index, scores[index], isLargeScreen = false)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        ReturnButton(navController, Modifier.fillMaxWidth(0.8f))
    }
}

@Composable
private fun HallOfFameLargeLayout(
    navController: NavController,
    scores: List<Pair<String, Int>>,
    isLandscape: Boolean
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
            Text(
                text = "Panthéon",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (scores.isEmpty()) {
                Text(
                    text = "Aucun score disponible",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(24.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    items(scores.size) { index ->
                        ScoreCard(index, scores[index], isLargeScreen = true)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            ReturnButton(navController, Modifier.fillMaxWidth(0.6f))
        }
    }
}

@Composable
private fun ScoreCard(index: Int, score: Pair<String, Int>, isLargeScreen: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (index) {
                0 -> Color(0xFFFFD700) // Gold for 1st place
                1 -> Color(0xFFC0C0C0) // Silver for 2nd place
                2 -> Color(0xFFCD7F32) // Bronze for 3rd place
                else -> Color(0xFFD2B48C).copy(alpha = 1f) // Coffee color with 50% opacity
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Medal or number
            Text(
                text = when (index) {
                    0 -> "🥇"
                    1 -> "🥈"
                    2 -> "🥉"
                    else -> "${index + 1}"
                },
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = if (isLargeScreen) 28.sp else 24.sp
                ),
                modifier = Modifier.padding(end = 12.dp)
            )

            // Name and score
            Text(
                text = score.first,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = if (index in 0..2) (if (isLargeScreen) 24.sp else 20.sp) else (if (isLargeScreen) 20.sp else 16.sp)
                ),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${score.second}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = if (index in 0..2) (if (isLargeScreen) 24.sp else 20.sp) else (if (isLargeScreen) 20.sp else 16.sp)
                )
            )
        }
    }
}

@Composable
private fun ReturnButton(navController: NavController, modifier: Modifier) {
    OutlinedButton(
        onClick = { navController.navigate("test_menu") },
        modifier = modifier
            .height(48.dp)
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
    ) {
        Text("Retour", style = MaterialTheme.typography.bodyLarge)
    }
}