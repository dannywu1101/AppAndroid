// com.example.proyectobufetec/components/NavigationDrawer.kt

package com.example.proyectobufetec.components

import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectobufetec.viewmodel.AuthState
import com.example.proyectobufetec.viewmodel.UserViewModel

@Composable
fun NavigationDrawer(
    navController: NavController,
    appViewModel: UserViewModel,
    onNavigate: (String) -> Unit
) {
    val authState by appViewModel.authState.collectAsState() // Observe the new AuthState

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route

    ModalDrawerSheet(modifier = Modifier) {
        when (authState) {
            is AuthState.Success -> {
                // Display routes available only to logged-in users
                LoggedInNavigationItems(currentDestination, onNavigate)
            }
            else -> {
                // Show a message or limited items if the user is not logged in
                GuestNavigationItems(currentDestination, onNavigate)
            }
        }

        // Display shared navigation items available to all users
        SharedNavigationItems(currentDestination, onNavigate)
    }
}

@Composable
private fun LoggedInNavigationItems(
    currentDestination: String?,
    onNavigate: (String) -> Unit
) {
    NavigationDrawerItem(
        label = { Text("Ruta 1") },
        selected = currentDestination == "route1",
        onClick = { onNavigate("route1") }
    )
    NavigationDrawerItem(
        label = { Text("Ruta 2") },
        selected = currentDestination == "route2",
        onClick = { onNavigate("route2") }
    )
}

@Composable
private fun GuestNavigationItems(
    currentDestination: String?,
    onNavigate: (String) -> Unit
) {
    Text("Inicie sesión para acceder a más funciones.")
}

@Composable
private fun SharedNavigationItems(
    currentDestination: String?,
    onNavigate: (String) -> Unit
) {
    NavigationDrawerItem(
        label = { Text("ChatBot") },
        selected = currentDestination == "chatbot",
        onClick = { onNavigate("chatbot") }
    )
    NavigationDrawerItem(
        label = { Text("Casos Legales") },
        selected = currentDestination == "casos legales",
        onClick = { onNavigate("casos legales") }
    )
    NavigationDrawerItem(
        label = { Text("Biblioteca") },
        selected = currentDestination == "biblioteca",
        onClick = { onNavigate("biblioteca") }
    )
    NavigationDrawerItem(
        label = { Text("Perfil") },
        selected = currentDestination == "profile",
        onClick = { onNavigate("profile") }
    )
    NavigationDrawerItem(
        label = { Text("Búsqueda Abogados") },
        selected = currentDestination == "busqueda abogados",
        onClick = { onNavigate("busqueda abogados") }
    )
    NavigationDrawerItem(
        label = { Text("Estado del Caso") },
        selected = currentDestination == "estado caso",
        onClick = { onNavigate("estado caso") }
    )
    NavigationDrawerItem(
        label = { Text("Perfil Abogados") },
        selected = currentDestination == "perfil abogados",
        onClick = { onNavigate("perfil abogados") }
    )
}
