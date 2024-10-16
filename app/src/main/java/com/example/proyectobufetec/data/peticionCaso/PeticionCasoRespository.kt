// com.example.proyectobufetec.data.repository.PeticionCasoRepository.kt

package com.example.proyectobufetec.data.repository

import com.example.proyectobufetec.data.api.PeticionCasoApiService
import com.example.proyectobufetec.data.api.PeticionCasoRequest

class PeticionCasoRepository(private val apiService: PeticionCasoApiService) {

    suspend fun createPeticionCaso(descripcion: String) =
        apiService.createPeticionCaso(PeticionCasoRequest(descripcion))
}
