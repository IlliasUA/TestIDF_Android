package legOS.testidf.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import legOS.testidf.loadScores
import java.io.IOException

@Composable
fun HallOfFameScreen(navController: NavController) {
    val context = LocalContext.current
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
            Log.e("HallOfFameScreen", "Error : honor_audio.mp3", e)
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
        scores = loadScores(context).take(10) // Ограничиваем до 10 записей
    }

    // Загрузка фонового изображения из assets
    val backgroundImage = remember {
        try {
            context.assets.open("images/background_5.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("HallOfFameScreen", "Error : background_5.jpg", e)
            null
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Отображение фонового изображения
        backgroundImage?.let { image ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent), // Прозрачный фон для Column, чтобы видно было изображение
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Salle d'honneur",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            if (scores.isEmpty()) {
                Text(
                    "",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(scores.size) { index ->
                        val (name, score) = scores[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when (index) {
                                    0 -> Color(0xFFFFD700) // Золотой для 1-го места
                                    1 -> Color(0xFFC0C0C0) // Серебряный для 2-го места
                                    2 -> Color(0xFFCD7F32) // Бронзовый для 3-го места
                                    in 3..9 -> Color(0xFFD2B48C).copy(alpha = 0.5f) // Кофейный цвет с 50% прозрачностью
                                    else -> Color(0xFFD2B48C).copy(alpha = 0.5f) // По умолчанию кофейный
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .background(Color.Transparent),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Отображение медали или номера
                                when (index) {
                                    0 -> {
                                        Text(
                                            "🥇",
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    1 -> {
                                        Text(
                                            "🥈",
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    2 -> {
                                        Text(
                                            "🥉",
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    else -> {
                                        Text(
                                            "${index + 1}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                }

                                // Имя и счет
                                Text(
                                    name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = if (index in 0..2) 20.sp else 16.sp
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "$score",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = if (index in 0..2) 20.sp else 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Кнопка "Retour"
            Button(
                onClick = { navController.navigate("test_menu") },
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
                Text("Retour", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}