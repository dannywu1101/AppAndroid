// com.example.proyectobufetec/screens/LoginScreen.kt

package com.example.proyectobufetec.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectobufetec.data.LoginUserRequest
import com.example.proyectobufetec.viewmodel.LoginUserState
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {

    val snackbarHostState = remember { SnackbarHostState() }
    val loginState by userViewModel.login.collectAsState()



    LaunchedEffect(loginState) {
        when (val login = loginState) {

            is LoginUserState.Loading -> {

            }

            is LoginUserState.Success -> {
                // Show the snackbar
                snackbarHostState.showSnackbar("Login exitoso")
                navController.navigate("home")
                userViewModel.isUserLogged = true
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
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = userViewModel.email,
            onValueChange = { userViewModel.email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = userViewModel.password,
            onValueChange = { userViewModel.password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                userViewModel.loginUser(
                    LoginUserRequest(
                        userViewModel.email,
                        userViewModel.password
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = {

            navController.navigate("register")
        }) {
            Text("Registrar nueva cuenta")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {

                //userViewModel.isUserLogged = false
                navController.navigate("home")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar como invitado")
        }


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
        else -> {
        }
    }
}