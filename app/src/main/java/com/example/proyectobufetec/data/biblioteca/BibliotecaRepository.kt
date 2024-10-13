package com.example.proyectobufetec.data.biblioteca

import retrofit2.Response

class BibliotecaRepository(private val bibliotecaApiService: BibliotecaApiService) {

    // Fetch all files from Biblioteca
    suspend fun getBibliotecaFiles(): Result<List<BibliotecaResponse>> {
        return try {
            val response: Response<List<BibliotecaResponse>> = bibliotecaApiService.getBibliotecaFiles()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error fetching Biblioteca files: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
