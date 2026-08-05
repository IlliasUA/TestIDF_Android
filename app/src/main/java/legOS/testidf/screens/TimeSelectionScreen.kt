package legOS.testidf.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import legOS.testidf.R
import legOS.testidf.loadImageFromAssets

@Composable
fun TimeSelectionScreen(navController: NavController, category: String) {
    val context = LocalResources.current
    val androidContext = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val playerName = navController.previousBackStackEntry?.savedStateHandle?.get<String>("playerName")
        ?: navController.previousBackStackEntry?.arguments?.getString("playerName")
        ?: context.getString(R.string.name_label)
    Log.d("TimeSelectionScreen", "Received player name: $playerName")

    // Получаем локализованное название категории
    val categoryDisplayName = when(category) {
        "tanks" -> context.getString(R.string.category_tanks)
        "artillery" -> context.getString(R.string.category_artillery)
        "recon" -> context.getString(R.string.category_recon)
        "genie" -> context.getString(R.string.category_engineer)
        "air" -> context.getString(R.string.category_air)
        else -> category.replace("_", " ")
    }

    // Load the background image
    val backgroundImage = loadImageFromAssets(androidContext, "images/background_2.jpg")

    if (isLandscape) {
        // ГОРИЗОНТАЛЬНАЯ ОРИЕНТАЦИЯ
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
                .systemBarsPadding()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ЛЕВАЯ ЧАСТЬ - Информация
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "${context.getString(R.string.time_selection_category_label)}: $categoryDisplayName",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            context.getString(R.string.player_label),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            playerName,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // ПРАВАЯ ЧАСТЬ - Кнопки
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    context.getString(R.string.time_selection_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10, 15, 20).forEach { time ->
                        Button(
                            onClick = {
                                Log.d("TimeSelectionScreen", "Navigating to test with time: $time, playerName: $playerName")
                                navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                                navController.navigate("test/$category/$time?playerName=$playerName")
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                context.getString(R.string.seconds, time),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        context.getString(R.string.back_button),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    } else {
        // ВЕРТИКАЛЬНАЯ ОРИЕНТАЦИЯ (оригинальный код)
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
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                context.getString(R.string.time_selection_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf(10, 15, 20).forEach { time ->
                    Button(
                        onClick = {
                            Log.d("TimeSelectionScreen", "Navigating to test with time: $time, playerName: $playerName")
                            navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                            navController.navigate("test/$category/$time?playerName=$playerName")
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .height(60.dp)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            context.getString(R.string.seconds, time),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp))

            Button(
                onClick = { navController.navigate("test_menu") },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(
                    context.getString(R.string.back_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
