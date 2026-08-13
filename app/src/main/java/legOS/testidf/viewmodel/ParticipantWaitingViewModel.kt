package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Job
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
    private var resultsProcessingJob: Job? = null

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

        // Firestore filters on the server. This avoids downloading results from
        // other groups and keeps security rules compatible with this listener.
        resultsListener = firestore.collection("test_results")
            .whereEqualTo("groupId", groupId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ParticipantWaitingVM", "❌ Error listening to results", error)
                    _uiState.value = _uiState.value.copy(isLoadingResults = false)
                    return@addSnapshotListener
                }

                Log.d("ParticipantWaitingVM", "📊 Group results snapshot received")
                Log.d("ParticipantWaitingVM", "Group results: ${snapshot?.size() ?: 0}")

                if (snapshot != null && !snapshot.isEmpty) {
                    resultsProcessingJob?.cancel()
                    resultsProcessingJob = viewModelScope.launch {
                        try {
                            val latestResult = snapshot.documents
                                .filter { it.getString("sessionId") != null }
                                .maxByOrNull { document ->
                                    document.getTimestamp("completedAt")?.let { timestamp ->
                                        timestamp.seconds * 1_000_000_000L + timestamp.nanoseconds
                                    } ?: Long.MIN_VALUE
                                }
                            val latestSessionId = latestResult?.getString("sessionId")

                            if (latestSessionId == null) {
                                _uiState.value = _uiState.value.copy(
                                    testResults = emptyList(),
                                    isLoadingResults = false
                                )
                                return@launch
                            }

                            // Only one session lookup is needed for the whole snapshot.
                            val sessionDoc = firestore.collection("test_sessions")
                                .document(latestSessionId)
                                .get()
                                .await()
                            val sessionTitle = sessionDoc.getString("title") ?: "Test"

                            val sortedResults = snapshot.documents
                                .asSequence()
                                .filter { it.getString("sessionId") == latestSessionId }
                                .mapNotNull { doc ->
                                    val participantId = doc.getString("participantId")
                                        ?: return@mapNotNull null
                                    val score = (doc.getLong("score") ?: 0).toInt()
                                    val totalQuestions = (doc.getLong("totalQuestions") ?: 0).toInt()
                                    TestResult(
                                        sessionId = latestSessionId,
                                        sessionTitle = sessionTitle,
                                        participantId = participantId,
                                        participantName = doc.getString("participantName") ?: "Participant",
                                        score = score,
                                        totalQuestions = totalQuestions,
                                        percentage = if (totalQuestions > 0) {
                                            score.toDouble() / totalQuestions * 100
                                        } else {
                                            0.0
                                        },
                                        completedAt = doc.getTimestamp("completedAt")
                                    )
                                }
                                .sortedByDescending { it.percentage }
                                .toList()

                            _uiState.value = _uiState.value.copy(
                                testResults = sortedResults,
                                isLoadingResults = false
                            )
                            Log.d(
                                "ParticipantWaitingVM",
                                "✅ Loaded ${sortedResults.size} results for session $latestSessionId"
                            )
                        } catch (e: Exception) {
                            Log.e("ParticipantWaitingVM", "❌ Error processing group results", e)
                            _uiState.value = _uiState.value.copy(isLoadingResults = false)
                        }
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
        resultsProcessingJob?.cancel()
        notificationListener = null
        groupListener = null
        groupClosureListener = null
        resultsListener = null
        resultsProcessingJob = null
        Log.d("ParticipantWaitingVM", "✅ All listeners stopped")
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
        Log.d("ParticipantWaitingVM", "ViewModel cleared")
    }
}
