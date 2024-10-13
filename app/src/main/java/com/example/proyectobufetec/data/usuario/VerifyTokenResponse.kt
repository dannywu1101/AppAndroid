// data/usuario/VerifyTokenResponse.kt
package com.example.proyectobufetec.data.usuario

data class VerifyTokenResponse(
    val message: String,
    val user: User  // Assuming `User` is the class representing user data
)

data class User(
    val id: Int,
    val email: String,
    val role: String
)
