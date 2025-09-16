package legOS.testidf.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.Air_Data
import com.example.quizapp.Art_Data
import com.example.quizapp.Genie_Data
import com.example.quizapp.Recon_Data
import com.example.quizapp.Test_Data
import com.example.quizapp.Test_bm2
import kotlinx.parcelize.Parcelize
import java.io.IOException

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
fun CreationScreen(navController: NavController) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<CreationItem>>(emptyList()) }
    var selectedItems by remember { mutableStateOf<List<CreationItem>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var showSelectedItems by remember { mutableStateOf(false) }

    // Background image
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
        if (query.isBlank()) {
            searchResults = emptyList()
            return
        }

        isSearching = true
        try {
            val results = searchAllData(query.trim(), context)
            searchResults = results
            Log.d("CreationScreen", "Search for '$query' found ${results.size} results")
        } catch (e: Exception) {
            Log.e("CreationScreen", "Error during search", e)
            searchResults = emptyList()
        }
        isSearching = false
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
        try {
            // Сохраняем selectedItems в SavedStateHandle текущего экрана
            navController.currentBackStackEntry?.savedStateHandle?.set("selectedItems", selectedItems)
            Log.d("CreationScreen", "Saved ${selectedItems.size} items to SavedStateHandle")

            // Логируем содержимое для отладки
            selectedItems.forEachIndexed { index, item ->
                Log.d("CreationScreen", "Item $index: ${item.name} (${item.category})")
            }

            // Переходим к выбору времени
            navController.navigate("custom_time_selection/${selectedItems.size}")

        } catch (e: Exception) {
            Log.e("CreationScreen", "Error saving selected items", e)
            // Fallback: используем глобальный объект
            TestDataHolder.selectedItems = selectedItems
            navController.navigate("custom_time_selection/${selectedItems.size}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Créateur de Test") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    // Badge with selected count
                    Badge(
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text("${selectedItems.size}")
                    }

                    // Toggle view button
                    TextButton(
                        onClick = { showSelectedItems = !showSelectedItems }
                    ) {
                        Text(if (showSelectedItems) "Recherche" else "Sélection")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .then(
                    backgroundImage?.let {
                        Modifier.paint(
                            painter = BitmapPainter(it),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Modifier.background(MaterialTheme.colorScheme.background)
                )
        ) {
            if (!showSelectedItems) {
                // Search interface
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
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

                        Spacer(modifier = Modifier.height(8.dp))

                        // Кнопка OK для подтверждения поиска
                        Button(
                            onClick = {
                                performSearch(searchQuery)
                                keyboardController?.hide()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = searchQuery.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            if (isSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Recherche en cours...")
                            } else {
                                Text("OK", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }

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
                    searchResults.isEmpty() && searchQuery.isNotBlank() && !isSearching -> {
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
                    isSearching -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
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
                if (selectedItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Aucun élément sélectionné",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Minimum 4 éléments requis pour créer un test",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    Column {
                        // Action buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    selectedItems = emptyList()
                                    Log.d("CreationScreen", "Cleared all selected items")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Effacer tout")
                            }

                            Button(
                                onClick = { navigateToCustomTest() },
                                enabled = selectedItems.size >= 4,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary
                                )
                            ) {
                                Text("Créer Test (${selectedItems.size})")
                            }
                        }

                        // Selected items summary
                        if (selectedItems.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Éléments sélectionnés: ${selectedItems.size}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    val categoryCounts = selectedItems.groupingBy { it.category }.eachCount()
                                    categoryCounts.forEach { (category, count) ->
                                        Text(
                                            text = "$category: $count",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        }

                        // Selected items list
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
}

// Глобальный объект как fallback
object TestDataHolder {
    var selectedItems: List<CreationItem> = emptyList()
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
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Catégorie: ${item.category}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (item.additionalImages.isNotEmpty()) {
                        Text(
                            text = "${item.additionalImages.size + 1} images disponibles",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (isSelected) {
                    IconButton(
                        onClick = onRemoveClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Retirer",
                            tint = MaterialTheme.colorScheme.onError
                        )
                    }
                } else {
                    IconButton(
                        onClick = onAddClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Ajouter",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Image
            val mainBitmap = remember(item.mainImage) {
                try {
                    context.assets.open(item.mainImage).use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    }
                } catch (e: IOException) {
                    Log.e("CreationItemCard", "Error loading main image ${item.mainImage}", e)
                    null
                }
            }

            mainBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Image de ${item.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            } ?: run {
                // Показываем placeholder если изображение не загрузилось
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Image non disponible",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Description (shortened)
            Text(
                text = item.description.take(100) + if (item.description.length > 100) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun searchAllData(query: String, context: Context): List<CreationItem> {
    val airDataQuestions = Air_Data.QUESTION
    val artDataQuestions = Art_Data.QUESTION
    val genieDataQuestions = Genie_Data.QUESTION
    val reconDataQuestions = Recon_Data.QUESTION
    val tankDataQuestions = Test_Data.QUESTION
    val bm2DataQuestions = Test_bm2.QUESTION

    val results = mutableListOf<CreationItem>()
    val queryLower = query.lowercase()

    // Search in Air_Data
    airDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.name == question.correct }) {
                val mainImagePath = "air_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "air_images/$it" }

                results.add(
                    CreationItem(
                        name = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths,
                        category = "Aviation"
                    )
                )
            }
        }
    }

    // Search in Art_Data
    artDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.name == question.correct }) {
                val mainImagePath = "artillery_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "artillery_images/$it" }

                results.add(
                    CreationItem(
                        name = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths,
                        category = "Artillerie"
                    )
                )
            }
        }
    }

    // Search in Genie_Data
    genieDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.name == question.correct }) {
                val mainImagePath = "genie_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "genie_images/$it" }

                results.add(
                    CreationItem(
                        name = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths,
                        category = "Génie"
                    )
                )
            }
        }
    }

    // Search in Recon_Data
    reconDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.name == question.correct }) {
                val mainImagePath = "recon_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "recon_images/$it" }

                results.add(
                    CreationItem(
                        name = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths,
                        category = "Reconnaissance"
                    )
                )
            }
        }
    }

    // Search in Test_Data (Tanks)
    tankDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.name == question.correct }) {
                val mainImagePath = "tank_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "tank_images/$it" }

                results.add(
                    CreationItem(
                        name = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths,
                        category = "Chars"
                    )
                )
            }
        }
    }

    // Search in Test_bm2
    bm2DataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.name == question.correct }) {
                val mainImagePath = "bm2_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "bm2_images/$it" }

                results.add(
                    CreationItem(
                        name = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths,
                        category = "Militaire"
                    )
                )
            }
        }
    }

    Log.d("CreationScreen", "Search completed: found ${results.size} items for query '$query'")
    return results
}