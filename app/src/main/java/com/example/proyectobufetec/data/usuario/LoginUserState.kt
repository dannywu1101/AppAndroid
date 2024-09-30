// com.example.proyectobufetec.data.usuario.LoginUserState.kt

package com.example.proyectobufetec.data.usuario

// Sealed class to handle login states
sealed class LoginUserState {
    object Initial : LoginUserState()  // The initial state before login
    object Loading : LoginUserState()  // Loading state during login
    data class Success(val tokenResponse: TokenResponse) : LoginUserState()  // Success state with the TokenResponse
    data class Error(val errorMessage: String) : LoginUserState()  // Error state with the error message
}
