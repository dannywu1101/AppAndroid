// com.example.proyectobufetec/screens/clientes/EstadoCasoScreen.kt

package com.example.proyectobufetec.screens.clientes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel

@Composable
fun EstadoCasoScreen(navController: NavController, appViewModel: UserViewModel) {
    // Replace with real data in future
    val abogadoEncargado = "Lic. Juan Perez"
    val folio = "F123456789"
    val estadoDelCaso = "En espera"
    val descripcionDelCaso = "El cliente requiere asistencia legal en la revisión de un contrato de arrendamiento."

    Spacer(modifier = Modifier.height(40.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

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
                    Text(text = abogadoEncargado)
                }

                // Folio
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Folio:", fontWeight = FontWeight.SemiBold)
                    Text(text = folio)
                }

                // Estado del Caso
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Estado del Caso:", fontWeight = FontWeight.SemiBold)
                    Text(text = estadoDelCaso)
                }

                // Descripción del Caso
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Descripción del Caso:", fontWeight = FontWeight.SemiBold)
                    Text(text = descripcionDelCaso)
                }
            }
        }

        // Spacer for some space between box and button
        Spacer(modifier = Modifier.height(24.dp))

        // Button for navigation or actions (optional)
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Volver")
        }
    }
}
