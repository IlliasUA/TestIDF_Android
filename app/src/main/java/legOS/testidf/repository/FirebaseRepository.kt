package legOS.testidf.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import legOS.testidf.screens.CreationItem
import java.util.UUID

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Создает тестовую сессию из выбранных элементов CreationScreen
     */
    suspend fun createTestSessionFromCreation(
        adminId: String,
        groupId: String,
        title: String,
        selectedItems: List<CreationItem>,
        timeLimit: Int
    ): Result<String> {
        return try {
            Log.d("FirebaseRepo", "=== START createTestSessionFromCreation ===")

            val sessionId = UUID.randomUUID().toString()
            Log.d("FirebaseRepo", "Generated sessionId: $sessionId")

            // Преобразуем CreationItem в легковесные ссылки
            val questionRefs = selectedItems.map { item ->
                hashMapOf(
                    "name" to item.name,
                    "category" to item.category,
                    "imagePath" to item.mainImage
                )
            }
            Log.d("FirebaseRepo", "Created ${questionRefs.size} question refs")

            // Получаем участников группы
            Log.d("FirebaseRepo", "Fetching group: $groupId")
            val groupDoc = firestore.collection("groups")
                .document(groupId)
                .get()
                .await()

            val participantIds = groupDoc.get("participantIds") as? List<String> ?: emptyList()
            Log.d("FirebaseRepo", "Found ${participantIds.size} participants")

            // Создаем документ сессии
            val sessionData = hashMapOf(
                "sessionId" to sessionId,
                "groupId" to groupId,
                "adminId" to adminId,
                "title" to title,
                "questionRefs" to questionRefs,
                "participantIds" to participantIds,
                "status" to "pending",
                "timeLimit" to timeLimit,
                "createdAt" to Timestamp.now()
            )

            Log.d("FirebaseRepo", "Writing to Firestore...")
            firestore.collection("test_sessions")
                .document(sessionId)
                .set(sessionData)
                .await()

            Log.d("FirebaseRepo", "✅ Session created successfully")
            Log.d("FirebaseRepo", "=== END createTestSessionFromCreation ===")

            Result.success(sessionId)

        } catch (e: Exception) {
            Log.e("FirebaseRepo", "❌ Error creating session", e)
            Log.e("FirebaseRepo", "Error message: ${e.message}")
            Log.e("FirebaseRepo", "Error stack: ${e.stackTraceToString()}")
            Result.failure(e)
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
                "groupCode" to groupCode,  // ДОБАВЛЕНО
                "participantIds" to emptyList<String>(),
                "createdAt" to Timestamp.now()
            )

            firestore.collection("groups")
                .document(groupId)
                .set(groupData)
                .await()

            Log.d("FirebaseRepo", "Group created with code: $groupCode")
            Result.success(Pair(groupId, groupCode))  // Возвращаем и ID, и код

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