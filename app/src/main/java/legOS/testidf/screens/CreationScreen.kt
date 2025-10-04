package legOS.testidf.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import legOS.testidf.viewmodel.CreationViewModel
import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizapp.*
import kotlinx.parcelize.Parcelize
import legOS.testidf.data.UserSession
import java.io.IOException


enum class CreationMode {
    OFFLINE,  // Одиночный режим - старая версия
    ONLINE    // Коллективный режим - с Firebase
}
@Parcelize
data class CreationItem(
    val name: String,
    val description: String,
    val mainImage: String,
    val additionalImages: List<String>,
    val category: String
) : Parcelable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationScreen(
    navController: NavController,
    mode: CreationMode = CreationMode.OFFLINE,  // По умолчанию офлайн
    viewModel: CreationViewModel = viewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf<List<CreationItem>>(emptyList()) }
    var selectedItems by rememberSaveable { mutableStateOf<List<CreationItem>>(emptyList()) }
    var showSelectedItems by rememberSaveable { mutableStateOf(false) }
    // Подписка на состояние ViewModel
    val uiState by viewModel.uiState.collectAsState()

    val backgroundImage = remember {
        try {
            context.assets.open("images/background_3.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("CreationScreen", "Error loading background_3.jpg", e)
            null
        }
    }

    fun performSearch(query: String) {
        searchResults = searchAllData(query.trim(), context)
        Log.d("CreationScreen", "Search for '$query' found ${searchResults.size} results")
    }

    fun addToSelection(item: CreationItem) {
        if (selectedItems.none { it.name == item.name }) {
            selectedItems = selectedItems + item
            Log.d("CreationScreen", "Added item: ${item.name}. Total selected: ${selectedItems.size}")
        }
    }

    fun removeFromSelection(item: CreationItem) {
        selectedItems = selectedItems.filter { it.name != item.name }
        Log.d("CreationScreen", "Removed item: ${item.name}. Total selected: ${selectedItems.size}")
    }

    fun navigateToCustomTest() {
        when (mode) {
            CreationMode.OFFLINE -> {
                // ОФЛАЙН РЕЖИМ - старая логика без Firebase
                try {
                    TestDataHolder.selectedItems = selectedItems
                    Log.d("CreationScreen", "Saved ${selectedItems.size} items to TestDataHolder (OFFLINE)")

                    selectedItems.forEachIndexed { index, item ->
                        Log.d("CreationScreen", "Item $index: ${item.name} (${item.category})")
                    }

                    // Переход к выбору времени (старый экран)
                    navController.currentBackStackEntry?.savedStateHandle?.set("selectedItems", selectedItems)
                    navController.navigate("custom_time_selection/${selectedItems.size}")

                } catch (e: Exception) {
                    Log.e("CreationScreen", "Error navigating to custom test (OFFLINE)", e)
                }
            }

            CreationMode.ONLINE -> {
                Log.d("CreationScreen", "Starting online test creation")

                viewModel.createTestSession(
                    selectedItems = selectedItems,
                    timeLimit = 15
                ) { sessionId ->
                    Log.d("CreationScreen", "✅ Success callback received: $sessionId")

                    // Переход к экрану отправки теста
                    try {
                        navController.navigate("send_test/$sessionId") {
                            // Не очищаем backstack для возможности возврата
                        }
                        Log.d("CreationScreen", "Navigation initiated to send_test/$sessionId")
                    } catch (e: Exception) {
                        Log.e("CreationScreen", "Navigation error", e)
                    }
                }
            }
        }
    }


    if (isLandscape) {
        // ГОРИЗОНТАЛЬНАЯ ОРИЕНТАЦИЯ
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    backgroundImage?.let {
                        Modifier.paint(painter = BitmapPainter(it), contentScale = ContentScale.Crop)
                    } ?: Modifier.background(MaterialTheme.colorScheme.background)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // ЛЕВАЯ ЧАСТЬ - Результаты поиска
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    if (!showSelectedItems) {
                        when {
                            searchResults.isEmpty() && searchQuery.isBlank() -> {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Recherchez des éléments pour créer votre test",
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            searchResults.isEmpty() && searchQuery.isNotBlank() -> {
                                Text(
                                    text = "Aucun élément trouvé pour \"$searchQuery\"",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            else -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    contentPadding = PaddingValues(top = 8.dp)
                                ) {
                                    items(searchResults) { item ->
                                        CreationItemCard(
                                            item = item,
                                            context = context,
                                            isSelected = selectedItems.any { it.name == item.name },
                                            onAddClick = { addToSelection(item) },
                                            onRemoveClick = { removeFromSelection(item) }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Показываем выбранные элементы
                        if (selectedItems.isEmpty()) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Aucun élément sélectionné",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Minimum 4 éléments requis pour créer un test",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                contentPadding = PaddingValues(top = 8.dp)
                            ) {
                                items(selectedItems) { item ->
                                    CreationItemCard(
                                        item = item,
                                        context = context,
                                        isSelected = true,
                                        onAddClick = { },
                                        onRemoveClick = { removeFromSelection(item) }
                                    )
                                }
                            }
                        }
                    }
                }

                // ПРАВАЯ ЧАСТЬ - Элементы управления
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!showSelectedItems) {
                        // Search Bar
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp, top = 8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { newValue ->
                                    searchQuery = newValue
                                    performSearch(newValue)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                placeholder = { Text("Entrez ici") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Rechercher"
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = {
                                            searchQuery = ""
                                            searchResults = emptyList()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Effacer"
                                            )
                                        }
                                    }
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        performSearch(searchQuery)
                                        keyboardController?.hide()
                                    }
                                ),
                                singleLine = true
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("test_menu") {
                                        popUpTo("test_menu") { inclusive = false }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Retour",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                )
                            }

                            Button(
                                onClick = { navigateToCustomTest() },  // ИСПРАВЛЕНО: здесь была ошибка
                                modifier = Modifier.weight(1f),  // ИСПРАВЛЕНО: было fillMaxWidth()
                                enabled = selectedItems.size >= 4 && (mode == CreationMode.OFFLINE || !uiState.isLoading),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedItems.size >= 4)
                                        Color(0xFF4CAF50)
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                if (mode == CreationMode.ONLINE && uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text(
                                    when {
                                        mode == CreationMode.ONLINE && uiState.isLoading -> "Création..."
                                        mode == CreationMode.ONLINE -> "Créer Test en Ligne (${selectedItems.size})"
                                        else -> "Créer Test (${selectedItems.size})"
                                    },
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                )
                            }
                        }


                        // Badge for selected items count
                        if (selectedItems.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )
                            ) {
                                Text(
                                    text = "Éléments sélectionnés: ${selectedItems.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        // Selected items view controls
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp, top = 8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { showSelectedItems = false },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Retour",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                        )
                                    }

                                    Button(
                                        onClick = { selectedItems = emptyList() },
                                        modifier = Modifier.weight(1f),
                                        enabled = selectedItems.isNotEmpty(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Effacer",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = { navigateToCustomTest() },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = selectedItems.size >= 4 && !uiState.isLoading,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedItems.size >= 4 && !uiState.isLoading)
                                            Color(0xFF4CAF50)
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    if (uiState.isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(Modifier.width(8.dp))
                                    }
                                    Text(
                                        if (uiState.isLoading) "Création..." else "Créer Test (${selectedItems.size})",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                                    )
                                }

                                if (selectedItems.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "Éléments sélectionnés: ${selectedItems.size}",
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            val categoryCounts = selectedItems.groupingBy { it.category }.eachCount()
                                            categoryCounts.forEach { (category, count) ->
                                                Text(
                                                    text = "$category: $count",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                            if (selectedItems.size < 4) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Encore ${4 - selectedItems.size} éléments requis",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        // ВЕРТИКАЛЬНАЯ ОРИЕНТАЦИЯ
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    backgroundImage?.let {
                        Modifier.paint(painter = BitmapPainter(it), contentScale = ContentScale.Crop)
                    } ?: Modifier.background(MaterialTheme.colorScheme.background)
                )
                .systemBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (!showSelectedItems) {
                // Search interface
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { newValue ->
                            searchQuery = newValue
                            performSearch(newValue)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text("Entrez ici") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Rechercher"
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    searchQuery = ""
                                    searchResults = emptyList()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Effacer"
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                performSearch(searchQuery)
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate("test_menu") {
                                popUpTo("test_menu") { inclusive = false }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Retour")
                    }

                    Button(
                        onClick = { showSelectedItems = !showSelectedItems },  // ИСПРАВЛЕНО
                        modifier = Modifier.weight(1f),  // ИСПРАВЛЕНО: было fillMaxWidth()
                        enabled = selectedItems.size >= 4,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedItems.size >= 4)
                                Color(0xFF4CAF50)
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        if (mode == CreationMode.ONLINE && uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(
                            when {
                                mode == CreationMode.ONLINE && uiState.isLoading -> "Création..."
                                mode == CreationMode.ONLINE -> "Créer Test en Ligne (${selectedItems.size})"
                                else -> "Créer Test (${selectedItems.size})"
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Results
                when {
                    searchResults.isEmpty() && searchQuery.isBlank() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Recherchez des éléments pour créer votre test",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    searchResults.isEmpty() && searchQuery.isNotBlank() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aucun élément trouvé pour \"$searchQuery\"",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(searchResults) { item ->
                                CreationItemCard(
                                    item = item,
                                    context = context,
                                    isSelected = selectedItems.any { it.name == item.name },
                                    onAddClick = { addToSelection(item) },
                                    onRemoveClick = { removeFromSelection(item) }
                                )
                            }
                        }
                    }
                }
            } else {
                // Selected items view
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        // Первая строка - кнопки Retour и Effacer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showSelectedItems = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Retour")
                            }

                            Button(
                                onClick = { selectedItems = emptyList() },
                                modifier = Modifier.weight(1f),
                                enabled = selectedItems.isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Effacer")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Вторая строка - кнопка Créer Test
                        Button(
                            onClick = { navigateToCustomTest() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = selectedItems.size >= 4 && !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedItems.size >= 4 && !uiState.isLoading)
                                    Color(0xFF4CAF50)
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Text(if (uiState.isLoading) "Création..." else "Créer Test (${selectedItems.size})")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (selectedItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Aucun élément sélectionné",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Minimum 4 éléments requis pour créer un test",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Éléments sélectionnés: ${selectedItems.size}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            val categoryCounts = selectedItems.groupingBy { it.category }.eachCount()
                            categoryCounts.forEach { (category, count) ->
                                Text(
                                    text = "$category: $count",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            if (selectedItems.size < 4) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Encore ${4 - selectedItems.size} éléments requis",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(selectedItems) { item ->
                            CreationItemCard(
                                item = item,
                                context = context,
                                isSelected = true,
                                onAddClick = { },
                                onRemoveClick = { removeFromSelection(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Остальной код остается без изменений
object TestDataHolder {
    var selectedItems: List<CreationItem> = emptyList()

    fun getAndClearItems(): List<CreationItem> {
        val items = selectedItems
        return items
    }

    fun clearItems() {
        selectedItems = emptyList()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreationItemCard(
    item: CreationItem,
    context: Context,
    isSelected: Boolean,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.name, style = MaterialTheme.typography.headlineSmall)
                    Text("Catégorie: ${item.category}", style = MaterialTheme.typography.bodySmall)
                    if (item.additionalImages.isNotEmpty()) {
                        Text("${item.additionalImages.size + 1} images disponibles", style = MaterialTheme.typography.bodySmall)
                    }
                }

                if (isSelected) {
                    IconButton(
                        onClick = onRemoveClick,
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, "Retirer", tint = MaterialTheme.colorScheme.onError)
                    }
                } else {
                    IconButton(
                        onClick = onAddClick,
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Add, "Ajouter", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            val mainBitmap = remember(item.mainImage) {
                try {
                    context.assets.open(item.mainImage).use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    }
                } catch (e: IOException) { null }
            }

            mainBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Image de ${item.name}",
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
            } ?: Box(
                modifier = Modifier.fillMaxWidth().height(150.dp).background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("Image non disponible")
            }

            Text(
                text = item.description.take(100) + if (item.description.length > 100) "..." else "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun searchAllData(query: String, context: Context): List<CreationItem> {
    // Если запрос пустой, возвращаем пустой список
    if (query.isBlank()) return emptyList()

    val airDataQuestions = Air_Data.QUESTION
    val artDataQuestions = Art_Data.QUESTION
    val genieDataQuestions = Genie_Data.QUESTION
    val reconDataQuestions = Recon_Data.QUESTION
    val tankDataQuestions = Test_Data.QUESTION
    val bm2DataQuestions = Test_bm2.QUESTION

    val results = mutableListOf<CreationItem>()

    // Нормализуем запрос: удаляем пробелы, тире и переводим в нижний регистр
    val normalizedQuery = query.lowercase().replace(" ", "").replace("-", "").replace("_", "").trim()

    // Создаем карту для сортировки по релевантности
    val relevanceMap = mutableMapOf<CreationItem, Int>()

    // Функция для нормализации имени
    fun normalizeName(name: String): String {
        return name.lowercase().replace(" ", "").replace("-", "").replace("_", "")
    }

    // Функция для добавления элемента с расчетом релевантности
    fun addItemWithRelevance(
        name: String,
        description: String,
        mainImagePath: String,
        additionalImagePaths: List<String>,
        category: String
    ) {
        if (results.none { it.name == name }) {
            val normalizedName = normalizeName(name)
            val originalNameLower = name.lowercase()

            // Расчет релевантности
            val relevance = when {
                // Точное совпадение (игнорируя пробелы и тире)
                normalizedName == normalizedQuery -> 100

                // Точное совпадение с оригинальным запросом
                originalNameLower == query.lowercase() -> 95

                // Начинается с запроса (нормализованного)
                normalizedName.startsWith(normalizedQuery) -> 90

                // Начинается с оригинального запроса
                originalNameLower.startsWith(query.lowercase()) -> 85

                // Содержит полную последовательность символов
                normalizedName.contains(normalizedQuery) -> 70

                // Проверка по частям слова
                else -> {
                    // Разбиваем оригинальное имя на слова
                    val words = originalNameLower.split(" ", "-", "_")

                    // Проверяем, начинается ли какое-то слово с запроса
                    val startsWithQuery = words.any { it.startsWith(query.lowercase()) }
                    if (startsWithQuery) {
                        65
                    } else {
                        // Проверяем, содержится ли запрос в каком-то слове
                        val containsQuery = words.any { it.contains(query.lowercase()) }
                        if (containsQuery) {
                            60
                        } else {
                            // Последовательный поиск символов (для опечаток)
                            var searchIndex = 0
                            val queryChars = normalizedQuery.toCharArray()
                            var allFound = true

                            for (char in queryChars) {
                                val found = normalizedName.indexOf(char, searchIndex)
                                if (found == -1) {
                                    allFound = false
                                    break
                                }
                                searchIndex = found + 1
                            }

                            // Если все символы найдены в последовательности
                            if (allFound) 40 else 0
                        }
                    }
                }
            }

            if (relevance > 0) {
                val item = CreationItem(
                    name = name,
                    description = description,
                    mainImage = mainImagePath,
                    additionalImages = additionalImagePaths,
                    category = category
                )
                results.add(item)
                relevanceMap[item] = relevance
            }
        }
    }

    // Search in Air_Data
    airDataQuestions.forEach { question ->
        val mainImagePath = "air_images/${question.image}"
        val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "air_images/$it" }

        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: "Description non disponible",
            mainImagePath = mainImagePath,
            additionalImagePaths = additionalImagePaths,
            category = "Aviation"
        )
    }

    // Search in Art_Data
    artDataQuestions.forEach { question ->
        val mainImagePath = "artillery_images/${question.image}"
        val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "artillery_images/$it" }

        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: "Description non disponible",
            mainImagePath = mainImagePath,
            additionalImagePaths = additionalImagePaths,
            category = "Artillerie"
        )
    }

    // Search in Genie_Data
    genieDataQuestions.forEach { question ->
        val mainImagePath = "genie_images/${question.image}"
        val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "genie_images/$it" }

        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: "Description non disponible",
            mainImagePath = mainImagePath,
            additionalImagePaths = additionalImagePaths,
            category = "Génie"
        )
    }

    // Search in Recon_Data
    reconDataQuestions.forEach { question ->
        val mainImagePath = "recon_images/${question.image}"
        val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "recon_images/$it" }

        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: "Description non disponible",
            mainImagePath = mainImagePath,
            additionalImagePaths = additionalImagePaths,
            category = "Reconnaissance"
        )
    }

    // Search in Test_Data (Tanks)
    tankDataQuestions.forEach { question ->
        val mainImagePath = "tank_images/${question.image}"
        val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "tank_images/$it" }

        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: "Description non disponible",
            mainImagePath = mainImagePath,
            additionalImagePaths = additionalImagePaths,
            category = "Chars"
        )
    }

    // Search in Test_bm2
    bm2DataQuestions.forEach { question ->
        val mainImagePath = "bm2_images/${question.image}"
        val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "bm2_images/$it" }

        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: "Description non disponible",
            mainImagePath = mainImagePath,
            additionalImagePaths = additionalImagePaths,
            category = "Militaire"
        )
    }

    // Сортируем результаты по релевантности (от большей к меньшей)
    val sortedResults = results.sortedByDescending { relevanceMap[it] ?: 0 }

    Log.d("CreationScreen", "Search completed: found ${sortedResults.size} items for query '$query'")
    return sortedResults
}
