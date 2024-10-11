package com.example.proyectobufetec.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectobufetec.data.chatbot.ChatRepository

class ChatViewModelFactory(
    private val chatRepository: ChatRepository,
    private val context: Context  // Added context parameter
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(chatRepository, context) as T  // Pass context to the ViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
