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
            Spacer(modifier = Modifier.height(80.dp))
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar por folio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
                                    navController = navController
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
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .background(TecBlue, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable {
                navController.navigate("info_casos_legales/$casoId")
            }
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
    }
}