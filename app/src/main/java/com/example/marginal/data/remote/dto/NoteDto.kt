package com.example.marginal.data.remote.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.example.marginal.domain.model.Note
import com.example.marginal.domain.model.NoteCategory
import java.util.Date

// Firestore needs a no-arg constructor for automatic deserialization —
// that's why every field has a default. Never used outside data/.
data class NoteDto(
    @DocumentId val id: String = "",
    val title: String = "",
    val body: String = "",
    val category: String = NoteCategory.PERSONAL.name,
    @ServerTimestamp val createdAt: Date? = null,
    @ServerTimestamp val updatedAt: Date? = null,
)

fun NoteDto.toDomain(): Note = Note(
    id = id,
    title = title,
    body = body,
    category = runCatching { NoteCategory.valueOf(category) }.getOrDefault(NoteCategory.PERSONAL),
    createdAt = createdAt?.time ?: 0L,
    updatedAt = updatedAt?.time ?: 0L,
)
