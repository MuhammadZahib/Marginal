package com.example.marginal.presentation.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marginal.domain.model.NoteCategory
import com.example.marginal.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditNoteUiState(
    val title: String = "",
    val body: String = "",
    val category: NoteCategory = NoteCategory.PERSONAL,
    val isEditMode: Boolean = false,
    val isSaving: Boolean = false,
)

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val noteId: String? = savedStateHandle.get<String>("noteId")?.ifBlank { null }

    private val _uiState = MutableStateFlow(AddEditNoteUiState(isEditMode = noteId != null))
    val uiState: StateFlow<AddEditNoteUiState> = _uiState.asStateFlow()

    init {
        // Edit mode: prefill from the current notes list (a single snapshot is enough — this
        // form doesn't need to stay live-synced while the user is mid-edit).
        if (noteId != null) {
            viewModelScope.launch {
                val existing = noteRepository.observeNotes().first().find { it.id == noteId }
                if (existing != null) {
                    _uiState.value = _uiState.value.copy(
                        title = existing.title,
                        body = existing.body,
                        category = existing.category,
                    )
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value)
    }

    fun onBodyChange(value: String) {
        _uiState.value = _uiState.value.copy(body = value)
    }

    fun onCategoryChange(value: NoteCategory) {
        _uiState.value = _uiState.value.copy(category = value)
    }

    fun save(onDone: () -> Unit) {
        val state = _uiState.value
        if (state.title.isBlank() && state.body.isBlank()) return

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            val result = if (noteId != null) {
                noteRepository.updateNote(noteId, state.title, state.body, state.category)
            } else {
                noteRepository.addNote(state.title, state.body, state.category)
            }
            _uiState.value = _uiState.value.copy(isSaving = false)
            result.onSuccess { onDone() }
            // TODO: surface result.exceptionOrNull() to the UI once we add an error banner here
        }
    }

    fun delete(onDone: () -> Unit) {
        val id = noteId ?: return
        viewModelScope.launch {
            noteRepository.deleteNote(id).onSuccess { onDone() }
        }
    }
}
