// com.example.proyectobufetec/viewmodel/UserViewModel.kt

package com.example.proyectobufetec.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.proyectobufetec.data.usuario.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val usuarioApiService: UsuarioApiService,
    private val context: Context
) : ViewModel() {

    // User credentials state
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // State management
    private val _loginState = MutableStateFlow<LoginUserState>(LoginUserState.Initial)
    val loginState: StateFlow<LoginUserState> = _loginState

    private val _registerState = MutableStateFlow<RegisterUserState>(RegisterUserState.Initial)
    val registerState: StateFlow<RegisterUserState> = _registerState

    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged: StateFlow<Boolean> = _isUserLogged

    // Helper function to create EncryptedSharedPreferences
    private fun getEncryptedSharedPreferences(): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "auth_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // Save token in EncryptedSharedPreferences
    private fun saveAuthToken(token: String) {
        val sharedPreferences = getEncryptedSharedPreferences()
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    // Retrieve token from EncryptedSharedPreferences
    fun getAuthToken(): String? {
        val sharedPreferences = getEncryptedSharedPreferences()
        return sharedPreferences.getString("auth_token", null)
    }

    // Login function with token handling
    fun loginUser(user: LoginRequest) {
        _loginState.value = LoginUserState.Loading
        viewModelScope.launch {
            try {
                val response = usuarioApiService.login(user)
                if (response.isSuccessful) {
                    val tokenResponse = response.body()!!
                    saveAuthToken(tokenResponse.token)  // Save token
                    setUserLogged(true)
                    _loginState.value = LoginUserState.Success(tokenResponse)
                } else {
                    _loginState.value = LoginUserState.Error("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUserState.Error("Error in login: ${e.message}")
            }
        }
    }

    // Register user and handle token saving
    fun registerUser(user: RegisterRequest) {
        _registerState.value = RegisterUserState.Loading
        viewModelScope.launch {
            try {
                val response = usuarioApiService.register(user)
                if (response.isSuccessful) {
                    val tokenResponse = response.body()!!
                    saveAuthToken(tokenResponse.token)  // Save token
                    _registerState.value = RegisterUserState.Success(tokenResponse)
                } else {
                    _registerState.value = RegisterUserState.Error("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterUserState.Error("Error during registration: ${e.message}")
            }
        }
    }

    // Set user logged state
    fun setUserLogged(isLogged: Boolean) {
        _isUserLogged.value = isLogged
    }

    // Verify the token and update user login status
    fun verifyToken() {
        val token = getAuthToken()
        if (token != null) {
            viewModelScope.launch {
                try {
                    val response = usuarioApiService.verifyToken("Bearer $token")
                    if (response.isSuccessful) {
                        setUserLogged(true)
                    } else {
                        setUserLogged(false)
                    }
                } catch (e: Exception) {
                    setUserLogged(false)
                }
            }
        } else {
            setUserLogged(false)
        }
    }
}
