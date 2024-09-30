// data/usuario/RegisterRequest.kt
package com.example.proyectobufetec.data.usuario

data class RegisterRequest(
    val nombre: String,
    val fecha_nacimiento: String,
    val sexo: String,
    val email: String,
    val contrasena: String,
    val rol: String = "Usuario",
    val numero_telefono: String = "0"
)