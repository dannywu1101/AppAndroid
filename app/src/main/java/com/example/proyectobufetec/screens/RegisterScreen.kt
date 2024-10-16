// com.example.proyectobufetec/screens/RegisterScreen.kt

package com.example.proyectobufetec.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectobufetec.data.usuario.RegisterRequest
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.AuthState
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, appViewModel: UserViewModel) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("Selecciona una fecha") }
    var gender by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val authState by appViewModel.authState.collectAsState()

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

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Registro exitoso!")
                }
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                coroutineScope.launch {
                    val errorMessage = (authState as AuthState.Error).message
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
            Text(
                text = "Registro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TecBlue,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            InputField(label = "Nombre Completo", value = fullName, onValueChange = { fullName = it })
            InputField(label = "Género", value = gender, onValueChange = { gender = it })

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

            InputField(label = "Correo", value = email, onValueChange = { email = it })
            InputField(label = "Correo nuevamente", value = confirmEmail, onValueChange = { confirmEmail = it })

            InputField(
                label = "Contraseña",
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation()
            )
            InputField(
                label = "Contraseña nuevamente",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (validateFields(fullName, email, confirmEmail, password, confirmPassword)) {
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

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        visualTransformation = visualTransformation
    )
}

private fun validateFields(
    fullName: String,
    email: String,
    confirmEmail: String,
    password: String,
    confirmPassword: String
): Boolean {
    return fullName.isNotEmpty() &&
            email.isNotEmpty() &&
            email == confirmEmail &&
            password.isNotEmpty() &&
            password == confirmPassword
}
