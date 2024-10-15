// com.example.proyectobufetec/data/abogado/AbogadoApliService

package com.example.proyectobufetec.data.abogado

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import com.example.proyectobufetec.data.abogado.AbogadoResponse

interface AbogadoApiService {

    // Fetch all Abogados
    @GET("abogados")
    suspend fun getAbogados(): Response<List<AbogadoResponse>>

    // Add a new Abogado
    @POST("abogados")
    suspend fun addAbogado(@Body abogado: AbogadoResponse): Response<AbogadoResponse>
}
