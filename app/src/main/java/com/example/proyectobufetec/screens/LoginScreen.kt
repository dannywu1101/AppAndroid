// com.example.proyectobufetec.screens.LoginScreen.kt

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    context: Context
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val loginState by userViewModel.loginState.collectAsState()
    val isUserLogged by userViewModel.isUserLogged.collectAsState() // Collecting the user logged state

    // Access email and password from the ViewModel
    val email by remember { mutableStateOf(userViewModel.email) }
    val password by remember { mutableStateOf(userViewModel.password) }

    // Verify token on launch
    LaunchedEffect(Unit) {
        userViewModel.verifyToken() // Trigger token verification
    }

    // Navigate to home if the user is logged in
    LaunchedEffect(isUserLogged) {
        if (isUserLogged) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true } // Clear the backstack
            }
        }
    }

    // Handle login state changes
    LaunchedEffect(loginState) {
        when (val login = loginState) {
            is LoginUserState.Loading -> {
                // Optionally show a loading UI
            }
            is LoginUserState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
                snackbarHostState.showSnackbar("Login exitoso")
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

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { userViewModel.email = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { userViewModel.password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    userViewModel.loginUser(
                        LoginRequest(
                            email = email,
                            contrasena = password
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("register") }) {
                Text("Registrar nueva cuenta")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar como invitado")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Loading(loginState)
        }
    }
}

@Composable
private fun Loading(loginState: LoginUserState) {
    if (loginState is LoginUserState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
