package legOS.testidf.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.Question
import legOS.testidf.loadImageFromAssets
import legOS.testidf.getImagePath

@Composable
fun MoreInfoScreen(navController: NavController, category: String, index: Int, timeLimit: Int) {
    val context = LocalContext.current
    val navBackStackEntry = navController.previousBackStackEntry ?: return
    val questions = navBackStackEntry.savedStateHandle.get<List<Question>>("questions") ?: emptyList()
    val currentQuestion = questions.getOrNull(index)

    if (currentQuestion == null) {
        Text("Question not found", modifier = Modifier.padding(16.dp))
        return
    }

    Log.d("MoreInfoScreen", "Displaying question at index $index: ${currentQuestion.image}, category: $category, additionalImages: ${currentQuestion.additionalImages}")

    // Load the background image
    val backgroundImage = loadImageFromAssets(context, "images/background_2.jpg")

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
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Отображение правильного ответа вместо "Plus d'infos - Question n" для "final" и "bm2"
        Text(
            text = if (category in listOf("final", "bm2")) {
                "Réponse correcte: ${currentQuestion.correct}"
            } else {
                "Plus d'infos - Question ${index + 1}"
            },
            style = MaterialTheme.typography.headlineMedium.copy(
                color = if (category in listOf("final", "bm2")) Color(0xFF006400) else MaterialTheme.colorScheme.onBackground,
                fontWeight = if (category in listOf("final", "bm2")) FontWeight.SemiBold else FontWeight.Normal
            ),
            modifier = Modifier.padding(16.dp)
        )

        // Горизонтальный скроллинг изображений
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Основное изображение
            item {
                val mainImagePath = getImagePath(category, currentQuestion)
                val mainBitmap = loadImageFromAssets(context, mainImagePath)
                mainBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Main Image",
                        modifier = Modifier
                            .size(435.dp, 300.dp)
                    )
                } ?: Text("Main Image not found: ${currentQuestion.image}", modifier = Modifier.padding(8.dp))
            }

            // Дополнительные изображения только для "final" и "bm2"
            if (category in listOf("final", "bm2")) {
                items(currentQuestion.additionalImages?.filterNotNull() ?: emptyList()) { additionalImagePath ->
                    val originalCategory = currentQuestion.category ?: category
                    val imageFolder = when (originalCategory) {
                        "tanks" -> "tank_images"
                        "artillery" -> "artillery_images"
                        "recon" -> "recon_images"
                        "genie" -> "genie_images"
                        "air" -> "air_images"
                        "bm2" -> "bm2_images"
                        else -> "final_images"
                    }
                    val fullAdditionalPath = "$imageFolder/$additionalImagePath"
                    Log.d("MoreInfoScreen", "Attempting to load additional image: $fullAdditionalPath")
                    val assetManager = context.assets
                    val files = assetManager.list(imageFolder)
                    Log.d("MoreInfoScreen", "Available files in $imageFolder: ${files?.joinToString()}")
                    val additionalBitmap = loadImageFromAssets(context, fullAdditionalPath)
                    additionalBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Additional Image",
                            modifier = Modifier
                                .size(435.dp, 300.dp)
                        )
                    } ?: run {
                        Log.e("MoreInfoScreen", "Additional image not found: $fullAdditionalPath")
                        Text("Additional Image not found: $fullAdditionalPath", modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }

        // Прокручиваемый текст описания и дополнительной информации
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = currentQuestion.description ?: "Aucune description disponible",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                currentQuestion.moreInfo?.let { moreInfo ->
                    Text(
                        text = moreInfo,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .width(200.dp)
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

// Вспомогательная функция для получения подкаталога на основе категории (оставлена для совместимости)
private fun getImageFolder(category: String): String {
    return when (category) {
        "tanks" -> "tank_images"
        "artillery" -> "artillery_images"
        "recon" -> "recon_images"
        "genie" -> "genie_images"
        "air" -> "air_images"
        "bm2" -> "bm2_images"
        "final" -> "final_images"
        else -> "tank_images"
    }
}