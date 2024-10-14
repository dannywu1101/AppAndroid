package com.example.proyectobufetec.data.caso

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class CasoResponseUser(
    val numero_expediente: String,
    val descripcion: String,
    val estado: String,
    val abogadoName: String
)

data class CasoResponseAbogado(
    val id: Int,
    val numero_expediente: String,
    val descripcion: String,
    val estado: String,
    val clienteName: String
)

data class CasoFile(
    val id: Int,
    val presignedUrl: String,
    val fileName: String
)

interface CasoApiService {

    // Get all cases for a Cliente based on the token
    @GET("casos/usuarioCaso")
    suspend fun getUsuarioCasos(): Response<List<CasoResponseUser>>

    // Get all cases for an Abogado based on the token
    @GET("casos/abogadoCasos")
    suspend fun getAbogadoCasos(): Response<List<CasoResponseAbogado>>

    // Get files associated with a specific case
    @GET("casos/{id}/files")
    suspend fun getCasoFiles(@Path("id") casoId: Int): Response<List<CasoFile>>
}

