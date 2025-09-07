package legOS.testidf.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TestMenuScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choisisez une catégorie", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(40.dp))

        // Левая колонка
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(
                "Chars de combat" to "tanks",
                "Artillerie" to "artillery",
                "Reconnaissance" to "recon",
                "Génie" to "genie",
                "Avion/Hélicoptère" to "air"
            ).forEach { (text, category) ->
                Button(
                    onClick = { navController.navigate("time_selection/$category") },
                    modifier = Modifier
                        .width(300.dp)
                        .height(80.dp)
                        .padding(10.dp)
                        .background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Правая колонка
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(
                "TEST BM2" to "bm2",
                "TEST FINAL" to "final"
            ).forEach { (text, category) ->
                Button(
                    onClick = {
                        if (category == "final") {
                            navController.navigate("player_name")
                        } else {
                            navController.navigate("time_selection/$category")
                        }
                    },
                    modifier = Modifier
                        .width(300.dp)
                        .height(80.dp)
                        .padding(10.dp)
                        .background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(text, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Отступ перед нижними кнопками
        Spacer(modifier = Modifier.height(20.dp))

        // Строка с кнопками "Retour" и "Salle d'honneur"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Кнопка "Retour" слева, темно-красного цвета
            Button(
                onClick = { navController.navigate("main_menu") },
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp)
                    .padding(10.dp)
                    .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B0000).copy(alpha = 0.8f),
                    contentColor = Color.White
                )
            ) {
                Text("Retour", style = MaterialTheme.typography.bodyMedium)
            }

            // Кнопка "Salle d'honneur" справа, желтого цвета
            Button(
                onClick = { navController.navigate("hall_of_fame") },
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp)
                    .padding(10.dp)
                    .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFF00).copy(alpha = 0.8f),
                    contentColor = Color.Black
                )
            ) {
                Text("Salle d'honneur", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}