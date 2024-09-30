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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.launch

val TecBlue = Color(0xFF0033A0)  // Azul representativo del TEC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalCasesScreen(navController: NavController, appViewModel: UserViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCase by remember { mutableStateOf<Triple<String, String, Pair<String, String>>?>(null) }
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // List of legal cases to display (folio, nombre, estado, descripcion)
    val legalCases = listOf(
        Triple("1234", "Ignacio López", "Abierto" to "Caso de disputa por contrato."),
        Triple("4321", "Javier García", "Cerrado" to "Caso de divorcio finalizado."),
        Triple("3214", "Cristina Martínez", "Pendiente" to "Disputa laboral en proceso."),
        Triple("3124", "María Rodríguez", "Cerrado" to "Caso de adopción exitoso."),
        Triple("1243", "Miguel Sánchez", "Abierto" to "Fraude financiero en investigación."),
        Triple("2134", "Alberto Nuñez", "Pendiente" to "Caso de herencia disputada."),
        Triple("2143", "Cristián Pérez", "Cerrado" to "Demanda por negligencia resuelta."),
        Triple("3241", "Carlos Hernández", "Abierto" to "Caso de propiedad intelectual."),
        Triple("1324", "Gabriela Gómez", "Pendiente" to "Caso de acoso laboral."),
        Triple("1342", "Oscar García", "Cerrado" to "Demanda por difamación resuelta."),
        Triple("1423", "Pablo López", "Abierto" to "Caso de pensión alimenticia en curso."),
        Triple("1432", "Cristóbal Gutiérrez", "Abierto" to "Demanda por daños personales.")
    )

    // Filtered cases based on search query
    val filteredCases = legalCases.filter { (folio, _, _) ->
        folio.contains(searchQuery, ignoreCase = true) || searchQuery.isEmpty()
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            selectedCase?.let { case ->
                LegalCaseInfo(case.first, case.second, case.third.first, case.third.second)
            }
        },
        sheetShape = RoundedCornerShape(0.dp), // Remove corner rounding to make it fullscreen
        sheetPeekHeight = 0.dp // Initially hide the sheet
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, start = 16.dp, end = 16.dp)
        ) {
            // Search Bar with OutlinedTextField
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

            // Header Row with "Folio" and "Nombre de Cliente"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Folio",
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

            // List of Legal Cases (Folio, Nombre)
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
                    filteredCases.forEach { (folio, nombre, estadoDescripcion) ->
                        LegalCaseItem(folio = folio, nombre = nombre) {
                            selectedCase = Triple(folio, nombre, estadoDescripcion)
                            coroutineScope.launch {
                                sheetState.bottomSheetState.expand() // Expands to fullscreen
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
fun LegalCaseItem(folio: String, nombre: String, onClick: () -> Unit) {
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
                text = folio,
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

@Composable
fun LegalCaseInfo(folio: String, nombre: String, estado: String, descripcion: String) {
    Column(
        modifier = Modifier
            .fillMaxSize() // This ensures that the bottom sheet takes up the full screen
            .padding(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Información del Caso",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TecBlue
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Folio text with "Folio:" in bold
        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Folio: ")
                pop()
                append(folio)
            },
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Nombre text with "Nombre:" in bold
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

        // Estado text with "Estado:" in bold
        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Estado: ")
                pop()
                append(estado)
            },
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Descripción text with "Descripción:" in bold
        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Descripción: ")
                pop()
                append(descripcion)
            },
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}