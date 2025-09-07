package legOS.testidf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.quizapp.Question
import com.example.quizapp.Test_Data
import com.example.quizapp.Air_Data
import com.example.quizapp.Art_Data
import com.example.quizapp.Genie_Data
import com.example.quizapp.Recon_Data
import com.example.quizapp.Test_bm2
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

fun loadScores(context: Context): List<Pair<String, Int>> {
    val file = File(context.getExternalFilesDir(null), "hall_of_fame.txt")
    if (!file.exists()) {
        Log.d("Utils", "Файл hall_of_fame.txt не существует")
        return emptyList()
    }
    return FileInputStream(file).bufferedReader().useLines { lines ->
        lines.mapNotNull { line ->
            val parts = line.trim().split(",")
            if (parts.size == 2) {
                val score = parts[1].toIntOrNull()
                if (score != null && parts[0].isNotBlank()) {
                    parts[0].trim() to score
                } else {
                    Log.w("Utils", "Неверный формат счета или пустое имя в строке: $line")
                    null
                }
            } else {
                Log.w("Utils", "Неверный формат строки: $line")
                null
            }
        }.toList().sortedByDescending { it.second }
    }
}

fun saveScore(context: Context, name: String, score: Int) {
    Log.d("Utils", "Attempting to save score for name: '$name', score: $score")
    val effectiveName = if (name.trim().isBlank()) {
        Log.w("Utils", "Попытка сохранить результат с пустым именем, используя Anonyme")
        "Anonyme"
    } else {
        name.trim()
    }
    Log.d("Utils", "Effective name after trim: '$effectiveName'")
    val file = File(context.getExternalFilesDir(null), "hall_of_fame.txt")
    val currentScores = loadScores(context).toMutableList()
    if (!currentScores.contains(effectiveName to score)) {
        currentScores.add(effectiveName to score)
    }
    val topScores = currentScores.sortedByDescending { it.second }.take(10)
    try {
        FileOutputStream(file).bufferedWriter().use { writer ->
            topScores.forEach { (name, score) ->
                writer.write("$name,$score\n")
            }
        }
        Log.d("Utils", "Score saved successfully for: $effectiveName")
    } catch (e: Exception) {
        Log.e("Utils", "Failed to save score to hall_of_fame.txt: ${e.message}", e)
    }
}

fun loadImageFromAssets(context: Context, fileName: String): Bitmap? {
    return try {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        bitmap
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun clearHallOfFame(context: Context) {
    val file = File(context.getExternalFilesDir(null), "hall_of_fame.txt")
    if (file.exists()) {
        if (file.delete()) {
            Log.d("Utils", "hall_of_fame.txt успешно удалён")
        } else {
            Log.e("Utils", "Не удалось удалить hall_of_fame.txt")
        }
    } else {
        Log.d("Utils", "Файл hall_of_fame.txt не существует, ничего не удалено")
    }
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

fun getImagePath(category: String, question: Question): String {
    val folder = when (question.category ?: category) {
        "tanks" -> "tank_images"
        "artillery" -> "artillery_images"
        "recon" -> "recon_images"
        "genie" -> "genie_images"
        "air" -> "air_images"
        "bm2" -> "bm2_images"
        "final" -> "final_images"
        else -> "tank_images"
    }
    return "$folder/${question.image}"
}

fun buildFinalTestQuestions(): List<Question> {
    val finalQuestions = mutableListOf<Question>()
    finalQuestions.addAll(Test_Data.QUESTION.shuffled().take(10).map { it.copy(category = "tanks") })
    finalQuestions.addAll(Art_Data.QUESTION.shuffled().take(5).map { it.copy(category = "artillery") })
    finalQuestions.addAll(Recon_Data.QUESTION.shuffled().take(15).map { it.copy(category = "recon") })
    finalQuestions.addAll(Genie_Data.QUESTION.shuffled().take(5).map { it.copy(category = "genie") })
    finalQuestions.addAll(Air_Data.QUESTION.shuffled().take(5).map { it.copy(category = "air") })
    finalQuestions.addAll(Test_bm2.QUESTION.shuffled().take(5).map { it.copy(category = "bm2") })

    // Добавляем additionalImages для вопросов "final" с учётом исходной категории
    finalQuestions.filter { it.category == "final" }.forEach { question ->
        val baseImage = question.image
        val additionalImages = when (question.category) {
            "air" -> listOf("air_extra1.jpg", "air_extra2.jpg")
            "artillery" -> listOf("art1_extra.jpg", "art2_extra.jpg")
            "tanks" -> listOf("${baseImage}_extra1.jpg", "${baseImage}_extra2.jpg")
            "recon" -> listOf("${baseImage}_extra1.jpg", "${baseImage}_extra2.jpg")
            "genie" -> listOf("${baseImage}_extra1.jpg", "${baseImage}_extra2.jpg")
            "bm2" -> listOf("${baseImage}_extra1.jpg", "${baseImage}_extra2.jpg")
            else -> listOf("${baseImage}_extra1.jpg", "${baseImage}_extra2.jpg")
        }
        val updatedQuestion = question.copy(additionalImages = additionalImages)
        val index = finalQuestions.indexOf(question)
        if (index != -1) finalQuestions[index] = updatedQuestion
    }

    return finalQuestions.shuffled()
}