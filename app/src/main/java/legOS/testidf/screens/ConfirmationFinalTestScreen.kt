package legOS.testidf.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.graphics.asImageBitmap
import legOS.testidf.loadImageFromAssets


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ConfirmationFinalTestScreen(navController: NavController, playerName: String) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = LocalContext.current as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Load the background image (with error handling)
    val backgroundImage = runCatching { loadImageFromAssets(context, "images/background_2.jpg") }
        .getOrNull()

    // Choose layout based on screen size and orientation
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fallback background
            .then(
                backgroundImage?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it.asImageBitmap()),
                        contentScale = ContentScale.Crop
                    )
                } ?: Modifier
            )
            .safeDrawingPadding() // Respect system bars (notch, status/nav bars)
    ) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                // Small screens (phones)
                ConfirmationCompactLayout(navController, playerName, isLandscape)
            }
            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                // Tablets or large screens
                ConfirmationLargeLayout(navController, playerName, isLandscape)
            }
        }
    }
}

@Composable
private fun ConfirmationCompactLayout(navController: NavController, playerName: String, isLandscape: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Confirmez le test FINAL pour $playerName",
            style = MaterialTheme.typography.headlineSmall, // Smaller for phones
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(Modifier.height(16.dp))

        if (isLandscape) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConfirmButton(navController, playerName, Modifier.weight(1f))
                CancelButton(navController, Modifier.weight(1f))
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ConfirmButton(navController, playerName, Modifier.fillMaxWidth(0.8f))
                CancelButton(navController, Modifier.fillMaxWidth(0.8f))
            }
        }
    }
}

@Composable
private fun ConfirmationLargeLayout(navController: NavController, playerName: String, isLandscape: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = if (isLandscape) 32.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Confirmez le test FINAL pour $playerName",
                style = MaterialTheme.typography.headlineMedium, // Larger for tablets
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Spacer(Modifier.height(24.dp))

            ConfirmButton(navController, playerName, Modifier.fillMaxWidth(0.6f))
            Spacer(Modifier.height(16.dp))
            CancelButton(navController, Modifier.fillMaxWidth(0.6f))
        }
    }
}

@Composable
private fun ConfirmButton(navController: NavController, playerName: String, modifier: Modifier) {
    Button(
        onClick = {
            Log.d("ConfirmationFinalTestScreen", "Confirming test for $playerName")
            navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
            navController.navigate("time_selection/final?playerName=$playerName")
        },
        modifier = modifier
            .height(48.dp) // Standard height for buttons
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text("Confirmer", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun CancelButton(navController: NavController, modifier: Modifier) {
    OutlinedButton(
        onClick = { navController.navigate("test_menu") },
        modifier = modifier
            .height(48.dp)
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
    ) {
        Text("Annuler", style = MaterialTheme.typography.bodyLarge)
    }
}