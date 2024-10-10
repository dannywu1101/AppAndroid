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
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.proyectobufetec.data.repository.ChatRepository

class MainActivity : ComponentActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the token from EncryptedSharedPreferences
        val token = getTokenFromEncryptedPrefs()

        // Initialize UserViewModel using the token for authenticated calls
        val usuarioApiService = RetrofitInstance.getUsuarioApi(token) // Fetch API with auth token
        val userFactory = UserViewModelFactory(usuarioApiService, this)
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)

        // Initialize ChatViewModel using the token for authenticated calls
        val chatApiService = RetrofitInstance.getChatApi(token) // Pass token to chat API
        val chatRepository = ChatRepository(chatApiService) // Initialize repository with chat API
        val chatFactory = ChatViewModelFactory(chatRepository, this)
        chatViewModel = ViewModelProvider(this, chatFactory).get(ChatViewModel::class.java)

        setContent {
            NavTemplateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        appViewModel = userViewModel,
                        chatViewModel = chatViewModel,
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
