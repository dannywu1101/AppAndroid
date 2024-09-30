// data/usuario/Usuario.kt
package com.example.proyectobufetec.data.usuario

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val fechaNacimiento: String, // Adjust format if needed
    val sexo: String,
    val email: String,
    val contrasena: String,
    val rol: String,
    val numeroTelefono: String
)