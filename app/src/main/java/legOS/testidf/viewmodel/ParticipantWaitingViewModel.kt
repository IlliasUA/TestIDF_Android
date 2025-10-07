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

data class AvailableTest(
    val sessionId: String = "",
    val title: String = "",
    val questionCount: Int = 0,
    val timeLimit: Int = 0
)

data class ParticipantWaitingUiState(
    val availableTests: List<AvailableTest> = emptyList()
)

class ParticipantWaitingViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private var notificationListener: ListenerRegistration? = null

    private val _uiState = MutableStateFlow(ParticipantWaitingUiState())
    val uiState: StateFlow<ParticipantWaitingUiState> = _uiState

    fun startListeningForTests(userId: String) {
        Log.d("ParticipantWaitingVM", "========================================")
        Log.d("ParticipantWaitingVM", "📬 Setting up test listener for user: $userId")

        notificationListener?.remove()

        // ИСПРАВЛЕНО: Добавлен фильтр isActive
        notificationListener = firestore.collection("notifications")
            .whereEqualTo("recipientId", userId)
            .whereEqualTo("isRead", false)
            .whereEqualTo("isActive", true) // ДОБАВЛЕНО
            .whereEqualTo("type", "test_invitation") // ДОБАВЛЕНО для ясности
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
                        _uiState.value = ParticipantWaitingUiState(availableTests = emptyList())
                        return@addSnapshotListener
                    }

                    // ИСПРАВЛЕНО: Используем корутины для последовательной загрузки
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
                                    // Загружаем данные сессии
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

                        _uiState.value = ParticipantWaitingUiState(availableTests = tests)
                    }
                }
            }
    }

    fun stopListening() {
        Log.d("ParticipantWaitingVM", "Stopping notification listener")
        notificationListener?.remove()
        notificationListener = null
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
        Log.d("ParticipantWaitingVM", "ViewModel cleared")
    }
}