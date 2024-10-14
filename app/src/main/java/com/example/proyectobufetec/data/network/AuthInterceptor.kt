package com.example.proyectobufetec.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authToken = tokenManager.getToken()

        Log.d("AuthInterceptor", "Request URL: ${originalRequest.url}")
        Log.d("AuthInterceptor", "Auth Token: ${authToken ?: "No Token"}")

        return if (authToken != null && !originalRequest.url.encodedPath.endsWith("/login")) {
            Log.d("AuthInterceptor", "Adding Authorization header")
            val modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $authToken")
                .build()

            Log.d("AuthInterceptor", "Modified Headers: ${modifiedRequest.headers}")
            chain.proceed(modifiedRequest)
        } else {
            Log.d("AuthInterceptor", "Proceeding without token.")
            chain.proceed(originalRequest)
        }
    }
}
