package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
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
import legOS.testidf.data.UserSession
import legOS.testidf.viewmodel.ParticipantRegistrationViewModel
import legOS.testidf.viewmodel.ParticipantWaitingViewModel
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

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
    var showGroupClosedDialog by remember { mutableStateOf(false) }
    var selectedTab by rememberSaveable { mutableStateOf(0) }

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

    LaunchedEffect(Unit) {
        val userId = UserSession.userId
        Log.d("ParticipantWaitingScreen", "🚀 ParticipantWaitingScreen started")
        Log.d("ParticipantWaitingScreen", "User ID: $userId, Group ID: ${UserSession.groupId}")

        if (userId != null) {
            waitingViewModel.startListeningForTests(userId)
            waitingViewModel.startListeningForGroupStatus()
            waitingViewModel.startListeningForResults()
            Log.d("ParticipantWaitingScreen", "✅ Listeners started")
        }
    }

    LaunchedEffect(uiState.isGroupActive) {
        if (!uiState.isGroupActive) {
            showGroupClosedDialog = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            waitingViewModel.stopListening()
        }
    }

    // Group closure dialog
    if (showGroupClosedDialog) {
        AlertDialog(
            onDismissRequest = { showGroupClosedDialog = false },
            icon = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(
                    stringResource(R.string.group_closed_title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        uiState.groupClosedMessage ?: stringResource(R.string.group_closed_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        stringResource(R.string.group_closed_details),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showGroupClosedDialog = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    // Main UI
    if (isLandscape) {
        LandscapeLayout(
            uiState = uiState,
            onStartTest = { sessionId -> navController.navigate("take_test/$sessionId") },
            onLeaveClick = { showLeaveDialog = true },
            backgroundImage = backgroundImage
        )
    } else {
        PortraitLayout(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            uiState = uiState,
            onStartTest = { sessionId -> navController.navigate("take_test/$sessionId") },
            onLeaveClick = { showLeaveDialog = true },
            backgroundImage = backgroundImage
        )
    }

    // Leave confirmation dialog
    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = { Text(stringResource(R.string.leave_group_title)) },
            text = {
                Text(stringResource(R.string.leave_group_message))
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
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(stringResource(R.string.leave))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}

@Composable
fun PortraitLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    uiState: legOS.testidf.viewmodel.ParticipantWaitingUiState,
    onStartTest: (String) -> Unit,
    onLeaveClick: () -> Unit,
    backgroundImage: androidx.compose.ui.graphics.ImageBitmap?
) {
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
        Text(
            stringResource(R.string.welcome_participant, UserSession.userName ?: ""),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.0f)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                text = {
                    Text(
                        stringResource(R.string.tests_available),
                        color = if (selectedTab == 0)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                },
                icon = {
                    Icon(
                        Icons.Default.Assignment,
                        null,
                        tint = if (selectedTab == 0)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                text = {
                    Text(
                        stringResource(R.string.results_title),
                        color = if (selectedTab == 1)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                },
                icon = {
                    Icon(
                        Icons.Default.Leaderboard,
                        null,
                        tint = if (selectedTab == 1)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            )
        }

        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> TestsTab(
                    availableTests = uiState.availableTests,
                    isGroupActive = uiState.isGroupActive,
                    onStartTest = onStartTest
                )
                1 -> ResultsTab(
                    testResults = uiState.testResults,
                    isLoading = uiState.isLoadingResults
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onLeaveClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.ExitToApp, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.leave_group_button))
        }
    }
}

@Composable
fun LandscapeLayout(
    uiState: legOS.testidf.viewmodel.ParticipantWaitingUiState,
    onStartTest: (String) -> Unit,
    onLeaveClick: () -> Unit,
    backgroundImage: androidx.compose.ui.graphics.ImageBitmap?
) {
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
        // ЛЕВАЯ ЧАСТЬ - Résultats
        Card(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        Icons.Default.Leaderboard,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        stringResource(R.string.results_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Divider(modifier = Modifier.padding(bottom = 12.dp))

                if (uiState.isLoadingResults) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.testResults.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Assessment,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            stringResource(R.string.no_results),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Группируем и показываем результаты
                    val groupedResults = uiState.testResults.groupBy { it.sessionTitle }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        groupedResults.forEach { (sessionTitle, results) ->
                            item {
                                Text(
                                    sessionTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            itemsIndexed(results.sortedByDescending { it.percentage }) { index, result ->
                                ResultCard(result = result, position = index + 1)
                            }
                        }
                    }
                }
            }
        }

        // ПРАВАЯ ЧАСТЬ - Tests
        Card(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(
                            Icons.Default.Assignment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            stringResource(R.string.tests_available),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Divider(modifier = Modifier.padding(bottom = 12.dp))

                    Text(
                        stringResource(R.string.welcome_participant, UserSession.userName ?: ""),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Контент Tests
                    if (uiState.availableTests.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.HourglassEmpty,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                if (uiState.isGroupActive) {
                                    stringResource(R.string.no_tests_message)
                                } else {
                                    stringResource(R.string.group_closed_no_tests)
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.availableTests) { test ->
                                TestCard(
                                    title = test.title,
                                    questionCount = test.questionCount,
                                    timeLimit = test.timeLimit,
                                    onStartClick = { onStartTest(test.sessionId) }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onLeaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.ExitToApp, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.leave_group_button))
                }
            }
        }
    }
}

@Composable
fun TestsTab(
    availableTests: List<legOS.testidf.viewmodel.AvailableTest>,
    isGroupActive: Boolean,
    onStartTest: (String) -> Unit
) {
    if (availableTests.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.HourglassEmpty,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                if (isGroupActive) {
                    stringResource(R.string.no_tests_message)
                } else {
                    stringResource(R.string.group_closed_no_tests)
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(availableTests) { test ->
                TestCard(
                    title = test.title,
                    questionCount = test.questionCount,
                    timeLimit = test.timeLimit,
                    onStartClick = { onStartTest(test.sessionId) }
                )
            }
        }
    }
}

@Composable
fun ResultsTab(
    testResults: List<legOS.testidf.viewmodel.TestResult>,
    isLoading: Boolean
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (testResults.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Assessment,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.no_results),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            val groupedResults = testResults.groupBy { it.sessionTitle }

            groupedResults.forEach { (sessionTitle, results) ->
                item {
                    Text(
                        text = sessionTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                itemsIndexed(results.sortedByDescending { it.percentage }) { index, result ->
                    ResultCard(result = result, position = index + 1)
                }
            }
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
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    stringResource(R.string.questions_with_time, questionCount, timeLimit),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
            Button(onClick = onStartClick) {
                Text(stringResource(R.string.start_test_button))
            }
        }
    }
}

@Composable
fun ResultCard(
    result: legOS.testidf.viewmodel.TestResult,
    position: Int
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val formattedDate = result.completedAt?.toDate()?.let { dateFormat.format(it) } ?: ""

    // Цвета медалей
    val medalColors = when (position) {
        1 -> listOf(Color(0xFFFFD700), Color(0xFFFFA500)) // Золотой
        2 -> listOf(Color(0xFFC0C0C0), Color(0xFF808080)) // Серебряный
        3 -> listOf(Color(0xFFCD7F32), Color(0xFF8B4513)) // Бронзовый
        else -> null
    }

    val scoreColor = when {
        result.percentage >= 80 -> MaterialTheme.colorScheme.tertiary
        result.percentage >= 50 -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (medalColors != null) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Box {
            // Градиент для топ-3
            if (medalColors != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = medalColors.map { it.copy(alpha = 0.2f) }
                            )
                        )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Позиция с медалью
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (medalColors != null) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Brush.radialGradient(medalColors),
                                    shape = androidx.compose.foundation.shape.CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#$position",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                        }
                    } else {
                        Text(
                            text = "#$position",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = if (medalColors != null) medalColors[0] else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        result.participantName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        stringResource(R.string.score, result.score, result.totalQuestions),
                        style = MaterialTheme.typography.titleLarge,
                        color = scoreColor
                    )
                    Text(
                        stringResource(R.string.percentage_value, String.format("%.1f", result.percentage).toDouble().toInt()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = scoreColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}