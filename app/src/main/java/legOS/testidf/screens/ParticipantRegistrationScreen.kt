package legOS.testidf.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import legOS.testidf.loadImageFromAssets
import legOS.testidf.viewmodel.ParticipantRegistrationViewModel

@Composable
fun ParticipantRegistrationScreen(
    navController: NavController,
    viewModel: ParticipantRegistrationViewModel = viewModel()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var participantName by rememberSaveable { mutableStateOf("") }
    var groupCode by rememberSaveable { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val backgroundImage = loadImageFromAssets(context, "images/background_6.png")

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
                        text = "Rejoindre une compétition",
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
                            Icon(
                                Icons.Default.QrCode,
                                null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Mode Compétition",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Demandez le code\nde groupe à votre chef",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // ПРАВАЯ ЧАСТЬ - Форма
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
                            .fillMaxWidth(0.95f)
                            .wrapContentHeight()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Поле имени
                            OutlinedTextField(
                                value = participantName,
                                onValueChange = { participantName = it },
                                label = { Text("Votre nom") },
                                placeholder = { Text("Ex: Jean Dupont") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words
                                )
                            )

                            // Поле кода группы
                            OutlinedTextField(
                                value = groupCode,
                                onValueChange = {
                                    if (it.length <= 6) {
                                        groupCode = it.uppercase()
                                    }
                                },
                                label = { Text("Code du groupe") },
                                placeholder = { Text("ABC123") },
                                leadingIcon = {
                                    Icon(Icons.Default.QrCode, null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Characters
                                )
                            )

                            // Сообщение об ошибке
                            if (uiState.error != null) {
                                Text(
                                    text = uiState.error ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            // Кнопка Rejoindre
                            Button(
                                onClick = {
                                    viewModel.joinGroup(
                                        participantName = participantName.trim(),
                                        groupCode = groupCode.trim()
                                    ) { success ->
                                        if (success) {
                                            navController.navigate("participant_waiting") {
                                                popUpTo("competition") { inclusive = true }
                                            }
                                        }
                                    }
                                },
                                enabled = !uiState.isLoading &&
                                        participantName.trim().isNotBlank() &&
                                        groupCode.trim().length == 6,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Rejoindre", style = MaterialTheme.typography.bodyLarge)
                                }
                            }

                            // Кнопка возврата
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
                    text = "Rejoindre une compétition",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Entrez votre nom et le code du groupe",
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
                            value = participantName,
                            onValueChange = { participantName = it },
                            label = { Text("Votre nom") },
                            placeholder = { Text("Ex: Jean Dupont") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, null)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words
                            )
                        )

                        OutlinedTextField(
                            value = groupCode,
                            onValueChange = {
                                if (it.length <= 6) {
                                    groupCode = it.uppercase()
                                }
                            },
                            label = { Text("Code du groupe") },
                            placeholder = { Text("ABC123") },
                            leadingIcon = {
                                Icon(Icons.Default.QrCode, null)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Characters
                            ),
                            supportingText = {
                                Text("Code fourni par le chef de groupe")
                            }
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
                                viewModel.joinGroup(
                                    participantName = participantName.trim(),
                                    groupCode = groupCode.trim()
                                ) { success ->
                                    if (success) {
                                        navController.navigate("participant_waiting") {
                                            popUpTo("competition") { inclusive = true }
                                        }
                                    }
                                }
                            },
                            enabled = !uiState.isLoading &&
                                    participantName.trim().isNotBlank() &&
                                    groupCode.trim().length == 6,
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
                                Text("Rejoindre", style = MaterialTheme.typography.bodyLarge)
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
}