package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectobufetec.data.biblioteca.BibliotecaRepository

class BibliotecaViewModelFactory(
    private val bibliotecaRepository: BibliotecaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BibliotecaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BibliotecaViewModel(bibliotecaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
