package com.example.proyectobufetec.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectobufetec.data.abogado.AbogadoRepository

class AbogadoViewModelFactory(
    private val abogadoRepository: AbogadoRepository,
    private val context: Context // Add context as a parameter
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AbogadoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AbogadoViewModel(abogadoRepository, context) as T // Pass context to ViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
