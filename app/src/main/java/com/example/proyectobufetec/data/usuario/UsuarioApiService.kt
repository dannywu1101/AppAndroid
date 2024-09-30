// data/usuario/UsuarioApiService.kt
package com.example.proyectobufetec.data.usuario

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface UsuarioApiService {

    // Register a new Usuario
    @POST("usuarios/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<TokenResponse>

    // Login a Usuario
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<TokenResponse>

}
