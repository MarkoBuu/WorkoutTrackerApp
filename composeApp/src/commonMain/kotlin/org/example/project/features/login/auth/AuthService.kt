package org.example.project.features.login.auth

import kotlinx.coroutines.flow.Flow
import org.example.project.features.profile.data.User

interface AuthService {
    val currentUser: Flow<User>
    val currentUserId: String
    val isAuthenticated: Boolean

    suspend fun authenticate(email: String, password: String)
    suspend fun createUser(email: String, password: String)
    suspend fun signOut()
}