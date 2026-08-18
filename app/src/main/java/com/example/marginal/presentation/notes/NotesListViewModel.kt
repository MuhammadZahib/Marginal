package com.example.marginal.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marginal.domain.model.Note
import com.example.marginal.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    noteRepository: NoteRepository,
) : ViewModel() {

    val notes: StateFlow<List<Note>> = noteRepository.observeNotes()
        // Signing out mid-listen makes Firestore reject the active snapshot
        // listener with PERMISSION_DENIED (the auth token it was using just
        // disappeared). That's expected during sign-out, not a real error —
        // swallow it instead of letting it crash the app.
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}