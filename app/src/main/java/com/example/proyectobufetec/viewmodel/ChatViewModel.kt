package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobufetec.data.chatbot.ChatRepository
import com.example.proyectobufetec.data.network.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class ChatViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {

    // Holds the chatbot's latest response
    private val _chatBotResponse = MutableStateFlow("¡Hola! Soy tu asistente virtual.")
    val chatBotResponse: StateFlow<String> = _chatBotResponse

    // Holds the loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Send message to the chatbot
    fun sendMessage(userMessage: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val authToken = TokenManager.getToken()  // Get the token from TokenManager

            if (authToken != null) {
                Log.d("ChatViewModel", "Sending message with token: $authToken")
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
}
