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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import legOS.testidf.R
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
                                stringResource(R.string.image_unavailable),
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
                                stringResource(R.string.question_number_short, index + 1),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                stringResource(R.string.category_label, question.category),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )

                            Text(
                                stringResource(R.string.correct_answer, question.correctAnswer),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = Color(0xFF2E7D32)
                            )

                            Text(
                                stringResource(R.string.your_answer_label, userAnswer ?: stringResource(R.string.no_answer)),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                            )

                            Text(
                                if (isCorrect) stringResource(R.string.correct_mark) else stringResource(R.string.incorrect_mark),
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
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.test_completed),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            stringResource(R.string.score, correctCount, totalQuestions),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (correctCount >= totalQuestions * 0.7) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                        )

                        val percentage = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
                        Text(
                            stringResource(R.string.percentage, percentage),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                val categoryStats = results.groupBy { it.first.category }
                if (categoryStats.size > 1) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                stringResource(R.string.by_category),
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
                    modifier = Modifier.fillMaxWidth(0.8f),
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
                        Text(stringResource(R.string.test), style = MaterialTheme.typography.bodyMedium)
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
                        Text(stringResource(R.string.menu), style = MaterialTheme.typography.bodyMedium)
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
            .safeDrawingPadding(),
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
                    stringResource(R.string.custom_test_completed),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    stringResource(R.string.score, correctCount, totalQuestions),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (correctCount >= totalQuestions * 0.7) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )

                val percentage = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
                Text(
                    stringResource(R.string.percentage, percentage),
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
                        stringResource(R.string.results_by_category),
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
                                stringResource(R.string.image_not_available),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            stringResource(R.string.question_with_category, index + 1, question.category),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            stringResource(R.string.correct_answer_label, question.correctAnswer),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = Color(0xFF2E7D32)
                        )

                        Text(
                            stringResource(R.string.your_answer_result, userAnswer ?: stringResource(R.string.no_answer_given)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (isCorrect) stringResource(R.string.correct_mark) else stringResource(R.string.incorrect_mark),
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
                Text(stringResource(R.string.test), style = MaterialTheme.typography.bodyLarge)
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
                Text(stringResource(R.string.menu), style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
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
    Log.d("CustomResultsScreen", "=== Validating pure technical name ===")
    Log.d("CustomResultsScreen", "Correct answer: '$correctAnswer'")
    Log.d("CustomResultsScreen", "User answer: '$userAnswer' (normalized)")

    val technicalParts = extractTechnicalParts(correctAnswer)
    if (technicalParts == null) {
        Log.d("CustomResultsScreen", "Failed to extract technical parts")
        return false
    }

    val (baseName, number) = technicalParts
    Log.d("CustomResultsScreen", "Extracted parts - Base: '$baseName', Number: '$number'")

    val validVariations = generateTechnicalVariations(baseName, number)

    Log.d("CustomResultsScreen", "Generated ${validVariations.size} variations:")
    validVariations.forEachIndexed { index, variation ->
        val normalized = normalizeAnswer(variation)
        val matches = normalized == userAnswer
        Log.d("CustomResultsScreen", "  [$index] '$variation' -> normalized: '$normalized' ${if (matches) "✓ MATCH" else ""}")
    }

    val result = validVariations.any { variation ->
        normalizeAnswer(variation) == userAnswer
    }

    Log.d("CustomResultsScreen", "Result: ${if (result) "✓ VALID" else "✗ INVALID"}")
    Log.d("CustomResultsScreen", "======================================")

    return result
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
    // Normaliser en lowercase pour l'analyse
    val normalized = technicalName.lowercase()

    val patterns = listOf(
        // Formats avec séparateur (tiret ou espace)
        Regex("^([a-z]+)[-\\s]([0-9]+[a-z]*)$"),
        // Formats sans séparateur
        Regex("^([a-z]+)([0-9]+[a-z]*)$"),
        // Formats courts avec séparateur
        Regex("^([a-z]{1,3})[-\\s]([0-9]+[a-z]*)$"),
        // Formats courts sans séparateur
        Regex("^([a-z]{1,3})([0-9]+[a-z]*)$"),
        // Format nombre-lettre-nombre (ex: 2S7)
        Regex("^([0-9]+)([a-z]+)([0-9]+)$"),
        // Format lettre-nombre-lettre (ex: M1A2)
        Regex("^([a-z]+)([0-9]+)([a-z]+)$")
    )

    for (pattern in patterns) {
        val match = pattern.find(normalized)
        if (match != null) {
            val groups = match.groupValues

            // Gestion spéciale pour les formats avec 3 groupes (ex: 2S7, M1A2)
            if (groups.size == 4 && groups[3].isNotEmpty()) {
                // Format: nombre-lettre-nombre (ex: 2S7 -> "2s" + "7")
                if (groups[1][0].isDigit()) {
                    val baseName = groups[1] + groups[2]  // "2s"
                    val number = groups[3]                 // "7"
                    return Pair(baseName, number)
                }
                // Format: lettre-nombre-lettre (ex: M1A2 -> "m" + "1a2")
                else {
                    val baseName = groups[1]               // "m"
                    val number = groups[2] + groups[3]     // "1a2"
                    return Pair(baseName, number)
                }
            }
            // Formats standards avec 2 groupes
            else if (groups.size >= 3) {
                val baseName = groups[1]
                val number = groups[2]
                return Pair(baseName, number)
            }
        }
    }

    return null
}


private fun generateTechnicalVariations(baseName: String, number: String): List<String> {
    val variations = mutableListOf<String>()

    // Générer les variations de casse pour baseName
    val baseNameVariations = listOf(
        baseName.lowercase(),           // "s" ou "pion"
        baseName.uppercase(),           // "S" ou "PION"
        baseName.capitalize()           // "S" ou "Pion"
    ).distinct()

    // Générer les variations de casse pour number (si contient des lettres)
    val numberVariations = if (number.any { it.isLetter() }) {
        listOf(
            number.lowercase(),         // "2s7"
            number.uppercase(),         // "2S7"
            number.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } // "2S7"
        ).distinct()
    } else {
        listOf(number) // Juste le nombre si pas de lettres
    }

    // Combiner toutes les variations avec différents séparateurs
    for (baseVar in baseNameVariations) {
        for (numVar in numberVariations) {
            variations.add("$baseVar-$numVar")   // "s-2s7", "S-2S7", etc.
            variations.add("$baseVar $numVar")   // "s 2s7", "S 2S7", etc.
            variations.add("$baseVar$numVar")    // "s2s7", "S2S7", etc.
        }
    }

    // Ajouter aussi les variations nombre-base (inversé)
    for (numVar in numberVariations) {
        for (baseVar in baseNameVariations) {
            variations.add("$numVar-$baseVar")   // "2s7-pion"
            variations.add("$numVar $baseVar")   // "2s7 pion"
            variations.add("$numVar$baseVar")    // "2s7pion"
        }
    }

    return variations.distinct()
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