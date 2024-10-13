package com.example.proyectobufetec.data.biblioteca

data class BibliotecaResponse(
    val descripcion: String,
    val presignedUrl: String,
    val fileName: String
)
