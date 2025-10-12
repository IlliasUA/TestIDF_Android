package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundImage: ImageBitmap? = remember {
        try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("HelpScreen", "Error loading background.png", e)
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Фон
        backgroundImage?.let { image: ImageBitmap ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Верхняя панель с кнопкой назад
            TopAppBar(
                title = {
                    Text(
                        "Guide",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
                )
            )

            // Контент с прокруткой
            val scrollState = rememberScrollState()

            if (isLandscape) {
                // Горизонтальная ориентация - две колонки
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HelpSection(
                            title = "📚 Catégories",
                            description = "Testez vos connaissances par catégorie militaire :",
                            items = listOf(
                                "Chars de combat" to "Identifiez les chars de combat",
                                "Artillerie" to "Reconnaissez les systèmes d'artillerie",
                                "Reconnaissance" to "Véhicules de reconnaissance et d'éclairage",
                                "Génie" to "Équipements du génie militaire",
                                "Avion/Hélicoptère" to "Aéronefs militaires"
                            )
                        )

                        HelpSection(
                            title = "🎯 Test avancé",
                            description = "Un mélange de toutes les catégories pour tester vos connaissances globales. Questions plus difficiles et chronométrées.",
                            items = emptyList()
                        )

                        HelpSection(
                            title = "🏆 Test final",
                            description = "Le test ultime ! Toutes les catégories, questions complexes, temps limité. Vos résultats seront enregistrés dans le Panthéon.",
                            items = emptyList()
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HelpSection(
                            title = "👥 Test collectif",
                            description = "Mode multijoueur en temps réel :",
                            items = listOf(
                                "Chef" to "Créez un groupe et envoyez des tests aux participants",
                                "Participant" to "Rejoignez un groupe avec un code et participez aux tests",
                                "Compétition" to "Comparez vos scores en temps réel"
                            )
                        )

                        HelpSection(
                            title = "✏️ Creation",
                            description = "Créez vos propres tests personnalisés :",
                            items = listOf(
                                "Questions personnalisées" to "Ajoutez vos propres questions et images",
                                "Paramètres flexibles" to "Définissez le temps par question"
                            )
                        )

                        HelpSection(
                            title = "🔍 Rechercher",
                            description = "Explorez le catalogue complet des véhicules et équipements militaires avec leurs caractéristiques détaillées.",
                            items = emptyList()
                        )
                    }
                }
            } else {
                // Вертикальная ориентация - одна колонка
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HelpSection(
                        title = "📚 Catégories",
                        description = "Testez vos connaissances par catégorie militaire :",
                        items = listOf(
                            "Chars de combat" to "Identifiez les chars de combat",
                            "Artillerie" to "Reconnaissez les systèmes d'artillerie",
                            "Reconnaissance" to "Véhicules de reconnaissance et d'éclairage",
                            "Génie" to "Équipements du génie militaire",
                            "Avion/Hélicoptère" to "Aéronefs militaires"
                        )
                    )

                    HelpSection(
                        title = "🎯 Test avancé",
                        description = "Un mélange de toutes les catégories pour tester vos connaissances globales. Questions plus difficiles et chronométrées.",
                        items = emptyList()
                    )

                    HelpSection(
                        title = "🏆 Test final",
                        description = "Le test ultime ! Toutes les catégories, questions complexes, temps limité. Vos résultats seront enregistrés dans le Panthéon.",
                        items = emptyList()
                    )

                    HelpSection(
                        title = "👥 Test collectif",
                        description = "Mode multijoueur en temps réel :",
                        items = listOf(
                            "Chef" to "Créez un groupe et envoyez des tests aux participants",
                            "Participant" to "Rejoignez un groupe avec un code et participez aux tests",
                            "Compétition" to "Comparez vos scores en temps réel"
                        )
                    )

                    HelpSection(
                        title = "✏️ Creation",
                        description = "Créez vos propres tests personnalisés :",
                        items = listOf(
                            "Questions personnalisées" to "Ajoutez vos propres questions et images",
                            "Paramètres flexibles" to "Définissez le temps par question"
                        )
                    )

                    HelpSection(
                        title = "🔍 Rechercher",
                        description = "Explorez le catalogue complet des véhicules et équipements militaires avec leurs caractéristiques détaillées.",
                        items = emptyList()
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun HelpSection(
    title: String,
    description: String,
    items: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = if (items.isNotEmpty()) 12.dp else 0.dp)
            )

            if (items.isNotEmpty()) {
                items.forEach { (itemTitle, itemDescription) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "• ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = itemTitle,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = itemDescription,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}