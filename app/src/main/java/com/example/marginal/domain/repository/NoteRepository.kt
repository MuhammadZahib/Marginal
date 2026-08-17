package com.example.marginal.domain.repository

import com.example.marginal.domain.model.Note
import com.example.marginal.domain.model.NoteCategory
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun observeNotes(): Flow<List<Note>>

    suspend fun addNote(title: String, body: String, category: NoteCategory): Result<String>

    suspend fun updateNote(noteId: String, title: String, body: String, category: NoteCategory): Result<Unit>

    suspend fun deleteNote(noteId: String): Result<Unit>
}
