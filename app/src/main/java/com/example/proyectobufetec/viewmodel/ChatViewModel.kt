package com.example.proyectobufetec.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.proyectobufetec.data.chatbot.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository, private val context: Context) : ViewModel() {

    // Holds the current chatbot response
    private val _chatBotResponse = MutableStateFlow("¡Hola! Soy tu asistente virtual, ¿en qué puedo ayudarte hoy?")
    val chatBotResponse: StateFlow<String> = _chatBotResponse

    // Holds the loading state for chatbot responses
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Sends a message to the chat bot and updates the state with the response
    fun sendMessage(userMessage: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val authToken = getAuthToken()

            // Check if the token exists
            if (authToken != null) {
                val result = chatRepository.getChatBotResponse(userMessage, authToken)
                result.onSuccess { response ->
                    _chatBotResponse.value = response
                }.onFailure { error ->
                    _chatBotResponse.value = "Error: ${error.message}"
                }
            } else {
                _chatBotResponse.value = "Error: No valid authentication token found."
            }
            _isLoading.value = false
        }
    }

    // Fetches the auth token from EncryptedSharedPreferences
    private fun getAuthToken(): String? {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return sharedPreferences.getString("auth_token", null)
    }
}
