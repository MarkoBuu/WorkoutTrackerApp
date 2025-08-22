package org.example.project.features.profile.ui

import org.example.project.features.profile.data.User

data class ProfileScreenUiState(
    val userInfo: User? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)