// com.example.proyectobufetec/data/network/TokenManager

package com.example.proyectobufetec.data.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

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
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearToken() {
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
                client.newCall(request).execute().use { it.isSuccessful }
            } catch (e: Exception) {
                clearToken()
                false
            }
        }
    }

    fun extractRoleFromToken(token: String): String {
        val parts = token.split(".")
        if (parts.size < 2) return "Guest"
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        return JSONObject(payload).getString("role")
    }
}
