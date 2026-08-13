package legOS.testidf.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

/**
 * Singleton для хранения данных текущей сессии пользователя
 * Используется для хранения информации о Chef после регистрации/входа
 */
object UserSession {

    private const val PREFS_NAME = "user_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_GROUP_ID = "group_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_GROUP_CODE = "group_code"

    private var preferences: SharedPreferences? = null

    // Данные пользователя
    var userId: String? = null
        private set

    var groupId: String? = null
        private set

    var userName: String? = null
        private set

    var userRole: String? = null
        private set


    private var groupCode: String? = null

    /** Restores the last known session after Android recreates the app process. */
    fun initialize(context: Context) {
        preferences = context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        preferences?.let { prefs ->
            userId = prefs.getString(KEY_USER_ID, null)
            groupId = prefs.getString(KEY_GROUP_ID, null)
            userName = prefs.getString(KEY_USER_NAME, null)
            userRole = prefs.getString(KEY_USER_ROLE, null)
            groupCode = prefs.getString(KEY_GROUP_CODE, null)
        }

        Log.d("UserSession", "Session restored: role=$userRole, group=$groupId")
    }

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
        persist()

        Log.d("UserSession", "Admin session set: $name (Group: $groupId)")
    }

    /**
     * Устанавливает сессию участника
     */
    fun setParticipantSession(
        userId: String,
        name: String,
        email: String,
        groupId: String? = null
    ) {
        this.userId = userId
        this.userName = name
        this.userRole = "participant"
        this.groupId = groupId
        persist()

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
        groupCode = null
        persist()

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
        persist()
        Log.d("UserSession", "Group data cleared")
    }

    fun clearAll() {
        userId = null
        userName = null
        userRole = null
        groupId = null
        groupCode = null
        persist()
        Log.d("UserSession", "All session data cleared")
    }

    private fun persist() {
        val prefs = preferences
        if (prefs == null) {
            Log.w("UserSession", "Session storage is not initialized")
            return
        }

        prefs.edit {
            putString(KEY_USER_ID, userId)
            putString(KEY_GROUP_ID, groupId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_ROLE, userRole)
            putString(KEY_GROUP_CODE, groupCode)
        }
    }
}
