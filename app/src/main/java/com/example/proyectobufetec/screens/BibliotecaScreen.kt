// com.example.proyectobufetec/screens/BibliotecaScreen.kt

package com.example.proyectobufetec.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@Composable
fun BibliotecaScreen(navController: NavController, appViewModel: UserViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Lista de links
    val links = listOf(
        "https://example.com/link1",
        "https://example.com/link2",
        "https://example.com/link3"
        // Agregar más links aquí
    )

    Spacer(modifier = Modifier.height(40.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Text(text = "Bienvenidos a la Biblioteca", modifier = Modifier.padding(top = 30.dp))

        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(text = "Búsqueda") },
            modifier = Modifier.fillMaxWidth().padding(top = 70.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de links
        LazyColumn {
            items(links) { link ->
                Text(
                    text = link,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            // Abrir el link en el navegador
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            context.startActivity(intent)
                        }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
