// com.example.proyectobufetec/viewmodel/UserViewModelFactory.kt

package com.example.proyectobufetec.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectobufetec.data.usuario.UsuarioApiService

class UserViewModelFactory(
    private val usuarioApiService: UsuarioApiService,
    private val context: Context  // Add Context to the factory
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            // Pass the context along with the service
            return UserViewModel(usuarioApiService, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
