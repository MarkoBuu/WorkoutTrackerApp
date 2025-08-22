package org.example.project.features.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.features.login.auth.AuthService
import org.example.project.features.profile.data.User

class ProfileViewModel(
    private val authService: AuthService
) : ViewModel() {

    private var _refresh = MutableStateFlow(false)
    private val refresh = _refresh.asStateFlow()

    val currentUser: StateFlow<User?> = authService.currentUser
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val profileUiState: StateFlow<ProfileScreenUiState> =
        combine(refresh, currentUser) { refreshFlag, user ->
            getUserInfo(user)
        }.flatMapLatest { it }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileScreenUiState()
        )

    fun getUserInfo(user: User?): Flow<ProfileScreenUiState> = flow {
        emit(ProfileScreenUiState(isLoading = true))

        try {
            delay(1000) // Simulate loading

            emit(
                ProfileScreenUiState(
                    userInfo = user, // Can be null
                    isLoggedIn = user != null && !user.isAnonymous,
                    isLoading = false
                )
            )
        } catch (e: Exception) {
            emit(
                ProfileScreenUiState(
                    isLoading = false,
                    error = "Failed to load profile information"
                )
            )
        }
    }

    fun refresh() {
        _refresh.value = true
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                authService.signOut()
                refresh()
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}