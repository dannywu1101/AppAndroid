// com.example.proyectobufetec/viewmodel/CasoViewModel

package com.example.proyectobufetec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectobufetec.data.caso.CasoRepository
import com.example.proyectobufetec.data.caso.CasoResponseUser
import com.example.proyectobufetec.data.caso.CasoResponseAbogado
import com.example.proyectobufetec.data.caso.CasoFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CasoViewModel(private val casoRepository: CasoRepository) : ViewModel() {

    // State to hold the list of cases for the Usuario
    private val _usuarioCasos = MutableStateFlow<List<CasoResponseUser>>(emptyList())
    val usuarioCasos: StateFlow<List<CasoResponseUser>> = _usuarioCasos

    // State to hold the list of cases for the Abogado
    private val _abogadoCasos = MutableStateFlow<List<CasoResponseAbogado>>(emptyList())
    val abogadoCasos: StateFlow<List<CasoResponseAbogado>> = _abogadoCasos

    // State to hold the files for a specific case
    private val _casoFiles = MutableStateFlow<List<CasoFile>>(emptyList())
    val casoFiles: StateFlow<List<CasoFile>> = _casoFiles

    // Loading state to indicate ongoing API requests
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Error message state to display any errors that occur
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Fetch Usuario cases from the API
    fun fetchUsuarioCasos() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = casoRepository.getUsuarioCasos()
            result.onSuccess { casos ->
                _usuarioCasos.value = casos
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
            _isLoading.value = false
        }
    }

    // Fetch Abogado cases from the API
    fun fetchAbogadoCasos() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = casoRepository.getAbogadoCasos()
            result.onSuccess { casos ->
                _abogadoCasos.value = casos
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
            _isLoading.value = false
        }
    }

    fun fetchCasoByID(caseID: Int): CasoResponseAbogado? {
        return abogadoCasos.value.find { it.id == caseID }
    }

    fun fetchCasoFiles(casoId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = casoRepository.getCasoFiles(casoId)
            result.onSuccess { files ->
                _casoFiles.value = files
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
            _isLoading.value = false
        }
    }

}
