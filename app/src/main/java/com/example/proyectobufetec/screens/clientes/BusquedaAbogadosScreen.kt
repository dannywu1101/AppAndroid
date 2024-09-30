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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaAbogadosScreen(navController: NavController, appViewModel: UserViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedAbogado by remember { mutableStateOf<Pair<String, String>?>(null) }
    var selectedEspecialidad by remember { mutableStateOf("Todas") }
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // Lista de abogados con su especialidad
    val abogados = listOf(
        "Juan Pérez" to "Divorcios",
        "María González" to "Familiar",
        "Carlos Hernández" to "Fiscal",
        "Ana Rodríguez" to "Laboral",
        "Javier Ramírez" to "Familiar",
        "Cristina López" to "Familiar",
        "Oscar García" to "Laboral",
        "Gabriela Sánchez" to "Divorcios",
        "Miguel Pérez" to "Laboral",
        "Alberto Núñez" to "Fiscal"
    )

    // Lista de especialidades
    val especialidades = listOf("Todas", "Divorcios", "Laboral", "Familiar", "Fiscal")

    // Filtrado de abogados basado en la búsqueda y especialidad
    val filteredAbogados = abogados.filter { (nombre, especialidad) ->
        (nombre.contains(searchQuery, ignoreCase = true) || searchQuery.isEmpty()) &&
                (selectedEspecialidad == "Todas" || especialidad == selectedEspecialidad)
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            selectedAbogado?.let { abogado ->
                AbogadoInfo(abogado.first, abogado.second)
            }
        },
        sheetShape = RoundedCornerShape(0.dp), // Sin bordes redondeados
        sheetPeekHeight = 0.dp // Ocultar inicialmente la hoja
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, start = 16.dp, end = 16.dp)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar abogados") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("Buscar abogados"), // <-- Added tag for testing
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Filtro de especialidad
            DropdownEspecialidades(
                especialidades = especialidades,
                selectedEspecialidad = selectedEspecialidad,
                onEspecialidadSelected = { selectedEspecialidad = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Encabezado con "Nombre" y "Especialidad"
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

            // Lista de abogados
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
                    filteredAbogados.forEach { (nombre, especialidad) ->
                        AbogadoItem(nombre = nombre, especialidad = especialidad) {
                            selectedAbogado = nombre to especialidad
                            coroutineScope.launch {
                                sheetState.bottomSheetState.expand() // Expande la hoja inferior
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
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

@Composable
fun AbogadoInfo(nombre: String, especialidad: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Información del Abogado",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TecBlue
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar "Nombre"
        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Nombre: ")
                pop()
                append(nombre)
            },
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar "Especialidad"
        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Especialidad: ")
                pop()
                append(especialidad)
            },
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

