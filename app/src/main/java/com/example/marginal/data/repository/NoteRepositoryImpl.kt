package com.example.marginal.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.example.marginal.data.remote.dto.NoteDto
import com.example.marginal.data.remote.dto.toDomain
import com.example.marginal.domain.model.Note
import com.example.marginal.domain.model.NoteCategory
import com.example.marginal.domain.repository.NoteRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : NoteRepository {

    // How long we wait for the SERVER to confirm a write before giving up
    // and treating it as done anyway. Firestore already applies writes to
    // its local cache instantly (that's why a note appears in the list even
    // offline) and syncs to the server automatically once back online — so
    // without this timeout, a fully offline save would spin its loader
    // forever waiting for a server confirmation that can't arrive yet.
    private val writeTimeoutMs = 6000L

    private fun notesCollection() =
        firestore.collection("users").document(requireUid()).collection("notes")

    private fun requireUid(): String =
        auth.currentUser?.uid ?: error("NoteRepository called with no signed-in user")

    override fun observeNotes(): Flow<List<Note>> = callbackFlow {
        val registration: ListenerRegistration = notesCollection()
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val notes = snapshot?.toObjects(NoteDto::class.java)?.map { it.toDomain() } ?: emptyList()
                trySend(notes)
            }
        awaitClose { registration.remove() }
    }

    override suspend fun addNote(title: String, body: String, category: NoteCategory): Result<String> = runCatching {
        val doc = notesCollection().document()
        val dto = NoteDto(id = doc.id, title = title, body = body, category = category.name)
        withTimeoutOrNull(writeTimeoutMs) { doc.set(dto).await() }
        doc.id
    }

    override suspend fun updateNote(
        noteId: String,
        title: String,
        body: String,
        category: NoteCategory,
    ): Result<Unit> = runCatching {
        withTimeoutOrNull(writeTimeoutMs) {
            notesCollection().document(noteId).update(
                mapOf(
                    "title" to title,
                    "body" to body,
                    "category" to category.name,
                    "updatedAt" to FieldValue.serverTimestamp(),
                )
            ).await()
        }
        Unit
    }

    override suspend fun deleteNote(noteId: String): Result<Unit> = runCatching {
        withTimeoutOrNull(writeTimeoutMs) {
            notesCollection().document(noteId).delete().await()
        }
        Unit
    }
}
