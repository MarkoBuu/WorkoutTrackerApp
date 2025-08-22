package org.example.project.features.login.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.features.login.auth.AuthService
import org.example.project.features.profile.data.User

class LoginViewModel(
    private val authService: AuthService
): BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        launchWithCatchingException {
            authService.currentUser.collect{
                _currentUser.value = it
            }
        }
    }
    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue) }
    }


    fun onSingInClick(){
        val currentState = _uiState.value

        if(currentState.email.isEmpty()){
            _uiState.update { it.copy(errorMessage = "Email is required") }
            return
        }
        if(currentState.password.isEmpty()){
            _uiState.update { it.copy(errorMessage = "Password is required") }
            return
        }

        launchWithCatchingException {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                authService.authenticate(currentState.email, currentState.password)
                _uiState.update { it.copy(isLoading = false, errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun onSingUpClick(){
        val currentState = _uiState.value

        if(currentState.email.isEmpty()){
            _uiState.update { it.copy(errorMessage = "Email is required") }
            return
        }
        if(currentState.password.isEmpty()){
            _uiState.update { it.copy(errorMessage = "Password is required") }
            return
        }

        launchWithCatchingException {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                authService.createUser(currentState.email, currentState.password)
                _uiState.update { it.copy(isLoading = false, errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }


    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearEmailAndPassword(){
        _uiState.update { it.copy(email = "") }
        _uiState.update { it.copy(password = "") }
    }

    fun onSignOut(){
        launchWithCatchingException {
            authService.signOut()
        }
    }
}