package com.example.proyectobufetec.screens.abogado

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import com.example.proyectobufetec.ui.theme.TecBlue

@Composable
fun InfoCasosLegalesScreen(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val expediente = navBackStackEntry.arguments?.getString("expediente")
    val cliente = navBackStackEntry.arguments?.getString("cliente")
    val abogadoAsignado = navBackStackEntry.arguments?.getString("abogadoAsignado")
    val estado = navBackStackEntry.arguments?.getString("estado")
    val descripcion = navBackStackEntry.arguments?.getString("descripcion")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 40.dp)
    ) {
        // Botón de regreso
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Regresar",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // El título centrado
        Text(
            text = "Información del Caso",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TecBlue,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // El resto del contenido alineado a la izquierda
        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Expediente: ")
                pop()
                append(expediente ?: "N/A")
            },
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Cliente: ")
                pop()
                append(cliente ?: "N/A")
            },
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Abogado Asignado: ")
                pop()
                append(abogadoAsignado ?: "N/A")
            },
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Estado: ")
                pop()
                append(estado ?: "N/A")
            },
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("Descripción: ")
                pop()
                append(descripcion ?: "N/A")
            },
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.Start)
        )
    }
}

