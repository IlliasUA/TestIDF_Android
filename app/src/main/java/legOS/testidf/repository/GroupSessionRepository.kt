package legOS.testidf.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/** Owns the lifecycle of all Firestore data that belongs to one temporary group. */
class GroupSessionRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun closeAndDeleteGroup(groupId: String) {
        val groupReference = firestore.collection("groups").document(groupId)
        val group = groupReference.get().await()
        if (!group.exists()) return

        // Block new joins and submissions before removing the group's documents.
        groupReference.update(
            mapOf(
                "isActive" to false,
                "closedAt" to Timestamp.now()
            )
        ).await()

        deleteDocuments("notifications", groupId)
        deleteDocuments("test_results", groupId)
        deleteDocuments("test_sessions", groupId)
        groupReference.delete().await()
    }

    private suspend fun deleteDocuments(collection: String, groupId: String) {
        val documents = firestore.collection(collection)
            .whereEqualTo("groupId", groupId)
            .get()
            .await()
            .documents

        documents.chunked(FIRESTORE_SAFE_BATCH_SIZE).forEach { chunk ->
            val batch = firestore.batch()
            chunk.forEach { batch.delete(it.reference) }
            batch.commit().await()
        }
    }

    private companion object {
        const val FIRESTORE_SAFE_BATCH_SIZE = 450
    }
}
