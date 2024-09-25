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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalCasesScreen(navController: NavController, appViewModel: UserViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCase by remember { mutableStateOf<Pair<String, String>?>(null) }
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // List of legal cases to display
    val legalCases = listOf(
        "Folio 1" to "Nombre del Caso 1",
        "Folio 2" to "Nombre del Caso 2",
        "Folio 3" to "Nombre del Caso 3",
        "Folio 4" to "Nombre del Caso 4",
        "Folio 5" to "Nombre del Caso 5"
    )

    // Filtered cases based on search query
    val filteredCases = legalCases.filter { (folio, _) ->
        folio.contains(searchQuery, ignoreCase = true) || searchQuery.isEmpty()
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            selectedCase?.let { case ->
                LegalCaseInfo(case.first, case.second) {
                    // Close the bottom sheet when the back button is clicked
                    coroutineScope.launch {
                        sheetState.bottomSheetState.hide()
                    }
                }
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

            // List of Legal Cases (Folio, Nombre)
            Column(
                modifier = Modifier
                    .verticalScroll(ScrollState(0))
                    .fillMaxHeight()
            ) {
                if (filteredCases.isEmpty()) {
                    Text(text = "No se encontraron resultados", modifier = Modifier.padding(16.dp))
                } else {
                    filteredCases.forEach { (folio, nombre) ->
                        LegalCaseItem(folio = folio, nombre = nombre) {
                            selectedCase = folio to nombre
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
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = folio,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = nombre,
                fontSize = 18.sp,
                modifier = Modifier.weight(2f)
            )
        }
    }
}

@Composable
fun LegalCaseInfo(folio: String, nombre: String, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize() // This ensures that the bottom sheet takes up the full screen
            .padding(70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Información del Caso", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Folio: $folio", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Nombre: $nombre", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(32.dp))
    }
}
