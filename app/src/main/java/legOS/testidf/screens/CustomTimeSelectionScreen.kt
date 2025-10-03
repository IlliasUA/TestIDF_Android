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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import legOS.testidf.loadImageFromAssets

@Composable
fun CustomTimeSelectionScreen(navController: NavController, questionCount: String) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Load the background image
    val backgroundImage = loadImageFromAssets(context, "images/background_2.jpg")

    // ВАЖНО: Получаем данные из предыдущего экрана
    val selectedItems = navController.previousBackStackEntry?.savedStateHandle?.get<List<CreationItem>>("selectedItems")
        ?: TestDataHolder.selectedItems

    Log.d("CustomTimeSelectionScreen", "Received ${selectedItems.size} items")
    Log.d("CustomTimeSelectionScreen", "Question count parameter: $questionCount")

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
                    "Test Personnalisé",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "${selectedItems.size} éléments sélectionnés",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedItems.isEmpty()) {
                    Card(
                        modifier = Modifier.padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            "Aucun élément sélectionné!",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Éléments sélectionnés:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            selectedItems.take(5).forEach { item ->
                                Text(
                                    "• ${item.name}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (selectedItems.size > 5) {
                                Text(
                                    "... et aussi ${selectedItems.size - 5}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
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
                    "Sélectionnez le temps par question",
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
                                Log.d("CustomTimeSelectionScreen", "Navigating to custom test with time: $time, items: ${selectedItems.size}")

                                navController.currentBackStackEntry?.savedStateHandle?.set("selectedItems", selectedItems)
                                TestDataHolder.selectedItems = selectedItems
                                navController.navigate("custom_test/${selectedItems.size}/$time")
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("$time secondes", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text("Retour", style = MaterialTheme.typography.bodyMedium)
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
                "Test Personnalisé",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "${selectedItems.size} éléments sélectionnés",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Sélectionnez le temps par question",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                listOf(10, 15, 20).forEach { time ->
                    Button(
                        onClick = {
                            Log.d("CustomTimeSelectionScreen", "Navigating to custom test with time: $time, items: ${selectedItems.size}")

                            navController.currentBackStackEntry?.savedStateHandle?.set("selectedItems", selectedItems)
                            TestDataHolder.selectedItems = selectedItems
                            navController.navigate("custom_test/${selectedItems.size}/$time")
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
                        Text("$time secondes", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedItems.isEmpty()) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        "Aucun élément sélectionné!",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Éléments sélectionnés:",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        selectedItems.take(3).forEach { item ->
                            Text(
                                "• ${item.name}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (selectedItems.size > 3) {
                            Text(
                                "... et aussi ${selectedItems.size - 3}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(8.dp),
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