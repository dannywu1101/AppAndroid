// com.example.proyectobufetec.screens/clientes/BibliotecaScreen.kt

package com.example.proyectobufetec.screens.clientes

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.BibliotecaViewModel
import androidx.compose.ui.Alignment

@Composable
fun BibliotecaScreen(navController: NavController, bibliotecaViewModel: BibliotecaViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Collect data from ViewModel
    val files by bibliotecaViewModel.files.collectAsState()
    val isLoading by bibliotecaViewModel.isLoading.collectAsState()
    val errorMessage by bibliotecaViewModel.errorMessage.collectAsState()

    // Fetch files when the screen loads
    LaunchedEffect(Unit) {
        bibliotecaViewModel.fetchBibliotecaFiles()
    }

    Spacer(modifier = Modifier.height(40.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage != null) {
            Text(
                text = errorMessage ?: "Unknown error",
                color = TecBlue,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(text = "Búsqueda") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter files based on the search query
            val filteredFiles = files.filter {
                it.titulo.contains(searchQuery, ignoreCase = true) ||
                        it.descripcion.contains(searchQuery, ignoreCase = true)
            }

            // List of filtered files
            LazyColumn {
                items(filteredFiles) { file ->
                    BibliotecaItem(
                        titulo = file.titulo,
                        descripcion = file.descripcion,
                        link = file.presignedUrl,
                        context = context
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun BibliotecaItem(titulo: String, descripcion: String, link: String, context: android.content.Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = titulo,
            color = TecBlue,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    context.startActivity(intent)
                }
        )
        Text(
            text = descripcion,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
