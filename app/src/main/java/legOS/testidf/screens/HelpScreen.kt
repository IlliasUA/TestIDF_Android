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
import legOS.testidf.R
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
                        context.getString(R.string.help_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = context.getString(R.string.back_button),
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
                            title = context.getString(R.string.help_categories_title),
                            description = context.getString(R.string.help_categories_description),
                            items = listOf(
                                context.getString(R.string.help_categories_tanks_title) to
                                        context.getString(R.string.help_categories_tanks_desc),
                                context.getString(R.string.help_categories_artillery_title) to
                                        context.getString(R.string.help_categories_artillery_desc),
                                context.getString(R.string.help_categories_recon_title) to
                                        context.getString(R.string.help_categories_recon_desc),
                                context.getString(R.string.help_categories_engineer_title) to
                                        context.getString(R.string.help_categories_engineer_desc),
                                context.getString(R.string.help_categories_air_title) to
                                        context.getString(R.string.help_categories_air_desc)
                            )
                        )

                        HelpSection(
                            title = context.getString(R.string.help_advanced_title),
                            description = context.getString(R.string.help_advanced_description),
                            items = emptyList()
                        )

                        HelpSection(
                            title = context.getString(R.string.help_final_title),
                            description = context.getString(R.string.help_final_description),
                            items = emptyList()
                        )

                        HelpSection(
                            title = context.getString(R.string.help_search_title),
                            description = context.getString(R.string.help_search_description),
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
                            title = context.getString(R.string.help_collective_title),
                            description = context.getString(R.string.help_collective_description),
                            items = listOf(
                                context.getString(R.string.help_collective_chef_title) to
                                        context.getString(R.string.help_collective_chef_desc),
                                context.getString(R.string.help_collective_participant_title) to
                                        context.getString(R.string.help_collective_participant_desc),
                                context.getString(R.string.help_collective_competition_title) to
                                        context.getString(R.string.help_collective_competition_desc)
                            )
                        )

                        HelpSection(
                            title = context.getString(R.string.help_creation_title),
                            description = context.getString(R.string.help_creation_description),
                            items = listOf(
                                context.getString(R.string.help_creation_custom_title) to
                                        context.getString(R.string.help_creation_custom_desc),
                                context.getString(R.string.help_creation_flexible_title) to
                                        context.getString(R.string.help_creation_flexible_desc)
                            )
                        )

                        HelpSection(
                            title = context.getString(R.string.help_ai_assistant_title),
                            description = context.getString(R.string.help_ai_assistant_description),
                            items = listOf(
                                context.getString(R.string.help_ai_assistant_questions_title) to
                                        context.getString(R.string.help_ai_assistant_questions_desc)
                            ),
                            iconPath = "images/icon_ai.webp"
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
                        title = context.getString(R.string.help_categories_title),
                        description = context.getString(R.string.help_categories_description),
                        items = listOf(
                            context.getString(R.string.help_categories_tanks_title) to
                                    context.getString(R.string.help_categories_tanks_desc),
                            context.getString(R.string.help_categories_artillery_title) to
                                    context.getString(R.string.help_categories_artillery_desc),
                            context.getString(R.string.help_categories_recon_title) to
                                    context.getString(R.string.help_categories_recon_desc),
                            context.getString(R.string.help_categories_engineer_title) to
                                    context.getString(R.string.help_categories_engineer_desc),
                            context.getString(R.string.help_categories_air_title) to
                                    context.getString(R.string.help_categories_air_desc)
                        )
                    )

                    HelpSection(
                        title = context.getString(R.string.help_advanced_title),
                        description = context.getString(R.string.help_advanced_description),
                        items = emptyList()
                    )

                    HelpSection(
                        title = context.getString(R.string.help_final_title),
                        description = context.getString(R.string.help_final_description),
                        items = emptyList()
                    )

                    HelpSection(
                        title = context.getString(R.string.help_collective_title),
                        description = context.getString(R.string.help_collective_description),
                        items = listOf(
                            context.getString(R.string.help_collective_chef_title) to
                                    context.getString(R.string.help_collective_chef_desc),
                            context.getString(R.string.help_collective_participant_title) to
                                    context.getString(R.string.help_collective_participant_desc),
                            context.getString(R.string.help_collective_competition_title) to
                                    context.getString(R.string.help_collective_competition_desc)
                        )
                    )

                    HelpSection(
                        title = context.getString(R.string.help_creation_title),
                        description = context.getString(R.string.help_creation_description),
                        items = listOf(
                            context.getString(R.string.help_creation_custom_title) to
                                    context.getString(R.string.help_creation_custom_desc),
                            context.getString(R.string.help_creation_flexible_title) to
                                    context.getString(R.string.help_creation_flexible_desc)
                        )
                    )

                    HelpSection(
                        title = context.getString(R.string.help_search_title),
                        description = context.getString(R.string.help_search_description),
                        items = emptyList()
                    )

                    HelpSection(
                        title = context.getString(R.string.help_ai_assistant_title),
                        description = context.getString(R.string.help_ai_assistant_description),
                        items = listOf(
                            context.getString(R.string.help_ai_assistant_questions_title) to
                                    context.getString(R.string.help_ai_assistant_questions_desc)
                        ),
                        iconPath = "images/icon_ai.webp"
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
    items: List<Pair<String, String>>,
    iconPath: String? = null
) {
    val context = LocalContext.current

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
            // Заголовок с опциональной иконкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Загрузка иконки если указан путь
                if (iconPath != null) {
                    val iconBitmap = remember(iconPath) {
                        try {
                            context.assets.open(iconPath).use { inputStream ->
                                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                            }
                        } catch (e: IOException) {
                            Log.e("HelpSection", "Error loading icon: $iconPath", e)
                            null
                        }
                    }

                    iconBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Section Icon",
                            modifier = Modifier
                                .size(42.dp)
                                .padding(end = 8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

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