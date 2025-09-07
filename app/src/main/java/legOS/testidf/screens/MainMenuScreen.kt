package legOS.testidf.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainMenuScreen(navController: NavController) {
    val showQuitConfirmation = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(400.dp))

        Button(
            onClick = { navController.navigate("test_menu") },
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
                .padding(10.dp)
                .background(Color.Transparent)
        ) {
            Text("TEST D'IDENTIFICATION")
        }

        Button(
            onClick = { navController.navigate("info_screen") },
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
                .padding(10.dp)
                .background(Color.Transparent)
        ) {
            Text("Info")
        }

        Button(
            onClick = { showQuitConfirmation.value = true },
            modifier = Modifier
                .width(300.dp)
                .height(80.dp)
                .padding(10.dp)
                .background(Color.Transparent)
        ) {
            Text("Quitter")
        }

        // Добавляем Spacer для создания пространства перед надписями
        Spacer(modifier = Modifier.weight(1f)) // Занимает оставшееся пространство, чтобы подвинуть надписи вниз

        // Надпись "Beta 1.0"
        Text(
            text = "Beta 1.0",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 4.dp) // Небольшой отступ снизу для разделения надписей
        )

        // Надпись "GROMOV I."
        Text(
            text = "GROMOV I.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp) // Отступ снизу для эстетики
        )

        // Диалог подтверждения выхода
        if (showQuitConfirmation.value) {
            AlertDialog(
                onDismissRequest = { showQuitConfirmation.value = false },
                title = { Text("Confirmation") },
                text = { Text("Voulez-vous vraiment quitter l'application ?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showQuitConfirmation.value = false
                            // Завершение активности
                            (navController.context as? ComponentActivity)?.finish()
                        }
                    ) {
                        Text("Oui")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showQuitConfirmation.value = false }
                    ) {
                        Text("Non")
                    }
                }
            )
        }
    }
}