package legOS.testidf.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
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

    // Load the background image
    val backgroundImage = loadImageFromAssets(context, "images/background_2.jpg")

    // ВАЖНО: Получаем данные из предыдущего экрана
    val selectedItems = navController.previousBackStackEntry?.savedStateHandle?.get<List<CreationItem>>("selectedItems")
        ?: TestDataHolder.selectedItems

    Log.d("CustomTimeSelectionScreen", "Received ${selectedItems.size} items")
    Log.d("CustomTimeSelectionScreen", "Question count parameter: $questionCount")

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
            ),
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
            "${selectedItems.size} éléments sélectionnés", // Используем реальное количество
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

                        // КРИТИЧНО: Передаем данные дальше!
                        navController.currentBackStackEntry?.savedStateHandle?.set("selectedItems", selectedItems)

                        // Также обновляем глобальный объект как fallback
                        TestDataHolder.selectedItems = selectedItems

                        // Navigate to custom test with selected time
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