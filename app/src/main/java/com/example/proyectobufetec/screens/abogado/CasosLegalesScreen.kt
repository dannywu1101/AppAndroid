package com.example.proyectobufetec.screens.abogado

import android.content.Intent
import android.net.Uri
import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

    // Use LazyColumn as the main container
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 90.dp, start = 16.dp, end = 16.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar casos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
        }

        item {
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
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else if (errorMessage != null) {
            item {
                Text(
                    text = errorMessage ?: "Unknown error",
                    color = TecBlue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val filteredCases = abogadoCasos.filter {
                it.numero_expediente.contains(searchQuery, ignoreCase = true) || searchQuery.isEmpty()
            }

            if (filteredCases.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron resultados",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = TecBlue,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
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
        if (isExpanded) {
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
            .clickable { isExpanded = !isExpanded },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Unknown error",
                    color = White,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn {
                    items(casoFiles) { file ->
                        CasoFileItem(
                            fileName = file.fileName,
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

@Composable
fun CasoFileItem(fileName: String, link: String, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = fileName,
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
    }
}
