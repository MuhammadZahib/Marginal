package com.example.marginal.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marginal.domain.model.AuthUser
import com.example.marginal.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val resetEmailSent: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Eagerly shared so Splash can read .value immediately without waiting for a collector.
    val currentUser: StateFlow<AuthUser?> = authRepository.currentUser
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            authRepository.signIn(email, password)
                .onSuccess {
                    _uiState.value = AuthUiState()
                    onSuccess()
                }
                .onFailure { e -> _uiState.value = AuthUiState(errorMessage = e.message ?: "Couldn't sign in") }
        }
    }

    fun signUp(name: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            authRepository.signUp(name, email, password)
                .onSuccess {
                    _uiState.value = AuthUiState()
                    onSuccess()
                }
                .onFailure { e -> _uiState.value = AuthUiState(errorMessage = e.message ?: "Couldn't create account") }
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            authRepository.sendPasswordReset(email)
                .onSuccess { _uiState.value = AuthUiState(resetEmailSent = true) }
                .onFailure { e -> _uiState.value = AuthUiState(errorMessage = e.message ?: "Couldn't send reset email") }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            authRepository.changePassword(currentPassword, newPassword)
                .onSuccess {
                    _uiState.value = AuthUiState()
                    onSuccess()
                }
                .onFailure { e -> _uiState.value = AuthUiState(errorMessage = e.message ?: "Couldn't change password") }
        }
    }

    fun signOut() = authRepository.signOut()
}
