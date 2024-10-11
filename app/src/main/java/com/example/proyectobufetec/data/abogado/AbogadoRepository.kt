package com.example.proyectobufetec.data.abogado

import retrofit2.Response

class AbogadoRepository(private val abogadoApiService: AbogadoApiService) {

    // Fetch all Abogados from the API
    suspend fun getAbogados(): Result<List<AbogadoResponse>> {
        return try {
            val response: Response<List<AbogadoResponse>> = abogadoApiService.getAbogados()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error fetching Abogados: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Add a new Abogado
    suspend fun addAbogado(abogado: AbogadoResponse): Result<AbogadoResponse> {
        return try {
            val response = abogadoApiService.addAbogado(abogado)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error adding Abogado: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
