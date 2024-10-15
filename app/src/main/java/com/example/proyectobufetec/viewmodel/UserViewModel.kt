// com.example.proyectobufetec/viewmodel/UserViewModel.kt

package com.example.proyectobufetec.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobufetec.data.network.TokenManager
import com.example.proyectobufetec.data.usuario.LoginRequest
import com.example.proyectobufetec.data.usuario.RegisterRequest
import com.example.proyectobufetec.data.usuario.TokenResponse
import com.example.proyectobufetec.data.usuario.UsuarioApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val usuarioApiService: UsuarioApiService,
    private val tokenManager: TokenManager
) : ViewModel() {

    // Mutable state flow to store email and password values
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // New: Manage user type (Admin, Abogado, Cliente, Usuario, Guest)
    private val _userType = MutableStateFlow<UserType>(UserType.Guest)
    val userType: StateFlow<UserType> = _userType

    init {
        // Automatically check token validity on launch
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

    /**
     * Determines the user role from the provided token.
     */
    private fun getUserRoleFromToken(token: String): UserType {
        val role = tokenManager.extractRoleFromToken(token) // Extract role from token
        return when (role) {
            "Admin" -> UserType.Admin
            "Abogado" -> UserType.Abogado
            "Cliente" -> UserType.Cliente
            "Usuario" -> UserType.Usuario
            else -> UserType.Guest
        }
    }

    /**
     * Logs in the user by making a request with the provided credentials.
     */
    fun loginUser(user: LoginRequest) = handleAuthRequest {
        usuarioApiService.login(user)
    }

    /**
     * Registers a new user.
     */
    fun registerUser(user: RegisterRequest) = handleAuthRequest {
        usuarioApiService.register(user)
    }

    // logout
    fun logoutUser() {
        tokenManager.clearToken()
        _authState.value = AuthState.Idle
        _userType.value = UserType.Guest // Reset to Guest on logout
        Log.d("UserViewModel", "User logged out successfully.")
    }


    /**
     * Handles the authentication requests.
     */
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

    // Handle changes to the email field
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    // Handle changes to the password field
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    // Clears the stored email and password
    fun clearCredentials() {
        _email.value = ""
        _password.value = ""
    }
}

// Enum class for managing different user roles
enum class UserType {
    Admin, Abogado, Cliente, Usuario, Guest
}

// Sealed class to manage different authentication states
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val response: TokenResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}
