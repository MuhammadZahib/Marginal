package com.example.marginal.domain.repository

import com.example.marginal.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: Flow<AuthUser?>

    suspend fun signIn(email: String, password: String): Result<Unit>

    suspend fun signUp(name: String, email: String, password: String): Result<Unit>

    suspend fun sendPasswordReset(email: String): Result<Unit>

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>

    /** Deletes all of the user's notes, then the account itself. Requires the current password to reauthenticate. */
    suspend fun deleteAccount(currentPassword: String): Result<Unit>

    fun signOut()
}
