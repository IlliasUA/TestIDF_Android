package legOS.testidf.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import legOS.testidf.R
import legOS.testidf.utils.ChatMessage
import legOS.testidf.utils.GeminiApiClient
import legOS.testidf.utils.GeminiDiagnostics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(navController: NavController) {
    val context = LocalContext.current
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showDiagnostics by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Initialize Gemini API Client
    val geminiClient = remember { GeminiApiClient() }

    // Add initial welcome message
    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            messages = listOf(
                ChatMessage(
                    text = context.getString(R.string.ai_welcome_message),
                    isUser = false
                )
            )
        }
    }

    // Auto-scroll to bottom when new message is added
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scrollState.animateScrollToItem(messages.size - 1)
        }
    }

    fun sendMessage() {
        if (inputText.isBlank()) return

        val userMessage = inputText.trim()
        messages = messages + ChatMessage(text = userMessage, isUser = true)
        inputText = ""
        isLoading = true

        coroutineScope.launch {
            try {
                val aiResponse = geminiClient.sendMessage(userMessage, messages)
                messages = messages + ChatMessage(text = aiResponse, isUser = false)
            } catch (e: Exception) {
                Log.e("AIAssistant", "Error getting response", e)
                val errorMessage = when {
                    e.message?.contains("rate limit", ignoreCase = true) == true ->
                        context.getString(R.string.ai_error_rate_limit)
                    e.message?.contains("API key", ignoreCase = true) == true ->
                        context.getString(R.string.ai_error_api_key)
                    e.message?.contains("network", ignoreCase = true) == true ->
                        context.getString(R.string.ai_error_network)
                    e.message?.contains("not enabled", ignoreCase = true) == true ->
                        "❌ API Key Error\n\n${e.message}\n\nTry running diagnostics with the bug icon above."
                    else ->
                        context.getString(R.string.ai_error_generic)
                }
                messages = messages + ChatMessage(
                    text = errorMessage,
                    isUser = false
                )
            } finally {
                isLoading = false
            }
        }
    }

    fun runDiagnostics() {
        showDiagnostics = true
        isLoading = true

        coroutineScope.launch {
            try {
                val result = GeminiDiagnostics.diagnose()

                val diagnosticMessage = buildString {
                    appendLine("🔍 GEMINI API DIAGNOSTICS")
                    appendLine("=" .repeat(30))
                    appendLine()

                    appendLine("API Key Configuration:")
                    if (result.apiKeyConfigured) {
                        appendLine("✅ API Key is configured")
                        appendLine("   Length: ${result.apiKeyLength} chars")
                        appendLine("   Prefix: ${result.apiKeyPrefix}...")
                    } else {
                        appendLine("❌ API Key NOT configured")
                    }
                    appendLine()

                    appendLine("Available Models:")
                    if (result.modelsFound) {
                        appendLine("✅ Found ${result.availableModels.size} models")
                        result.availableModels.forEach { model ->
                            appendLine("   • ${model.name}")
                        }
                    } else {
                        appendLine("❌ NO models found")
                        appendLine("   This means your API key is NOT")
                        appendLine("   activated for Gemini API")
                    }
                    appendLine()

                    if (result.workingModel != null) {
                        appendLine("Test Result:")
                        appendLine("✅ SUCCESS with: ${result.workingModel}")
                    } else if (result.testSuccessful) {
                        appendLine("Test Result:")
                        appendLine("✅ Test passed!")
                    } else {
                        appendLine("Test Result:")
                        appendLine("❌ Test failed")
                    }

                    if (result.error != null) {
                        appendLine()
                        appendLine("Error:")
                        appendLine(result.error)
                    }

                    appendLine()
                    appendLine("=" .repeat(30))

                    if (!result.modelsFound) {
                        appendLine()
                        appendLine("⚠️ ACTION REQUIRED:")
                        appendLine("1. Go to: aistudio.google.com")
                        appendLine("2. Create NEW API key")
                        appendLine("3. Update local.properties")
                        appendLine("4. Rebuild project")
                    }
                }

                messages = messages + ChatMessage(
                    text = diagnosticMessage,
                    isUser = false
                )
            } catch (e: Exception) {
                messages = messages + ChatMessage(
                    text = "❌ Diagnostics failed: ${e.message}",
                    isUser = false
                )
            } finally {
                isLoading = false
                showDiagnostics = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Top Bar
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.ai_assistant_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    // Diagnostic button
                    IconButton(
                        onClick = { runDiagnostics() },
                        enabled = !isLoading
                    ) {
                        Icon(
                            Icons.Default.BugReport,
                            contentDescription = "Run Diagnostics",
                            tint = if (isLoading) Color.Gray else MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )

            // Chat Messages
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)
                }

                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(0.7f),
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 4.dp,
                                    bottomEnd = 16.dp
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Text(
                                        text = if (showDiagnostics) "Running diagnostics..."
                                        else stringResource(R.string.ai_typing),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Input Field
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(stringResource(R.string.ai_input_placeholder))
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = { sendMessage() }
                        ),
                        enabled = !isLoading,
                        maxLines = 4,
                        shape = RoundedCornerShape(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FilledIconButton(
                        onClick = { sendMessage() },
                        enabled = inputText.isNotBlank() && !isLoading,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(R.string.ai_send_button)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 2.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (message.isUser)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}