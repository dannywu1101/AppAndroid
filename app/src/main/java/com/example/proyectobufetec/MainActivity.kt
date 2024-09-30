package com.example.proyectobufetec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.proyectobufetec.navigation.AppNavHost
import com.example.proyectobufetec.ui.theme.NavTemplateTheme
import com.example.proyectobufetec.viewmodel.UserViewModel
import com.example.proyectobufetec.viewmodel.UserViewModelFactory
import com.example.proyectobufetec.data.network.RetrofitInstance // Import RetrofitInstance

class MainActivity : ComponentActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Use the RetrofitInstance to get the UsuarioApiService
        val usuarioApiService = RetrofitInstance.api

        // Initialize the UserViewModel using UserViewModelFactory
        val factory = UserViewModelFactory(usuarioApiService)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        setContent {
            NavTemplateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(appViewModel = userViewModel, padding = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
