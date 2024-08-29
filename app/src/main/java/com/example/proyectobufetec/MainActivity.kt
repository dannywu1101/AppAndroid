package com.example.proyectobufetec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.proyectobufetec.ui.theme.ProyectoBufetecTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoBufetecTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProyectoAndroid(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ProyectoAndroid(modifier: Modifier = Modifier) {
    //TODO: Add your UI code here
    // Marcelo
}


