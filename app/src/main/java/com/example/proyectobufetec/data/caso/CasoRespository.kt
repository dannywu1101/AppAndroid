// com.example.proyectobufetec/data/caso/CasoRepository.kt

package com.example.proyectobufetec.data.caso

class CasoRepository(private val casoApiService: CasoApiService) {

    suspend fun getUsuarioCasos(): Result<List<CasoResponseUser>> = try {
        val response = casoApiService.getUsuarioCasos()
        if (response.isSuccessful) {
            Result.success(response.body() ?: emptyList())
        } else {
            Result.failure(Exception("Failed to fetch Usuario cases: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getAbogadoCasos(): Result<List<CasoResponseAbogado>> = try {
        val response = casoApiService.getAbogadoCasos()
        if (response.isSuccessful) {
            Result.success(response.body() ?: emptyList())
        } else {
            Result.failure(Exception("Failed to fetch Abogado cases: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getCasoFiles(casoId: Int): Result<List<CasoFile>> = try {
        val response = casoApiService.getCasoFiles(casoId)
        if (response.isSuccessful) {
            Result.success(response.body() ?: emptyList())
        } else {
            Result.failure(Exception("Failed to fetch files: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
