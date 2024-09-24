// com.example.proyectobufetec/viewmodel/UserViewModel.kt

package com.example.proyectobufetec.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.proyectobufetec.data.LoginUserRequest
import com.example.proyectobufetec.data.LoginUserResponse
import com.example.proyectobufetec.data.RegisterUserRequest
import com.example.proyectobufetec.data.RegisterUserResponse
import com.example.proyectobufetec.service.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class UserViewModel(private val userService: UserService) : ViewModel() {

    var email by mutableStateOf("Prueba")
    var password by mutableStateOf("1234")

    var isUserLogged by mutableStateOf(false)

    private val _login = MutableStateFlow<LoginUserState>(LoginUserState.Initial)
    val login: StateFlow<LoginUserState> = _login

    private val _register = MutableStateFlow<RegisterUserState>(RegisterUserState.Initial)
    val register: StateFlow<RegisterUserState> = _register


    fun registerUser(user: RegisterUserRequest) {

        viewModelScope.launch {

            try {
                _register.value = RegisterUserState.Loading
                val response = userService.addUser(user)
                _register.value = RegisterUserState.Success(response)
            } catch (e: Exception) {
                _register.value = RegisterUserState.Error(e.message.toString())
            }
        }
    }


    fun loginUser(user: LoginUserRequest) {

        _login.value = LoginUserState.Initial
        viewModelScope.launch {

            try {
                _login.value = LoginUserState.Loading
                val response = userService.loginUser(user)
                //isUserLogged = true
                _login.value = LoginUserState.Success(response)
            } catch (e: Exception) {
                _login.value = LoginUserState.Error("Error en el login: " + e.message.toString())
            }
        }
    }





}


sealed class LoginUserState {
    object Initial : LoginUserState()
    object Loading : LoginUserState()
    data class Success(val loginResponse: LoginUserResponse) : LoginUserState()
    data class Error(val errorMessage: String) : LoginUserState()
}

sealed class RegisterUserState {
    object Initial : RegisterUserState()
    object Loading : RegisterUserState()
    data class Success(val registerResponse: RegisterUserResponse) : RegisterUserState()
    data class Error(val errorMessage: String) : RegisterUserState()
}