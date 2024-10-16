// com.example.proyectobufetec/screens/clientes/CrearCasoScreen.kt

package com.example.proyectobufetec.screens.clientes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController

@Composable
fun CrearCasoScreen(navController: NavController) {
    // Manage the state of the input fields
    var direccion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Title
        Text(
            text = "Petición caso / Pedir Caso / Pedir ayuda legal",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // TextField for Dirección del domicilio
        CustomMinimalTextField(
            label = "Dirección del domicilio",
            value = direccion,
            onValueChange = { direccion = it }
        )

        // TextField for Descripción del caso
        CustomMinimalTextField(
            label = "Descripción del caso",
            value = descripcion,
            onValueChange = { descripcion = it },
            height = 120.dp // Taller field for case description
        )

        // Enhanced Warning Message
        WarningMessage(
            message = "Incluye información acerca de tus ingresos mensuales, el caso, y número de WhatsApp"
        )

        // Submit Button
        Button(
            onClick = {
                println("Dirección: $direccion")
                println("Descripción: $descripcion")
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(0.8f) // Button width is 80% of the screen width
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(text = "Enviar", fontSize = 16.sp)
        }
    }
}

@Composable
fun CustomMinimalTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    height: Dp = 50.dp
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(12.dp)) // Rounded corners
                .background(Color(0xFFE0E0E0)) // Slightly darker gray background
                .border(1.dp, Color(0xFFBBBBBB), RoundedCornerShape(12.dp)) // Subtle border
                .padding(12.dp) // Padding for inner text
        )
    }
}

@Composable
fun WarningMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(Color(0xFFFFF3CD), shape = RoundedCornerShape(8.dp)) // Light yellow background
            .border(1.dp, Color(0xFFFFD966), shape = RoundedCornerShape(8.dp)) // Yellow border
            .padding(12.dp) // Inner padding
    ) {
        Text(
            text = message,
            color = Color(0xFF856404), // Dark yellow text color
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
