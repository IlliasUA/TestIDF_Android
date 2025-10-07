package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession
import legOS.testidf.viewmodel.Participant
import legOS.testidf.viewmodel.SendTestViewModel
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestSession(
    val sessionId: String,
    val title: String,
    val questionCount: Int,
    val timeLimit: Int
) : Parcelable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendTestScreen(
    navController: NavController,
    sessionId: String,
    viewModel: SendTestViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    // State variables
    var availableTests by rememberSaveable { mutableStateOf<List<TestSession>>(emptyList()) }
    var selectedTestId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var showDeleteConfirmDialog by remember { mutableStateOf<String?>(null) }
    var showExitConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    val backgroundImage = remember {
        try {
            context.assets.open("images/background_6.png").use { stream: InputStream ->
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("SendTestScreen", "Error loading background", e)
            null
        }
    }

    suspend fun loadTests() {
        try {
            val groupId = UserSession.groupId
            Log.d("SendTestScreen", "Loading tests for group: $groupId")

            if (groupId != null) {
                val allSessions = firestore.collection("test_sessions")
                    .whereEqualTo("groupId", groupId)
                    .get()
                    .await()

                availableTests = allSessions.documents
                    .filter { it.getString("status") == "ready" }
                    .mapNotNull { doc ->
                        val title = doc.getString("title")
                        val questionRefs = doc.get("questionRefs") as? List<*> ?: emptyList<Any>()
                        val timeLimit = doc.getLong("timeLimit")?.toInt() ?: 15

                        if (!title.isNullOrEmpty() && questionRefs.isNotEmpty()) {
                            TestSession(
                                sessionId = doc.id,
                                title = title,
                                questionCount = questionRefs.size,
                                timeLimit = timeLimit
                            )
                        } else null
                    }

                Log.d("SendTestScreen", "Loaded ${availableTests.size} tests")
            }
        } catch (e: Exception) {
            Log.e("SendTestScreen", "Error loading tests", e)
        }
    }

    suspend fun deleteGroupSession() {
        try {
            val groupId = UserSession.groupId
            if (groupId != null) {
                firestore.collection("groups").document(groupId).delete().await()
                val sessions = firestore.collection("test_sessions")
                    .whereEqualTo("groupId", groupId)
                    .get()
                    .await()
                sessions.documents.forEach { session ->
                    firestore.collection("test_sessions").document(session.id).delete().await()
                }
                Log.d("SendTestScreen", "Group session $groupId deleted")
            }
        } catch (e: Exception) {
            Log.e("SendTestScreen", "Error deleting group session", e)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSession(sessionId)
        delay(500)
        loadTests()
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        navController.currentBackStackEntry?.savedStateHandle?.let { handle ->
            val testCreated = handle.get<Boolean>("testCreated")
            if (testCreated == true) {
                handle.remove<Boolean>("testCreated")
                delay(500)
                loadTests()
            }
        }
    }

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(3000)
            showSuccessMessage = false
        }
    }

    if (isLandscape) {
        // ГОРИЗОНТАЛЬНЫЙ РЕЖИМ
        Row(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    backgroundImage?.let {
                        Modifier.paint(painter = BitmapPainter(it), contentScale = ContentScale.Crop)
                    } ?: Modifier.background(MaterialTheme.colorScheme.background)
                )
                .systemBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(0.5f).fillMaxHeight()) {
                Text("Participants (${uiState.participants.size})", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))

                if (uiState.participants.isEmpty()) {
                    Card(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.PersonOff, null, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Aucun participant")
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.participants) { ParticipantItem(it) }
                    }
                }
            }

            Column(modifier = Modifier.weight(0.5f).fillMaxHeight()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Mes tests", style = MaterialTheme.typography.titleLarge)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { scope.launch { loadTests() } }) {
                            Icon(Icons.Default.Refresh, "Actualiser")
                        }

                        IconButton(onClick = { showExitConfirmDialog = true }) {
                            Icon(Icons.Default.Home, "Menu principal")
                        }

                        FilledTonalButton(onClick = {
                            val testName = "Test ${System.currentTimeMillis()}"
                            navController.currentBackStackEntry?.savedStateHandle?.set("testName", testName)
                            navController.navigate("creation_online")
                        }) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Créer")
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (uiState.groupCode.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Code groupe", style = MaterialTheme.typography.labelSmall)
                                Text(uiState.groupCode, style = MaterialTheme.typography.titleLarge)
                            }
                            Icon(Icons.Default.QrCode, null, modifier = Modifier.size(28.dp))
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                if (availableTests.isEmpty()) {
                    Card(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Assignment, null, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Aucun test créé", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Cliquez sur Créer pour commencer", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(availableTests) { test ->
                            TestCard(
                                test = test,
                                isSelected = selectedTestId == test.sessionId,
                                onSelect = { selectedTestId = test.sessionId },
                                onDelete = { showDeleteConfirmDialog = test.sessionId }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                AnimatedVisibility(visible = showSuccessMessage) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text("Test envoyé avec succès!", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            selectedTestId?.let { testId ->
                                navController.navigate("session_results/$testId")
                            }
                        },
                        enabled = selectedTestId != null && uiState.hasCompletedTests,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Assessment, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Résultats")
                    }

                    Button(
                        onClick = {
                            selectedTestId?.let { testId ->
                                viewModel.sendTestToParticipants(testId)
                                showSuccessMessage = true
                            }
                        },
                        enabled = selectedTestId != null && uiState.participants.isNotEmpty() && !uiState.isSending,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (uiState.isSending) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        } else {
                            Icon(Icons.Default.Send, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Envoyer")
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
                        Modifier.paint(painter = BitmapPainter(it), contentScale = ContentScale.Crop)
                    } ?: Modifier.background(MaterialTheme.colorScheme.background)
                )
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Chef", style = MaterialTheme.typography.headlineMedium)
                Row {
                    IconButton(onClick = { scope.launch { loadTests() } }) {
                        Icon(Icons.Default.Refresh, "Actualiser")
                    }
                    IconButton(onClick = { showExitConfirmDialog = true }) {
                        Icon(Icons.Default.Home, "Menu")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (uiState.groupCode.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Code du groupe", style = MaterialTheme.typography.labelMedium)
                            Text(uiState.groupCode, style = MaterialTheme.typography.headlineMedium)
                        }
                        Icon(Icons.Default.QrCode, null, modifier = Modifier.size(40.dp))
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    val testName = "Test ${System.currentTimeMillis()}"
                    navController.currentBackStackEntry?.savedStateHandle?.set("testName", testName)
                    navController.navigate("creation_online")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Créer un test")
            }

            Spacer(Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Tests (${availableTests.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Participants (${uiState.participants.size})") }
                )
            }

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                0 -> {
                    if (availableTests.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Assignment, null, modifier = Modifier.size(64.dp))
                                Spacer(Modifier.height(16.dp))
                                Text("Aucun test créé")
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(8.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(availableTests) { test ->
                                    TestCard(
                                        test = test,
                                        isSelected = selectedTestId == test.sessionId,
                                        onSelect = { selectedTestId = test.sessionId },
                                        onDelete = { showDeleteConfirmDialog = test.sessionId }
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    if (uiState.participants.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.PersonOff, null, modifier = Modifier.size(64.dp))
                                Spacer(Modifier.height(16.dp))
                                Text("Aucun participant")
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(8.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.participants) { ParticipantItem(it) }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(visible = showSuccessMessage) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Test envoyé avec succès!")
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        selectedTestId?.let { testId ->
                            navController.navigate("session_results/$testId")
                        }
                    },
                    enabled = selectedTestId != null && uiState.hasCompletedTests,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Assessment, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Résultats")
                }

                Button(
                    onClick = {
                        selectedTestId?.let { testId ->
                            viewModel.sendTestToParticipants(testId)
                            showSuccessMessage = true
                        }
                    },
                    enabled = selectedTestId != null && uiState.participants.isNotEmpty() && !uiState.isSending,
                    modifier = Modifier.weight(1f)
                ) {
                    if (uiState.isSending) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Icon(Icons.Default.Send, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Envoyer")
                    }
                }
            }
        }
    }

    // Dialog for deleting a test
    showDeleteConfirmDialog?.let { testId ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text("Supprimer le test?") },
            text = { Text("Cette action est irréversible.") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            FirebaseFirestore.getInstance()
                                .collection("test_sessions")
                                .document(testId)
                                .delete()
                                .await()

                            availableTests = availableTests.filter { it.sessionId != testId }
                            if (selectedTestId == testId) selectedTestId = null
                            showDeleteConfirmDialog = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = null }) { Text("Annuler") }
            }
        )
    }

    // Dialog for confirming exit to main menu
    if (showExitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showExitConfirmDialog = false },
            title = { Text("Quitter vers le menu principal ?") },
            text = {
                Text(
                    "Cela supprimera la session de groupe actuelle. Cette action est irréversible.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            deleteGroupSession()
                            showExitConfirmDialog = false
                            navController.navigate("test_menu") {
                                popUpTo("test_menu") { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Quitter")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
fun TestCard(test: TestSession, isSelected: Boolean, onSelect: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onSelect
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(test.title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("${test.questionCount} questions • ${test.timeLimit}s/question", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Close, "Supprimer", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun ParticipantItem(participant: Participant) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Text(participant.name)
        }
    }
}