package legOS.testidf.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.content.res.Configuration
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import legOS.testidf.R
import legOS.testidf.loadImageFromAssets

@Composable
fun PlayerNameScreen(navController: NavController) {
    var playerName by rememberSaveable { mutableStateOf("") }
    val context = LocalResources.current
    val androidContext = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Load the background image
    val backgroundBitmap = loadImageFromAssets(androidContext, "images/background_2.jpg")

    if (isLandscape) {
        PlayerNameLandscapeLayout(
            playerName = playerName,
            onPlayerNameChange = { playerName = it },
            navController = navController,
            backgroundBitmap = backgroundBitmap
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    backgroundBitmap?.let {
                        Modifier.paint(
                            painter = BitmapPainter(it.asImageBitmap()),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Modifier.background(MaterialTheme.colorScheme.background)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                context.getString(R.string.enter_name_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it },
                label = { Text(context.getString(R.string.name_label)) },
                modifier = Modifier
                    .width(300.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (playerName.isNotBlank()) {
                            Log.d("PlayerNameScreen", "Saving player name: $playerName")
                            navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                            navController.navigate("confirmation_final_test?playerName=$playerName")
                        }
                    },
                    enabled = playerName.isNotBlank(),
                    modifier = Modifier
                        .width(200.dp)
                        .height(60.dp)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        context.getString(R.string.continue_button),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                    Text(
                        context.getString(R.string.back_button),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerNameLandscapeLayout(
    playerName: String,
    onPlayerNameChange: (String) -> Unit,
    navController: NavController,
    backgroundBitmap: android.graphics.Bitmap?
) {
    val context = LocalResources.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .then(
                backgroundBitmap?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it.asImageBitmap()),
                        contentScale = ContentScale.Crop
                    )
                } ?: Modifier.background(MaterialTheme.colorScheme.background)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .safeDrawingPadding(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Left side - Title or empty space
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                context.getString(R.string.enter_name_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
        }

        // Right side - Text field and buttons
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = playerName,
                onValueChange = { onPlayerNameChange(it) },
                label = { Text(context.getString(R.string.name_label)) },
                modifier = Modifier
                    .width(300.dp)
                    .padding(top = 8.dp, bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (playerName.isNotBlank()) {
                        Log.d("PlayerNameScreen", "Saving player name: $playerName")
                        navController.currentBackStackEntry?.savedStateHandle?.set("playerName", playerName)
                        navController.navigate("confirmation_final_test?playerName=$playerName")
                    }
                },
                enabled = playerName.isNotBlank(),
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    context.getString(R.string.continue_button),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                Text(
                    context.getString(R.string.back_button),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
