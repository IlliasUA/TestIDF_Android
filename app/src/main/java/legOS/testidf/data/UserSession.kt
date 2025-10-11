package legOS.testidf.data

import android.util.Log

/**
 * Singleton для хранения данных текущей сессии пользователя
 * Используется для хранения информации о Chef после регистрации/входа
 */
object UserSession {

    // Данные пользователя
    var userId: String? = null


    var groupId: String? = null


    var userName: String? = null



    var userRole: String? = null


    private var groupCode: String? = null

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
            - Role: $userRole
            - Group ID: $groupId
        """.trimIndent()
    }
    fun clearGroupData() {
        groupId = null
        groupCode = null
        Log.d("UserSession", "Group data cleared")
    }

    fun clearAll() {
        userId = null
        userName = null
        userRole = null
        groupId = null
        groupCode = null
        Log.d("UserSession", "All session data cleared")
    }
}