package com.example.marginal.domain.model

data class Note(
    val id: String,
    val title: String,
    val body: String,
    val category: NoteCategory,
    val createdAt: Long,
    val updatedAt: Long,
)

enum class NoteCategory {
    PERSONAL, WORK, IDEAS, URGENT
}
