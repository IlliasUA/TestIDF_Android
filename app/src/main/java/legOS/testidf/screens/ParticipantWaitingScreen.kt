package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import legOS.testidf.data.UserSession
import legOS.testidf.viewmodel.ParticipantRegistrationViewModel
import legOS.testidf.viewmodel.ParticipantWaitingViewModel
import java.io.IOException
import java.io.InputStream

@Composable
fun ParticipantWaitingScreen(
    navController: NavController,
    waitingViewModel: ParticipantWaitingViewModel = viewModel(),
    registrationViewModel: ParticipantRegistrationViewModel = viewModel()
) {
    val uiState by waitingViewModel.uiState.collectAsState()
    val registrationUiState by registrationViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var showLeaveDialog by remember { mutableStateOf(false) }

    // Load background image
    val backgroundImage = remember {
        try {
            context.assets.open("images/background_6.png").use { stream: InputStream ->
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("ParticipantWaitingScreen", "Error loading background_6.png", e)
            null
        }
    }

    // Начинаем слушать уведомления
    LaunchedEffect(Unit) {
        val userId = UserSession.userId
        if (userId != null) {
            waitingViewModel.startListeningForTests(userId)
        }
    }

    // Очищаем слушатель при выходе
    DisposableEffect(Unit) {
        onDispose {
            waitingViewModel.stopListening()
        }
    }

    if (isLandscape) {
        // ГОРИЗОНТАЛЬНЫЙ РЕЖИМ (unchanged)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    backgroundImage?.let {
                        Modifier.paint(
                            painter = BitmapPainter(it),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Modifier.background(MaterialTheme.colorScheme.background)
                )
                .systemBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ЛЕВАЯ ЧАСТЬ - Приветствие и кнопка "Quitter le groupe"
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Bienvenue, ${UserSession.userName}!",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Кнопка выхода из группы
                OutlinedButton(
                    onClick = { showLeaveDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.ExitToApp,
                        null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Quitter le groupe")
                }
            }

            // ПРАВАЯ ЧАСТЬ - Статус и список тестов/информативный текст
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {
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

                Spacer(Modifier.height(16.dp))

                // Список тестов
                if (uiState.availableTests.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(uiState.availableTests) { test ->
                            TestCard(
                                title = test.title,
                                questionCount = test.questionCount,
                                timeLimit = test.timeLimit,
                                onStartClick = {
                                    navController.navigate("take_test/${test.sessionId}")
                                }
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
                .then(
                    backgroundImage?.let {
                        Modifier.paint(
                            painter = BitmapPainter(it),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Modifier.background(MaterialTheme.colorScheme.background)
                )
                .systemBarsPadding()
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

            // Список тестов или информативный текст
            if (uiState.availableTests.isEmpty()) {
                Spacer(Modifier.weight(1f)) // Push content to bottom
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
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
                Spacer(Modifier.height(16.dp)) // Space between status card and test list
                Text(
                    "Tests disponibles:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(uiState.availableTests) { test ->
                        TestCard(
                            title = test.title,
                            questionCount = test.questionCount,
                            timeLimit = test.timeLimit,
                            onStartClick = {
                                navController.navigate("take_test/${test.sessionId}")
                            }
                        )
                    }
                }
            }

            // Кнопка выхода из группы
            OutlinedButton(
                onClick = { showLeaveDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.Default.ExitToApp,
                    null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Quitter le groupe")
            }
        }
    }

    // Диалог подтверждения выхода
    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = { Text("Quitter le groupe?") },
            text = {
                Text(
                    "Êtes-vous sûr de vouloir quitter? Vous devrez entrer à nouveau le code du groupe pour rejoindre.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLeaveDialog = false
                        registrationViewModel.leaveGroup { success ->
                            if (success) {
                                navController.navigate("test_menu") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = !registrationUiState.isLoading
                ) {
                    if (registrationUiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onError
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Quitter")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLeaveDialog = false },
                    enabled = !registrationUiState.isLoading
                ) {
                    Text("Annuler")
                }
            }
        )
    }

    // Snackbar для ошибок
    if (registrationUiState.error != null) {
        LaunchedEffect(registrationUiState.error) {
            // Можно добавить Snackbar если нужно
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