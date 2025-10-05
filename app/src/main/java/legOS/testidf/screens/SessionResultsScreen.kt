package legOS.testidf.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import legOS.testidf.loadImageFromAssets
import legOS.testidf.viewmodel.SessionResultsViewModel

@Composable
fun SessionResultsScreen(
    navController: NavController,
    sessionId: String,
    viewModel: SessionResultsViewModel = viewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val backgroundImage = loadImageFromAssets(context, "images/background_6.png")

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sessionId) {
        viewModel.loadResults(sessionId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                backgroundImage?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it.asImageBitmap()),
                        contentScale = ContentScale.Crop
                    )
                } ?: Modifier
            )
    ) {
        if (isLandscape) {
            // ГОРИЗОНТАЛЬНЫЙ РЕЖИМ
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ЛЕВАЯ ЧАСТЬ - Список результатов
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                ) {
                    // Заголовок с кнопкой возврата - ВЫРОВНЕН ПО ВЫСОТЕ
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp), // ФИКСИРОВАННАЯ ВЫСОТА
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.ArrowBack, "Retour")
                        }
                        Text(
                            "Résultats (${uiState.results.size})",
                            style = MaterialTheme.typography.titleLarge // ТОТ ЖЕ РАЗМЕР
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (uiState.results.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.HourglassEmpty,
                                    null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "En attente des résultats",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp) // УМЕНЬШЕН ОТСТУП
                        ) {
                            items(uiState.results) { result ->
                                ParticipantResultCard(result)
                            }
                        }
                    }
                }

                // ПРАВАЯ ЧАСТЬ - Статистика и информация
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                ) {
                    // Заголовок - ВЫРОВНЕН ПО ВЫСОТЕ
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp), // ТА ЖЕ ВЫСОТА ЧТО И СЛЕВА
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            "Statistiques",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Информация о тесте
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                uiState.sessionTitle,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "${uiState.totalQuestions} questions • ${uiState.timeLimit}s/question",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Статистика
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ResultStatItem(
                                label = "Participants",
                                value = uiState.results.size.toString(),
                                icon = Icons.Default.Person
                            )
                            ResultStatItem(
                                label = "Moyenne",
                                value = "${uiState.averageScore}%",
                                icon = Icons.Default.BarChart
                            )
                            ResultStatItem(
                                label = "Meilleur",
                                value = "${uiState.bestScore}%",
                                icon = Icons.Default.TrendingUp
                            )
                        }
                    }
                }
            }
        } else {
            // ВЕРТИКАЛЬНЫЙ РЕЖИМ
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Retour")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            uiState.sessionTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "${uiState.totalQuestions} questions • ${uiState.timeLimit}s/question",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ResultStatItem(
                            label = "Participants",
                            value = uiState.results.size.toString(),
                            icon = Icons.Default.Person
                        )
                        ResultStatItem(
                            label = "Moyenne",
                            value = "${uiState.averageScore}%",
                            icon = Icons.Default.BarChart
                        )
                        ResultStatItem(
                            label = "Meilleur",
                            value = "${uiState.bestScore}%",
                            icon = Icons.Default.TrendingUp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.results.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.HourglassEmpty,
                                null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                "En attente des résultats",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.results) { result ->
                            ParticipantResultCard(result)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultStatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ParticipantResultCard(result: ParticipantResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp), // УМЕНЬШЕНА ВЫСОТА НА 25% (было ~80dp)
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp), // УМЕНЬШЕН PADDING
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Позиция
            Surface(
                modifier = Modifier.size(36.dp), // УМЕНЬШЕН РАЗМЕР
                shape = MaterialTheme.shapes.small,
                color = when {
                    result.rank == 1 -> Color(0xFFFFD700)
                    result.rank == 2 -> Color(0xFFC0C0C0)
                    result.rank == 3 -> Color(0xFFCD7F32)
                    else -> MaterialTheme.colorScheme.primaryContainer
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "${result.rank}",
                        style = MaterialTheme.typography.titleSmall, // УМЕНЬШЕН ШРИФТ
                        color = if (result.rank <= 3) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    result.participantName,
                    style = MaterialTheme.typography.bodyLarge // УМЕНЬШЕН ШРИФТ
                )
                Text(
                    "${result.score}/${result.totalQuestions} correctes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Score
            Surface(
                shape = MaterialTheme.shapes.small,
                color = when {
                    result.percentage >= 80 -> Color(0xFF4CAF50)
                    result.percentage >= 60 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                }.copy(alpha = 0.2f)
            ) {
                Text(
                    "${result.percentage}%",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), // УМЕНЬШЕН PADDING
                    style = MaterialTheme.typography.titleSmall, // УМЕНЬШЕН ШРИФТ
                    color = when {
                        result.percentage >= 80 -> Color(0xFF4CAF50)
                        result.percentage >= 60 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                )
            }
        }
    }
}

data class ParticipantResult(
    val participantId: String = "",
    val participantName: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val percentage: Int = 0,
    val rank: Int = 0
)