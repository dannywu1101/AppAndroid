package com.example.proyectobufetec

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.proyectobufetec.data.abogado.AbogadoRepository
import com.example.proyectobufetec.data.biblioteca.BibliotecaRepository
import com.example.proyectobufetec.data.caso.CasoRepository
import com.example.proyectobufetec.data.chatbot.ChatRepository
import com.example.proyectobufetec.data.network.RetrofitInstance
import com.example.proyectobufetec.data.network.TokenManager
import com.example.proyectobufetec.navigation.AppNavHost
import com.example.proyectobufetec.ui.theme.NavTemplateTheme
import com.example.proyectobufetec.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var abogadoViewModel: AbogadoViewModel
    private lateinit var bibliotecaViewModel: BibliotecaViewModel
    private lateinit var casoViewModel: CasoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize TokenManager and ViewModels
        TokenManager.initialize(applicationContext)
        initializeViewModels()

        setContent {
            NavTemplateTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        appViewModel = userViewModel,
                        chatViewModel = chatViewModel,
                        abogadoViewModel = abogadoViewModel,
                        bibliotecaViewModel = bibliotecaViewModel,
                        casoViewModel = casoViewModel,
                        context = this@MainActivity,
                        padding = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Check token validity
        lifecycleScope.launch {
            val isValid = withContext(Dispatchers.IO) { TokenManager.isTokenValid() }
            if (isValid) {
                Log.i("MainActivity", "Token verified.")
            } else {
                Log.w("MainActivity", "Invalid token.")
            }
        }
    }

    private fun initializeViewModels() {
        val usuarioApiService = RetrofitInstance.getUsuarioApi()
        userViewModel = ViewModelProvider(
            this, UserViewModelFactory(usuarioApiService, TokenManager)
        )[UserViewModel::class.java]

        chatViewModel = ViewModelProvider(
            this, ChatViewModelFactory(ChatRepository(RetrofitInstance.getChatApi()))
        )[ChatViewModel::class.java]

        abogadoViewModel = ViewModelProvider(
            this, AbogadoViewModelFactory(AbogadoRepository(RetrofitInstance.getAbogadoApi()), this)
        )[AbogadoViewModel::class.java]

        bibliotecaViewModel = ViewModelProvider(
            this, BibliotecaViewModelFactory(BibliotecaRepository(RetrofitInstance.getBibliotecaApi()))
        )[BibliotecaViewModel::class.java]

        casoViewModel = ViewModelProvider(
            this, CasoViewModelFactory(CasoRepository(RetrofitInstance.getCasoApi()))
        )[CasoViewModel::class.java]
    }
}
