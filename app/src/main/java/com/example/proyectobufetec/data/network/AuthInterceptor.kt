package com.example.proyectobufetec.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authToken: String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Log the request URL and token information for debugging
        Log.d("AuthInterceptor", "Request URL: ${originalRequest.url}")
        Log.d("AuthInterceptor", "Auth Token: ${authToken ?: "No Token"}")

        // Check if the request is not a login request
        return if (!originalRequest.url.encodedPath.contains("/usuarios/login")) {
            if (authToken != null) {
                // Add the Authorization header with the token for non-login requests
                Log.d("AuthInterceptor", "Adding token to request: $authToken")
                val modifiedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $authToken") // Use header() instead of addHeader() to avoid multiple headers
                    .build()

                // Log the headers added to the request for debugging
                Log.d("AuthInterceptor", "Modified Request Headers: ${modifiedRequest.headers}")

                // Proceed with the modified request
                chain.proceed(modifiedRequest)
            } else {
                // Log that no token was available to be added
                Log.d("AuthInterceptor", "No token available to add to request.")
                chain.proceed(originalRequest)
            }
        } else {
            // For login requests, no token is added
            Log.d("AuthInterceptor", "Login request detected, no token added.")
            chain.proceed(originalRequest)
        }
    }
}
