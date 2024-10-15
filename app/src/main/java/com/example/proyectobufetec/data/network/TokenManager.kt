// com.example.proyectobufetec/data/network/TokenManager

package com.example.proyectobufetec.data.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

object TokenManager {

    private lateinit var sharedPreferences: SharedPreferences
    private val client = OkHttpClient.Builder().build()

    fun initialize(context: Context) {
        if (!::sharedPreferences.isInitialized) {
            Log.d("TokenManager", "Initializing TokenManager")
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            sharedPreferences = EncryptedSharedPreferences.create(
                "auth_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun saveToken(token: String) {
        Log.d("TokenManager", "Saving token: $token")
        sharedPreferences.edit().putString("auth_token", token).apply()
        RetrofitInstance.rebuildRetrofit() // Ensure Retrofit uses the new token
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString("auth_token", null)
        Log.d("TokenManager", "Retrieved token: $token")
        return token
    }

    fun clearToken() {
        Log.d("TokenManager", "Clearing token")
        sharedPreferences.edit().remove("auth_token").apply()
    }

    suspend fun isTokenValid(): Boolean {
        val token = getToken() ?: return false
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("https://bufetec-postgres.onrender.com/api/usuarios/verify")
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        Log.d("TokenManager", "Token is valid.")
                        true
                    } else {
                        Log.w("TokenManager", "Invalid token.")
                        clearToken()
                        false
                    }
                }
            } catch (e: Exception) {
                Log.e("TokenManager", "Token validation error: ${e.message}")
                clearToken()
                false
            }
        }
    }
}
