// com.example.proyectobufetec/viewmodel/BibliotecaViewModelFactory

package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectobufetec.data.biblioteca.BibliotecaRepository

class BibliotecaViewModelFactory(
    private val repository: BibliotecaRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BibliotecaViewModel::class.java)) {
            return BibliotecaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
