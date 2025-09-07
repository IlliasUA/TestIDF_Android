package legOS.testidf.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import legOS.testidf.loadImageFromAssets

@Composable
fun TimeSelectionScreen(navController: NavController, category: String) {
    val context = LocalContext.current
    val playerName = navController.previousBackStackEntry?.savedStateHandle?.get<String>("playerName")
        ?: navController.previousBackStackEntry?.arguments?.getString("playerName") ?: "Anonyme"
    Log.d("TimeSelectionScreen", "Received player name: $playerName")

    // Load the background image
    val backgroundImage = loadImageFromAssets(context, "images/background_2.jpg")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                backgroundImage?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it.asImageBitmap()),
                        contentScale = ContentScale.Crop
                    )
                } ?: Modifier.background(MaterialTheme.colorScheme.background) // Fallback to default background if image fails to load
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Sélectionnez le temps par question",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOf(10, 15, 20).forEach { time ->
                Button(
                    onClick = {
                        Log.d("TimeSelectionScreen", "Navigating to test with time: $time, playerName: $playerName")
                        navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                        navController.navigate("test/$category/$time?playerName=$playerName")
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("$time secondes", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(120.dp))
        Button(
            onClick = { navController.navigate("test_menu") },
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text("Retour", style = MaterialTheme.typography.bodyMedium)
        }
    }
}