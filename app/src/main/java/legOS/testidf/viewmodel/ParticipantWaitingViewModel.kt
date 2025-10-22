package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession

data class AvailableTest(
    val sessionId: String = "",
    val title: String = "",
    val questionCount: Int = 0,
    val timeLimit: Int = 0
)

data class TestResult(
    val sessionId: String = "",
    val sessionTitle: String = "",
    val participantId: String = "",
    val participantName: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val percentage: Double = 0.0,
    val completedAt: com.google.firebase.Timestamp? = null
)

data class ParticipantWaitingUiState(
    val availableTests: List<AvailableTest> = emptyList(),
    val testResults: List<TestResult> = emptyList(),
    val isLoadingResults: Boolean = false,
    val isGroupActive: Boolean = true,
    val groupClosedMessage: String? = null
)

class ParticipantWaitingViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private var notificationListener: ListenerRegistration? = null
    private var groupListener: ListenerRegistration? = null
    private var groupClosureListener: ListenerRegistration? = null
    private var resultsListener: ListenerRegistration? = null

    private val _uiState = MutableStateFlow(ParticipantWaitingUiState())
    val uiState: StateFlow<ParticipantWaitingUiState> = _uiState

    fun startListeningForTests(userId: String) {
        Log.d("ParticipantWaitingVM", "========================================")
        Log.d("ParticipantWaitingVM", "📬 Setting up test listener for user: $userId")

        notificationListener?.remove()

        notificationListener = firestore.collection("notifications")
            .whereEqualTo("recipientId", userId)
            .whereEqualTo("isRead", false)
            .whereEqualTo("isActive", true)
            .whereEqualTo("type", "test_invitation")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ParticipantWaitingVM", "❌ Listen failed", error)
                    return@addSnapshotListener
                }

                Log.d("ParticipantWaitingVM", "📬 Notification snapshot received")

                if (snapshot != null) {
                    Log.d("ParticipantWaitingVM", "Snapshot size: ${snapshot.size()}")

                    if (snapshot.isEmpty) {
                        Log.d("ParticipantWaitingVM", "⚠️ No active notifications found")
                        _uiState.value = _uiState.value.copy(availableTests = emptyList())
                        return@addSnapshotListener
                    }

                    viewModelScope.launch {
                        val tests = mutableListOf<AvailableTest>()

                        snapshot.documents.forEach { doc ->
                            val sessionId = doc.getString("sessionId")
                            val isActive = doc.getBoolean("isActive") ?: false

                            Log.d("ParticipantWaitingVM", "Processing notification ${doc.id}:")
                            Log.d("ParticipantWaitingVM", "  - sessionId: $sessionId")
                            Log.d("ParticipantWaitingVM", "  - isActive: $isActive")

                            if (sessionId != null) {
                                try {
                                    val sessionDoc = firestore.collection("test_sessions")
                                        .document(sessionId)
                                        .get()
                                        .await()

                                    if (sessionDoc.exists()) {
                                        val title = sessionDoc.getString("title") ?: "Test"
                                        val questionRefs = sessionDoc.get("questionRefs") as? List<*> ?: emptyList<Any>()
                                        val timeLimit = (sessionDoc.getLong("timeLimit") ?: 15).toInt()

                                        tests.add(
                                            AvailableTest(
                                                sessionId = sessionId,
                                                title = title,
                                                questionCount = questionRefs.size,
                                                timeLimit = timeLimit
                                            )
                                        )

                                        Log.d("ParticipantWaitingVM", "✅ Test loaded: $title")
                                    } else {
                                        Log.w("ParticipantWaitingVM", "⚠️ Session $sessionId not found")
                                    }
                                } catch (e: Exception) {
                                    Log.e("ParticipantWaitingVM", "❌ Error loading session $sessionId", e)
                                }
                            }
                        }

                        Log.d("ParticipantWaitingVM", "✅ Total tests available: ${tests.size}")
                        Log.d("ParticipantWaitingVM", "========================================")

                        _uiState.value = _uiState.value.copy(availableTests = tests)
                    }
                }
            }
    }

    fun startListeningForGroupStatus() {
        val groupId = UserSession.groupId
        val userId = UserSession.userId

        Log.d("ParticipantWaitingVM", "==============================================")
        Log.d("ParticipantWaitingVM", "👁️ startListeningForGroupStatus CALLED")
        Log.d("ParticipantWaitingVM", "User ID: $userId")
        Log.d("ParticipantWaitingVM", "Group ID: $groupId")

        if (groupId == null) {
            Log.e("ParticipantWaitingVM", "❌ groupId is NULL - cannot start listener")
            return
        }

        if (userId == null) {
            Log.e("ParticipantWaitingVM", "❌ userId is NULL - cannot start listener")
            return
        }

        Log.d("ParticipantWaitingVM", "✅ Starting listeners...")

        // Слушаем существование группы И её статус isActive
        groupListener = firestore.collection("groups")
            .document(groupId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ParticipantWaitingVM", "❌ Error listening to group", error)
                    return@addSnapshotListener
                }

                Log.d("ParticipantWaitingVM", "📬 Group snapshot received")
                Log.d("ParticipantWaitingVM", "Snapshot exists: ${snapshot?.exists()}")

                if (snapshot == null || !snapshot.exists()) {
                    Log.w("ParticipantWaitingVM", "⚠️⚠️⚠️ GROUP DELETED!")

                    _uiState.value = _uiState.value.copy(
                        isGroupActive = false,
                        groupClosedMessage = "Le chef a quitté la session"
                    )

                    Log.d("ParticipantWaitingVM", "✅ UI State updated: isGroupActive = false")
                } else {
                    // Проверяем поле isActive
                    val isActive = snapshot.getBoolean("isActive") ?: true

                    Log.d("ParticipantWaitingVM", "Group isActive field: $isActive")

                    if (!isActive) {
                        Log.w("ParticipantWaitingVM", "⚠️⚠️⚠️ GROUP MARKED AS INACTIVE!")

                        _uiState.value = _uiState.value.copy(
                            isGroupActive = false,
                            groupClosedMessage = "Le chef a quitté la session"
                        )

                        Log.d("ParticipantWaitingVM", "✅ UI State updated: isGroupActive = false")
                    } else {
                        Log.d("ParticipantWaitingVM", "✅ Group is active")
                    }
                }
            }

        // Слушаем уведомления о закрытии группы
        Log.d("ParticipantWaitingVM", "Setting up closure notification listener...")

        groupClosureListener = firestore.collection("notifications")
            .whereEqualTo("recipientId", userId)
            .whereEqualTo("type", "GROUP_CLOSED")
            .whereEqualTo("isRead", false)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.e("ParticipantWaitingVM", "❌ Error listening to group closure notifications", error)
                    return@addSnapshotListener
                }

                Log.d("ParticipantWaitingVM", "📬 Closure notification snapshot received")
                Log.d("ParticipantWaitingVM", "Notifications count: ${snapshots?.size() ?: 0}")

                if (snapshots != null && !snapshots.isEmpty) {
                    Log.d("ParticipantWaitingVM", "⚠️⚠️⚠️ FOUND ${snapshots.size()} CLOSURE NOTIFICATIONS!")

                    snapshots.documents.forEach { doc ->
                        val message = doc.getString("message") ?: "Le groupe n'est plus actif"

                        _uiState.value = _uiState.value.copy(
                            isGroupActive = false,
                            groupClosedMessage = message
                        )

                        // Отмечаем уведомление как прочитанное
                        viewModelScope.launch {
                            try {
                                doc.reference.update("isRead", true).await()
                                Log.d("ParticipantWaitingVM", "✅ Notification marked as read")
                            } catch (e: Exception) {
                                Log.e("ParticipantWaitingVM", "❌ Error marking notification as read", e)
                            }
                        }
                    }
                }
            }

        Log.d("ParticipantWaitingVM", "✅ Listeners setup completed")
        Log.d("ParticipantWaitingVM", "==============================================")
    }

    // ОБНОВЛЕННАЯ ФУНКЦИЯ: Загрузка только последних результатов
    fun startListeningForResults() {
        val groupId = UserSession.groupId

        Log.d("ParticipantWaitingVM", "========================================")
        Log.d("ParticipantWaitingVM", "📊 Setting up results listener")
        Log.d("ParticipantWaitingVM", "Group ID: $groupId")

        if (groupId == null) {
            Log.e("ParticipantWaitingVM", "❌ groupId is NULL - cannot load results")
            return
        }

        _uiState.value = _uiState.value.copy(isLoadingResults = true)

        resultsListener?.remove()

        // Загружаем все результаты, затем фильтруем только последнюю сессию
        resultsListener = firestore.collection("test_results")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ParticipantWaitingVM", "❌ Error listening to results", error)
                    _uiState.value = _uiState.value.copy(isLoadingResults = false)
                    return@addSnapshotListener
                }

                Log.d("ParticipantWaitingVM", "📊 All results snapshot received")
                Log.d("ParticipantWaitingVM", "Total results: ${snapshot?.size() ?: 0}")

                if (snapshot != null && !snapshot.isEmpty) {
                    viewModelScope.launch {
                        val results = mutableListOf<TestResult>()
                        var latestSessionTimestamp: com.google.firebase.Timestamp? = null
                        var latestSessionId: String? = null

                        // ШАГ 1: Найти последнюю сессию группы
                        for (doc in snapshot.documents) {
                            val sessionId = doc.getString("sessionId") ?: continue
                            val completedAt = doc.getTimestamp("completedAt") ?: continue

                            try {
                                val sessionDoc = firestore.collection("test_sessions")
                                    .document(sessionId)
                                    .get()
                                    .await()

                                if (!sessionDoc.exists()) continue

                                val sessionGroupId = sessionDoc.getString("groupId")

                                // Проверяем принадлежность к нашей группе
                                if (sessionGroupId == groupId) {
                                    // Находим последнюю сессию
                                    if (latestSessionTimestamp == null || completedAt > latestSessionTimestamp) {
                                        latestSessionTimestamp = completedAt
                                        latestSessionId = sessionId
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("ParticipantWaitingVM", "❌ Error checking session", e)
                            }
                        }

                        Log.d("ParticipantWaitingVM", "Latest session ID: $latestSessionId")
                        Log.d("ParticipantWaitingVM", "Latest session timestamp: $latestSessionTimestamp")

                        if (latestSessionId == null) {
                            Log.d("ParticipantWaitingVM", "No sessions found for this group")
                            _uiState.value = _uiState.value.copy(
                                testResults = emptyList(),
                                isLoadingResults = false
                            )
                            return@launch
                        }

                        // ШАГ 2: Загружаем только результаты последней сессии
                        for (doc in snapshot.documents) {
                            val sessionId = doc.getString("sessionId")

                            // Пропускаем результаты НЕ последней сессии
                            if (sessionId != latestSessionId) {
                                continue
                            }

                            try {
                                val sessionDoc = firestore.collection("test_sessions")
                                    .document(sessionId)
                                    .get()
                                    .await()

                                if (!sessionDoc.exists()) continue

                                val sessionTitle = sessionDoc.getString("title") ?: "Test"
                                val participantId = doc.getString("participantId") ?: continue
                                val score = (doc.getLong("score") ?: 0).toInt()
                                val totalQuestions = (doc.getLong("totalQuestions") ?: 0).toInt()
                                val percentage = if (totalQuestions > 0) {
                                    (score.toDouble() / totalQuestions) * 100
                                } else 0.0
                                val completedAt = doc.getTimestamp("completedAt")

                                // Получаем имя участника
                                val userDoc = firestore.collection("users")
                                    .document(participantId)
                                    .get()
                                    .await()

                                val participantName = userDoc.getString("name") ?: "Participant"

                                results.add(
                                    TestResult(
                                        sessionId = sessionId,
                                        sessionTitle = sessionTitle,
                                        participantId = participantId,
                                        participantName = participantName,
                                        score = score,
                                        totalQuestions = totalQuestions,
                                        percentage = percentage,
                                        completedAt = completedAt
                                    )
                                )

                                Log.d("ParticipantWaitingVM", "  Added: $participantName - $score/$totalQuestions")

                            } catch (e: Exception) {
                                Log.e("ParticipantWaitingVM", "❌ Error processing result", e)
                            }
                        }

                        // Сортируем по проценту (лучшие сверху)
                        val sortedResults = results.sortedByDescending { it.percentage }

                        Log.d("ParticipantWaitingVM", "========================================")
                        Log.d("ParticipantWaitingVM", "✅ Loaded ${sortedResults.size} results for latest session")
                        sortedResults.forEach { result ->
                            Log.d("ParticipantWaitingVM", "  - ${result.participantName}: ${result.score}/${result.totalQuestions} (${result.sessionTitle})")
                        }
                        Log.d("ParticipantWaitingVM", "========================================")

                        _uiState.value = _uiState.value.copy(
                            testResults = sortedResults,
                            isLoadingResults = false
                        )
                    }
                } else {
                    Log.d("ParticipantWaitingVM", "No results found in database")
                    _uiState.value = _uiState.value.copy(
                        testResults = emptyList(),
                        isLoadingResults = false
                    )
                }
            }
    }

    fun stopListening() {
        Log.d("ParticipantWaitingVM", "🛑 Stopping all listeners")
        notificationListener?.remove()
        groupListener?.remove()
        groupClosureListener?.remove()
        resultsListener?.remove()
        notificationListener = null
        groupListener = null
        groupClosureListener = null
        resultsListener = null
        Log.d("ParticipantWaitingVM", "✅ All listeners stopped")
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
        Log.d("ParticipantWaitingVM", "ViewModel cleared")
    }
}