package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.IOException
import kotlin.math.min

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TestMenuScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val density = LocalDensity.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Адаптивные размеры экрана
    val screenHeightDp = with(density) { configuration.screenHeightDp.dp }
    val screenWidthDp = with(density) { configuration.screenWidthDp.dp }
    val isCompactHeight = screenHeightDp < 600.dp || windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    // Load background image (same as MainMenuScreen for consistency)
    val backgroundImage: ImageBitmap? = remember {
        try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("TestMenuScreen", "Error loading background.png", e)
            null
        }
    }

    // Сбрасываем состояние при входе на экран
    LaunchedEffect(Unit) {
        Log.d("TestMenuScreen", "Screen launched/relaunched - resetting state")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Display background image - заполняет весь экран включая системные панели
        backgroundImage?.let { image: ImageBitmap ->
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
                    TestMenuCompactLayout(
                        navController = navController,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidthDp,
                        screenHeight = screenHeightDp
                    )
                }
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                    TestMenuLargeLayout(
                        navController = navController,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidthDp,
                        screenHeight = screenHeightDp
                    )
                }
            }
        }
    }
}

@Composable
private fun TestMenuCompactLayout(
    navController: NavController,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    // Создаем новое состояние прокрутки при каждом входе на экран
    val scrollState = rememberScrollState()

    // Сбрасываем прокрутку в начало при входе на экран
    LaunchedEffect(Unit) {
        scrollState.animateScrollTo(0)
    }

    // Адаптивные отступы
    val horizontalPadding = min(16.dp, screenWidth * 0.04f)
    val verticalPadding = if (isCompactHeight) 8.dp else min(24.dp, screenHeight * 0.03f)
    val buttonSpacing = if (isCompactHeight) 6.dp else 8.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalArrangement = if (isCompactHeight) Arrangement.Top else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "Choisisez une catégorie",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = when {
                    isCompactHeight -> 16.sp
                    screenHeight < 600.dp -> 18.sp
                    else -> 20.sp
                }
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = if (isCompactHeight) 12.dp else 16.dp)
        )

        if (!isCompactHeight) {
            Spacer(Modifier.height(if (isLandscape) 8.dp else 12.dp))
        }

        // Основные кнопки категорий
        Column(
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOf(
                "Chars de combat" to "tanks",
                "Artillerie" to "artillery",
                "Reconnaissance" to "recon",
                "Génie" to "genie",
                "Avion/Hélicoptère" to "air",
                "TEST BM2" to "bm2",
                "TEST FINAL" to "final"
            ).forEach { (text, category) ->
                CategoryButton(
                    text = text,
                    category = category,
                    navController = navController,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    isTertiary = category in listOf("bm2", "final"),
                    isCompact = isCompactHeight
                )
            }

            // Дополнительное пространство перед кнопками действий
            Spacer(Modifier.height(if (isCompactHeight) 8.dp else 12.dp))

            // Кнопки поиска и создания
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "catalog",
                    text = "🔍",
                    color = Color(0xFF4CAF50),
                    isCompact = isCompactHeight
                )
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "creation",
                    text = "Creation",
                    color = Color(0xFF4CAF50),
                    isCompact = isCompactHeight
                )
            }

            // Дополнительное пространство
            Spacer(Modifier.height(buttonSpacing))

            // Кнопки возврата и зала славы
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "main_menu",
                    text = "Retour",
                    color = Color(0xFF8B0000),
                    isCompact = isCompactHeight
                )
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "hall_of_fame",
                    text = "🏆",
                    color = Color(0xFFFFFF00),
                    isCompact = isCompactHeight
                )
            }
        }

        // Нижний отступ для обеспечения прокрутки
        Spacer(Modifier.height(if (isCompactHeight) 16.dp else 24.dp))
    }
}

@Composable
private fun TestMenuLargeLayout(
    navController: NavController,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    // Создаем новое состояние прокрутки при каждом входе на экран
    val scrollState = rememberScrollState()

    // Сбрасываем прокрутку в начало при входе на экран
    LaunchedEffect(Unit) {
        scrollState.animateScrollTo(0)
    }

    // Адаптивные отступы
    val horizontalPadding = min(32.dp, screenWidth * 0.05f)
    val verticalPadding = min(32.dp, screenHeight * 0.04f)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = if (isLandscape) min(32.dp, screenWidth * 0.04f) else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
            Text(
                text = "Choisisez une catégorie",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = when {
                        isCompactHeight -> 20.sp
                        screenHeight < 700.dp -> 24.sp
                        else -> 28.sp
                    }
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = if (isCompactHeight) 16.dp else 24.dp)
            )

            // Основные кнопки в две колонки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Левая колонка
                Column(
                    verticalArrangement = Arrangement.spacedBy(if (isCompactHeight) 8.dp else 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    listOf(
                        "Chars de combat" to "tanks",
                        "Artillerie" to "artillery",
                        "Reconnaissance" to "recon",
                        "Génie" to "genie",
                        "Avion/Hélicoptère" to "air"
                    ).forEach { (text, category) ->
                        CategoryButton(
                            text = text,
                            category = category,
                            navController = navController,
                            modifier = Modifier.fillMaxWidth(0.9f),
                            isTertiary = false,
                            isCompact = isCompactHeight
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                // Правая колонка
                Column(
                    verticalArrangement = Arrangement.spacedBy(if (isCompactHeight) 8.dp else 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    listOf(
                        "TEST BM2" to "bm2",
                        "TEST FINAL" to "final"
                    ).forEach { (text, category) ->
                        CategoryButton(
                            text = text,
                            category = category,
                            navController = navController,
                            modifier = Modifier.fillMaxWidth(0.9f),
                            isTertiary = true,
                            isCompact = isCompactHeight
                        )
                    }
                }
            }

            // Дополнительные кнопки
            Spacer(Modifier.height(if (isCompactHeight) 8.dp else 12.dp))

            // Кнопки поиска и создания
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "catalog",
                    text = "📚",
                    color = Color(0xFF4CAF50),
                    isCompact = isCompactHeight
                )
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "creation",
                    text = "Creation",
                    color = Color(0xFF4CAF50),
                    isCompact = isCompactHeight
                )
            }

            Spacer(Modifier.height(if (isCompactHeight) 6.dp else 8.dp))

            // Кнопки возврата и зала славы
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "main_menu",
                    text = "Retour",
                    color = Color(0xFF8B0000),
                    isCompact = isCompactHeight
                )
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "hall_of_fame",
                    text = "🏆",
                    color = Color(0xFFFFFF00),
                    isCompact = isCompactHeight
                )
            }

            // Нижний отступ для обеспечения прокрутки
            Spacer(Modifier.height(if (isCompactHeight) 16.dp else 24.dp))
        }
    }
}

@Composable
private fun CategoryButton(
    text: String,
    category: String,
    navController: NavController,
    modifier: Modifier,
    isTertiary: Boolean,
    isCompact: Boolean = false
) {
    Button(
        onClick = {
            if (category == "final") {
                navController.navigate("player_name")
            } else {
                navController.navigate("time_selection/$category")
            }
        },
        modifier = modifier
            .height(
                when {
                    isCompact -> 44.dp
                    text.length > 15 -> 56.dp
                    else -> 52.dp
                }
            )
            .padding(vertical = if (isCompact) 2.dp else 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isTertiary) {
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
            } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            },
            contentColor = if (isTertiary) {
                MaterialTheme.colorScheme.onTertiary
            } else {
                MaterialTheme.colorScheme.onPrimary
            }
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(
            horizontal = if (isCompact) 8.dp else 16.dp,
            vertical = if (isCompact) 8.dp else 12.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = when {
                    isCompact -> 13.sp
                    text.length > 15 -> 14.sp
                    else -> 16.sp
                }
            ),
            textAlign = TextAlign.Center,
            maxLines = if (text.length > 15) 2 else 1
        )
    }
}

@Composable
private fun ReturnButton(
    navController: NavController,
    modifier: Modifier,
    route: String,
    text: String,
    color: Color,
    isCompact: Boolean = false
) {
    Button(
        onClick = { navController.navigate(route) },
        modifier = modifier
            .height(if (isCompact) 44.dp else 52.dp)
            .padding(vertical = if (isCompact) 2.dp else 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.8f),
            contentColor = if (color == Color(0xFFFFFF00)) Color.Black else Color.White
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(
            horizontal = if (isCompact) 8.dp else 12.dp,
            vertical = if (isCompact) 6.dp else 8.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = if (isCompact) 12.sp else 15.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}