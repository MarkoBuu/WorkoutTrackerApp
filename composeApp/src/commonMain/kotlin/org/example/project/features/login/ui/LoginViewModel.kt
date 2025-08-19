package org.example.project.features.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String){
        viewModelScope.launch {
            _loginState.value = LoginState.Idle

            try {
                delay(1500)

                if(email == "a" && password == "a"){
                    _loginState.value = LoginState.Success
                } else {
                    _loginState.value = LoginState.Error("Invalid credentials")
                }
            } catch (e: Exception){
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState(){
        _loginState.value = LoginState.Idle
    }

}