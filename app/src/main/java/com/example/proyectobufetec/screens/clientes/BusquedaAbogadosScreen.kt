// com.example.proyectobufetec/screens/clientes/BusquedaAbogadosScreen.kt

package com.example.proyectobufetec.screens.clientes

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
import com.example.proyectobufetec.viewmodel.AbogadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaAbogadosScreen(navController: NavController, abogadoViewModel: AbogadoViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedEspecialidad by remember { mutableStateOf("Todas") }

    val abogados by abogadoViewModel.abogados.collectAsState()
    val isLoading by abogadoViewModel.isLoading.collectAsState()
    val errorMessage by abogadoViewModel.errorMessage.collectAsState()

    // Fetch abogados when the screen loads
    LaunchedEffect(Unit) {
        abogadoViewModel.fetchAbogados()
    }

    // Filtrado de abogados basado en la búsqueda y especialidad
    val filteredAbogados = abogados.filter { abogado ->
        val especialidad = abogado.especialidad ?: ""
        (especialidad.contains(searchQuery, ignoreCase = true) || searchQuery.isEmpty()) &&
                (selectedEspecialidad == "Todas" || especialidad == selectedEspecialidad)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 90.dp, start = 16.dp, end = 16.dp)
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar abogados") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownEspecialidades(
                especialidades = listOf("Todas", "Divorcios", "Laboral", "Familiar", "Fiscal"),
                selectedEspecialidad = selectedEspecialidad,
                onEspecialidadSelected = { selectedEspecialidad = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nombre",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TecBlue,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = "Especialidad",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TecBlue,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(ScrollState(0))
                    .fillMaxHeight()
            ) {
                if (filteredAbogados.isEmpty()) {
                    Text(
                        text = "No se encontraron resultados",
                        modifier = Modifier.padding(16.dp),
                        color = TecBlue,
                        textAlign = TextAlign.Center
                    )
                } else {
                    filteredAbogados.forEach { abogado ->
                        AbogadoItem(
                            nombre = abogado.nombre,
                            especialidad = abogado.especialidad ?: "Desconocida"  // Handle null value for especialidad
                        ) {
                            // Navegar a la pantalla de detalles del abogado
                            navController.navigate("info_abogado/${abogado.nombre}/${abogado.especialidad ?: "Desconocida"}")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DropdownEspecialidades(
    especialidades: List<String>,
    selectedEspecialidad: String,
    onEspecialidadSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedEspecialidad)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            especialidades.forEach { especialidad ->
                DropdownMenuItem(
                    text = { Text(especialidad) },
                    onClick = {
                        onEspecialidadSelected(especialidad)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AbogadoItem(nombre: String, especialidad: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = TecBlue,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                modifier = Modifier.weight(2f)
            )
            Text(
                text = especialidad,
                fontSize = 18.sp,
                color = White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}
