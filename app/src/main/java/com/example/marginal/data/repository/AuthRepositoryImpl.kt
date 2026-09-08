package com.example.marginal.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.example.marginal.domain.model.AuthUser
import com.example.marginal.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : AuthRepository {

    override val currentUser: Flow<AuthUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(
                firebaseAuth.currentUser?.let {
                    AuthUser(uid = it.uid, email = it.email, displayName = it.displayName)
                }
            )
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
        Unit
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(name).build()
        )?.await()
        Unit
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> = runCatching {
        val user = auth.currentUser ?: error("Not signed in")
        val email = user.email ?: error("No email on this account")
        // Firebase requires a "recent" login before allowing a password
        // change — reauthenticating with the current password satisfies that.
        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential).await()
        user.updatePassword(newPassword).await()
    }

    override suspend fun deleteAccount(currentPassword: String): Result<Unit> = runCatching {
        val user = auth.currentUser ?: error("Not signed in")
        val email = user.email ?: error("No email on this account")
        val uid = user.uid

        // Firebase requires a recent login before letting you delete an account —
        // same rule as changing a password.
        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential).await()

        // Delete their notes BEFORE the account, so nothing gets orphaned in
        // Firestore if something goes wrong partway through.
        val notesSnapshot = firestore.collection("users").document(uid).collection("notes").get().await()
        val batch = firestore.batch()
        for (doc in notesSnapshot.documents) {
            batch.delete(doc.reference)
        }
        batch.commit().await()

        // Now the account itself.
        user.delete().await()
    }

    override fun signOut() = auth.signOut()
}
