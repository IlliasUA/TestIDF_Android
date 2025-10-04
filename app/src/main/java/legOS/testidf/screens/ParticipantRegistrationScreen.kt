package legOS.testidf.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
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

    var participantName by remember { mutableStateOf("") }
    var groupCode by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val backgroundImage = loadImageFromAssets(context, "images/background_2.jpg")

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = if (isLandscape) 32.dp else 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
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

            // Карточка с формой
            Card(
                modifier = Modifier.fillMaxWidth(if (isLandscape) 0.6f else 0.95f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Поле имени
                    OutlinedTextField(
                        value = participantName,
                        onValueChange = { participantName = it },
                        label = { Text("Votre nom") },
                        placeholder = { Text("Ex: Jean Dupont") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
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
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null
                            )
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

                    // Сообщение об ошибке
                    if (uiState.error != null) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Кнопка Rejoindre
                    Button(
                        onClick = {
                            viewModel.joinGroup(
                                participantName = participantName.trim(),
                                groupCode = groupCode.trim()
                            ) { success ->
                                if (success) {
                                    // Переход к экрану ожидания тестов
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
                            Text(
                                "Rejoindre",
                                style = MaterialTheme.typography.bodyLarge
                            )
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
}