// com.example.proyectobufetec/service/UserService

package com.example.proyectobufetec.service


import com.example.proyectobufetec.data.LoginUserRequest
import com.example.proyectobufetec.data.LoginUserResponse
import com.example.proyectobufetec.data.RegisterUserRequest
import com.example.proyectobufetec.data.RegisterUserResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {


    companion object {
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val instance: UserService = Retrofit.Builder()
            .baseUrl("https://render-starter-postgredb.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(UserService::class.java)
    }

    @POST("api/users/register")
    suspend fun addUser(@Body user: RegisterUserRequest) : RegisterUserResponse


    @POST("api/users/login")
    suspend fun loginUser(@Body user: LoginUserRequest) : LoginUserResponse

}