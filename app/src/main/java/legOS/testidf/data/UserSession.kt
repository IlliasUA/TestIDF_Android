package legOS.testidf.data

import android.util.Log

/**
 * Singleton для хранения данных текущей сессии пользователя
 * Используется для хранения информации о Chef после регистрации/входа
 */
object UserSession {

    // Данные пользователя
    var userId: String? = null
        private set

    var groupId: String? = null
        private set

    var userName: String? = null
        private set

    var userEmail: String? = null
        private set

    var userRole: String? = null
        private set

    /**
     * Устанавливает сессию администратора (Chef)
     */
    fun setAdminSession(
        userId: String?,
        groupId: String?,
        name: String,
        email: String
    ) {
        this.userId = userId
        this.groupId = groupId
        this.userName = name
        this.userEmail = email
        this.userRole = "admin"

        Log.d("UserSession", "Admin session set: $name (Group: $groupId)")
    }

    /**
     * Устанавливает сессию участника
     */
    fun setParticipantSession(
        userId: String,
        name: String,
        email: String
    ) {
        this.userId = userId
        this.userName = name
        this.userEmail = email
        this.userRole = "participant"
        this.groupId = null // Участник может быть в нескольких группах

        Log.d("UserSession", "Participant session set: $name")
    }

    /**
     * Очищает сессию при выходе
     */
    fun clearSession() {
        userId = null
        groupId = null
        userName = null
        userEmail = null
        userRole = null

        Log.d("UserSession", "Session cleared")
    }

    /**
     * Проверяет, является ли пользователь администратором
     */
    fun isAdmin(): Boolean = userRole == "admin"

    /**
     * Проверяет, авторизован ли пользователь
     */
    fun isLoggedIn(): Boolean = userId != null

    /**
     * Возвращает информацию о сессии для логирования
     */
    fun getSessionInfo(): String {
        return """
            UserSession Info:
            - User ID: $userId
            - Name: $userName
            - Email: $userEmail
            - Role: $userRole
            - Group ID: $groupId
        """.trimIndent()
    }
}