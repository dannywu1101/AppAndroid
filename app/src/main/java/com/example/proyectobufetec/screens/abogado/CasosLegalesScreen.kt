// com.example.proyectobufetec/screens/CasosLegalesScreen.kt

package com.example.proyectobufetec.screens.abogado

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalCasesScreen(navController: NavController, appViewModel: UserViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    data class LegalCase(
        val expediente: String,
        val cliente: String,
        val abogadoAsignado: String,
        val estado: String,
        val descripcion: String,
    )

    // Lista de casos legales con expediente, cliente, estado, descripción y abogado asignado
    val legalCases = listOf(
        LegalCase("253-2024", "Ignacio López", "Juan Pérez", "Abierto", "Caso de disputa por contrato."),
        LegalCase("254-2024", "Javier García", "María González", "Cerrado", "Caso de divorcio finalizado."),
        LegalCase("255-2024", "Cristina Martínez", "Carlos Hernández", "Pendiente", "Disputa laboral en proceso."),
        // Otros casos...
    )

    // Filtrado de casos basado en la búsqueda
    val filteredCases = legalCases.filter { it.expediente.contains(searchQuery, ignoreCase = true) || searchQuery.isEmpty() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 90.dp, start = 16.dp, end = 16.dp)
    ) {
        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar casos") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado con "Expediente" y "Nombre de Cliente"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Expediente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TecBlue,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Nombre de Cliente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TecBlue,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de casos legales
        Column(
            modifier = Modifier
                .verticalScroll(ScrollState(0))
                .fillMaxHeight()
        ) {
            if (filteredCases.isEmpty()) {
                Text(
                    text = "No se encontraron resultados",
                    modifier = Modifier.padding(16.dp),
                    color = TecBlue,
                    textAlign = TextAlign.Center
                )
            } else {
                filteredCases.forEach { legalCase ->
                    LegalCaseItem(expediente = legalCase.expediente, nombre = legalCase.cliente) {
                        // Navegar a la pantalla de InfoCasosLegalesScreen con los datos del caso
                        navController.navigate("info_casos_legales/${legalCase.expediente}/${legalCase.cliente}/${legalCase.abogadoAsignado}/${legalCase.estado}/${legalCase.descripcion}")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun LegalCaseItem(expediente: String, nombre: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = TecBlue, // background color
                shape = RoundedCornerShape(8.dp) // Rounded corners
            )
            .padding(16.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), // Padding around text
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
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.End
            )
        }
    }
}

