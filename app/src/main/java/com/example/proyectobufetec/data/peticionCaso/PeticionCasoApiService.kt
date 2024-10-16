// com.example.proyectobufetec.data.api.PeticionCasoApiService.kt

package com.example.proyectobufetec.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class PeticionCasoRequest(val descripcion: String)

data class PeticionCasoResponse(val id: Int, val id_user: Int, val descripcion: String)

interface PeticionCasoApiService {
    @POST("peticionCaso/")
    suspend fun createPeticionCaso(@Body request: PeticionCasoRequest): Response<PeticionCasoResponse>
}
