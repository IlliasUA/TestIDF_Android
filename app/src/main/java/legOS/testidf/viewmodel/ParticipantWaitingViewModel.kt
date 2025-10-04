package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
        // Слушаем уведомления в реальном времени
        notificationListener = firestore.collection("notifications")
            .whereEqualTo("recipientId", userId)
            .whereEqualTo("isRead", false)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ParticipantWaitingVM", "Listen failed", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val tests = mutableListOf<AvailableTest>()

                    for (doc in snapshot.documents) {
                        val sessionId = doc.getString("sessionId") ?: continue

                        // Загружаем данные сессии
                        firestore.collection("test_sessions")
                            .document(sessionId)
                            .get()
                            .addOnSuccessListener { sessionDoc ->
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

                                _uiState.value = ParticipantWaitingUiState(availableTests = tests)
                                Log.d("ParticipantWaitingVM", "Test available: $title")
                            }
                    }
                }
            }
    }

    fun stopListening() {
        notificationListener?.remove()
        notificationListener = null
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
}