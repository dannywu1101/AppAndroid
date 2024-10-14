package com.example.proyectobufetec.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobufetec.data.network.TokenManager
import com.example.proyectobufetec.data.usuario.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val usuarioApiService: UsuarioApiService,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        // Automatically check token validity on initialization
        checkTokenOnLaunch()
    }

    /**
     * Checks if a valid token exists on launch and updates authState accordingly.
     */
    private fun checkTokenOnLaunch() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token != null && tokenManager.isTokenValid()) {
                Log.d("UserViewModel", "Valid token found, setting authState to Success.")
                _authState.value = AuthState.Success(TokenResponse("Usuario logged in",token))
            } else {
                Log.d("UserViewModel", "No valid token found, setting authState to Idle.")
                _authState.value = AuthState.Idle
            }
        }
    }

    fun loginUser(user: LoginRequest) = handleAuthRequest {
        usuarioApiService.login(user)
    }

    fun registerUser(user: RegisterRequest) = handleAuthRequest {
        usuarioApiService.register(user)
    }

    private fun handleAuthRequest(request: suspend () -> retrofit2.Response<TokenResponse>) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val response = request()
                if (response.isSuccessful) {
                    response.body()?.let {
                        tokenManager.saveToken(it.token)
                        _authState.value = AuthState.Success(it)
                    }
                } else {
                    _authState.value = AuthState.Error(response.message())
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    // Clear email and password after successful login
    fun clearCredentials() {
        _email.value = ""
        _password.value = ""
    }
}

// Auth state management to streamline state handling
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: TokenResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}
