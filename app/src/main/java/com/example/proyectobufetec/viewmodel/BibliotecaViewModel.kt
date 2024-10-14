// com.example.proyectobufetec.viewmodel.BibliotecaViewModel.kt

package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobufetec.data.biblioteca.BibliotecaRepository
import com.example.proyectobufetec.data.biblioteca.BibliotecaResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BibliotecaViewModel(
    private val bibliotecaRepository: BibliotecaRepository
) : ViewModel() {

    // State to hold the list of biblioteca files
    private val _files = MutableStateFlow<List<BibliotecaResponse>>(emptyList())
    val files: StateFlow<List<BibliotecaResponse>> = _files

    // Loading state to show progress indicator
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Error message state to show any errors
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Fetch files from the biblioteca repository
    fun fetchBibliotecaFiles() {
        _isLoading.value = true // Set loading to true
        viewModelScope.launch {
            val result = bibliotecaRepository.getBibliotecaFiles()
            result.onSuccess { files ->
                if (files.isNotEmpty()) {
                    _files.value = files // Update files state
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "No files available."
                }
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Error fetching files."
            }
            _isLoading.value = false // Set loading to false
        }
    }
}
