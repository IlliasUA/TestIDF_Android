package legOS.testidf.screens

import android.content.Context
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
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
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
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<SearchResult>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    // Background image
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recherche") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour"
                        )
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
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Entrez ici") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Ok"
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

            // Search Button
            Button(
                onClick = {
                    performSearch(searchQuery)
                    keyboardController?.hide()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = searchQuery.isNotBlank() && !isSearching
            ) {
                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (isSearching) "Recherche..." else "Ok")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Results
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchResultCard(
    result: SearchResult,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Aircraft Name
            Text(
                text = result.aircraftName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Main Image
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
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Description
            Text(
                text = result.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Additional Images (вертикальный стек снизу вверх, в порядке очереди)
            if (result.additionalImages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Images supplémentaires :",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Простой Column для расположения изображений снизу вверх (в порядке списка)
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
                                    .height(200.dp),  // Такой же размер, как главное изображение
                                contentScale = ContentScale.Crop  // Такой же, как у главного
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

private fun searchInAirData(query: String, context: Context): List<SearchResult> {
    val airDataQuestions = Air_Data.QUESTION
    val artDataQuestions = Art_Data.QUESTION
    val genieDataQuestions = Genie_Data.QUESTION
    val reconDataQuestions = Recon_Data.QUESTION
    val tankDataQuestions = Test_Data.QUESTION
    val bm2DataQuestions = Test_bm2.QUESTION

    val results = mutableListOf<SearchResult>()
    val queryLower = query.lowercase()

    // Search in Air_Data
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

    // Search in Art_Data
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

    // Search in Genie_Data
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

    // Search in Recon_Data
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

    // Search in Test_Data (Tanks)
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

    // Search in Test_bm2 (BM2)
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
