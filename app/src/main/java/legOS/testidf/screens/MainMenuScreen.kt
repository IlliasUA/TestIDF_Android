package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.IOException

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainMenuScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val showQuitConfirmation = remember { mutableStateOf(false) }

    // Load background image with error handling
    val backgroundImage = remember {
        try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("MainMenuScreen", "Error loading background.png", e)
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
    ) {
        // Display background image
        backgroundImage?.let { image ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                MainMenuCompactLayout(navController, showQuitConfirmation, isLandscape)
            }
            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                MainMenuLargeLayout(navController, showQuitConfirmation, isLandscape)
            }
        }
    }

    // Quit confirmation dialog
    if (showQuitConfirmation.value) {
        AlertDialog(
            onDismissRequest = { showQuitConfirmation.value = false },
            title = { Text("Confirmation", style = MaterialTheme.typography.headlineSmall) },
            text = {
                Text(
                    "Voulez-vous vraiment quitter l'application ?",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showQuitConfirmation.value = false
                        (navController.context as? ComponentActivity)?.finish()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Oui", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showQuitConfirmation.value = false },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Non", style = MaterialTheme.typography.labelLarge)
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun MainMenuCompactLayout(
    navController: NavController,
    showQuitConfirmation: MutableState<Boolean>,
    isLandscape: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1.5f)) // Increased weight to push buttons lower

        Text(
            text = "", // Kept empty as in original code
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MenuButton(
                    text = "TEST D'IDENTIFICATION",
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.weight(1f)
                )
                MenuButton(
                    text = "Info",
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.weight(1f)
                )
                MenuButton(
                    text = "Quitter",
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuButton(
                    text = "TEST D'IDENTIFICATION",
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                MenuButton(
                    text = "Info",
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                MenuButton(
                    text = "Quitter",
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }

        Spacer(Modifier.weight(0.5f)) // Reduced weight to balance layout

        Text(
            text = "Beta 1.0",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "GROMOV I.",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun MainMenuLargeLayout(
    navController: NavController,
    showQuitConfirmation: MutableState<Boolean>,
    isLandscape: Boolean
) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(1.5f)) // Increased weight to push buttons lower

            Text(
                text = "Quiz App",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuButton(
                    text = "TEST D'IDENTIFICATION",
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                MenuButton(
                    text = "Info",
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                MenuButton(
                    text = "Quitter",
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            }

            Spacer(Modifier.weight(0.5f)) // Reduced weight to balance layout

            Text(
                text = "Beta 1.0",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "GROMOV I.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun MenuButton(text: String, onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp
        )
    }
}