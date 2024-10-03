package com.example.proyectobufetec.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectobufetec.data.usuario.RegisterRequest
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.RegisterUserState
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, appViewModel: UserViewModel) {
    // State variables for input fields
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("Selecciona una fecha") }
    var gender by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Observe register state from ViewModel
    val registerState by appViewModel.registerState.collectAsState()

    // DatePicker dialog setup
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Show snackbar messages for registration results
    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterUserState.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Registration successful!")
                }
                // Navigate to login or another screen after success
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }
            is RegisterUserState.Error -> {
                coroutineScope.launch {
                    val errorMessage = (registerState as RegisterUserState.Error).errorMessage
                    snackbarHostState.showSnackbar("Error: $errorMessage")
                }
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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

            TextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Gender input as a TextField
            TextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth()
            )

            // DatePicker for birth date selection
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { datePickerDialog.show() }
                    .background(Color.LightGray)
                    .padding(16.dp)
            ) {
                Text(text = selectedDate)
            }

            // Email input fields
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = confirmEmail,
                onValueChange = { confirmEmail = it },
                label = { Text("Correo nuevamente") },
                modifier = Modifier.fillMaxWidth()
            )

            // Password input fields
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Contraseña nuevamente") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Register button
            Button(
                onClick = {
                    if (email == confirmEmail && password == confirmPassword && fullName.isNotEmpty()) {
                        appViewModel.registerUser(
                            RegisterRequest(
                                nombre = fullName,
                                email = email,
                                contrasena = password,
                                sexo = gender,
                                fecha_nacimiento = selectedDate
                            )
                        )
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please ensure all fields are filled correctly.")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}
