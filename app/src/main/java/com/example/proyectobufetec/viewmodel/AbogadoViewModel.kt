// com.example.proyectobufetec/viewmodel/AbogadoViewModel

package com.example.proyectobufetec.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobufetec.data.abogado.AbogadoRepository
import com.example.proyectobufetec.data.abogado.AbogadoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AbogadoViewModel(
    private val abogadoRepository: AbogadoRepository,
    private val context: Context
) : ViewModel() {

    private val _abogados = MutableStateFlow<List<AbogadoResponse>>(emptyList())
    val abogados: StateFlow<List<AbogadoResponse>> = _abogados

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Fetch all Abogados
    fun fetchAbogados() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = abogadoRepository.getAbogados()
            result.onSuccess { abogados ->
                _abogados.value = abogados
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
            _isLoading.value = false
        }
    }
}
