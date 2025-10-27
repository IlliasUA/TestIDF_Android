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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quizapp.Question
import legOS.testidf.R
import legOS.testidf.loadImageFromAssets
import legOS.testidf.utils.getDescriptionText
import legOS.testidf.utils.getMoreInfoText

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
            text = stringResource(R.string.more_info_question_not_found),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        return
    }

    Log.d("MoreInfoScreen", "Displaying question at index $index")
    Log.d("MoreInfoScreen", "Main image: ${currentQuestion.image}")
    Log.d("MoreInfoScreen", "Category: $category")
    Log.d("MoreInfoScreen", "Additional images: ${currentQuestion.additionalImages}")

    // Load background image
    val backgroundImage = loadImageFromAssets(context, "images/background_2.jpg")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        backgroundImage?.let {
            Image(
                painter = BitmapPainter(it.asImageBitmap()),
                contentDescription = stringResource(R.string.more_info_background_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            if (isLandscape) {
                MoreInfoLandscapeLayout(navController, category, currentQuestion, index, context)
            } else {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        MoreInfoCompactLayout(navController, category, currentQuestion, index, false, context)
                    }
                    WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                        MoreInfoLargeLayout(navController, category, currentQuestion, index, false, context)
                    }
                }
            }
        }
    }
}

/**
 * Возвращает список всех путей изображений, исключая изображение показанное в тесте
 * question.image содержит изображение, которое УЖЕ было показано в тесте (может быть случайным)
 */
private fun getAllImagePaths(question: Question, category: String): List<String> {
    // Изображение, которое было показано в тесте
    val shownImageName = question.image

    Log.d("MoreInfoScreen", "=== getAllImagePaths ===")
    Log.d("MoreInfoScreen", "Category: $category")
    Log.d("MoreInfoScreen", "Question: ${question.correct}")
    Log.d("MoreInfoScreen", "Image shown in test: $shownImageName")

    // Получаем оригинальный Question для доступа ко ВСЕМ изображениям
    val originalQuestion = findOriginalQuestion(question.correct, category)

    if (originalQuestion == null) {
        Log.e("MoreInfoScreen", "❌ Could not find original question for: ${question.correct}")
        return emptyList()
    }

    // Для финального теста используем оригинальную категорию вопроса
    val actualCategory = if (category == "final") {
        getQuestionCategory(question.correct)
    } else {
        category
    }

    Log.d("MoreInfoScreen", "Actual category: $actualCategory")

    val imageFolder = getImageFolder(actualCategory)
    val allImages = mutableListOf<String>()

    // 1. Добавляем ОРИГИНАЛЬНОЕ основное изображение (если оно не было показано)
    if (originalQuestion.image != shownImageName) {
        val mainImagePath = "$imageFolder/${originalQuestion.image}"
        allImages.add(mainImagePath)
        Log.d("MoreInfoScreen", "✅ Added original main image: $mainImagePath")
    } else {
        Log.d("MoreInfoScreen", "⏭️ Skipping main image (was shown in test)")
    }

    // 2. Добавляем дополнительные изображения (исключая показанное)
    originalQuestion.additionalImages?.filterNotNull()?.forEach { additionalImage ->
        if (additionalImage != shownImageName) {
            val additionalPath = "$imageFolder/$additionalImage"
            allImages.add(additionalPath)
            Log.d("MoreInfoScreen", "✅ Added additional image: $additionalPath")
        } else {
            Log.d("MoreInfoScreen", "⏭️ Skipping additional image (was shown in test): $additionalImage")
        }
    }

    Log.d("MoreInfoScreen", "Total images (excluding shown): ${allImages.size}")
    Log.d("MoreInfoScreen", "======================")
    return allImages
}

/**
 * Определяет категорию вопроса по его имени
 */
private fun getQuestionCategory(questionName: String): String {
    return when {
        com.example.quizapp.Test_Data.QUESTION.any { it.correct == questionName } -> "tanks"
        com.example.quizapp.Art_Data.QUESTION.any { it.correct == questionName } -> "artillery"
        com.example.quizapp.Air_Data.QUESTION.any { it.correct == questionName } -> "air"
        com.example.quizapp.Genie_Data.QUESTION.any { it.correct == questionName } -> "genie"
        com.example.quizapp.Recon_Data.QUESTION.any { it.correct == questionName } -> "recon"
        com.example.quizapp.Test_bm2.QUESTION.any { it.correct == questionName } -> "bm2"
        else -> "tanks" // fallback
    }
}

/**
 * Находит оригинальный вопрос по имени и категории
 */
private fun findOriginalQuestion(name: String, category: String): Question? {
    return when (category) {
        "tanks" -> com.example.quizapp.Test_Data.QUESTION.find { it.correct == name }
        "artillery" -> com.example.quizapp.Art_Data.QUESTION.find { it.correct == name }
        "recon" -> com.example.quizapp.Recon_Data.QUESTION.find { it.correct == name }
        "genie" -> com.example.quizapp.Genie_Data.QUESTION.find { it.correct == name }
        "air" -> com.example.quizapp.Air_Data.QUESTION.find { it.correct == name }
        "bm2" -> com.example.quizapp.Test_bm2.QUESTION.find { it.correct == name }
        "final" -> {
            // Для финального теста пробуем найти в любой категории
            com.example.quizapp.Test_Data.QUESTION.find { it.correct == name }
                ?: com.example.quizapp.Art_Data.QUESTION.find { it.correct == name }
                ?: com.example.quizapp.Air_Data.QUESTION.find { it.correct == name }
                ?: com.example.quizapp.Genie_Data.QUESTION.find { it.correct == name }
                ?: com.example.quizapp.Recon_Data.QUESTION.find { it.correct == name }
                ?: com.example.quizapp.Test_bm2.QUESTION.find { it.correct == name }
        }
        else -> null
    }
}

@Composable
private fun MoreInfoLandscapeLayout(
    navController: NavController,
    category: String,
    question: Question,
    index: Int,
    context: android.content.Context
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Левая часть - изображения
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            val allImagePaths = getAllImagePaths(question, category)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(allImagePaths) { imagePath ->
                    AsyncImage(
                        model = "file:///android_asset/$imagePath",
                        contentDescription = stringResource(R.string.more_info_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp, max = 300.dp),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.placeholder)
                    )
                }
            }
        }

        // Правая часть - текст и управление
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.more_info_correct_answer, question.correct),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color(0xFF006400),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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
                        text = question.getDescriptionText(context) ?: stringResource(R.string.more_info_no_description),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    question.getMoreInfoText(context)?.let { moreInfo ->
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
    isLandscape: Boolean,
    context: android.content.Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.more_info_correct_answer, question.correct),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color(0xFF006400),
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Горизонтальная прокрутка изображений
        val allImagePaths = getAllImagePaths(question, category)

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(allImagePaths) { imagePath ->
                AsyncImage(
                    model = "file:///android_asset/$imagePath",
                    contentDescription = stringResource(R.string.more_info_image),
                    modifier = Modifier.size(
                        width = if (isLandscape) 350.dp else 416.dp,
                        height = if (isLandscape) 260.dp else 286.dp
                    ),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.placeholder)
                )
            }
        }

        // Прокручиваемый текст
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
                    text = question.getDescriptionText(context) ?: stringResource(R.string.more_info_no_description),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                question.getMoreInfoText(context)?.let { moreInfo ->
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
    isLandscape: Boolean,
    context: android.content.Context
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
                text = stringResource(R.string.more_info_correct_answer, question.correct),
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF006400),
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            val allImagePaths = getAllImagePaths(question, category)

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(allImagePaths) { imagePath ->
                    AsyncImage(
                        model = "file:///android_asset/$imagePath",
                        contentDescription = stringResource(R.string.more_info_image),
                        modifier = Modifier.size(
                            width = if (isLandscape) 480.dp else 585.dp,
                            height = if (isLandscape) 390.dp else 455.dp
                        ),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.placeholder)
                    )
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
                        text = question.getDescriptionText(context) ?: stringResource(R.string.more_info_no_description),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    question.getMoreInfoText(context)?.let { moreInfo ->
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
        Text(
            stringResource(R.string.more_info_return_button),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Возвращает папку с изображениями для категории
 */
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