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
import com.example.proyectobufetec.viewmodel.AuthState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    context: Context
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val authState by userViewModel.authState.collectAsState()

    // Ensure safe navigation after the NavHost is ready
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Login exitoso")
                }
                // Navigate safely after the composition is ready
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar((authState as AuthState.Error).message)
                }
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        LoginContent(
            email = userViewModel.email.collectAsState().value,
            password = userViewModel.password.collectAsState().value,
            onEmailChange = userViewModel::onEmailChange,
            onPasswordChange = userViewModel::onPasswordChange,
            onLoginClick = { email, password ->
                scope.launch {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        userViewModel.loginUser(LoginRequest(email, password))
                    } else {
                        snackbarHostState.showSnackbar("Por favor, complete ambos campos.")
                    }
                }
            },
            onGuestLogin = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
            onRegisterClick = {
                navController.navigate("register")
            },
            isLoading = authState is AuthState.Loading
        )
    }
}

@Composable
private fun LoginContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: (String, String) -> Unit,
    onGuestLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    isLoading: Boolean
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
            onValueChange = onEmailChange,
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Ingresar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onRegisterClick) {
            Text("Registrar nueva cuenta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGuestLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar como invitado")
        }
    }
}


@Composable
private fun LoadingIndicator(loginState: LoginUserState) {
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
