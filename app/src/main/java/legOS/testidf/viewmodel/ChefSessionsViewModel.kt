package legOS.testidf.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import legOS.testidf.data.UserSession
import legOS.testidf.screens.ChefTestSession

data class ChefSessionsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessions: List<ChefTestSession> = emptyList()
)

class ChefSessionsViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(ChefSessionsUiState())
    val uiState: StateFlow<ChefSessionsUiState> = _uiState

    fun loadSessions() {
        viewModelScope.launch {
            _uiState.value = ChefSessionsUiState(isLoading = true)

            try {
                val userId = UserSession.userId ?: throw Exception("User not logged in")

                val sessionsSnapshot = firestore.collection("test_sessions")
                    .whereEqualTo("adminId", userId)
                    .get()
                    .await()

                val sessions = sessionsSnapshot.documents.map { doc ->
                    val questionRefs = doc.get("questionRefs") as? List<*> ?: emptyList<Any>()
                    val participantIds = doc.get("participantIds") as? List<*> ?: emptyList<Any>()
                    val createdAtTimestamp = doc.getTimestamp("createdAt")?.toDate()?.time ?: 0L

                    // ИЗМЕНЕНО: Используем ChefTestSession вместо TestSession
                    ChefTestSession(
                        sessionId = doc.id,
                        title = doc.getString("title") ?: "Test sans titre",
                        questionCount = questionRefs.size,
                        participantCount = participantIds.size,
                        status = doc.getString("status") ?: "pending",
                        createdAt = createdAtTimestamp
                    )
                }.sortedByDescending { it.createdAt }

                _uiState.value = ChefSessionsUiState(sessions = sessions)
                Log.d("ChefSessionsVM", "Loaded ${sessions.size} sessions")

            } catch (e: Exception) {
                Log.e("ChefSessionsVM", "Error loading sessions", e)
                _uiState.value = ChefSessionsUiState(
                    error = "Erreur de chargement: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}