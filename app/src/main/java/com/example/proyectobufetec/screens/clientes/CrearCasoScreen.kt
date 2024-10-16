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
import com.example.proyectobufetec.data.repository.PeticionCasoRepository
import com.example.proyectobufetec.data.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun CrearCasoScreen(navController: NavController) {
    // Initialize the repository
    val repository = PeticionCasoRepository(RetrofitInstance.getPeticionCasoApi())
    val scope = rememberCoroutineScope()

    // Manage the state of the input fields and UI feedback
    var direccion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

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

        // Warning Message
        WarningMessage(
            message = "Incluye información acerca de tus ingresos mensuales, el caso, y número de WhatsApp"
        )

        // Submit Button
        Button(
            onClick = {
                if (descripcion.isNotBlank()) {
                    scope.launch {
                        isLoading = true
                        val combinedText = "$direccion - $descripcion"
                        val response = repository.createPeticionCaso(combinedText)

                        if (response.isSuccessful) {
                            message = "Petición enviada con éxito."
                            navController.navigate("home") {
                                popUpTo("crearCaso") { inclusive = true }
                            }
                        } else {
                            message = "Error al enviar la petición. Inténtalo de nuevo."
                        }
                        isLoading = false
                    }
                } else {
                    message = "Por favor, ingresa la descripción del caso."
                }
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(0.8f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading // Disable the button while loading
        ) {
            Text(text = if (isLoading) "Enviando..." else "Enviar", fontSize = 16.sp)
        }

        // Display any feedback message
        if (message.isNotBlank()) {
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center
            )
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
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE0E0E0))
                .border(1.dp, Color(0xFFBBBBBB), RoundedCornerShape(12.dp))
                .padding(12.dp)
        )
    }
}

@Composable
fun WarningMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(Color(0xFFFFF3CD), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFFFD966), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = message,
            color = Color(0xFF856404),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
