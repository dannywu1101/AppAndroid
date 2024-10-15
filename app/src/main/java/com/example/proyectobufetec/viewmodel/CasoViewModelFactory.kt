// com.example.proyectobufetec/viewmodel/CasoViewModelFactory

package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectobufetec.data.caso.CasoRepository

class CasoViewModelFactory(
    private val casoRepository: CasoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CasoViewModel::class.java)) {
            return CasoViewModel(casoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
