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
import com.example.proyectobufetec.data.network.RetrofitInstance
import com.example.proyectobufetec.navigation.AppNavHost
import com.example.proyectobufetec.ui.theme.NavTemplateTheme
import com.example.proyectobufetec.viewmodel.UserViewModel
import com.example.proyectobufetec.viewmodel.UserViewModelFactory
import com.example.proyectobufetec.viewmodel.ChatViewModel
import com.example.proyectobufetec.viewmodel.ChatViewModelFactory
import com.example.proyectobufetec.viewmodel.AbogadoViewModel
import com.example.proyectobufetec.viewmodel.AbogadoViewModelFactory
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.proyectobufetec.data.chatbot.ChatRepository
import com.example.proyectobufetec.data.abogado.AbogadoRepository

class MainActivity : ComponentActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var abogadoViewModel: AbogadoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the token from EncryptedSharedPreferences
        val token = getTokenFromEncryptedPrefs()

        // Initialize UserViewModel using the token for authenticated calls
        val usuarioApiService = RetrofitInstance.getUsuarioApi(token)
        val userFactory = UserViewModelFactory(usuarioApiService, this)
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)

        // Initialize ChatViewModel
        val chatApiService = RetrofitInstance.getChatApi(token)
        val chatRepository = ChatRepository(chatApiService)
        val chatFactory = ChatViewModelFactory(chatRepository, this)
        chatViewModel = ViewModelProvider(this, chatFactory).get(ChatViewModel::class.java)

        // Initialize AbogadoViewModel
        val abogadoApiService = RetrofitInstance.getAbogadoApi(token)  // Add this service in RetrofitInstance
        val abogadoRepository = AbogadoRepository(abogadoApiService)
        val abogadoFactory = AbogadoViewModelFactory(abogadoRepository, this)
        abogadoViewModel = ViewModelProvider(this, abogadoFactory).get(AbogadoViewModel::class.java)

        setContent {
            NavTemplateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        appViewModel = userViewModel,
                        chatViewModel = chatViewModel,
                        abogadoViewModel = abogadoViewModel,  // Pass the AbogadoViewModel
                        context = this,
                        padding = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Function to retrieve the token from EncryptedSharedPreferences
    private fun getTokenFromEncryptedPrefs(): String? {
        val masterKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return sharedPreferences.getString("auth_token", null)
    }
}
