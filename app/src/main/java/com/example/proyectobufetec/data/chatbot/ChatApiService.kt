// com.example.proyectobufetec/data/network/ChatApiService

package com.example.proyectobufetec.data.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatApiService {

    @POST("chat")
    suspend fun getChatResponse(
        @Body body: RequestBody,
        @Header("Authorization") token: String  // Add Authorization header
    ): Response<ResponseBody>
}
