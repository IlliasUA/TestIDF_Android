package legOS.testidf.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import legOS.testidf.loadImageFromAssets
import legOS.testidf.viewmodel.AdminRegistrationViewModel
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun AdminRegistrationScreen(
    navController: NavController,
    viewModel: AdminRegistrationViewModel = viewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val clipboardManager = LocalClipboardManager.current

    // ИСПРАВЛЕНО: rememberSaveable вместо remember
    var name by rememberSaveable { mutableStateOf("") }
    var groupName by rememberSaveable { mutableStateOf("") }

    var showGroupCodeDialog by rememberSaveable { mutableStateOf(false) }
    var groupCode by rememberSaveable { mutableStateOf("") }
    var sessionId by rememberSaveable { mutableStateOf("") } // ДОБАВЛЕНО
    var showCopiedMessage by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val backgroundImage = loadImageFromAssets(context, "images/background_6.png")

    val isFormValid = name.trim().isNotBlank() && groupName.trim().isNotBlank()

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
                // ЛЕВАЯ ЧАСТЬ - Информация
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Créer un groupe",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Mode Compétition",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Organisez des tests\npour votre équipe",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // ПРАВАЯ ЧАСТЬ - Форма (ИСПРАВЛЕНО)
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)  // ИСПРАВЛЕНО: не 100% ширины
                            .wrapContentHeight()   // ДОБАВЛЕНО: высота по содержимому
                            .padding(vertical = 8.dp),  // ДОБАВЛЕНО: отступы сверху/снизу
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),  // ИСПРАВЛЕНО: уменьшен padding с 20dp
                            verticalArrangement = Arrangement.spacedBy(12.dp)  // ИСПРАВЛЕНО: уменьшен spacing с 16dp
                        ) {
                            // Имя Chef
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Votre nom") },
                                placeholder = { Text("Ex: Jean Dupont") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words
                                ),
                                isError = name.isNotBlank() && name.trim().length < 2,
                                supportingText = if (name.isNotBlank() && name.trim().length < 2) {
                                    { Text("Minimum 2 caractères") }
                                } else null
                            )

                            // Nom du groupe
                            OutlinedTextField(
                                value = groupName,
                                onValueChange = { groupName = it },
                                label = { Text("Nom du groupe") },
                                placeholder = { Text("Ex: Escadron Alpha") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words
                                )
                            )

                            // Message d'erreur
                            if (uiState.error != null) {
                                Text(
                                    text = uiState.error ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            // Bouton Créer
                            Button(
                                onClick = {
                                    viewModel.registerAdmin(
                                        name = name.trim(),
                                        groupName = groupName.trim()
                                    ) { success, _, _, code ->
                                        if (success && code != null) {
                                            groupCode = code
                                            showGroupCodeDialog = true
                                        }
                                    }
                                },
                                enabled = !uiState.isLoading && isFormValid,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),  // ИСПРАВЛЕНО: уменьшена высота с 50dp
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),  // ИСПРАВЛЕНО: уменьшен размер с 24dp
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Créer le groupe", style = MaterialTheme.typography.bodyLarge)
                                }
                            }

                            // Bouton retour
                            TextButton(
                                onClick = { navController.navigateUp() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Retour")
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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Créer un groupe",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Organisez des tests pour votre équipe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Votre nom") },
                            placeholder = { Text("Ex: Jean Dupont") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words
                            ),
                            isError = name.isNotBlank() && name.trim().length < 2,
                            supportingText = if (name.isNotBlank() && name.trim().length < 2) {
                                { Text("Minimum 2 caractères") }
                            } else null
                        )

                        OutlinedTextField(
                            value = groupName,
                            onValueChange = { groupName = it },
                            label = { Text("Nom du groupe") },
                            placeholder = { Text("Ex: Escadron Alpha") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words
                            )
                        )

                        if (uiState.error != null) {
                            Text(
                                text = uiState.error ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.registerAdmin(
                                    name = name.trim(),
                                    groupName = groupName.trim()
                                ) { success, _, _, code ->
                                    if (success && code != null) {
                                        groupCode = code
                                        showGroupCodeDialog = true
                                    }
                                }
                            },
                            enabled = !uiState.isLoading && isFormValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Créer le groupe", style = MaterialTheme.typography.bodyLarge)
                            }
                        }

                        TextButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Retour")
                        }
                    }
                }
            }
        }
    }

    // Диалог с кодом группы (без изменений)
    if (showGroupCodeDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    "Groupe créé!",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Partagez ce code avec vos subordonnés :",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                groupCode,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    letterSpacing = 8.sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(groupCode))
                                    showCopiedMessage = true
                                }
                            ) {
                                Icon(Icons.Default.ContentCopy, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Copier le code")
                            }

                            if (showCopiedMessage) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "✓ Code copié!",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚠️", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(end = 8.dp))
                            Text(
                                "Notez ce code pour que vos subordonnés puissent rejoindre!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showGroupCodeDialog = false
                        // ИЗМЕНЕНО: переход к send_test с sessionId
                        navController.navigate("send_test/$sessionId") {
                            popUpTo("competition") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continuer")
                }
            }
        )
    }
}