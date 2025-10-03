package legOS.testidf.screens

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import legOS.testidf.loadImageFromAssets

@Composable
fun CustomResultsScreen(navController: NavController, questionCount: String, timeLimit: String) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val customQuestions = navController.previousBackStackEntry?.savedStateHandle?.get<List<CustomTestQuestion>>("customQuestions")
        ?: emptyList()
    val customAnswers = navController.previousBackStackEntry?.savedStateHandle?.get<List<String?>>("customAnswers")
        ?: emptyList()

    Log.d("CustomResultsScreen", "Questions: ${customQuestions.size}, Answers: ${customAnswers.size}")

    val results = customQuestions.zip(customAnswers) { question, answer ->
        val isCorrect = isAnswerCorrect(answer, question.correctAnswer)
        Triple(question, answer, isCorrect)
    }

    val correctCount = results.count { it.third }
    val totalQuestions = customQuestions.size

    Log.d("CustomResultsScreen", "Correct answers: $correctCount/$totalQuestions")

    val backgroundImage = loadImageFromAssets(context, "images/background_3.jpg")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                backgroundImage?.let {
                    Modifier.paint(
                        painter = BitmapPainter(it.asImageBitmap()),
                        contentScale = ContentScale.Crop
                    )
                } ?: Modifier.background(MaterialTheme.colorScheme.background)
            )
    ) {
        if (isLandscape) {
            CustomResultsLandscapeLayout(
                results = results,
                correctCount = correctCount,
                totalQuestions = totalQuestions,
                context = context,
                navController = navController
            )
        } else {
            CustomResultsPortraitLayout(
                results = results,
                correctCount = correctCount,
                totalQuestions = totalQuestions,
                context = context,
                navController = navController
            )
        }
    }
}

@Composable
private fun CustomResultsLandscapeLayout(
    results: List<Triple<CustomTestQuestion, String?, Boolean>>,
    correctCount: Int,
    totalQuestions: Int,
    context: Context,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ЛЕВАЯ ЧАСТЬ - Список результатов с изображениями (60%)
        LazyColumn(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results.size) { index ->
                val (question, userAnswer, isCorrect) = results[index]

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect)
                            Color(0xFF90EE90).copy(alpha = 0.9f)
                        else
                            Color(0xFFFFB6C1).copy(alpha = 0.9f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val bitmap = loadImageFromAssets(context, question.imagePath)
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Question Image",
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(100.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Box(
                            modifier = Modifier
                                .width(140.dp)
                                .height(100.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clip(MaterialTheme.shapes.medium),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Image\nnon disponible",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Question ${index + 1}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                "Catégorie: ${question.category}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )

                            Text(
                                "Correct: ${question.correctAnswer}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = Color(0xFF2E7D32)
                            )

                            Text(
                                "Vous: ${userAnswer ?: "Aucune"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                            )

                            Text(
                                if (isCorrect) "✓ Correct" else "✗ Incorrect",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }
        }

        // ПРАВАЯ ЧАСТЬ - Статистика и кнопки (40%) с прокруткой
        LazyColumn(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
                .padding(end = 8.dp), // Prevent content from touching the right edge
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(0.8f), // Reduced width to 80% to shift left
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Test Terminé!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Score: $correctCount / $totalQuestions",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (correctCount >= totalQuestions * 0.7) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                        )

                        val percentage = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
                        Text(
                            "($percentage%)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                val categoryStats = results.groupBy { it.first.category }
                if (categoryStats.size > 1) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(0.8f), // Reduced width to 80% to shift left
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                "Par catégorie:",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            categoryStats.forEach { (category, categoryResults) ->
                                val categoryCorrect = categoryResults.count { it.third }
                                val categoryTotal = categoryResults.size

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        category,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "$categoryCorrect/$categoryTotal",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        color = if (categoryCorrect == categoryTotal) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f), // Reduced width to 80% to align with cards
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { navController.navigate("creation") },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Test", style = MaterialTheme.typography.bodyMedium)
                    }

                    Button(
                        onClick = { navController.navigate("test_menu") },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Text("Menu", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomResultsPortraitLayout(
    results: List<Triple<CustomTestQuestion, String?, Boolean>>,
    correctCount: Int,
    totalQuestions: Int,
    context: Context,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .safeDrawingPadding(), // Ensure content is inset from system bars
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Test Personnalisé Terminé!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Score: $correctCount / $totalQuestions",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (correctCount >= totalQuestions * 0.7) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )

                val percentage = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
                Text(
                    "($percentage%)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        val categoryStats = results.groupBy { it.first.category }
        if (categoryStats.size > 1) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Résultats par catégorie:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    categoryStats.forEach { (category, categoryResults) ->
                        val categoryCorrect = categoryResults.count { it.third }
                        val categoryTotal = categoryResults.size

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                category,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "$categoryCorrect/$categoryTotal",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = if (categoryCorrect == categoryTotal) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results.size) { index ->
                val (question, userAnswer, isCorrect) = results[index]

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect)
                            Color(0xFF90EE90).copy(alpha = 0.9f)
                        else
                            Color(0xFFFFB6C1).copy(alpha = 0.9f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val bitmap = loadImageFromAssets(context, question.imagePath)
                        bitmap?.let {
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Question Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clip(MaterialTheme.shapes.medium),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Image non disponible",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            "Question ${index + 1} - ${question.category}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            "Réponse correcte: ${question.correctAnswer}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = Color(0xFF2E7D32)
                        )

                        Text(
                            "Votre réponse: ${userAnswer ?: "Aucune réponse"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (isCorrect) "✓ Correct" else "✗ Incorrect",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { navController.navigate("creation") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Test", style = MaterialTheme.typography.bodyLarge)
            }

            Button(
                onClick = { navController.navigate("test_menu") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text("Menu", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(2.dp)) // Safe zone below buttons for system navigation
    }
}

private fun isAnswerCorrect(userAnswer: String?, correctAnswer: String): Boolean {
    if (userAnswer.isNullOrBlank()) return false

    val normalizedUserAnswer = normalizeAnswer(userAnswer)
    val normalizedCorrectAnswer = normalizeAnswer(correctAnswer)

    Log.d("CustomResultsScreen", "Comparing: '$normalizedUserAnswer' vs '$normalizedCorrectAnswer'")

    if (normalizedUserAnswer == normalizedCorrectAnswer) {
        Log.d("CustomResultsScreen", "Exact match found")
        return true
    }

    if (isCombinedName(normalizedCorrectAnswer)) {
        return validateCombinedName(normalizedUserAnswer, normalizedCorrectAnswer)
    }

    if (isPureTechnicalName(normalizedCorrectAnswer)) {
        return validatePureTechnicalName(normalizedUserAnswer, normalizedCorrectAnswer)
    }

    val meaningfulParts = extractMeaningfulParts(normalizedCorrectAnswer)
    Log.d("CustomResultsScreen", "Meaningful parts: $meaningfulParts")

    val hasValidPart = meaningfulParts.any { part ->
        normalizedUserAnswer.contains(part)
    }

    if (hasValidPart) {
        Log.d("CustomResultsScreen", "Valid meaningful part found in user answer")
    }

    return hasValidPart
}

private fun isTechnicalAbbreviation(word: String): Boolean {
    val upperWord = word.uppercase()
    return upperWord.matches(Regex("^[A-Z]{2,5}$"))
}

private fun isCombinedName(name: String): Boolean {
    val words = name.split(Regex("\\s+"))
    return words.size >= 2 && (
            words.any { word -> word.contains(Regex("[0-9]")) && word.contains(Regex("[a-z]")) } ||
                    words.any { word -> isTechnicalAbbreviation(word) }
            )
}

private fun isPureTechnicalName(name: String): Boolean {
    val words = name.split(Regex("\\s+"))
    return words.size <= 2 &&
            words.all { word -> word.contains(Regex("[0-9]")) || word.length <= 3 } &&
            words.any { word -> word.contains(Regex("[0-9]")) }
}

private fun validateCombinedName(userAnswer: String, correctAnswer: String): Boolean {
    Log.d("CustomResultsScreen", "Validating combined name: '$correctAnswer'")

    val words = correctAnswer.split(Regex("\\s+"))
    val technicalParts = mutableListOf<String>()
    val regularWords = mutableListOf<String>()

    words.forEach { word ->
        when {
            word.contains(Regex("[0-9]")) -> technicalParts.add(word)
            isTechnicalAbbreviation(word) -> technicalParts.add(word)
            !isStopWord(word) -> regularWords.add(word)
        }
    }

    Log.d("CustomResultsScreen", "Technical parts: $technicalParts, Regular words: $regularWords")

    for (technicalPart in technicalParts) {
        if (technicalPart.contains(Regex("[0-9]"))) {
            val extractedParts = extractTechnicalParts(technicalPart)
            if (extractedParts != null) {
                val (baseName, number) = extractedParts
                val variations = generateTechnicalVariations(baseName, number)

                if (variations.any { normalizeAnswer(it) == userAnswer }) {
                    Log.d("CustomResultsScreen", "Match found with technical variation")
                    return true
                }
            }
        } else {
            if (normalizeAnswer(technicalPart) == userAnswer) {
                Log.d("CustomResultsScreen", "Match found with technical abbreviation: $technicalPart")
                return true
            }
        }
    }

    for (regularWord in regularWords) {
        if (normalizeAnswer(regularWord) == userAnswer) {
            Log.d("CustomResultsScreen", "Match found with regular word: $regularWord")
            return true
        }
    }

    val informalNames = extractInformalNames(regularWords)
    Log.d("CustomResultsScreen", "Informal names: $informalNames")

    for (informalName in informalNames) {
        if (normalizeAnswer(informalName) == userAnswer) {
            Log.d("CustomResultsScreen", "Match found with informal name: $informalName")
            return true
        }
    }

    val fullVariations = generateCombinedVariations(informalNames, technicalParts)
    return fullVariations.any { normalizeAnswer(it) == userAnswer }
}

private fun validatePureTechnicalName(userAnswer: String, correctAnswer: String): Boolean {
    Log.d("CustomResultsScreen", "Validating pure technical name: '$correctAnswer'")

    val technicalParts = extractTechnicalParts(correctAnswer)
    if (technicalParts == null) {
        return false
    }

    val (baseName, number) = technicalParts
    val validVariations = generateTechnicalVariations(baseName, number)

    Log.d("CustomResultsScreen", "Valid technical variations: $validVariations")

    return validVariations.any { variation ->
        normalizeAnswer(variation) == userAnswer
    }
}

private fun extractInformalNames(regularWords: List<String>): List<String> {
    val informalNames = mutableListOf<String>()

    if (regularWords.size >= 2) {
        for (i in 0..regularWords.size - 2) {
            for (j in i + 1..regularWords.size) {
                val phrase = regularWords.subList(i, j).joinToString(" ")
                if (phrase.split(" ").size >= 2) {
                    informalNames.add(phrase)
                }
            }
        }

        if (regularWords.isNotEmpty()) {
            informalNames.add(regularWords.joinToString(" "))
        }
    }

    return informalNames.distinct()
}

private fun generateCombinedVariations(informalNames: List<String>, technicalParts: List<String>): List<String> {
    val variations = mutableListOf<String>()

    for (informalName in informalNames) {
        for (technicalPart in technicalParts) {
            if (technicalPart.contains(Regex("[0-9]"))) {
                val extractedParts = extractTechnicalParts(technicalPart)
                if (extractedParts != null) {
                    val (baseName, number) = extractedParts
                    val techVariations = generateTechnicalVariations(baseName, number)

                    techVariations.forEach { techVar ->
                        variations.add("$techVar $informalName")
                        variations.add("$informalName $techVar")
                    }
                }
            } else {
                variations.add("$technicalPart $informalName")
                variations.add("$informalName $technicalPart")
            }
        }
    }

    return variations
}

private fun extractTechnicalParts(technicalName: String): Pair<String, String>? {
    val patterns = listOf(
        Regex("^([a-z]+)[-\\s]([0-9]+[a-z]*)$"),
        Regex("^([a-z]+)([0-9]+[a-z]*)$"),
        Regex("^([a-z]{1,3})[-\\s]([0-9]+[a-z]*)$"),
        Regex("^([a-z]{1,3})([0-9]+[a-z]*)$")
    )

    for (pattern in patterns) {
        val match = pattern.find(technicalName)
        if (match != null) {
            val baseName = match.groupValues[1]
            val number = match.groupValues[2]
            return Pair(baseName, number)
        }
    }

    return null
}

private fun generateTechnicalVariations(baseName: String, number: String): List<String> {
    return listOf(
        "$baseName-$number",
        "$baseName $number",
        "$baseName$number"
    )
}

private fun extractMeaningfulParts(answer: String): List<String> {
    val parts = mutableListOf<String>()
    val words = answer.split(Regex("\\s+"))

    if (words.size >= 2) {
        for (i in 0..words.size - 2) {
            val compound = "${words[i]} ${words[i + 1]}"
            if (!isOnlyShortWords(compound) && !containsOnlyStopWords(compound)) {
                parts.add(compound)
            }
        }

        if (words.size >= 3) {
            for (i in 0..words.size - 3) {
                val compound = "${words[i]} ${words[i + 1]} ${words[i + 2]}"
                if (!isOnlyShortWords(compound) && !containsOnlyStopWords(compound)) {
                    parts.add(compound)
                }
            }
        }
    }

    if (parts.isEmpty()) {
        val significantWords = words.filter { word ->
            word.length >= 4 && !isStopWord(word)
        }

        if (significantWords.size == 1) {
            parts.add(significantWords[0])
        } else if (words.size == 1 && words[0].length >= 3) {
            parts.add(words[0])
        }
    }

    return parts.distinct()
}

private fun isOnlyShortWords(phrase: String): Boolean {
    val words = phrase.split(Regex("\\s+"))
    return words.all { it.length <= 2 }
}

private fun containsOnlyStopWords(phrase: String): Boolean {
    val words = phrase.split(Regex("\\s+"))
    return words.all { isStopWord(it) }
}

private fun isStopWord(word: String): Boolean {
    val stopWords = setOf("the", "and", "or", "of", "in", "on", "at", "to", "for", "with", "by")
    return stopWords.contains(word.lowercase())
}

private fun normalizeAnswer(answer: String): String {
    return answer.trim()
        .lowercase()
        .replace(Regex("[^a-zA-Z0-9\\s\\-]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}