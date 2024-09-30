// com.example.proyectobufetec.data.usuatrio.RegisterUserState.kt

package com.example.proyectobufetec.viewmodel

import com.example.proyectobufetec.data.usuario.TokenResponse

// Sealed class to handle register states
sealed class RegisterUserState {
    object Initial : RegisterUserState()  // Initial state before registration starts
    object Loading : RegisterUserState()  // Loading state during registration
    data class Success(val tokenResponse: TokenResponse) : RegisterUserState()  // Success state with the TokenResponse
    data class Error(val errorMessage: String) : RegisterUserState()  // Error state with the error message
}
