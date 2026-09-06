package com.example.marginal.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marginal.domain.model.Note
import com.example.marginal.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    noteRepository: NoteRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val allNotes: StateFlow<List<Note>> = noteRepository.observeNotes()
        // The very first emission — whether it has notes or not — means the
        // listener is live. Only then can we trust "empty" as a real answer
        // instead of "haven't heard back yet".
        .onEach { _isLoading.value = false }
        .catch {
            _isLoading.value = false
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Firestore has no native full-text search, and this app's note count is
    // small (personal notes, not a database of thousands) — filtering the
    // already-loaded list client-side is simpler than standing up a search
    // service, and fast enough at this scale.
    val notes: StateFlow<List<Note>> = combine(allNotes, _searchQuery) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.title.contains(query, ignoreCase = true) || it.body.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(value: String) {
        _searchQuery.value = value
    }
}
