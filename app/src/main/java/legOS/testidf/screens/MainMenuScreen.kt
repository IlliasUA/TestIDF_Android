package legOS.testidf.screens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import legOS.testidf.LocaleManager
import legOS.testidf.R
import legOS.testidf.components.LanguageButton
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainMenuScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = activity!!)
    val density = LocalDensity.current
    val showQuitConfirmation = remember { mutableStateOf(false) }

    // Получаем текующий язык
    val currentLanguage = remember { mutableStateOf(LocaleManager.getCurrentLanguage(context)) }

    val onLanguageChange: (LocaleManager.Language) -> Unit = { newLanguage ->
        Log.d("MainMenuScreen", "=== Language change requested: ${newLanguage.code} ===")

        // Сохраняем язык (это также обновит Application Context если доступен)
        LocaleManager.setLanguage(context, newLanguage)

        Log.d("MainMenuScreen", "Language saved, recreating Activity...")

        // Пересоздаем Activity - LocaleManager уже обновил все что нужно
        activity?.recreate()
    }

    // Tailles adaptatives basées sur la densité de l'écran et la taille de la fenêtre
    val screenHeightDp = with(density) { configuration.screenHeightDp.dp }
    val screenWidthDp = with(density) { configuration.screenWidthDp.dp }
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isCompactHeight = screenHeightDp < 600.dp || windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    // Charger l'image de fond avec gestion d'erreur
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
    ) {
        // Afficher l'image de fond
        backgroundImage?.let { image ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Contenu avec marges sécurisées
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Mise en page adaptative
            when {
                windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact -> {
                    MainMenuCompactLayout(
                        navController = navController,
                        showQuitConfirmation = showQuitConfirmation,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidthDp,
                        screenHeight = screenHeightDp
                    )
                }
                else -> {
                    MainMenuLargeLayout(
                        navController = navController,
                        showQuitConfirmation = showQuitConfirmation,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidthDp,
                        screenHeight = screenHeightDp
                    )
                }
            }

            // Bouton de changement de langue (coin supérieur droit)
            LanguageButton(
                currentLanguage = currentLanguage.value,
                onLanguageChange = onLanguageChange,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
        }
    }

    // Dialogue de confirmation de sortie
    if (showQuitConfirmation.value) {
        AlertDialog(
            onDismissRequest = { showQuitConfirmation.value = false },
            title = {
                Text(
                    stringResource(R.string.quit_confirmation_title),
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    stringResource(R.string.quit_confirmation_message),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    (context as? ComponentActivity)?.finish()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitConfirmation.value = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}

@Composable
private fun MainMenuCompactLayout(
    navController: NavController,
    showQuitConfirmation: MutableState<Boolean>,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val scrollState = rememberScrollState()

    // Marges et tailles adaptatives
    val horizontalPadding = min(16.dp, screenWidth * 0.04f)
    val verticalPadding = if (isCompactHeight) 8.dp else min(24.dp, screenHeight * 0.03f)
    val buttonSpacing = if (isCompactHeight) 8.dp else 12.dp
    val buttonWidth = if (isLandscape) 0.9f else 0.85f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalArrangement = if (isCompactHeight) Arrangement.SpaceBetween else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espaceur supérieur
        if (!isCompactHeight) {
            Spacer(Modifier.height(screenHeight * 0.35f))
        } else {
            Spacer(Modifier.height(40.dp))
        }

        // Titre (masqué sur les très petits écrans)
        if (!isCompactHeight || screenHeight > 400.dp) {
            Text(
                text = "",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = when {
                        isCompactHeight -> 16.sp
                        screenHeight < 600.dp -> 18.sp
                        else -> 20.sp
                    }
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = if (isCompactHeight) 8.dp else 16.dp)
            )
        }

        // Boutons du menu
        if (isLandscape && screenWidth > 600.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = min(16.dp, screenWidth * 0.02f),
                    alignment = Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MenuButton(
                    text = stringResource(R.string.menu_button),
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.weight(1f),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = stringResource(R.string.info_button),
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.weight(1f),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = stringResource(R.string.quit_button),
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.weight(1f),
                    isCompact = isCompactHeight
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(buttonSpacing),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuButton(
                    text = stringResource(R.string.menu_button),
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = stringResource(R.string.info_button),
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = stringResource(R.string.quit_button),
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
            }
        }

        // Espaceur inférieur
        if (isLandscape) {
            Spacer(Modifier.weight(1f))
        } else {
            Spacer(Modifier.weight(1f).height(screenHeight * 0.05f))
        }

        // Texte juridique
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(bottom = if (isLandscape) 2.dp else 4.dp)
        ) {
            Text(
                text = stringResource(R.string.copyright),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = when {
                        isCompactHeight -> 8.sp
                        screenHeight < 600.dp -> 10.sp
                        else -> 12.sp
                    }
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.privacy_notice),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = when {
                        isCompactHeight -> 5.sp
                        screenHeight < 600.dp -> 6.sp
                        else -> 7.sp
                    }
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun MainMenuLargeLayout(
    navController: NavController,
    showQuitConfirmation: MutableState<Boolean>,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val scrollState = rememberScrollState()

    // Marges adaptatives
    val horizontalPadding = min(32.dp, screenWidth * 0.05f)
    val verticalPadding = min(32.dp, screenHeight * 0.04f)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = if (isLandscape) min(32.dp, screenWidth * 0.04f) else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Espaceur supérieur
            if (!isCompactHeight) {
                Spacer(Modifier.height(screenHeight * 0.35f))
            } else {
                Spacer(Modifier.height(48.dp))
            }

            // Boutons du menu
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    if (isCompactHeight) 12.dp else 16.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val buttonWidth = when {
                    screenWidth > 1000.dp -> 0.4f
                    screenWidth > 700.dp -> 0.5f
                    else -> 0.6f
                }

                MenuButton(
                    text = stringResource(R.string.menu_button),
                    onClick = { navController.navigate("test_menu") },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = stringResource(R.string.info_button),
                    onClick = { navController.navigate("info_screen") },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
                MenuButton(
                    text = stringResource(R.string.quit_button),
                    onClick = { showQuitConfirmation.value = true },
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    isCompact = isCompactHeight
                )
            }

            // Espaceur inférieur
            if (isLandscape) {
                Spacer(Modifier.weight(1f))
            } else {
                Spacer(Modifier.weight(1f).height(screenHeight * 0.05f))
            }

            // Texte juridique
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = if (isLandscape) 2.dp else 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.copyright),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = when {
                            isCompactHeight -> 10.sp
                            screenHeight < 700.dp -> 12.sp
                            else -> 14.sp
                        }
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.privacy_notice),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = when {
                            isCompactHeight -> 6.sp
                            screenHeight < 700.dp -> 7.sp
                            else -> 8.sp
                        }
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun MenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    isCompact: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(
                if (isCompact) 48.dp else 56.dp
            )
            .padding(vertical = if (isCompact) 2.dp else 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF607D8B),
            contentColor = Color.White,
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(
            horizontal = if (isCompact) 16.dp else 20.dp,
            vertical = if (isCompact) 8.dp else 12.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = when {
                    isCompact -> 17.sp
                    text.length > 20 -> 17.sp
                    else -> 18.sp
                }
            ),
            textAlign = TextAlign.Center,
            maxLines = if (text.length > 20) 2 else 1
        )
    }
}