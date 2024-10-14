package com.example.proyectobufetec.data.network

import android.content.Context
import android.util.Log
import com.example.proyectobufetec.data.abogado.AbogadoApiService
import com.example.proyectobufetec.data.biblioteca.BibliotecaApiService
import com.example.proyectobufetec.data.caso.CasoApiService
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

    private var retrofit: Retrofit? = null

    fun initialize(context: Context) {
        TokenManager.initialize(context)
        rebuildRetrofit()
    }

    private fun buildRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Ensure logging is working
            .addInterceptor(AuthInterceptor(TokenManager))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getRetrofit(): Retrofit {
        return retrofit ?: buildRetrofit().also { retrofit = it }
    }

    fun rebuildRetrofit() {
        Log.d("RetrofitInstance", "Rebuilding Retrofit instance")
        retrofit = buildRetrofit()
    }

    fun getUsuarioApi(): UsuarioApiService = getRetrofit().create(UsuarioApiService::class.java)
    fun getChatApi(): ChatApiService = getRetrofit().create(ChatApiService::class.java)
    fun getAbogadoApi(): AbogadoApiService = getRetrofit().create(AbogadoApiService::class.java)
    fun getBibliotecaApi(): BibliotecaApiService = getRetrofit().create(BibliotecaApiService::class.java)
    fun getCasoApi(): CasoApiService = getRetrofit().create(CasoApiService::class.java)
}
