// com.example.proyectobufetec/MainActivity.kt

package com.example.proyectobufetec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectobufetec.navigation.AppNavHost
import com.example.proyectobufetec.service.UserService
import com.example.proyectobufetec.ui.theme.NavTemplateTheme
import com.example.proyectobufetec.viewmodel.UserViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavTemplateTheme {
                val userViewModel = UserViewModel(UserService.instance)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(userViewModel, Modifier.padding(innerPadding))
                }
            }
        }
    }
}



