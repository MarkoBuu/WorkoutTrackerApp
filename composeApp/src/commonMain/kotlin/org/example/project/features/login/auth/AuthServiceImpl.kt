package org.example.project.features.login.auth

import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.features.profile.data.User

class AuthServiceImpl(
    private val auth: FirebaseAuth
) : AuthService {

    override val currentUserId: String
        get() = auth.currentUser?.uid ?: ""

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null && auth.currentUser?.isAnonymous == false

    override val currentUser: Flow<User> =
        auth.authStateChanged.map { firebaseUser ->
            firebaseUser?.let {
                User(
                    id = it.uid ?: "",
                    isAnonymous = it.isAnonymous,
                    email = it.email ?: ""
                )
            } ?: User()
        }

    override suspend fun authenticate(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            throw AuthException("Login failed: ${e.message}", e)
        }
    }

    override suspend fun createUser(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            throw AuthException("Registration failed: ${e.message}", e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}

class AuthException(message: String, cause: Exception? = null) : Exception(message, cause)

