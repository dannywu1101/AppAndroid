package com.example.proyectobufetec.data.network

import android.util.Log
import com.example.proyectobufetec.data.abogado.AbogadoApiService
import com.example.proyectobufetec.data.biblioteca.BibliotecaApiService
import com.example.proyectobufetec.data.usuario.UsuarioApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://bufetec-postgres.onrender.com/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Function to create a Retrofit instance with an optional auth token
    private fun getRetrofit(authToken: String?): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(authToken))  // Add the custom AuthInterceptor
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Service for Chat API, which now requires token support
    fun getChatApi(authToken: String?): ChatApiService {
        Log.d("RetrofitInstance", "getChatApi called with token: $authToken")
        return getRetrofit(authToken).create(ChatApiService::class.java)
    }

    // Service for Usuario API, with optional token support
    fun getUsuarioApi(authToken: String?): UsuarioApiService {
        Log.d("RetrofitInstance", "getUsuarioApi called with token: $authToken")
        return getRetrofit(authToken).create(UsuarioApiService::class.java)
    }

    // Service for Abogado API, with optional token support
    fun getAbogadoApi(authToken: String?): AbogadoApiService {
        Log.d("RetrofitInstance", "getAbogadoApi called with token: $authToken")
        return getRetrofit(authToken).create(AbogadoApiService::class.java)
    }

    fun getBibliotecaApi(authToken: String?): BibliotecaApiService {
        return getRetrofit(authToken).create(BibliotecaApiService::class.java)
    }
}
