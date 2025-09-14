package legOS.testidf.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import legOS.testidf.loadImageFromAssets

@Composable
fun CustomResultsScreen(navController: NavController, questionCount: String, timeLimit: String) {
    val context = LocalContext.current

    // Получаем данные из предыдущего экрана
    val customQuestions = navController.previousBackStackEntry?.savedStateHandle?.get<List<CustomTestQuestion>>("customQuestions")
        ?: emptyList()
    val customAnswers = navController.previousBackStackEntry?.savedStateHandle?.get<List<String?>>("customAnswers")
        ?: emptyList()

    Log.d("CustomResultsScreen", "Questions: ${customQuestions.size}, Answers: ${customAnswers.size}")

    // Вычисляем правильные ответы
    val results = customQuestions.zip(customAnswers) { question, answer ->
        val isCorrect = isAnswerCorrect(answer, question.correctAnswer)
        Triple(question, answer, isCorrect)
    }

    val correctCount = results.count { it.third }
    val totalQuestions = customQuestions.size

    Log.d("CustomResultsScreen", "Correct answers: $correctCount/$totalQuestions")

    // Загружаем фоновое изображение
    val backgroundImage = loadImageFromAssets(context, "images/background_3.jpg")

    Column(
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Заголовок результатов
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

        // Статистика по категориям
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

        // Список результатов
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
                        // Изображение
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
                        } ?: run {
                            Box(
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
                        }

                        // Информация о вопросе
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

                        // Статус ответа
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

        // Кнопки действий
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
                Text("Nouveau Test", style = MaterialTheme.typography.bodyLarge)
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
                Text("Menu Principal", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// Функция для сравнения ответов с поддержкой осмысленного частичного совпадения
private fun isAnswerCorrect(userAnswer: String?, correctAnswer: String): Boolean {
    if (userAnswer.isNullOrBlank()) return false

    val normalizedUserAnswer = normalizeAnswer(userAnswer)
    val normalizedCorrectAnswer = normalizeAnswer(correctAnswer)

    Log.d("CustomResultsScreen", "Comparing: '$normalizedUserAnswer' vs '$normalizedCorrectAnswer'")

    // Точное совпадение
    if (normalizedUserAnswer == normalizedCorrectAnswer) {
        Log.d("CustomResultsScreen", "Exact match found")
        return true
    }

    // Проверяем комбинированные названия (например, "Apache AH-64", "JLTV Falcon")
    if (isCombinedName(normalizedCorrectAnswer)) {
        return validateCombinedName(normalizedUserAnswer, normalizedCorrectAnswer)
    }

    // Специальная логика только для чисто технических названий (например, "Leopard-1")
    if (isPureTechnicalName(normalizedCorrectAnswer)) {
        return validatePureTechnicalName(normalizedUserAnswer, normalizedCorrectAnswer)
    }

    // Для обычных названий используем общую логику
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

// Проверяет, является ли слово технической аббревиатурой
private fun isTechnicalAbbreviation(word: String): Boolean {
    // Техническая аббревиатура: 2-5 заглавных букв после нормализации
    val upperWord = word.uppercase()
    return upperWord.matches(Regex("^[A-Z]{2,5}$"))
}

// Проверяет, является ли название комбинированным
private fun isCombinedName(name: String): Boolean {
    val words = name.split(Regex("\\s+"))
    return words.size >= 2 && (
            // Случай с цифрами: "UH-60 Black Hawk"
            words.any { word -> word.contains(Regex("[0-9]")) && word.contains(Regex("[a-z]")) } ||
                    // Случай с техническими аббревиатурами: "URO Vamtac", "JLTV Falcon"
                    words.any { word -> isTechnicalAbbreviation(word) }
            )
}

// Проверяет, является ли название чисто техническим (только техническое обозначение)
private fun isPureTechnicalName(name: String): Boolean {
    val words = name.split(Regex("\\s+"))
    // Чисто техническое название - это одно слово с цифрами, или очень короткие слова с цифрами
    return words.size <= 2 &&
            words.all { word -> word.contains(Regex("[0-9]")) || word.length <= 3 } &&
            words.any { word -> word.contains(Regex("[0-9]")) }
}

// Валидация для комбинированных названий (например, "UH-60 Black Hawk", "JLTV Falcon")
private fun validateCombinedName(userAnswer: String, correctAnswer: String): Boolean {
    Log.d("CustomResultsScreen", "Validating combined name: '$correctAnswer'")

    val words = correctAnswer.split(Regex("\\s+"))

    // Извлекаем технические части и обычные слова
    val technicalParts = mutableListOf<String>()
    val regularWords = mutableListOf<String>()

    words.forEach { word ->
        when {
            // Технические обозначения с цифрами: UH-60, AH-64
            word.contains(Regex("[0-9]")) -> technicalParts.add(word)
            // Технические аббревиатуры: URO, JLTV
            isTechnicalAbbreviation(word) -> technicalParts.add(word)
            // Обычные слова: Black, Hawk, Vamtac, Falcon
            !isStopWord(word) -> regularWords.add(word)
        }
    }

    Log.d("CustomResultsScreen", "Technical parts: $technicalParts, Regular words: $regularWords")

    // 1. Проверяем технические части
    for (technicalPart in technicalParts) {
        // Для технических обозначений с цифрами
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
            // Для технических аббревиатур (URO, JLTV)
            if (normalizeAnswer(technicalPart) == userAnswer) {
                Log.d("CustomResultsScreen", "Match found with technical abbreviation: $technicalPart")
                return true
            }
        }
    }

    // 2. Проверяем отдельные слова из названий/моделей
    for (regularWord in regularWords) {
        if (normalizeAnswer(regularWord) == userAnswer) {
            Log.d("CustomResultsScreen", "Match found with regular word: $regularWord")
            return true
        }
    }

    // 3. Проверяем неофициальные названия (составные фразы из обычных слов)
    val informalNames = extractInformalNames(regularWords)
    Log.d("CustomResultsScreen", "Informal names: $informalNames")

    for (informalName in informalNames) {
        if (normalizeAnswer(informalName) == userAnswer) {
            Log.d("CustomResultsScreen", "Match found with informal name: $informalName")
            return true
        }
    }

    // 4. Проверяем полные комбинации
    val fullVariations = generateCombinedVariations(informalNames, technicalParts)
    return fullVariations.any { normalizeAnswer(it) == userAnswer }
}

// Валидация только для чисто технических названий (например, "Leopard-1")
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

// Извлекает неофициальные названия из обычных слов
private fun extractInformalNames(regularWords: List<String>): List<String> {
    val informalNames = mutableListOf<String>()

    // Если есть 2 или более значимых слова, они могут составлять неофициальное название
    if (regularWords.size >= 2) {
        // Проверяем все возможные комбинации из 2+ слов
        for (i in 0..regularWords.size - 2) {
            for (j in i + 1..regularWords.size) {
                val phrase = regularWords.subList(i, j + 1).joinToString(" ")
                if (phrase.split(" ").size >= 2) {
                    informalNames.add(phrase)
                }
            }
        }

        // Добавляем полную фразу из всех обычных слов
        if (regularWords.isNotEmpty()) {
            informalNames.add(regularWords.joinToString(" "))
        }
    }

    return informalNames.distinct()
}

// Генерирует вариации для комбинированных названий
private fun generateCombinedVariations(informalNames: List<String>, technicalParts: List<String>): List<String> {
    val variations = mutableListOf<String>()

    // Полные комбинации
    for (informalName in informalNames) {
        for (technicalPart in technicalParts) {
            if (technicalPart.contains(Regex("[0-9]"))) {
                // Для технических обозначений с цифрами
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
                // Для технических аббревиатур
                variations.add("$technicalPart $informalName")
                variations.add("$informalName $technicalPart")
            }
        }
    }

    return variations
}

// Извлекает базовое название и номер из технического названия
private fun extractTechnicalParts(technicalName: String): Pair<String, String>? {
    // Паттерны для различных форматов: "leopard-1", "leopard 1", "f-16", "ah-64", "t-80"
    val patterns = listOf(
        Regex("^([a-z]+)[-\\s]([0-9]+[a-z]*)$"), // leopard-1, f-16a
        Regex("^([a-z]+)([0-9]+[a-z]*)$"),        // leopard1, f16a
        Regex("^([a-z]{1,3})[-\\s]([0-9]+[a-z]*)$"), // ah-64, t-80
        Regex("^([a-z]{1,3})([0-9]+[a-z]*)$")         // ah64, t80
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

// Генерирует допустимые вариации технического названия
private fun generateTechnicalVariations(baseName: String, number: String): List<String> {
    return listOf(
        "$baseName-$number",     // leopard-1
        "$baseName $number",     // leopard 1
        "$baseName$number"       // leopard1
    )
}

// Улучшенная функция для извлечения осмысленных частей (для нетехнических названий)
private fun extractMeaningfulParts(answer: String): List<String> {
    val parts = mutableListOf<String>()
    val words = answer.split(Regex("\\s+"))

    // Составные словосочетания из 2+ слов (например: "little bird", "black hawk")
    if (words.size >= 2) {
        for (i in 0..words.size - 2) {
            val compound = "${words[i]} ${words[i + 1]}"
            if (!isOnlyShortWords(compound) && !containsOnlyStopWords(compound)) {
                parts.add(compound)
            }
        }

        // Для длинных названий проверяем тройки слов
        if (words.size >= 3) {
            for (i in 0..words.size - 3) {
                val compound = "${words[i]} ${words[i + 1]} ${words[i + 2]}"
                if (!isOnlyShortWords(compound) && !containsOnlyStopWords(compound)) {
                    parts.add(compound)
                }
            }
        }
    }

    // Отдельные значимые слова (только если нет составных частей)
    if (parts.isEmpty()) {
        val significantWords = words.filter { word ->
            word.length >= 4 && !isStopWord(word)
        }

        if (significantWords.size == 1) {
            parts.add(significantWords[0])
        } else if (words.size == 1 && words[0].length >= 3) {
            // Для коротких одиночных слов
            parts.add(words[0])
        }
    }

    return parts.distinct()
}

// Проверяет, состоит ли фраза только из коротких слов
private fun isOnlyShortWords(phrase: String): Boolean {
    val words = phrase.split(Regex("\\s+"))
    return words.all { it.length <= 2 }
}

// Проверяет, состоит ли фраза только из служебных слов
private fun containsOnlyStopWords(phrase: String): Boolean {
    val words = phrase.split(Regex("\\s+"))
    return words.all { isStopWord(it) }
}

// Проверяет, является ли слово служебным
private fun isStopWord(word: String): Boolean {
    val stopWords = setOf("the", "and", "or", "of", "in", "on", "at", "to", "for", "with", "by")
    return stopWords.contains(word.lowercase())
}

// Функция для нормализации ответов
private fun normalizeAnswer(answer: String): String {
    return answer.trim()
        .lowercase()
        .replace(Regex("[^a-zA-Z0-9\\s\\-]"), " ") // Сохраняем дефисы для технических названий
        .replace(Regex("\\s+"), " ") // Заменяем множественные пробелы одним
        .trim()
}