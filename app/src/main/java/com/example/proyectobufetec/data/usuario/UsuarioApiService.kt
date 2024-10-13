// data/usuario/UsuarioApiService.kt
package com.example.proyectobufetec.data.usuario

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioApiService {

    @POST("usuarios/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<TokenResponse>

    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<TokenResponse>

    @GET("usuarios/verify")  // Token verification endpoint
    suspend fun verifyToken(@Header("Authorization") token: String): Response<VerifyTokenResponse>
}
