// com.example.proyectobufetec/screens/RegisterScreen

package com.example.proyectobufetec.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Calendar

val TecBlue = Color(0xFF0033A0)  // Azul representativo del TEC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    // Estado para almacenar la fecha seleccionada
    var selectedDate by remember { mutableStateOf("Selecciona una fecha") }
    var expanded by remember { mutableStateOf(false) } // Estado para controlar el menú desplegable de género
    var selectedGender by remember { mutableStateOf("Selecciona género") } // Género seleccionado
    val genderOptions = listOf("Masculino", "Femenino", "No binario", "Otro") // Opciones de género

    // Mostrar el selector de fecha cuando el campo de fecha sea clicado
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"  // Actualizar el estado con la fecha seleccionada
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TecBlue,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Por favor, completa los siguientes campos para registrarte.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        TextField(
            value = "",
            onValueChange = { /* TODO: Manejar cambio */ },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown para seleccionar el género
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded // Cambiar el estado para mostrar/ocultar el menú
            }
        ) {
            TextField(
                value = selectedGender,
                onValueChange = { selectedGender = it },
                label = { Text("Género") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(), // Anclaje del menú desplegable
                readOnly = true // El TextField solo sirve como selección, no como campo editable
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderOptions.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            selectedGender = gender
                            expanded = false // Cerrar el menú al seleccionar una opción
                        }
                    )
                }
            }
        }

        // Campo para seleccionar la fecha de nacimiento con click
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { datePickerDialog.show() } // Mostrar DatePicker
                .padding(16.dp)
                .background(Color.LightGray) // Fondo para simular campo de texto
        ) {
            Text(text = selectedDate)
        }

        TextField(
            value = "",
            onValueChange = { /* TODO: Manejar cambio */ },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = "",
            onValueChange = { /* TODO: Manejar cambio */ },
            label = { Text("Correo nuevamente") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = "",
            onValueChange = { /* TODO: Manejar cambio */ },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            value = "",
            onValueChange = { /* TODO: Manejar cambio */ },
            label = { Text("Contraseña nuevamente") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* TODO: Manejar registro */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }
    }
}