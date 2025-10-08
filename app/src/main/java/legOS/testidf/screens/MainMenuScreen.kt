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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
fun MainMenuScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val density = LocalDensity.current
    val showQuitConfirmation = remember { mutableStateOf(false) }

    // Адаптивные размеры на основе плотности экрана и размера окна
    val screenHeightDp = with(density) { configuration.screenHeightDp.dp }
    val screenWidthDp = with(density) { configuration.screenWidthDp.dp }
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isCompactHeight = screenHeightDp < 600.dp || windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    // Load background image with error handling
    val backgroundImage = remember {
        try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("MainMenuScreen", "Error loading background.png", e)
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            // Адаптивная компоновка на основе размера экрана
            when {
                windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact -> {
                    MainMenuCompactLayout(
                        navController = navController,
                        showQuitConfirmation = showQuitConfirmation,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidthDp,
                        screenHeight = screenHeightDp
                    )
                }
                else -> {
                    MainMenuLargeLayout(
                        navController = navController,
                        showQuitConfirmation = showQuitConfirmation,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidthDp,
                        screenHeight = screenHeightDp
                    )
                }
            }
        }
    }

    // Quit confirmation dialog
    if (showQuitConfirmation.value) {
        AlertDialog(
            onDismissRequest = { showQuitConfirmation.value = false },
            title = {
                Text(
                    "Confirmation",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = if (isCompactHeight) 18.sp else 22.sp
                    )
                )
            },
            text = {
                Text(
                    "Voulez-vous vraiment quitter l'application ?",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = if (isCompactHeight) 14.sp else 16.sp
                    ),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showQuitConfirmation.value = false
                        (navController.context as? ComponentActivity)?.finish()
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        "Oui",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = if (isCompactHeight) 14.sp else 16.sp
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showQuitConfirmation.value = false },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        "Non",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = if (isCompactHeight) 14.sp else 16.sp
                        )
                    )
                }
            },
            modifier = Modifier.padding(
                horizontal = min(16.dp, screenWidthDp * 0.05f),
                vertical = min(16.dp, screenHeightDp * 0.02f)
            )
        )
    }
}

@Composable
private fun MainMenuCompactLayout(
    navController: NavController,
    showQuitConfirmation: MutableState<Boolean>,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val scrollState = rememberScrollState()

    // Адаптивные отступы и размеры
    val horizontalPadding = min(16.dp, screenWidth * 0.04f)
    val verticalPadding = if (isCompactHeight) 8.dp else min(24.dp, screenHeight * 0.03f)
    val buttonSpacing = if (isCompactHeight) 8.dp else 12.dp
    val buttonWidth = if (isLandscape) 0.9f else 0.85f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Добавляем прокрутку для маленьких экранов
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalArrangement = if (isCompactHeight) Arrangement.SpaceBetween else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхний спейсер (адаптивный) - увеличиваем для сдвига вниз
        if (!isCompactHeight) {
            Spacer(Modifier.height(screenHeight * 0.28f)) // Увеличено с 0.22f до 0.28f
        } else {
            Spacer(Modifier.height(32.dp)) // Увеличено с 24dp до 32dp
        }

        // Заголовок (скрываем на очень маленьких экранах)
        if (!isCompactHeight || screenHeight > 400.dp) {
            Text(
                text = "", // Оставляем пустым как в оригинале
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = when {
                        isCompactHeight -> 16.sp
                        screenHeight < 600.dp -> 18.sp
                        else -> 20.sp
                    }
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = if (isCompactHeight) 8.dp else 16.dp)
            )
        }

        // Кнопки меню
        if (isLandscape && screenWidth > 600.dp) {
            // Горизонтальное расположение для широких экранов в ландшафте
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = min(16.dp, screenWidth * 0.02f),
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MenuButton(
                    text = "TEST D'IDENTIFICATION",
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.weight(1f),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = "Info",
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.weight(1f),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = "Quitter",
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.weight(1f),
                    isCompact = isCompactHeight
                )
            }
        } else {
            // Вертикальное расположение
            Column(
                verticalArrangement = Arrangement.spacedBy(buttonSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuButton(
                    text = "TESTS",
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = "INFO",
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = "QUITTER",
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
            }
        }

        // Нижний спейсер (адаптивный)
        if (!isCompactHeight) {
            Spacer(Modifier.height(screenHeight * 0.05f))
        } else {
            Spacer(Modifier.height(8.dp))
        }

        // Информация о версии
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Beta 1.0",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = when {
                        isCompactHeight -> 12.sp
                        screenHeight < 600.dp -> 14.sp
                        else -> 16.sp
                    }
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "IlliasUA",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = when {
                        isCompactHeight -> 10.sp
                        screenHeight < 600.dp -> 12.sp
                        else -> 14.sp
                    }
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun MainMenuLargeLayout(
    navController: NavController,
    showQuitConfirmation: MutableState<Boolean>,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val scrollState = rememberScrollState()

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Верхний спейсер - увеличиваем для сдвига вниз
            if (!isCompactHeight) {
                Spacer(Modifier.height(screenHeight * 0.28f)) // Увеличено с 0.22f до 0.28f
            } else {
                Spacer(Modifier.height(40.dp)) // Увеличено с 32dp до 40dp
            }

            // Кнопки меню
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    if (isCompactHeight) 12.dp else 16.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val buttonWidth = when {
                    screenWidth > 1000.dp -> 0.4f
                    screenWidth > 700.dp -> 0.5f
                    else -> 0.6f
                }

                MenuButton(
                    text = "TEST D'IDENTIFICATION",
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = "Info",
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = "Quitter",
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
            }

            // Нижний спейсер
            if (!isCompactHeight) {
                Spacer(Modifier.height(screenHeight * 0.08f))
            } else {
                Spacer(Modifier.height(16.dp))
            }

            // Информация о версии
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Beta 1.0",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = when {
                            isCompactHeight -> 14.sp
                            screenHeight < 700.dp -> 16.sp
                            else -> 18.sp
                        }
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "IlliasUA",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = when {
                            isCompactHeight -> 12.sp
                            screenHeight < 700.dp -> 14.sp
                            else -> 16.sp
                        }
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun MenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    isCompact: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(
                // Возвращаем исходную высоту
                if (isCompact) 48.dp else 56.dp
            )
            .padding(vertical = if (isCompact) 2.dp else 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(
            horizontal = if (isCompact) 16.dp else 20.dp,
            vertical = if (isCompact) 8.dp else 12.dp    // Разумные вертикальные отступы
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = when {
                    isCompact -> 17.sp // Уменьшено на 35% с 26.sp (26 * 0.65 = 16.9)
                    text.length > 20 -> 17.sp // Уменьшено на 35% с 26.sp
                    else -> 18.sp // Уменьшено на 35% с 28.sp (28 * 0.65 = 18.2)
                }
            ),
            textAlign = TextAlign.Center,
            maxLines = if (text.length > 20) 2 else 1 // Разрешаем перенос для длинного текста
        )
    }
}