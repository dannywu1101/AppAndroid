package com.example.proyectobufetec.screens.abogado

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.CasoViewModel

@Composable
fun InfoCasosLegalesScreen(
    navController: NavController,
    casoViewModel: CasoViewModel,
    caseID: Int
) {
    val caseDetails = casoViewModel.abogadoCasos.value.find { it.id == caseID }
    val casoFiles by casoViewModel.casoFiles.collectAsState()
    val isLoading by casoViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        casoViewModel.fetchCasoFiles(caseID)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 40.dp)
    ) {
        // Back button aligned to start
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Regresar",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Centered title
        Text(
            text = "Información del Caso",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TecBlue,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display case details
        caseDetails?.let { case ->
            CaseDetailItem(label = "Expediente", value = case.numero_expediente)
            CaseDetailItem(label = "Cliente", value = case.clienteName)
            CaseDetailItem(label = "Estado", value = case.estado)
            CaseDetailItem(label = "Descripción", value = case.descripcion)
        } ?: Text("No se encontró el caso.", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Display files associated with the case
        Text(
            text = "Archivos del Caso:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TecBlue
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (casoFiles.isEmpty()) {
            Text("No hay archivos disponibles.", fontSize = 18.sp)
        } else {
            LazyColumn {
                items(casoFiles) { file ->
                    CasoFileItem(
                        titulo = file.titulo,
                        descripcion = file.descripcion,
                        link = file.presignedUrl,
                        context = LocalContext.current
                    )
                }
            }
        }
    }
}

@Composable
fun CaseDetailItem(label: String, value: String?) {
    // Each detail item is aligned within the Column as needed
    Text(
        buildAnnotatedString {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            append("$label: ")
            pop()
            append(value ?: "N/A")
        },
        fontSize = 18.sp,
        modifier = Modifier.fillMaxWidth() // Ensure it spans the full width
    )
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
        Text(text = descripcion, fontSize = 14.sp, color = Color.Gray)
    }
}
