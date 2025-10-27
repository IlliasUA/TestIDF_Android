package legOS.testidf.screens

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import legOS.testidf.R
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
    var selectedParticipant by remember { mutableStateOf<ParticipantResult?>(null) }
    var selectedSessionIndex by remember { mutableStateOf(0) }

    LaunchedEffect(sessionId) {
        viewModel.loadResults(sessionId)
    }

    // ОТЛАДКА - добавьте эту часть
    LaunchedEffect(uiState.sessionGroups.size) {
        Log.d("SessionResultsScreen", "=== UI State Updated ===")
        Log.d("SessionResultsScreen", "Total session groups: ${uiState.sessionGroups.size}")
        uiState.sessionGroups.forEachIndexed { index, group ->
            Log.d("SessionResultsScreen", "Group $index: ${group.timestamp}, ${group.results.size} participants")
        }
    }

    // Получаем текущую сессию для отображения
    val currentSessionResults = uiState.sessionGroups.getOrNull(selectedSessionIndex)

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
                // ЛЕВАЯ ЧАСТЬ
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.ArrowBack, stringResource(R.string.back_navigation))
                        }
                        Text(
                            stringResource(R.string.session_results_title, currentSessionResults?.results?.size ?: 0),
                            style = MaterialTheme.typography.titleLarge
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
                    } else if (currentSessionResults == null || currentSessionResults.results.isEmpty()) {
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
                                    stringResource(R.string.waiting_for_results),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(currentSessionResults.results) { result ->
                                ParticipantResultCard(
                                    result = result,
                                    onInfoClick = { selectedParticipant = result }
                                )
                            }
                        }
                    }
                }

                // ПРАВАЯ ЧАСТЬ
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            stringResource(R.string.statistics_header),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(Modifier.height(12.dp))

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
                                stringResource(R.string.questions_with_time, uiState.totalQuestions, uiState.timeLimit),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // ВКЛАДКИ ДЛЯ РАЗНЫХ СЕССИЙ
                    if (uiState.sessionGroups.size > 1) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            ScrollableTabRow(
                                selectedTabIndex = selectedSessionIndex,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Color.Transparent,
                                edgePadding = 8.dp
                            ) {
                                uiState.sessionGroups.forEachIndexed { index, _ ->
                                    Tab(
                                        selected = selectedSessionIndex == index,
                                        onClick = { selectedSessionIndex = index },
                                        text = {
                                            Text(
                                                stringResource(R.string.test_number_label, index + 1),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    // СТАТИСТИКА ТЕКУЩЕЙ СЕССИИ
                    currentSessionResults?.let { sessionGroup ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ResultStatItem(
                                    label = stringResource(R.string.participants_count),
                                    value = "${sessionGroup.results.size}",
                                    icon = Icons.Default.Group
                                )
                                ResultStatItem(
                                    label = stringResource(R.string.average_score),
                                    value = "${sessionGroup.averageScore}%",
                                    icon = Icons.Default.BarChart
                                )
                                ResultStatItem(
                                    label = stringResource(R.string.best_score_label),
                                    value = "${sessionGroup.bestScore}%",
                                    icon = Icons.Default.EmojiEvents
                                )
                            }
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
                // HEADER
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.back_navigation))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.session_results_title, currentSessionResults?.results?.size ?: 0),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Spacer(Modifier.height(12.dp))

                // ИНФОРМАЦИЯ О СЕССИИ
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            uiState.sessionTitle,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            stringResource(R.string.questions_with_time, uiState.totalQuestions, uiState.timeLimit),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ВКЛАДКИ СЕССИЙ
                if (uiState.sessionGroups.size > 1) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        ScrollableTabRow(
                            selectedTabIndex = selectedSessionIndex,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = Color.Transparent,
                            edgePadding = 8.dp
                        ) {
                            uiState.sessionGroups.forEachIndexed { index, _ ->
                                Tab(
                                    selected = selectedSessionIndex == index,
                                    onClick = { selectedSessionIndex = index },
                                    text = {
                                        Text(
                                            stringResource(R.string.test_number_label, index + 1),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // СТАТИСТИКА
                currentSessionResults?.let { sessionGroup ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ResultStatItem(
                                label = stringResource(R.string.participants_count),
                                value = "${sessionGroup.results.size}",
                                icon = Icons.Default.Group
                            )
                            ResultStatItem(
                                label = stringResource(R.string.average_score),
                                value = "${sessionGroup.averageScore}%",
                                icon = Icons.Default.BarChart
                            )
                            ResultStatItem(
                                label = stringResource(R.string.best_score_label),
                                value = "${sessionGroup.bestScore}%",
                                icon = Icons.Default.EmojiEvents
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // СПИСОК УЧАСТНИКОВ
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (currentSessionResults == null || currentSessionResults.results.isEmpty()) {
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
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                stringResource(R.string.waiting_for_results),
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
                        items(currentSessionResults.results) { result ->
                            ParticipantResultCard(
                                result = result,
                                onInfoClick = { selectedParticipant = result }
                            )
                        }
                    }
                }
            }
        }

        // ДИАЛОГ С ДЕТАЛЬНЫМИ РЕЗУЛЬТАТАМИ
        selectedParticipant?.let { participant ->
            DetailedResultDialog(
                result = participant,
                onDismiss = { selectedParticipant = null }
            )
        }
    }
}

@Composable
private fun ParticipantResultCard(
    result: ParticipantResult,
    onInfoClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = when {
                    result.rank == 1 -> Color(0xFFFFD700)
                    result.rank == 2 -> Color(0xFFC0C0C0)
                    result.rank == 3 -> Color(0xFFCD7F32)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "#${result.rank}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (result.rank <= 3) Color.Black else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    result.participantName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    stringResource(R.string.correct_answers_count, result.score, result.totalQuestions),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Surface(
                shape = MaterialTheme.shapes.small,
                color = when {
                    result.percentage >= 80 -> Color(0xFF4CAF50)
                    result.percentage >= 60 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                }.copy(alpha = 0.2f)
            ) {
                Text(
                    stringResource(R.string.percentage_value, result.percentage),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = when {
                        result.percentage >= 80 -> Color(0xFF4CAF50)
                        result.percentage >= 60 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                )
            }

            Spacer(Modifier.width(8.dp))

            FilledTonalButton(
                onClick = onInfoClick,
                modifier = Modifier.height(36.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.info_button), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun DetailedResultDialog(
    result: ParticipantResult,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.85f),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(result.participantName)
                    Text(
                        stringResource(R.string.participant_score_details, result.score, result.totalQuestions, result.percentage),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, stringResource(R.string.close_dialog))
                }
            }
        },
        text = {
            if (result.answers.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.details_not_available),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(result.answers) { index, answer ->
                        AnswerDetailCard(
                            questionNumber = index + 1,
                            answer = answer
                        )
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
private fun AnswerDetailCard(
    questionNumber: Int,
    answer: Answer
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (answer.isCorrect)
                Color(0xFF4CAF50).copy(alpha = 0.1f)
            else
                Color(0xFFF44336).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.question_number_label, questionNumber),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    if (answer.isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    null,
                    tint = if (answer.isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.height(8.dp))
            Text(answer.questionText, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, null, Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    stringResource(R.string.user_answer_label, answer.userAnswer),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (answer.isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
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
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

data class ParticipantResult(
    val participantId: String = "",
    val participantName: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val percentage: Int = 0,
    val rank: Int = 0,
    val answers: List<Answer> = emptyList()
)

data class Answer(
    val questionText: String = "",
    val userAnswer: String = "",
    val correctAnswer: String = "",
    val isCorrect: Boolean = false
)

// НОВЫЙ: Группа результатов для одной сессии
data class SessionResultGroup(
    val timestamp: String = "",
    val results: List<ParticipantResult> = emptyList(),
    val averageScore: Int = 0,
    val bestScore: Int = 0
)