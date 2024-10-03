// com.example.proyectobufetec/screens/clientes/InfoAbogadosScreen.kt
package com.example.proyectobufetec.screens.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun InfoAbogadosScreen(navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val nombre = navBackStackEntry.arguments?.getString("nombre")
    val especialidad = navBackStackEntry.arguments?.getString("especialidad")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 40.dp)
    ) {
        // Botón de regreso
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Regresar",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Título centrado
        Text(
            text = "Información del Abogado",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TecBlue,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Información alineada a la izquierda
        Column(modifier = Modifier.align(Alignment.Start)) {
            Text(
                buildAnnotatedString {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("Nombre: ")
                    pop()
                    append(nombre ?: "N/A")
                },
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                buildAnnotatedString {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append("Especialidad: ")
                    pop()
                    append(especialidad ?: "N/A")
                },
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
