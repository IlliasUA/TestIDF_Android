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
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.res.stringResource
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
import legOS.testidf.R
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
    var searchResults by remember { mutableStateOf<List<SearchResult>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    val backgroundImage = remember {
        try {
            context.assets.open("images/background_2.jpg").use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        } catch (e: IOException) {
            Log.e("CatalogScreen", "Error loading background_3.jpg", e)
            null
        }
    }

    // Автоматический поиск при изменении запроса
    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            searchResults = emptyList()
            isSearching = false
        } else {
            isSearching = true
            // Небольшая задержка для избежания слишком частых поисков
            kotlinx.coroutines.delay(300)
            try {
                val results = searchInAirData(searchQuery.trim(), context)
                searchResults = results
                Log.d("CatalogScreen", "Auto-search for '$searchQuery' found ${results.size} results")
            } catch (e: Exception) {
                Log.e("CatalogScreen", "Error during auto-search", e)
                searchResults = emptyList()
            }
            isSearching = false
        }
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
                    isSearching -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.searching),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
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
                                text = stringResource(R.string.start_typing_to_search),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    searchResults.isEmpty() && searchQuery.isNotBlank() && !isSearching -> {
                        Text(
                            text = stringResource(R.string.no_results_for, searchQuery),
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
                            items(searchResults) { result ->
                                SearchResultLandscapeCard(result = result, context = context)
                            }
                        }
                    }
                }
            }

            // Правая часть - поисковая строка
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search Bar с кнопкой возврата СПРАВА
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onQueryChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            placeholder = { Text(stringResource(R.string.search_placeholder)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_tab)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = onClear) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = stringResource(R.string.clear_search)
                                        )
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                }
                            ),
                            singleLine = true
                        )
                    }

                    // Кнопка возврата - стрелка вправо
                    IconButton(
                        onClick = {
                            navController.navigate("test_menu") {
                                popUpTo("test_menu") { inclusive = false }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = stringResource(R.string.back_to_menu),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Информация о результатах
                if (searchQuery.isNotBlank() && !isSearching && searchResults.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.results_found, searchResults.size),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
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
                    contentDescription = stringResource(R.string.main_image, result.aircraftName),
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
                    text = stringResource(R.string.additional_images),
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
                                contentDescription = stringResource(R.string.additional_image, result.aircraftName),
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
                    contentDescription = stringResource(R.string.main_image, result.aircraftName),
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
                    text = stringResource(R.string.additional_images),
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
                                contentDescription = stringResource(R.string.additional_image, result.aircraftName),
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
                                text = stringResource(R.string.image_not_found_item),
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

        // Search Bar с кнопкой возврата слева
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка возврата - стрелка влево
            IconButton(
                onClick = {
                    navController.navigate("test_menu") {
                        popUpTo("test_menu") { inclusive = false }
                    }
                }
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_to_menu),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }

            Card(
                modifier = Modifier.weight(1f),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_tab)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = onClear) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.clear_search)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    singleLine = true
                )
            }
        }

        // Информация о результатах
        if (searchQuery.isNotBlank() && !isSearching && searchResults.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    text = stringResource(R.string.results_found, searchResults.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isSearching -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.searching),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
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
                            text = stringResource(R.string.start_typing_to_search),
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
                        text = stringResource(R.string.no_results_for, searchQuery),
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
    // Если запрос пустой, возвращаем пустой список
    if (query.isBlank()) return emptyList()

    val airDataQuestions = Air_Data.QUESTION
    val artDataQuestions = Art_Data.QUESTION
    val genieDataQuestions = Genie_Data.QUESTION
    val reconDataQuestions = Recon_Data.QUESTION
    val tankDataQuestions = Test_Data.QUESTION
    val bm2DataQuestions = Test_bm2.QUESTION

    val results = mutableListOf<SearchResult>()

    // Нормализуем запрос: удаляем пробелы, тире и переводим в нижний регистр
    val normalizedQuery = query.lowercase().replace(" ", "").replace("-", "").replace("_", "").trim()

    // Создаем карту для сортировки по релевантности
    val relevanceMap = mutableMapOf<SearchResult, Int>()

    // Функция для нормализации имени
    fun normalizeName(name: String): String {
        return name.lowercase().replace(" ", "").replace("-", "").replace("_", "")
    }

    // Получаем строку для "Description non disponible"
    val descriptionNotAvailable = context.getString(R.string.description_not_available)

    // Функция для добавления элемента с расчетом релевантности
    fun addItemWithRelevance(
        name: String,
        description: String,
        mainImagePath: String,
        additionalImagePaths: List<String>
    ) {
        if (results.none { it.aircraftName == name }) {
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
                val item = SearchResult(
                    aircraftName = name,
                    description = description,
                    mainImage = mainImagePath,
                    additionalImages = additionalImagePaths
                )
                results.add(item)
                relevanceMap[item] = relevance
            }
        }
    }

    // Поиск в Air Data
    airDataQuestions.forEach { question ->
        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: descriptionNotAvailable,
            mainImagePath = "air_images/${question.image}",
            additionalImagePaths = (question.additionalImages ?: emptyList()).map { "air_images/$it" }
        )
    }

    // Поиск в Artillery Data
    artDataQuestions.forEach { question ->
        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: descriptionNotAvailable,
            mainImagePath = "artillery_images/${question.image}",
            additionalImagePaths = (question.additionalImages ?: emptyList()).map { "artillery_images/$it" }
        )
    }

    // Поиск в Genie Data
    genieDataQuestions.forEach { question ->
        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: descriptionNotAvailable,
            mainImagePath = "genie_images/${question.image}",
            additionalImagePaths = (question.additionalImages ?: emptyList()).map { "genie_images/$it" }
        )
    }

    // Поиск в Recon Data
    reconDataQuestions.forEach { question ->
        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: descriptionNotAvailable,
            mainImagePath = "recon_images/${question.image}",
            additionalImagePaths = (question.additionalImages ?: emptyList()).map { "recon_images/$it" }
        )
    }

    // Поиск в Tank Data
    tankDataQuestions.forEach { question ->
        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: descriptionNotAvailable,
            mainImagePath = "tank_images/${question.image}",
            additionalImagePaths = (question.additionalImages ?: emptyList()).map { "tank_images/$it" }
        )
    }

    // Поиск в BM2 Data
    bm2DataQuestions.forEach { question ->
        addItemWithRelevance(
            name = question.correct,
            description = question.description ?: descriptionNotAvailable,
            mainImagePath = "bm2_images/${question.image}",
            additionalImagePaths = (question.additionalImages ?: emptyList()).map { "bm2_images/$it" }
        )
    }

    // Сортируем результаты по релевантности (от большей к меньшей)
    val sortedResults = results.sortedByDescending { relevanceMap[it] ?: 0 }

    Log.d("searchInAirData", "Search for '$query' found ${sortedResults.size} results")
    sortedResults.take(5).forEach { result ->
        Log.d("searchInAirData", "  - ${result.aircraftName} (relevance: ${relevanceMap[result]})")
    }

    return sortedResults
}