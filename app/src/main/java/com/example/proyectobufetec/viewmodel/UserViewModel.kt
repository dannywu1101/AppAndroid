// com.example.proyectobufetec/viewmodel/UserViewModel.kt

package com.example.proyectobufetec.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobufetec.data.usuario.LoginRequest
import com.example.proyectobufetec.data.usuario.LoginUserState
import com.example.proyectobufetec.data.usuario.RegisterRequest
import com.example.proyectobufetec.data.usuario.UsuarioApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val usuarioApiService: UsuarioApiService) : ViewModel() {

    var email by mutableStateOf("Prueba")
    var password by mutableStateOf("1234")

    private val _loginState = MutableStateFlow<LoginUserState>(LoginUserState.Initial)
    val loginState: StateFlow<LoginUserState> = _loginState

    private val _registerState = MutableStateFlow<RegisterUserState>(RegisterUserState.Initial)
    val registerState: StateFlow<RegisterUserState> = _registerState

    private val _isUserLogged = MutableStateFlow(false)
    val isUserLogged: StateFlow<Boolean> = _isUserLogged

    fun setUserLogged(isLogged: Boolean) {
        _isUserLogged.value = isLogged
    }

    // Login user function that handles TokenResponse
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
                } else {
                    _loginState.value = LoginUserState.Error("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUserState.Error("Error en el login: ${e.message}")
            }
        }
    }

    // Register user function that handles TokenResponse
    fun registerUser(user: RegisterRequest) {
        viewModelScope.launch {
            try {
                _registerState.value = RegisterUserState.Loading
                val response = usuarioApiService.register(user)

                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    _registerState.value = RegisterUserState.Success(tokenResponse!!)
                    println("Registration successful: ${tokenResponse.token}")
                } else {
                    _registerState.value = RegisterUserState.Error("Registration failed: ${response.message()}")
                    println("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _registerState.value = RegisterUserState.Error("Error during registration: ${e.message}")
                println("Error during registration: ${e.message}")
            }
        }
    }
}

