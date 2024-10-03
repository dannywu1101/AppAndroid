// com.example.proyectobufetec/screens/clientes/BibliotecaScreen.kt

package com.example.proyectobufetec.screens.clientes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import com.example.proyectobufetec.ui.theme.TecBlue


@Composable
fun BibliotecaScreen(navController: NavController, appViewModel: UserViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Lista de títulos, links y descripciones
    val links = listOf(
        Triple("Documentación de Compose", "https://developer.android.com/jetpack/compose/documentation", "Guía completa de Jetpack Compose para Android."),
        Triple("GitHub", "https://github.com", "Plataforma para control de versiones y colaboración en código."),
        Triple("Google", "https://www.google.com", "El motor de búsqueda más utilizado en el mundo."),
        Triple("Divorcios", "https://mexico.justia.com/derecho-de-familia/divorcio/", "Liga de información para personas que necesitan ayuda"),
        Triple("Divorcios", "https://mexico.justia.com/derecho-de-familia/divorcio/", "Liga de información para personas que necesitan ayuda"),
        Triple("Divorcios", "https://mexico.justia.com/derecho-de-familia/divorcio/", "Liga de información para personas que necesitan ayuda"),

    // Agrega más títulos, links y descripciones aquí
    )

    // Filtrar los enlaces en función de la consulta de búsqueda
    val filteredLinks = links.filter {
        it.first.contains(searchQuery, ignoreCase = true) ||  // Filtrar por título
                it.third.contains(searchQuery, ignoreCase = true)     // Filtrar por descripción
    }

    Spacer(modifier = Modifier.height(40.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(text = "Búsqueda") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de títulos y descripciones filtrada
        LazyColumn {
            items(filteredLinks) { (title, link, description) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp)) // Añadir sombra
                        .background(Color.White, RoundedCornerShape(8.dp)) // Fondo blanco con esquinas redondeadas
                        .padding(16.dp) // Padding interno
                ) {
                    // Título con estilo de link
                    Text(
                        text = title,
                        color = TecBlue, // Cambiar el color a azul
                        fontSize = 18.sp,   // Ajustar tamaño de la fuente
                        fontWeight = FontWeight.Bold, // Hacer el texto más visible
                        textDecoration = TextDecoration.Underline, // Subrayado
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Abrir el link en el navegador
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                context.startActivity(intent)
                            }
                    )
                    // Descripción debajo del título
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = Color.Gray,  // Color gris para la descripción
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
