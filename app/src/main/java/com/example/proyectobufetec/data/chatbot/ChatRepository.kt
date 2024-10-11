package com.example.proyectobufetec.data.chatbot

import android.util.Log
import com.example.proyectobufetec.data.network.ChatApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class ChatRepository(private val chatApiService: ChatApiService) {

    // Function to pass the auth token and message to the API call
    suspend fun getChatBotResponse(userMessage: String, authToken: String): Result<String> {
        return try {
            // Create a JSON object with the user message
            val jsonBody = JSONObject().apply {
                put("message", userMessage)
            }

            // Convert the JSON object to a RequestBody
            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaTypeOrNull())

            // Make the API call with the auth token included
            val response = chatApiService.getChatResponse(requestBody, authToken)

            // Handle the API response
            if (response.isSuccessful) {
                val bodyString = response.body()?.string()
                Log.d("ChatRepository", "Response Body: $bodyString")

                if (bodyString.isNullOrEmpty()) {
                    return Result.failure(Exception("Empty response from server"))
                }

                val jsonObject = JSONObject(bodyString)
                val textResult = jsonObject.optString("response")

                if (textResult.isEmpty()) {
                    return Result.failure(Exception("Malformed response: missing 'response' field"))
                }

                Result.success(textResult.trim())
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: HttpException) {
            Log.e("ChatRepository", "HTTP Exception: ${e.message()}")
            Result.failure(Exception("HTTP Exception: ${e.message}"))
        } catch (e: IOException) {
            Log.e("ChatRepository", "Network error: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("ChatRepository", "Unknown error: ${e.message}")
            Result.failure(e)
        }
    }
}
