package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectobufetec.data.network.TokenManager
import com.example.proyectobufetec.data.usuario.UsuarioApiService

class UserViewModelFactory(
    private val usuarioApiService: UsuarioApiService,
    private val tokenManager: TokenManager // Use TokenManager here
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(usuarioApiService, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
