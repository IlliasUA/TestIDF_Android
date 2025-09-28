package legOS.testidf.screens

import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
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
import com.example.quizapp.Air_Data
import com.example.quizapp.Art_Data
import com.example.quizapp.Genie_Data
import com.example.quizapp.Recon_Data
import com.example.quizapp.Test_Data
import com.example.quizapp.Test_bm2
import java.io.IOException

data class SearchResult(
    val aircraftName: String,
    val description: String,
    val mainImage: String,
    val additionalImages: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(navController: NavController) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf<List<SearchResult>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    val backgroundImage = remember {
        try {
            context.assets.open("images/background_3.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("CatalogScreen", "Error loading background_3.jpg", e)
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
            val results = searchInAirData(query.trim(), context)
            searchResults = results
            Log.d("CatalogScreen", "Search for '$query' found ${results.size} results")
        } catch (e: Exception) {
            Log.e("CatalogScreen", "Error during search", e)
            searchResults = emptyList()
        }
        isSearching = false
    }

    if (isLandscape) {
        CatalogLandscapeLayout(
            navController = navController,
            searchQuery = searchQuery,
            searchResults = searchResults,
            isSearching = isSearching,
            backgroundImage = backgroundImage,
            keyboardController = keyboardController,
            onQueryChange = { searchQuery = it },
            onSearch = { performSearch(searchQuery) },
            onClear = {
                searchQuery = ""
                searchResults = emptyList()
            },
            context = context
        )
    } else {
        CatalogPortraitLayout(
            navController = navController,
            searchQuery = searchQuery,
            searchResults = searchResults,
            isSearching = isSearching,
            backgroundImage = backgroundImage,
            keyboardController = keyboardController,
            onQueryChange = { searchQuery = it },
            onSearch = { performSearch(searchQuery) },
            onClear = {
                searchQuery = ""
                searchResults = emptyList()
            },
            context = context
        )
    }
}

// Обновленная компоновка для горизонтального режима
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogLandscapeLayout(
    navController: NavController,
    searchQuery: String,
    searchResults: List<SearchResult>,
    isSearching: Boolean,
    backgroundImage: androidx.compose.ui.graphics.ImageBitmap?,
    keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    context: Context
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                backgroundImage?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it),
                        contentScale = ContentScale.Crop
                    )
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
            // Левая часть - результаты поиска
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
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
                                text = "Entrez le nom pour commencer la recherche",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    searchResults.isEmpty() && searchQuery.isNotBlank() && !isSearching -> {
                        Text(
                            text = "Aucun trouvé pour \"$searchQuery\"",
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
                            contentPadding = PaddingValues(top = 8.dp) // Выравнивание с правой частью
                        ) {
                            items(searchResults) { result ->
                                SearchResultLandscapeCard(result = result, context = context)
                            }
                        }
                    }
                }
            }

            // Правая часть - поисковая строка и кнопки
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search Bar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp, top = 8.dp), // Выравнивание с LazyColumn
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text("Entrez ici") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = onClear) {
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
                                onSearch()
                                keyboardController?.hide()
                            }
                        ),
                        singleLine = true
                    )
                }

                // Buttons Row
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
                        onClick = {
                            onSearch()
                            keyboardController?.hide()
                        },
                        modifier = Modifier.weight(0.6f),
                        enabled = searchQuery.isNotBlank() && !isSearching
                    ) {
                        if (isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "...",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                            )
                        } else {
                            Text(
                                "Ok",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Компонент для горизонтального режима (без изменений, для контекста)
@Composable
private fun SearchResultLandscapeCard(
    result: SearchResult,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = result.aircraftName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = result.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            val mainBitmap = remember(result.mainImage) {
                try {
                    context.assets.open(result.mainImage).use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    }
                } catch (e: IOException) {
                    Log.e("SearchResultLandscapeCard", "Error loading main image ${result.mainImage}", e)
                    null
                }
            }

            mainBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Image principale : ${result.aircraftName}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (result.additionalImages.isNotEmpty()) {
                Text(
                    text = "Images supplémentaires:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    result.additionalImages.forEach { imagePath ->
                        val bitmap = remember(imagePath) {
                            try {
                                context.assets.open(imagePath).use { inputStream ->
                                    BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                                }
                            } catch (e: IOException) {
                                null
                            }
                        }

                        bitmap?.let { imgBitmap ->
                            Image(
                                bitmap = imgBitmap,
                                contentDescription = "Image supplémentaire : ${result.aircraftName}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(vertical = 8.dp),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
        }
    }
}

// Остальной код без изменений
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchResultCard(
    result: SearchResult,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = result.aircraftName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            val mainBitmap = remember(result.mainImage) {
                try {
                    context.assets.open(result.mainImage).use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                    }
                } catch (e: IOException) {
                    Log.e("SearchResultCard", "Error loading main image ${result.mainImage}", e)
                    null
                }
            }

            mainBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Image principale : ${result.aircraftName}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = result.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (result.additionalImages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Images supplémentaires :",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    result.additionalImages.forEach { imagePath ->
                        val bitmap = remember(imagePath) {
                            try {
                                context.assets.open(imagePath).use { inputStream ->
                                    BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
                                }
                            } catch (e: IOException) {
                                Log.e("SearchResultCard", "Error loading additional image $imagePath", e)
                                null
                            }
                        }

                        bitmap?.let { imgBitmap ->
                            Image(
                                bitmap = imgBitmap,
                                contentDescription = "Image supplémentaire : ${result.aircraftName}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(vertical = 8.dp),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Image non trouvée",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogPortraitLayout(
    navController: NavController,
    searchQuery: String,
    searchResults: List<SearchResult>,
    isSearching: Boolean,
    backgroundImage: androidx.compose.ui.graphics.ImageBitmap?,
    keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                backgroundImage?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it),
                        contentScale = ContentScale.Crop
                    )
                } ?: Modifier.background(MaterialTheme.colorScheme.background)
            )
            .systemBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Entrez ici") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = onClear) {
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
                        onSearch()
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
                onClick = {
                    onSearch()
                    keyboardController?.hide()
                },
                modifier = Modifier.weight(1f),
                enabled = searchQuery.isNotBlank() && !isSearching
            ) {
                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Recherche...")
                } else {
                    Text("Ok")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                            text = "Entrez le nom pour commencer la recherche",
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
                        text = "Aucun trouvé pour \"$searchQuery\"",
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
                    items(searchResults) { result ->
                        SearchResultCard(result = result, context = context)
                    }
                }
            }
        }
    }
}

private fun searchInAirData(query: String, context: Context): List<SearchResult> {
    val airDataQuestions = Air_Data.QUESTION
    val artDataQuestions = Art_Data.QUESTION
    val genieDataQuestions = Genie_Data.QUESTION
    val reconDataQuestions = Recon_Data.QUESTION
    val tankDataQuestions = Test_Data.QUESTION
    val bm2DataQuestions = Test_bm2.QUESTION

    val results = mutableListOf<SearchResult>()
    val queryLower = query.lowercase()

    airDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.aircraftName == question.correct }) {
                val mainImagePath = "air_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "air_images/$it" }

                results.add(
                    SearchResult(
                        aircraftName = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths
                    )
                )

                Log.d("searchInAirData", "Found air match: ${question.correct} with ${question.additionalImages?.size?.plus(1)} images")
            }
        }
    }

    artDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.aircraftName == question.correct }) {
                val mainImagePath = "artillery_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "artillery_images/$it" }

                results.add(
                    SearchResult(
                        aircraftName = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths
                    )
                )

                Log.d("searchInAirData", "Found artillery match: ${question.correct} with ${question.additionalImages?.size?.plus(1)} images")
            }
        }
    }

    genieDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.aircraftName == question.correct }) {
                val mainImagePath = "genie_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "genie_images/$it" }

                results.add(
                    SearchResult(
                        aircraftName = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths
                    )
                )

                Log.d("searchInAirData", "Found genie match: ${question.correct} with ${question.additionalImages?.size?.plus(1)} images")
            }
        }
    }

    reconDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.aircraftName == question.correct }) {
                val mainImagePath = "recon_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "recon_images/$it" }

                results.add(
                    SearchResult(
                        aircraftName = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths
                    )
                )

                Log.d("searchInAirData", "Found recon match: ${question.correct} with ${question.additionalImages?.size?.plus(1)} images")
            }
        }
    }

    tankDataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.aircraftName == question.correct }) {
                val mainImagePath = "tank_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "tank_images/$it" }

                results.add(
                    SearchResult(
                        aircraftName = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths
                    )
                )

                Log.d("searchInAirData", "Found tank match: ${question.correct} with ${question.additionalImages?.size?.plus(1)} images")
            }
        }
    }

    bm2DataQuestions.forEach { question ->
        if (question.correct.lowercase().contains(queryLower)) {
            if (results.none { it.aircraftName == question.correct }) {
                val mainImagePath = "bm2_images/${question.image}"
                val additionalImagePaths = (question.additionalImages ?: emptyList()).map { "bm2_images/$it" }

                results.add(
                    SearchResult(
                        aircraftName = question.correct,
                        description = question.description ?: "Description non disponible",
                        mainImage = mainImagePath,
                        additionalImages = additionalImagePaths
                    )
                )

                Log.d("searchInAirData", "Found bm2 match: ${question.correct} with ${question.additionalImages?.size?.plus(1)} images")
            }
        }
    }

    return results
}