package legOS.testidf.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import legOS.testidf.data.UserSession
import legOS.testidf.viewmodel.ParticipantWaitingViewModel

@Composable
fun ParticipantWaitingScreen(
    navController: NavController,
    viewModel: ParticipantWaitingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Начинаем слушать уведомления
    LaunchedEffect(Unit) {
        val userId = UserSession.userId
        if (userId != null) {
            viewModel.startListeningForTests(userId)
        }
    }

    // Очищаем слушатель при выходе
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopListening()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            "Bienvenue, ${UserSession.userName}!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Статус
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.availableTests.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(16.dp))
                    Text(
                        "En attente d'un test...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Icon(
                        Icons.Default.Notifications,
                        null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        "${uiState.availableTests.size} test(s) disponible(s)",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Список тестов
        if (uiState.availableTests.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.HourglassEmpty,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "Le chef va bientôt envoyer un test.\nVous serez notifié automatiquement.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        } else {
            Text(
                "Tests disponibles:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.availableTests) { test ->
                    TestCard(
                        title = test.title,
                        questionCount = test.questionCount,
                        timeLimit = test.timeLimit,
                        onStartClick = {
                            // Переход к прохождению теста
                            navController.navigate("take_test/${test.sessionId}")
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Кнопка выхода
        OutlinedButton(
            onClick = {
                UserSession.clearSession()
                navController.navigate("test_menu") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Quitter")
        }
    }
}

@Composable
fun TestCard(
    title: String,
    questionCount: Int,
    timeLimit: Int,
    onStartClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Assignment,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "$questionCount questions • ${timeLimit}s/question",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
            Button(onClick = onStartClick) {
                Text("Commencer")
            }
        }
    }
}