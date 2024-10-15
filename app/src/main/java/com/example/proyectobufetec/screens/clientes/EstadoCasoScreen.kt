// com.example.proyectobufetec/screens/clientes/EstadoCasoScreen.kt

package com.example.proyectobufetec.screens.clientes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.CasoViewModel

@Composable
fun EstadoCasoScreen(
    navController: NavController,
    casoViewModel: CasoViewModel
) {
    val casoState by casoViewModel.usuarioCasos.collectAsState()
    val errorMessage by casoViewModel.errorMessage.collectAsState()

    // Fetch the case on launch
    LaunchedEffect(Unit) {
        casoViewModel.fetchUsuarioCasos()
    }

    Spacer(modifier = Modifier.height(40.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Error fetching case details",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            casoState.isEmpty() -> {
                Text(
                    text = "No cases found",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                val caso = casoState.first() // Assuming the user has only one case

                // Shaded gray box containing the case information
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        // Abogado Encargado
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Abogado Encargado:", fontWeight = FontWeight.SemiBold)
                            Text(text = caso.abogadoName)
                        }

                        // Numero Expediente
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Expediente:", fontWeight = FontWeight.SemiBold)
                            Text(text = caso.numero_expediente)
                        }

                        // Estado del Caso
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Estado del Caso:", fontWeight = FontWeight.SemiBold)
                            Text(text = caso.estado)
                        }

                        // Descripción del Caso
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(text = "Descripción del Caso:", fontWeight = FontWeight.SemiBold)
                            Text(text = caso.descripcion)
                        }
                    }
                }
            }
        }
    }
}
