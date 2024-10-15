// com.example.proyectobufetec/screens/abogado/LegalCasesScreen.kt

package com.example.proyectobufetec.screens.abogado

import android.content.Intent
import android.net.Uri
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.CasoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalCasesScreen(
    navController: NavController,
    casoViewModel: CasoViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    val abogadoCasos by casoViewModel.abogadoCasos.collectAsState()
    val isLoading by casoViewModel.isLoading.collectAsState()
    val errorMessage by casoViewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        casoViewModel.fetchAbogadoCasos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar casos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Unknown error",
                        color = TecBlue,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    // Filtered cases and LazyColumn for scrolling content
                    val filteredCases = abogadoCasos.filter {
                        it.numero_expediente.contains(searchQuery, ignoreCase = true) ||
                                searchQuery.isEmpty()
                    }

                    if (filteredCases.isEmpty()) {
                        Text(
                            text = "No se encontraron resultados",
                            color = TecBlue,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredCases) { legalCase ->
                                LegalCaseItem(
                                    expediente = legalCase.numero_expediente,
                                    nombre = legalCase.clienteName,
                                    casoId = legalCase.id,
                                    casoViewModel = casoViewModel,
                                    context = LocalContext.current
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegalCaseItem(
    expediente: String,
    nombre: String,
    casoId: Int,
    casoViewModel: CasoViewModel,
    context: Context
) {
    var isExpanded by remember { mutableStateOf(false) }
    val casoFiles by casoViewModel.casoFiles.collectAsState()
    val isLoading by casoViewModel.isLoading.collectAsState()
    val errorMessage by casoViewModel.errorMessage.collectAsState()

    LaunchedEffect(isExpanded) {
        if (isExpanded && casoFiles.isEmpty() && !isLoading) {
            casoViewModel.fetchCasoFiles(casoId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .background(TecBlue, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = expediente,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = nombre,
                fontSize = 18.sp,
                color = White,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(2f)
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Unknown error",
                        color = White,
                        textAlign = TextAlign.Center
                    )
                }
                casoFiles.isEmpty() -> {
                    Text(
                        text = "No files available for this case.",
                        color = White,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    LazyColumn {
                        items(casoFiles) { file ->
                            CasoFileItem(
                                titulo = file.titulo,
                                descripcion = file.descripcion,
                                link = file.presignedUrl,
                                context = context
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CasoFileItem(titulo: String, descripcion: String, link: String, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp))
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
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = descripcion,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}