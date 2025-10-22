package legOS.testidf.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import legOS.testidf.screens.CreationItem
import legOS.testidf.getRandomImageForQuestion
import com.example.quizapp.*
import java.util.UUID

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Создает тестовую сессию из выбранных элементов CreationScreen
     * с случайным выбором изображений для каждого вопроса
     */
    suspend fun createTestSessionFromCreation(
        adminId: String,
        groupId: String,
        title: String,
        selectedItems: List<CreationItem>,
        timeLimit: Int
    ): Result<String> {
        return try {
            Log.d("FirebaseRepo", "========================================")
            Log.d("FirebaseRepo", "🚀 START createTestSessionFromCreation")
            Log.d("FirebaseRepo", "Admin ID: $adminId")
            Log.d("FirebaseRepo", "Group ID: $groupId")
            Log.d("FirebaseRepo", "Title: $title")
            Log.d("FirebaseRepo", "Selected items: ${selectedItems.size}")
            Log.d("FirebaseRepo", "Time limit: $timeLimit seconds")

            val sessionId = UUID.randomUUID().toString()
            Log.d("FirebaseRepo", "Generated sessionId: $sessionId")

            // Преобразуем CreationItem в questionRefs с СЛУЧАЙНЫМ выбором изображений
            val questionRefs = selectedItems.mapNotNull { item ->
                Log.d("FirebaseRepo", "📝 Processing item: ${item.name} (${item.category})")

                // Находим оригинальный Question из базы данных
                val question = findQuestionByNameAndCategory(item.name, item.category)

                if (question == null) {
                    Log.e("FirebaseRepo", "⚠️ Question not found: ${item.name} in ${item.category}")
                    return@mapNotNull null
                }

                // 🎲 КЛЮЧЕВАЯ ЛОГИКА: Выбираем случайное изображение
                // Функция getRandomImageForQuestion автоматически:
                // 1. Берет все доступные изображения (image + additionalImages)
                // 2. Исключает запрещенные изображения из EXCLUDED_IMAGES
                // 3. Возвращает случайное из оставшихся
                val selectedImage = getRandomImageForQuestion(question)

                Log.d("FirebaseRepo", "✅ Selected random image for ${item.name}: $selectedImage")
                Log.d("FirebaseRepo", "   Available images: ${question.image}${if (!question.additionalImages.isNullOrEmpty()) " + ${question.additionalImages.size} extra" else ""}")

                // Возвращаем данные для сохранения на сервер
                hashMapOf(
                    "name" to question.correct,
                    "category" to item.category,
                    "imagePath" to selectedImage  // 🎯 Сохраняем выбранное случайное изображение
                )
            }

            if (questionRefs.isEmpty()) {
                Log.e("FirebaseRepo", "❌ No valid questions found after processing")
                return Result.failure(Exception("No valid questions found"))
            }

            Log.d("FirebaseRepo", "✅ Prepared ${questionRefs.size} question references with random images")

            // Получаем участников группы
            Log.d("FirebaseRepo", "📋 Fetching group participants: $groupId")
            val groupDoc = firestore.collection("groups")
                .document(groupId)
                .get()
                .await()

            val participantIds = groupDoc.get("participantIds") as? List<String> ?: emptyList()
            Log.d("FirebaseRepo", "👥 Found ${participantIds.size} participants")

            // Создаем документ сессии
            val sessionData = hashMapOf(
                "sessionId" to sessionId,
                "groupId" to groupId,
                "adminId" to adminId,
                "title" to title,
                "questionRefs" to questionRefs,  // 📦 Сохраняем со случайными изображениями
                "participantIds" to participantIds,
                "status" to "ready",
                "timeLimit" to timeLimit,
                "createdAt" to Timestamp.now()
            )

            Log.d("FirebaseRepo", "💾 Writing session to Firestore...")
            Log.d("FirebaseRepo", "Question refs preview:")
            questionRefs.take(3).forEachIndexed { index, ref ->
                Log.d("FirebaseRepo", "  $index. ${ref["name"]} → ${ref["imagePath"]}")
            }
            if (questionRefs.size > 3) {
                Log.d("FirebaseRepo", "  ... and ${questionRefs.size - 3} more")
            }

            firestore.collection("test_sessions")
                .document(sessionId)
                .set(sessionData)
                .await()

            Log.d("FirebaseRepo", "✅ Session created successfully!")
            Log.d("FirebaseRepo", "Session ID: $sessionId")
            Log.d("FirebaseRepo", "All participants will see the SAME randomly selected images")
            Log.d("FirebaseRepo", "========================================")

            Result.success(sessionId)

        } catch (e: Exception) {
            Log.e("FirebaseRepo", "❌ Error creating session", e)
            Log.e("FirebaseRepo", "Error message: ${e.message}")
            Log.e("FirebaseRepo", "Error stack: ${e.stackTraceToString()}")
            Result.failure(e)
        }
    }

    /**
     * Находит вопрос по имени и категории в локальных данных
     */
    private fun findQuestionByNameAndCategory(name: String, category: String): Question? {
        return when (category) {
            "Chars" -> Test_Data.QUESTION.find { it.correct == name }
            "Artillerie" -> Art_Data.QUESTION.find { it.correct == name }
            "Aviation" -> Air_Data.QUESTION.find { it.correct == name }
            "Génie" -> Genie_Data.QUESTION.find { it.correct == name }
            "Reconnaissance" -> Recon_Data.QUESTION.find { it.correct == name }
            "Militaire" -> Test_bm2.QUESTION.find { it.correct == name }
            else -> {
                Log.e("FirebaseRepo", "❌ Unknown category: $category")
                null
            }
        }
    }

    /**
     * Регистрация администратора
     */
    suspend fun signUpAsAdmin(email: String, password: String, name: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("User ID is null")

            val user = User(
                userId = userId,
                email = email,
                name = name,
                role = UserRole.ADMIN
            )

            firestore.collection("users")
                .document(userId)
                .set(user)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Создание группы
     */
    suspend fun createGroup(adminId: String, groupName: String): Result<Pair<String, String>> {
        return try {
            val groupId = UUID.randomUUID().toString()

            // Генерируем 6-значный код группы
            val groupCode = generateGroupCode()

            val groupData = hashMapOf(
                "groupId" to groupId,
                "adminId" to adminId,
                "name" to groupName,
                "groupCode" to groupCode,
                "participantIds" to emptyList<String>(),
                "createdAt" to Timestamp.now()
            )

            firestore.collection("groups")
                .document(groupId)
                .set(groupData)
                .await()

            Log.d("FirebaseRepo", "Group created with code: $groupCode")
            Result.success(Pair(groupId, groupCode))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateGroupCode(): String {
        // Генерирует 6-значный буквенно-цифровой код
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // Без похожих символов (I, O, 0, 1)
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }

    // Модели данных
    data class User(
        val userId: String = "",
        val email: String = "",
        val name: String = "",
        val role: UserRole = UserRole.PARTICIPANT
    )

    enum class UserRole {
        ADMIN,
        PARTICIPANT
    }
}