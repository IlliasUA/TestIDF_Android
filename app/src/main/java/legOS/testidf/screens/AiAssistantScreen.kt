package legOS.testidf.screens

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import legOS.testidf.R
import legOS.testidf.utils.ChatMessage
import legOS.testidf.utils.GeminiApiClient
import java.io.IOException
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(navController: NavController) {
    val context = LocalContext.current
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Initialize Gemini API Client
    val geminiClient = remember { GeminiApiClient() }

    // ✅ NOUVEAU: Fonction pour obtenir un message d'accueil aléatoire
    fun getRandomWelcomeMessage(): String {
        val welcomeMessages = listOf(
            context.getString(R.string.ai_welcome_message_1),
            context.getString(R.string.ai_welcome_message_2),
            context.getString(R.string.ai_welcome_message_3),
            context.getString(R.string.ai_welcome_message_4),
            context.getString(R.string.ai_welcome_message_5),
            context.getString(R.string.ai_welcome_message_6)
        )
        return welcomeMessages[Random.nextInt(welcomeMessages.size)]
    }

    // ✅ NOUVEAU: Загружаем изображение AI-ассистента
    val iconAi = remember {
        try {
            context.assets.open("images/icon_ai.webp").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("AIAssistant", "Error loading icon_ai.webp", e)
            null
        }
    }

    // ✅ NOUVEAU: Загружаем фоновое изображение
    val backgroundImage = remember {
        try {
            context.assets.open("images/background_2.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("AIAssistant", "Error loading background_2.jpg", e)
            null
        }
    }

    // ✅ MODIFIÉ: Message d'accueil aléatoire au lieu d'un message fixe
    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            messages = listOf(
                ChatMessage(
                    text = getRandomWelcomeMessage(),
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ✅ NOUVEAU: Фоновое изображение
        backgroundImage?.let { image ->
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
            // ✅ ОБНОВЛЕНО: Top Bar полностью прозрачный, без диагностики
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.ai_assistant_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
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
                    MessageBubble(
                        message = message,
                        aiIcon = iconAi
                    )
                }

                // ✅ ОБНОВЛЕНО: Loading indicator с изображением AI слева, выровнено по Bottom
                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Bottom  // ✅ Bottom для выравнивания
                        ) {
                            // Изображение AI слева, выровнено по нижней границе
                            iconAi?.let { icon ->
                                Image(
                                    bitmap = icon,
                                    contentDescription = "AI Assistant",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(end = 8.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }

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
                                        text = stringResource(R.string.ai_typing),
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
                color = Color.Transparent,  // ✅ Полностью прозрачный фон Surface
                shadowElevation = 0.dp
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
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.80f),  // ✅ 80% непрозрачность внутри обводки
                                shape = RoundedCornerShape(24.dp)
                            ),
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
private fun MessageBubble(
    message: ChatMessage,
    aiIcon: androidx.compose.ui.graphics.ImageBitmap?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = if (message.isUser) Alignment.Top else Alignment.Bottom  // ✅ Bottom для AI
    ) {
        // ✅ ОБНОВЛЕНО: Изображение AI слева, выровнено по нижней границе
        if (!message.isUser) {
            aiIcon?.let { icon ->
                Image(
                    bitmap = icon,
                    contentDescription = "AI Assistant",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

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