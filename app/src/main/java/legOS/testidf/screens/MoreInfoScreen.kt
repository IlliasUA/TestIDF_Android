package legOS.testidf.screens

import android.content.res.Configuration
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quizapp.Question
import legOS.testidf.R
import legOS.testidf.loadImageFromAssets
import legOS.testidf.getImagePath

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MoreInfoScreen(navController: NavController, category: String, index: Int, timeLimit: Int) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val navBackStackEntry = navController.previousBackStackEntry ?: return
    val questions = navBackStackEntry.savedStateHandle.get<List<Question>>("questions") ?: emptyList()
    val currentQuestion = questions.getOrNull(index)

    if (currentQuestion == null) {
        Text(
            text = "Question not found",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        return
    }

    Log.d("MoreInfoScreen", "Displaying question at index $index: ${currentQuestion.image}, category: $category, additionalImages: ${currentQuestion.additionalImages}")

    // Load background image
    val backgroundImage = loadImageFromAssets(context, "images/background_2.jpg")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Display background image - заполняет весь экран включая системные панели
        backgroundImage?.let {
            Image(
                painter = BitmapPainter(it.asImageBitmap()),
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
            // Выбираем компоновку в зависимости от ориентации
            if (isLandscape) {
                MoreInfoLandscapeLayout(navController, category, currentQuestion, index)
            } else {
                // Для портретного режима используем старую логику
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        MoreInfoCompactLayout(navController, category, currentQuestion, index, false)
                    }
                    WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                        MoreInfoLargeLayout(navController, category, currentQuestion, index, false)
                    }
                }
            }
        }
    }
}

// Новый компонент для горизонтального режима
@Composable
private fun MoreInfoLandscapeLayout(
    navController: NavController,
    category: String,
    question: Question,
    index: Int
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Левая часть - изображения (занимает всю левую половину)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            // Вертикальная прокрутка изображений
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Главное изображение
                item {
                    val mainImagePath = getImagePath(category, question)
                    AsyncImage(
                        model = "file:///android_asset/$mainImagePath",
                        contentDescription = "Main Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp, max = 300.dp),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(legOS.testidf.R.drawable.placeholder)
                    )
                }

                // Дополнительные изображения
                if (category in listOf("final", "bm2")) {
                    items(question.additionalImages?.filterNotNull() ?: emptyList()) { additionalImagePath ->
                        val originalCategory = question.category ?: category
                        val imageFolder = getImageFolder(originalCategory)
                        val fullAdditionalPath = "$imageFolder/$additionalImagePath"
                        Log.d("MoreInfoScreen", "Loading additional image: $fullAdditionalPath")
                        AsyncImage(
                            model = "file:///android_asset/$fullAdditionalPath",
                            contentDescription = "Additional Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 200.dp, max = 300.dp),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(legOS.testidf.R.drawable.placeholder)
                        )
                    }
                }
            }
        }

        // Правая часть - текст и управление (занимает правую половину)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
            Text(
                text = if (category in listOf("final", "bm2")) {
                    "Réponse correcte: ${question.correct}"
                } else {
                    "Plus d'infos - Question ${index + 1}"
                },
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = if (category in listOf("final", "bm2")) Color(0xFF006400) else MaterialTheme.colorScheme.onBackground,
                    fontWeight = if (category in listOf("final", "bm2")) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Прокручиваемый текст
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = question.description ?: "Aucune description disponible",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    question.moreInfo?.let { moreInfo ->
                        Text(
                            text = moreInfo,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка возврата
            ReturnButton(navController, Modifier.fillMaxWidth(0.8f))
        }
    }
}

@Composable
private fun MoreInfoCompactLayout(
    navController: NavController,
    category: String,
    question: Question,
    index: Int,
    isLandscape: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (category in listOf("final", "bm2")) {
                "Réponse correcte: ${question.correct}"
            } else {
                "Plus d'infos - Question ${index + 1}"
            },
            style = MaterialTheme.typography.headlineSmall.copy(
                color = if (category in listOf("final", "bm2")) Color(0xFF006400) else MaterialTheme.colorScheme.onBackground,
                fontWeight = if (category in listOf("final", "bm2")) FontWeight.SemiBold else FontWeight.Normal
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Horizontal scrolling images
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            item {
                val mainImagePath = getImagePath(category, question)
                AsyncImage(
                    model = "file:///android_asset/$mainImagePath",
                    contentDescription = "Main Image",
                    modifier = Modifier.size(
                        width = if (isLandscape) 350.dp else 416.dp,
                        height = if (isLandscape) 260.dp else 286.dp
                    ),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(legOS.testidf.R.drawable.placeholder)
                )
            }

            if (category in listOf("final", "bm2")) {
                items(question.additionalImages?.filterNotNull() ?: emptyList()) { additionalImagePath ->
                    val originalCategory = question.category ?: category
                    val imageFolder = getImageFolder(originalCategory)
                    val fullAdditionalPath = "$imageFolder/$additionalImagePath"
                    Log.d("MoreInfoScreen", "Loading additional image: $fullAdditionalPath")
                    AsyncImage(
                        model = "file:///android_asset/$fullAdditionalPath",
                        contentDescription = "Additional Image",
                        modifier = Modifier.size(
                            width = if (isLandscape) 350.dp else 416.dp,
                            height = if (isLandscape) 260.dp else 286.dp
                        ),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(legOS.testidf.R.drawable.placeholder)
                    )
                }
            }
        }

        // Scrollable text
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = question.description ?: "Aucune description disponible",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                question.moreInfo?.let { moreInfo ->
                    Text(
                        text = moreInfo,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }

        ReturnButton(navController, Modifier.fillMaxWidth(0.8f))
    }
}

@Composable
private fun MoreInfoLargeLayout(
    navController: NavController,
    category: String,
    question: Question,
    index: Int,
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
                text = if (category in listOf("final", "bm2")) {
                    "Réponse correcte: ${question.correct}"
                } else {
                    "Plus d'infos - Question ${index + 1}"
                },
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = if (category in listOf("final", "bm2")) Color(0xFF006400) else MaterialTheme.colorScheme.onBackground,
                    fontWeight = if (category in listOf("final", "bm2")) FontWeight.SemiBold else FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    val mainImagePath = getImagePath(category, question)
                    AsyncImage(
                        model = "file:///android_asset/$mainImagePath",
                        contentDescription = "Main Image",
                        modifier = Modifier.size(
                            width = if (isLandscape) 480.dp else 585.dp,
                            height = if (isLandscape) 390.dp else 455.dp
                        ),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(legOS.testidf.R.drawable.placeholder)
                    )
                }

                if (category in listOf("final", "bm2")) {
                    items(question.additionalImages?.filterNotNull() ?: emptyList()) { additionalImagePath ->
                        val originalCategory = question.category ?: category
                        val imageFolder = getImageFolder(originalCategory)
                        val fullAdditionalPath = "$imageFolder/$additionalImagePath"
                        Log.d("MoreInfoScreen", "Loading additional image: $fullAdditionalPath")
                        AsyncImage(
                            model = "file:///android_asset/$fullAdditionalPath",
                            contentDescription = "Additional Image",
                            modifier = Modifier.size(
                                width = if (isLandscape) 500.dp else 585.dp,
                                height = if (isLandscape) 390.dp else 455.dp
                            ),
                            contentScale = ContentScale.Fit,
                            placeholder = painterResource(legOS.testidf.R.drawable.placeholder)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = question.description ?: "Aucune description disponible",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    question.moreInfo?.let { moreInfo ->
                        Text(
                            text = moreInfo,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
            }

            ReturnButton(navController, Modifier.fillMaxWidth(0.6f))
        }
    }
}

@Composable
private fun ReturnButton(navController: NavController, modifier: Modifier) {
    OutlinedButton(
        onClick = { navController.popBackStack() },
        modifier = modifier
            .height(48.dp)
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        shape = MaterialTheme.shapes.medium
    ) {
        Text("Retour", style = MaterialTheme.typography.bodyLarge)
    }
}

// Helper function for image folder (unchanged)
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