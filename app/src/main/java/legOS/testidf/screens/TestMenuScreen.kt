package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.IOException

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TestMenuScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
    ) {
        // Display background image
        backgroundImage?.let { image: ImageBitmap ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                TestMenuCompactLayout(navController, isLandscape)
            }
            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                TestMenuLargeLayout(navController, isLandscape)
            }
        }
    }
}

@Composable
private fun TestMenuCompactLayout(navController: NavController, isLandscape: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choisisez une catégorie",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(Modifier.height(if (isLandscape) 12.dp else 20.dp))

        // Single column for all buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                    isTertiary = category in listOf("bm2", "final")
                )
            }

            // Новая кнопка 📚 между TEST FINAL и Retour (строго как Retour)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "catalog",
                    text = "\uD83D\uDD0D",
                    color = Color(0xFF4CAF50) // Мягкий зелёный
                )
                Spacer(Modifier.width(8.dp))
                // Пустое место справа (невидимый Spacer с weight)
                Spacer(modifier = Modifier.weight(1f))
            }

            // Уменьшенный Spacer для чёткого позиционирования над Retour
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "main_menu",
                    text = "Retour",
                    color = Color(0xFF8B0000)
                )
                Spacer(Modifier.width(8.dp))
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "hall_of_fame",
                    text = "\uD83C\uDFC6",
                    color = Color(0xFFFFFF00)
                )
            }
        }
    }
}

@Composable
private fun TestMenuLargeLayout(navController: NavController, isLandscape: Boolean) {
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
                text = "Choisisez une catégorie",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Left column
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
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
                            isTertiary = false
                        )
                    }
                }

                // Right column
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
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
                            isTertiary = true
                        )
                    }
                }
            }

            // Новая кнопка 📚 между TEST FINAL и Retour (строго как Retour)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка 📚 слева (как Retour), справа пусто
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "catalog",
                    text = "📚",
                    color = Color(0xFF4CAF50) // Мягкий зелёный
                )
                Spacer(Modifier.width(16.dp))
                // Пустое место справа (невидимый Spacer с weight)
                Spacer(modifier = Modifier.weight(1f))
            }

            // Уменьшенный Spacer для чёткого позиционирования над Retour
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "main_menu",
                    text = "Retour",
                    color = Color(0xFF8B0000)
                )
                Spacer(Modifier.width(16.dp))
                ReturnButton(
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    route = "hall_of_fame",
                    text = "\uD83C\uDFC6",
                    color = Color(0xFFFFFF00)
                )
            }
        }
    }
}

@Composable
private fun CategoryButton(
    text: String,
    category: String,
    navController: NavController,
    modifier: Modifier,
    isTertiary: Boolean
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
            .height(56.dp)
            .padding(vertical = 4.dp),
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
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun ReturnButton(
    navController: NavController,
    modifier: Modifier,
    route: String,
    text: String,
    color: Color
) {
    Button(
        onClick = { navController.navigate(route) },
        modifier = modifier
            .height(56.dp)
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.8f),
            contentColor = if (color == Color(0xFFFFFF00)) Color.Black else Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 15.sp
        )
    }
}