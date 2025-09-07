package legOS.testidf.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography

// Определяем цветовые схемы на основе Python-кода
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6495ED), // BUTTON_COLOR (100, 149, 237)
    secondary = Color(0xFFFFA500), // Кнопка паузы (255, 165, 0)
    tertiary = Color(0xFFDC143C), // Кнопка выхода (220, 80, 80)
    background = Color(0xFFF0F0F5), // BACKGROUND_COLOR (240, 240, 245)
    surface = Color(0xFFF0F0F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6495ED), // BUTTON_COLOR
    secondary = Color(0xFFFFA500), // Кнопка паузы
    tertiary = Color(0xFFDC143C), // Кнопка выхода
    background = Color(0xFFF0F0F5), // BACKGROUND_COLOR
    surface = Color(0xFFF0F0F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Определяем кастомную типографику (аналог Arial из Python-кода)
val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif, // Используем SansSerif (близкий к Arial)
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp // font_large из Python
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp // font_medium из Python
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp // font_small из Python
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp // Для мелкого текста
    )
)

@Composable
fun TestIDFTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Используем системную тему
    dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S, // Динамические цвета для Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Используем кастомную типографику
        content = content
    )
}