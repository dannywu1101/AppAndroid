// com.example.proyectobufetec.viewmodel.BibliotecaViewModel

package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobufetec.data.biblioteca.BibliotecaRepository
import com.example.proyectobufetec.data.biblioteca.BibliotecaResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BibliotecaViewModel(private val bibliotecaRepository: BibliotecaRepository) : ViewModel() {

    private val _files = MutableStateFlow<List<BibliotecaResponse>>(emptyList())
    val files: StateFlow<List<BibliotecaResponse>> = _files

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Fetch files from Biblioteca
    fun fetchBibliotecaFiles() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = bibliotecaRepository.getBibliotecaFiles()
            result.onSuccess { files ->
                _files.value = files
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
            _isLoading.value = false
        }
    }
}
