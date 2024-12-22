package com.grupoct.gestionalmacen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupoct.gestionalmacen.data.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authService: AuthService)  : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        Log.d("LoginViewModel", "Attempting login with username: $username")
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val token = authService.login(username, password)
                // Log para verificar la respuesta de la API
                Log.d("LoginViewModel", "Token: $token")

                if (token != null) {
                    _loginState.value = LoginState.Success(token)
                } else {
                    _loginState.value = LoginState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
