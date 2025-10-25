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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.IOException

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun TestMenuScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(activity = context as ComponentActivity)
    val density = LocalDensity.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val screenHeightDp = with(density) { configuration.screenHeightDp.dp }
    val screenWidthDp = with(density) { configuration.screenWidthDp.dp }
    val isCompactHeight = screenHeightDp < 600.dp || windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    var selectedTab by remember { mutableStateOf(2) }

    val backgroundImage: ImageBitmap? = remember {
        try {
            context.assets.open("images/background.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("TestMenuScreen", "Error loading background.png", e)
            null
        }
    }

    val iconRetour = remember {
        try {
            context.assets.open("images/icon_retourn.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("TestMenuScreen", "Error loading icon_retourn.png", e)
            null
        }
    }

    val iconCategories = remember {
        try {
            context.assets.open("images/icon_categorie.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("TestMenuScreen", "Error loading icon_categorie.png", e)
            null
        }
    }

    val iconModes = remember {
        try {
            context.assets.open("images/icon_modes.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("TestMenuScreen", "Error loading icon_modes.png", e)
            null
        }
    }

    val iconRechercher = remember {
        try {
            context.assets.open("images/icon_recherche.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("TestMenuScreen", "Error loading icon_recherche.png", e)
            null
        }
    }

    val iconNews = remember {
        try {
            context.assets.open("images/icon_upgrade.png").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("TestMenuScreen", "Error loading icon_upgrade.png", e)
            null
        }
    }

    LaunchedEffect(Unit) {
        Log.d("TestMenuScreen", "Screen launched/relaunched - resetting state")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        backgroundImage?.let { image: ImageBitmap ->
            Image(
                bitmap = image,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    TabContent(
                        selectedTab = selectedTab,
                        navController = navController,
                        windowSizeClass = windowSizeClass,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidthDp,
                        screenHeight = screenHeightDp
                    )

                    // Кнопка помощи в правом верхнем углу
                    FloatingActionButton(
                        onClick = { navController.navigate("help_screen") },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(56.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            Icons.Default.Help,
                            contentDescription = "Aide",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                SideTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        if (tab == 0) {
                            navController.navigate("main_menu")
                        } else {
                            selectedTab = tab
                        }
                    },
                    iconRetour = iconRetour,
                    iconCategories = iconCategories,
                    iconModes = iconModes,
                    iconRechercher = iconRechercher,
                    iconNews = iconNews,
                    navController = navController
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    TabContent(
                        selectedTab = selectedTab,
                        navController = navController,
                        windowSizeClass = windowSizeClass,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidthDp,
                        screenHeight = screenHeightDp
                    )

                    // Кнопка помощи в правом верхнем углу
                    FloatingActionButton(
                        onClick = { navController.navigate("help_screen") },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(56.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            Icons.Default.Help,
                            contentDescription = "Aide",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                BottomTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        if (tab == 0) {
                            navController.navigate("main_menu")
                        } else {
                            selectedTab = tab
                        }
                    },
                    iconRetour = iconRetour,
                    iconCategories = iconCategories,
                    iconModes = iconModes,
                    iconRechercher = iconRechercher,
                    iconNews = iconNews,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun TabContent(
    selectedTab: Int,
    navController: NavController,
    windowSizeClass: androidx.compose.material3.windowsizeclass.WindowSizeClass,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    when (selectedTab) {
        1 -> { // Catégories
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    CategoriesCompactLayout(
                        navController = navController,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight
                    )
                }
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                    CategoriesLargeLayout(
                        navController = navController,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight
                    )
                }
            }
        }
        2 -> { // Modes
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    ModesCompactLayout(
                        navController = navController,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight
                    )
                }
                WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
                    ModesLargeLayout(
                        navController = navController,
                        isLandscape = isLandscape,
                        isCompactHeight = isCompactHeight,
                        screenWidth = screenWidth,
                        screenHeight = screenHeight
                    )
                }
            }
        }
        3 -> { // Rechercher
            LaunchedEffect(Unit) {
                navController.navigate("catalog")
            }
        }
    }
}

@Composable
private fun BottomTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    iconRetour: ImageBitmap?,
    iconCategories: ImageBitmap?,
    iconModes: ImageBitmap?,
    iconRechercher: ImageBitmap?,
    iconNews: ImageBitmap?,
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabBarItem(
                icon = iconRetour,
                label = "Retour",
                isSelected = false,
                onClick = { onTabSelected(0) },
                iconSize = 32.dp
            )

            TabBarItem(
                icon = iconCategories,
                label = "Catégories",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                iconSize = 32.dp
            )

            TabBarItem(
                icon = iconModes,
                label = "Modes",
                isSelected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                iconSize = 32.dp
            )

            TabBarItem(
                icon = iconRechercher,
                label = "Rechercher",
                isSelected = selectedTab == 3,
                onClick = { onTabSelected(3) },
                iconSize = 32.dp
            )

            TabBarItem(
                icon = iconNews,
                label = "News",
                isSelected = false,
                onClick = { navController.navigate("news_screen") },
                iconSize = 32.dp
            )
        }
    }
}

@Composable
private fun SideTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    iconRetour: ImageBitmap?,
    iconCategories: ImageBitmap?,
    iconModes: ImageBitmap?,
    iconRechercher: ImageBitmap?,
    iconNews: ImageBitmap?,
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .width(75.dp)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 2.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SideTabBarItem(
                icon = iconRetour,
                label = "Retour",
                isSelected = false,
                onClick = { onTabSelected(0) },
                iconSize = 32.dp
            )

            SideTabBarItem(
                icon = iconCategories,
                label = "Catégories",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                iconSize = 32.dp
            )

            SideTabBarItem(
                icon = iconModes,
                label = "Modes",
                isSelected = selectedTab == 2,
                onClick = { onTabSelected(2) },
                iconSize = 32.dp
            )

            SideTabBarItem(
                icon = iconRechercher,
                label = "Rechercher",
                isSelected = selectedTab == 3,
                onClick = { onTabSelected(3) },
                iconSize = 32.dp
            )

            SideTabBarItem(
                icon = iconNews,
                label = "News",
                isSelected = false,
                onClick = { navController.navigate("news_screen") },
                iconSize = 32.dp
            )
        }
    }
}

@Composable
private fun TabBarItem(
    icon: ImageBitmap?,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    iconSize: Dp
) {
    Column(
        modifier = Modifier
            .width(60.dp)
            .fillMaxHeight()
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(iconSize + 6.dp)
        ) {
            if (icon != null) {
                Image(
                    bitmap = icon,
                    contentDescription = label,
                    modifier = Modifier
                        .size(iconSize)
                        .then(
                            if (isSelected) {
                                Modifier.background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                    shape = MaterialTheme.shapes.small
                                )
                            } else {
                                Modifier
                            }
                        )
                        .padding(2.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp
            ),
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            },
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}

@Composable
private fun SideTabBarItem(
    icon: ImageBitmap?,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    iconSize: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(horizontal = 2.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(iconSize + 6.dp)
        ) {
            if (icon != null) {
                Image(
                    bitmap = icon,
                    contentDescription = label,
                    modifier = Modifier
                        .size(iconSize)
                        .then(
                            if (isSelected) {
                                Modifier.background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                    shape = MaterialTheme.shapes.small
                                )
                            } else {
                                Modifier
                            }
                        )
                        .padding(2.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(Modifier.height(2.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 8.sp
            ),
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            },
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier.padding(horizontal = 2.dp)
        )
    }
}

// ВКЛАДКА CATÉGORIES
@Composable
private fun CategoriesCompactLayout(
    navController: NavController,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        scrollState.animateScrollTo(0)
    }

    val horizontalPadding = min(16.dp, screenWidth * 0.04f)
    val verticalPadding = if (isCompactHeight) 8.dp else min(24.dp, screenHeight * 0.03f)
    val buttonSpacing = if (isCompactHeight) 6.dp else 8.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .padding(top = 72.dp),
        verticalArrangement = if (isCompactHeight) Arrangement.Top else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isCompactHeight) {
            Spacer(Modifier.height(if (isLandscape) 8.dp else 12.dp))
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOf(
                "Chars de combat" to "tanks",
                "Artillerie" to "artillery",
                "Reconnaissance" to "recon",
                "Génie" to "genie",
                "Avion/Hélicoptère" to "air"
            ).forEach { (text, category) ->
                CategoryButton(
                    text = text,
                    category = category,
                    navController = navController,
                    modifier = Modifier.fillMaxWidth(1f),
                    isCompact = isCompactHeight
                )
            }
        }

        Spacer(Modifier.height(if (isCompactHeight) 16.dp else 24.dp))
    }
}

@Composable
private fun CategoriesLargeLayout(
    navController: NavController,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        scrollState.animateScrollTo(0)
    }

    val horizontalPadding = min(32.dp, screenWidth * 0.05f)
    val verticalPadding = min(32.dp, screenHeight * 0.04f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(if (isCompactHeight) 8.dp else 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                listOf(
                    "Chars de combat" to "tanks",
                    "Artillerie" to "artillery",
                    "Reconnaissance" to "recon"
                ).forEach { (text, category) ->
                    CategoryButton(
                        text = text,
                        category = category,
                        navController = navController,
                        modifier = Modifier.fillMaxWidth(1f),
                        isCompact = isCompactHeight
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(if (isCompactHeight) 8.dp else 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                listOf(
                    "Génie" to "genie",
                    "Avion/Hélicoptère" to "air"
                ).forEach { (text, category) ->
                    CategoryButton(
                        text = text,
                        category = category,
                        navController = navController,
                        modifier = Modifier.fillMaxWidth(1f),
                        isCompact = isCompactHeight
                    )
                }
            }
        }

        Spacer(Modifier.height(if (isCompactHeight) 16.dp else 24.dp))
    }
}

// ВКЛАДКА MODES
@Composable
private fun ModesCompactLayout(
    navController: NavController,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        scrollState.animateScrollTo(0)
    }

    val horizontalPadding = min(16.dp, screenWidth * 0.04f)
    val verticalPadding = if (isCompactHeight) 8.dp else min(24.dp, screenHeight * 0.03f)
    val buttonSpacing = if (isCompactHeight) 6.dp else 8.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .padding(top = 72.dp),
        verticalArrangement = if (isCompactHeight) Arrangement.Top else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isCompactHeight) {
            Spacer(Modifier.height(if (isLandscape) 8.dp else 12.dp))
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ModeButton(
                text = "Test avancé",
                onClick = { navController.navigate("time_selection/bm2") },
                modifier = Modifier.fillMaxWidth(1f),
                isCompact = isCompactHeight
            )

            ModeButton(
                text = "Test final",
                onClick = { navController.navigate("player_name") },
                modifier = Modifier.fillMaxWidth(1f),
                isCompact = isCompactHeight
            )

            ModeButton(
                text = "Test collectif",
                onClick = { navController.navigate("competition") },
                modifier = Modifier.fillMaxWidth(1f),
                isCompact = isCompactHeight
            )

            ModeButton(
                text = "Creation",
                onClick = { navController.navigate("creation") },
                modifier = Modifier.fillMaxWidth(1f),
                isCompact = isCompactHeight
            )

            Spacer(Modifier.height(if (isCompactHeight) 8.dp else 12.dp))

            Button(
                onClick = { navController.navigate("hall_of_fame") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(if (isCompactHeight) 44.dp else 52.dp)
                    .padding(vertical = if (isCompactHeight) 2.dp else 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFF00),
                    contentColor = Color.Black
                ),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(
                    horizontal = if (isCompactHeight) 8.dp else 12.dp,
                    vertical = if (isCompactHeight) 6.dp else 8.dp
                )
            ) {
                Text(
                    text = "🏆 Panthéon",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = if (isCompactHeight) 12.sp else 15.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(if (isCompactHeight) 16.dp else 24.dp))
    }
}

@Composable
private fun ModesLargeLayout(
    navController: NavController,
    isLandscape: Boolean,
    isCompactHeight: Boolean,
    screenWidth: Dp,
    screenHeight: Dp
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        scrollState.animateScrollTo(0)
    }

    val horizontalPadding = min(32.dp, screenWidth * 0.05f)
    val verticalPadding = min(32.dp, screenHeight * 0.04f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(if (isCompactHeight) 8.dp else 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                ModeButton(
                    text = "Test avancé",
                    onClick = { navController.navigate("time_selection/bm2") },
                    modifier = Modifier.fillMaxWidth(1f),
                    isCompact = isCompactHeight
                )

                ModeButton(
                    text = "Test final",
                    onClick = { navController.navigate("player_name") },
                    modifier = Modifier.fillMaxWidth(1f),
                    isCompact = isCompactHeight
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(if (isCompactHeight) 8.dp else 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                ModeButton(
                    text = "Test collectif",
                    onClick = { navController.navigate("competition") },
                    modifier = Modifier.fillMaxWidth(1f),
                    isCompact = isCompactHeight
                )

                ModeButton(
                    text = "Creation",
                    onClick = { navController.navigate("creation") },
                    modifier = Modifier.fillMaxWidth(1f),
                    isCompact = isCompactHeight
                )
            }
        }

        Spacer(Modifier.height(if (isCompactHeight) 12.dp else 16.dp))

        Button(
            onClick = { navController.navigate("hall_of_fame") },
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .height(if (isCompactHeight) 44.dp else 52.dp)
                .padding(vertical = if (isCompactHeight) 2.dp else 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFFF00),
                contentColor = Color.Black
            ),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(
                horizontal = if (isCompactHeight) 8.dp else 12.dp,
                vertical = if (isCompactHeight) 6.dp else 8.dp
            )
        ) {
            Text(
                text = "🏆 Panthéon",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = if (isCompactHeight) 12.sp else 15.sp
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(if (isCompactHeight) 16.dp else 24.dp))
    }
}

@Composable
private fun CategoryButton(
    text: String,
    category: String,
    navController: NavController,
    modifier: Modifier,
    isCompact: Boolean = false
) {
    Button(
        onClick = {
            if (category == "final") {
                navController.navigate("player_name")
            } else {
                navController.navigate("time_selection/$category")
            }
        },
        modifier = modifier
            .height(
                when {
                    isCompact -> 44.dp
                    text.length > 15 -> 56.dp
                    else -> 52.dp
                }
            )
            .padding(vertical = if (isCompact) 2.dp else 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF607D8B),
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(
            horizontal = if (isCompact) 8.dp else 16.dp,
            vertical = if (isCompact) 8.dp else 12.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = when {
                    isCompact -> 13.sp
                    text.length > 15 -> 14.sp
                    else -> 16.sp
                }
            ),
            textAlign = TextAlign.Center,
            maxLines = if (text.length > 15) 2 else 1
        )
    }
}

@Composable
private fun ModeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    isCompact: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(if (isCompact) 44.dp else 52.dp)
            .padding(vertical = if (isCompact) 2.dp else 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF607D8B),
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(
            horizontal = if (isCompact) 8.dp else 16.dp,
            vertical = if (isCompact) 8.dp else 12.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = if (isCompact) 13.sp else 16.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}