package com.example.marginal.domain.repository

import com.example.marginal.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser: Flow<AuthUser?>

    suspend fun signIn(email: String, password: String): Result<Unit>

    suspend fun signUp(name: String, email: String, password: String): Result<Unit>

    suspend fun sendPasswordReset(email: String): Result<Unit>
}