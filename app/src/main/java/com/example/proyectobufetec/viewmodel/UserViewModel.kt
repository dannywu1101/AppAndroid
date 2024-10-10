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
import com.example.proyectobufetec.data.usuario.LoginRequest
import com.example.proyectobufetec.data.usuario.LoginUserState
import com.example.proyectobufetec.data.usuario.RegisterRequest
import com.example.proyectobufetec.data.usuario.UsuarioApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val usuarioApiService: UsuarioApiService,
    private val context: Context  // Pass context to access SharedPreferences
) : ViewModel() {

    // Define state variables for email and password
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // States for login and register processes
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

    // Function to save token in EncryptedSharedPreferences
    private fun saveAuthToken(token: String) {
        val sharedPreferences = getEncryptedSharedPreferences()
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    // Login user function that handles TokenResponse and saves the token
    fun loginUser(user: LoginRequest) {
        _loginState.value = LoginUserState.Initial
        viewModelScope.launch {
            try {
                _loginState.value = LoginUserState.Loading
                val response = usuarioApiService.login(user)
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    _loginState.value = LoginUserState.Success(tokenResponse!!)
                    setUserLogged(true)

                    // Save the token to EncryptedSharedPreferences
                    saveAuthToken(tokenResponse.token)
                } else {
                    _loginState.value = LoginUserState.Error("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUserState.Error("Error in login: ${e.message}")
            }
        }
    }

    // Register user function that handles TokenResponse and saves the token
    fun registerUser(user: RegisterRequest) {
        viewModelScope.launch {
            try {
                _registerState.value = RegisterUserState.Loading
                val response = usuarioApiService.register(user)

                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    _registerState.value = RegisterUserState.Success(tokenResponse!!)

                    // Save the token to EncryptedSharedPreferences
                    saveAuthToken(tokenResponse.token)
                } else {
                    _registerState.value = RegisterUserState.Error("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterUserState.Error("Error during registration: ${e.message}")
            }
        }
    }

    // Set user logged status
    fun setUserLogged(isLogged: Boolean) {
        _isUserLogged.value = isLogged
    }

    // Retrieve the token when needed
    fun getAuthToken(): String? {
        val sharedPreferences = getEncryptedSharedPreferences()
        return sharedPreferences.getString("auth_token", null)
    }
}
