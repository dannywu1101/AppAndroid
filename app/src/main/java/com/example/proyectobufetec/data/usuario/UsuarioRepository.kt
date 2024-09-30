// data/usuario/UsuarioRepository.kt
package com.example.proyectobufetec.data.usuario

import com.example.proyectobufetec.data.network.RetrofitInstance
import retrofit2.Response

class UsuarioRepository {

    // Register a new Usuario
    suspend fun register(registerRequest: RegisterRequest): Response<TokenResponse> {
        return RetrofitInstance.api.register(registerRequest)
    }

    // Login a Usuario
    suspend fun login(loginRequest: LoginRequest): Response<TokenResponse> {
        return RetrofitInstance.api.login(loginRequest)
    }

}
