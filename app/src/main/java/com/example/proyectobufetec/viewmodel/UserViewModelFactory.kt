// com.example.proyectobufetec/viewmodel/UserViewModelFactory.kt

package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectobufetec.data.usuario.UsuarioApiService

class UserViewModelFactory(private val usuarioApiService: UsuarioApiService) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(usuarioApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
