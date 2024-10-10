package com.example.proyectobufetec.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectobufetec.R
import com.example.proyectobufetec.viewmodel.UserViewModel
import com.example.proyectobufetec.data.usuario.LoginUserState
import com.example.proyectobufetec.data.usuario.LoginRequest
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel, context: Context) {
    val snackbarHostState = remember { SnackbarHostState() }
    val loginState by userViewModel.loginState.collectAsState()

    // Access email and password directly from the ViewModel
    val email = userViewModel.email
    val password = userViewModel.password

    LaunchedEffect(loginState) {
        when (val login = loginState) {
            is LoginUserState.Loading -> {
                // Show loading if needed
            }
            is LoginUserState.Success -> {
                // Save the token to EncryptedSharedPreferences
                saveAuthToken(context, login.tokenResponse.token)

                snackbarHostState.showSnackbar("Login exitoso")
                navController.navigate("home")
            }
            is LoginUserState.Error -> {
                snackbarHostState.showSnackbar(login.errorMessage)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bufetec),
                contentDescription = "Logo de Bufetec",
                modifier = Modifier.size(300.dp)
            )

            // Campo de texto para correo
            TextField(
                value = email,
                onValueChange = { userViewModel.email = it }, // Directly updating the ViewModel
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de texto para contraseña
            TextField(
                value = password,
                onValueChange = { userViewModel.password = it }, // Directly updating the ViewModel
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de iniciar sesión
            Button(
                onClick = {
                    userViewModel.loginUser(
                        LoginRequest(
                            email = email,       // Use the email from the ViewModel
                            contrasena = password // Use the password from the ViewModel
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de registrar nueva cuenta
            TextButton(onClick = { navController.navigate("register") }) {
                Text("Registrar nueva cuenta")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de iniciar como invitado
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar como invitado")
            }

            // Indicador de carga
            Loading(loginState)
        }
    }
}

@Composable
private fun Loading(loginState: LoginUserState) {
    when (loginState) {
        is LoginUserState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else -> {}
    }
}

// Function to save the token to EncryptedSharedPreferences
private fun saveAuthToken(context: Context, token: String) {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    sharedPreferences.edit().putString("auth_token", token).apply()
}
