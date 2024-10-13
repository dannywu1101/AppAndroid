package com.example.proyectobufetec.data.biblioteca

import retrofit2.Response
import retrofit2.http.GET

interface BibliotecaApiService {

    // Get all files from Biblioteca
    @GET("biblioteca")
    suspend fun getBibliotecaFiles(): Response<List<BibliotecaResponse>>
}
