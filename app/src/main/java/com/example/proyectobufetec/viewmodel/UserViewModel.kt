// com.example.proyectobufetec/viewmodel/UserViewModel.kt

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

    // New: Manage user type (Guest, User, Lawyer)
    private val _userType = MutableStateFlow<UserType>(UserType.Guest)
    val userType: StateFlow<UserType> = _userType

    init {
        checkTokenOnLaunch()
    }

    /**
     * Checks if a valid token exists on launch and updates authState and userType accordingly.
     */
    private fun checkTokenOnLaunch() {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if (token != null && tokenManager.isTokenValid()) {
                Log.d("UserViewModel", "Valid token found, setting authState to Success.")
                val userRole = getUserRoleFromToken(token) // Determine user role
                _userType.value = userRole
                _authState.value = AuthState.Success(TokenResponse("Usuario logged in", token))
            } else {
                Log.d("UserViewModel", "No valid token found, setting authState to Idle.")
                _authState.value = AuthState.Idle
                _userType.value = UserType.Guest // Default to Guest when no valid token
            }
        }
    }

    // New: Determine user role based on token or other logic
    private fun getUserRoleFromToken(token: String): UserType {
        // Placeholder: Replace this with actual logic to determine user role
        return when {
            token.contains("lawyer") -> UserType.Lawyer
            token.contains("user") -> UserType.User
            else -> UserType.Guest
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
                        val userRole = getUserRoleFromToken(it.token) // Set user role
                        _userType.value = userRole
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

    fun clearCredentials() {
        _email.value = ""
        _password.value = ""
    }
}

// Enum class for user roles
enum class UserType {
    Guest, User, Lawyer
}

// Auth state management to streamline state handling
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: TokenResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}
