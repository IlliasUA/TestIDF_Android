package legOS.testidf.screens

import com.google.firebase.Timestamp
import kotlinx.coroutines.delay
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession
import legOS.testidf.viewmodel.Participant
import legOS.testidf.viewmodel.SendTestViewModel
import legOS.testidf.R
import java.io.IOException
import java.io.InputStream
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TestSession(
    val id: String,
    val title: String,
    val questionCount: Int,
    val timeLimit: Int,
    val createdAt: com.google.firebase.Timestamp? = null,
    val testNumber: Int = 0
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

    // String resources
    val participantsLabel = stringResource(R.string.participants_header)
    val noParticipantMessage = stringResource(R.string.no_participants_message)
    val testsLabel = stringResource(R.string.tests_available)
    val refreshLabel = stringResource(R.string.refresh)
    val mainMenuLabel = stringResource(R.string.main_menu_title)
    val createButtonLabel = stringResource(R.string.create_test_button)
    val groupCodeLabel = stringResource(R.string.group_code_label)
    val noTestsCreated = stringResource(R.string.no_tests_created)
    val clickCreateMessage = stringResource(R.string.min_items_error)
    val deleteTestTitle = stringResource(R.string.quit_test_title)
    val deleteTestMessage = stringResource(R.string.quit_test_message)
    val deleteButtonLabel = stringResource(R.string.quit_button)
    val cancelButtonLabel = stringResource(R.string.cancel_button)
    val exitConfirmTitle = stringResource(R.string.quit_confirmation_title)
    val exitConfirmMessage = stringResource(R.string.quit_confirmation_message)
    val exitButtonLabel = stringResource(R.string.quit_button)
    val testSentSuccess = stringResource(R.string.test_sent_title)
    val resultsButtonLabel = stringResource(R.string.results_title)
    val sendButtonLabel = stringResource(R.string.sending_test)

    // State variables
    var availableTests by rememberSaveable { mutableStateOf<List<TestSession>>(emptyList()) }
    var selectedTestId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    var showDeleteConfirmDialog by remember { mutableStateOf<String?>(null) }
    var showExitConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var isLoadingTests by remember { mutableStateOf(false) }

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

    fun loadTests() {
        scope.launch {
            isLoadingTests = true

            try {
                Log.d("SendTestScreen", "==============================================")
                Log.d("SendTestScreen", "📚 Loading tests for session: $sessionId")

                val groupId = UserSession.groupId
                if (groupId == null) {
                    Log.e("SendTestScreen", "Group ID is null")
                    isLoadingTests = false
                    return@launch
                }

                Log.d("SendTestScreen", "Group ID: $groupId")

                val sessionsSnapshot = firestore.collection("test_sessions")
                    .whereEqualTo("groupId", groupId)
                    .get()
                    .await()

                Log.d("SendTestScreen", "Found ${sessionsSnapshot.documents.size} test sessions")

                val tests = mutableListOf<TestSession>()

                sessionsSnapshot.documents.forEach { doc ->
                    val id = doc.id
                    val title = doc.getString("title") ?: "Test sans titre"
                    val questionRefs = doc.get("questionRefs") as? List<*> ?: emptyList<Any>()
                    val timeLimit = (doc.getLong("timeLimit") ?: 15).toInt()
                    val createdAt = doc.getTimestamp("createdAt")

                    Log.d("SendTestScreen", "Loaded test: $id - $title (${questionRefs.size} questions)")

                    tests.add(
                        TestSession(
                            id = id,
                            title = title,
                            questionCount = questionRefs.size,
                            timeLimit = timeLimit,
                            createdAt = createdAt
                        )
                    )
                }

                val sortedTests = tests.sortedBy { it.createdAt }

                val numberedTests = sortedTests.mapIndexed { index, test ->
                    val testNumber = index + 1
                    test.copy(
                        title = "Test personnalisé №$testNumber - ${test.title}",
                        testNumber = testNumber
                    )
                }

                availableTests = numberedTests

                Log.d("SendTestScreen", "✅ Loaded ${numberedTests.size} tests with numbers")
                numberedTests.forEachIndexed { index, test ->
                    Log.d("SendTestScreen", "  Test ${index + 1}: ${test.title}")
                }
                Log.d("SendTestScreen", "==============================================")

                isLoadingTests = false

            } catch (e: Exception) {
                Log.e("SendTestScreen", "❌ Error loading tests", e)
                isLoadingTests = false
            }
        }
    }

    suspend fun deleteGroupSession() {
        try {
            val groupId = UserSession.groupId
            Log.d("SendTestScreen", "==============================================")
            Log.d("SendTestScreen", "🔴 STARTING GROUP DELETION")
            Log.d("SendTestScreen", "Group ID: $groupId")

            if (groupId == null) {
                Log.e("SendTestScreen", "❌ Group ID is null!")
                return
            }

            try {
                firestore.collection("groups")
                    .document(groupId)
                    .update("isActive", false)
                    .await()

                Log.d("SendTestScreen", "✅ Group marked as inactive")
            } catch (e: Exception) {
                Log.e("SendTestScreen", "❌ Error marking group as inactive", e)
            }

            kotlinx.coroutines.delay(500)

            val groupDoc = firestore.collection("groups")
                .document(groupId)
                .get()
                .await()

            if (!groupDoc.exists()) {
                Log.w("SendTestScreen", "⚠️ Group document doesn't exist!")
                return
            }

            val participantIds = groupDoc.get("participantIds") as? List<String> ?: emptyList()

            Log.d("SendTestScreen", "📋 Found ${participantIds.size} participants")

            if (participantIds.isNotEmpty()) {
                Log.d("SendTestScreen", "📤 Sending ${participantIds.size} notifications...")

                participantIds.forEachIndexed { index, participantId ->
                    try {
                        val notificationData = hashMapOf(
                            "type" to "GROUP_CLOSED",
                            "groupId" to groupId,
                            "message" to "Le chef a quitté la session",
                            "timestamp" to com.google.firebase.Timestamp.now(),
                            "recipientId" to participantId,
                            "isRead" to false
                        )

                        firestore.collection("notifications")
                            .add(notificationData)
                            .await()

                        Log.d("SendTestScreen", "✅ Notification ${index + 1}/${participantIds.size} created")

                    } catch (e: Exception) {
                        Log.e("SendTestScreen", "❌ Failed to send notification to $participantId", e)
                    }
                }

                kotlinx.coroutines.delay(2000)
            }

            Log.d("SendTestScreen", "🗑️ Deleting test sessions...")
            val sessions = firestore.collection("test_sessions")
                .whereEqualTo("groupId", groupId)
                .get()
                .await()

            sessions.documents.forEach { session ->
                firestore.collection("test_sessions").document(session.id).delete().await()
            }

            Log.d("SendTestScreen", "🗑️ Deleting group...")
            firestore.collection("groups").document(groupId).delete().await()

            Log.d("SendTestScreen", "✅ GROUP DELETION COMPLETED")
            Log.d("SendTestScreen", "==============================================")

        } catch (e: Exception) {
            Log.e("SendTestScreen", "❌ ERROR in deleteGroupSession", e)
            e.printStackTrace()
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
        // LANDSCAPE MODE
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
                Text("$participantsLabel (${uiState.participants.size})", style = MaterialTheme.typography.titleLarge)
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
                            Text(noParticipantMessage)
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
                    Text("$testsLabel (${availableTests.size})", style = MaterialTheme.typography.titleLarge)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { scope.launch { loadTests() } }) {
                            Icon(Icons.Default.Refresh, refreshLabel)
                        }

                        IconButton(onClick = { showExitConfirmDialog = true }) {
                            Icon(Icons.Default.Home, mainMenuLabel)
                        }

                        Button(onClick = {
                            val testName = "Chef"
                            navController.currentBackStackEntry?.savedStateHandle?.set("testName", testName)
                            navController.navigate("creation_online")
                        }) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(createButtonLabel)
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
                                Text(groupCodeLabel, style = MaterialTheme.typography.labelSmall)
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
                            Icon(Icons.AutoMirrored.Filled.Assignment, null, modifier = Modifier.size(48.dp))
                            Spacer(Modifier.height(8.dp))
                            Text(noTestsCreated, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text(clickCreateMessage, style = MaterialTheme.typography.bodySmall)
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
                                isSelected = selectedTestId == test.id,
                                onSelect = { selectedTestId = test.id },
                                onDelete = { showDeleteConfirmDialog = test.id }
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
                            Text(testSentSuccess, color = MaterialTheme.colorScheme.onPrimaryContainer)
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
                        Text(resultsButtonLabel)
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
                            Icon(Icons.AutoMirrored.Filled.Send, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(sendButtonLabel)
                        }
                    }
                }
            }
        }
    } else {
        // PORTRAIT MODE
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
                Text(stringResource(R.string.role_chef), style = MaterialTheme.typography.headlineMedium)
                Row {
                    IconButton(onClick = { scope.launch { loadTests() } }) {
                        Icon(Icons.Default.Refresh, refreshLabel)
                    }
                    IconButton(onClick = { showExitConfirmDialog = true }) {
                        Icon(Icons.Default.Home, stringResource(R.string.menu_button))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (uiState.groupCode.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(groupCodeLabel, style = MaterialTheme.typography.labelMedium)
                            Text(uiState.groupCode, style = MaterialTheme.typography.headlineMedium)
                        }
                        Icon(Icons.Default.QrCode, null, modifier = Modifier.size(40.dp))
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    val testName = "Chef"
                    navController.currentBackStackEntry?.savedStateHandle?.set("testName", testName)
                    navController.navigate("creation_online")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text(createButtonLabel)
            }

            Spacer(Modifier.height(16.dp))

            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("$testsLabel (${availableTests.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("$participantsLabel (${uiState.participants.size})") }
                )
            }

            Spacer(Modifier.height(16.dp))

            when (selectedTab) {
                0 -> {
                    if (availableTests.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Assignment, null, modifier = Modifier.size(64.dp))
                                Spacer(Modifier.height(16.dp))
                                Text(noTestsCreated)
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
                                        isSelected = selectedTestId == test.id,
                                        onSelect = { selectedTestId = test.id },
                                        onDelete = { showDeleteConfirmDialog = test.id }
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
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.PersonOff, null, modifier = Modifier.size(64.dp))
                                Spacer(Modifier.height(16.dp))
                                Text(noParticipantMessage)
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
                        Text(testSentSuccess)
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
                    Text(resultsButtonLabel)
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
                        Icon(Icons.AutoMirrored.Filled.Send, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(sendButtonLabel)
                    }
                }
            }
        }
    }

    // Dialog for deleting a test
    showDeleteConfirmDialog?.let { testId ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text(deleteTestTitle) },
            text = { Text(deleteTestMessage) },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                FirebaseFirestore.getInstance()
                                    .collection("test_sessions")
                                    .document(testId)
                                    .delete()
                                    .await()

                                loadTests()

                                if (selectedTestId == testId) {
                                    selectedTestId = null
                                }
                                showDeleteConfirmDialog = null
                            } catch (e: Exception) {
                                Log.e("SendTestScreen", "Error deleting test", e)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(deleteButtonLabel)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = null }) { Text(cancelButtonLabel) }
            }
        )
    }

    // Dialog for confirming exit to main menu
    if (showExitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showExitConfirmDialog = false },
            title = {
                Text(
                    exitConfirmTitle,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    exitConfirmMessage,
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
                    Text(exitButtonLabel)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmDialog = false }) {
                    Text(cancelButtonLabel)
                }
            }
        )
    }
}

@Composable
fun TestCard(test: TestSession, isSelected: Boolean, onSelect: () -> Unit, onDelete: () -> Unit) {
    val deleteLabel = stringResource(R.string.quit_button)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    test.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${test.questionCount} questions • ${test.timeLimit}s/question",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    deleteLabel,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ParticipantItem(participant: Participant) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Text(participant.name)
        }
    }
}
