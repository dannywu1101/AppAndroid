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
import com.example.proyectobufetec.viewmodel.UserType

enum class UserType {
    Admin, Abogado, Cliente, Usuario, Guest
}

@Composable
fun NavigationDrawer(
    navController: NavController,
    appViewModel: UserViewModel,
    onNavigate: (String) -> Unit
) {
    val authState by appViewModel.authState.collectAsState()
    val userType by appViewModel.userType.collectAsState(initial = UserType.Guest)

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route

    ModalDrawerSheet(modifier = Modifier) {
        when (authState) {
            is AuthState.Success -> {
                when (userType) {
                    UserType.Admin, UserType.Abogado -> LawyerNavigationItems(currentDestination, onNavigate, appViewModel::logoutUser)
                    UserType.Cliente, UserType.Usuario -> UserNavigationItems(currentDestination, onNavigate, appViewModel::logoutUser)
                    else -> GuestNavigationItems(currentDestination, onNavigate)
                }
            }
            else -> GuestNavigationItems(currentDestination, onNavigate) // Guest users
        }
    }
}

// Guest Navigation Items (No Logout option for guests)
@Composable
private fun GuestNavigationItems(
    currentDestination: String?,
    onNavigate: (String) -> Unit
) {
    NavigationDrawerItem(
        label = { Text("Biblioteca") },
        selected = currentDestination == "biblioteca",
        onClick = { onNavigate("biblioteca") }
    )
    NavigationDrawerItem(
        label = { Text("ChatBot") },
        selected = currentDestination == "chatbot",
        onClick = { onNavigate("chatbot") }
    )
    NavigationDrawerItem(
        label = { Text("Iniciar Sesión") },
        selected = currentDestination == "login",
        onClick = { onNavigate("login") }
    )
}

// User and Cliente Navigation Items (With Logout option)
@Composable
private fun UserNavigationItems(
    currentDestination: String?,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text("ChatBot") },
        selected = currentDestination == "chatbot",
        onClick = { onNavigate("chatbot") }
    )
    NavigationDrawerItem(
        label = { Text("Biblioteca") },
        selected = currentDestination == "biblioteca",
        onClick = { onNavigate("biblioteca") }
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
        label = { Text("Perfil") },
        selected = currentDestination == "profile",
        onClick = { onNavigate("profile") }
    )
    // Logout option
    NavigationDrawerItem(
        label = { Text("Cerrar Sesión") },
        selected = false,
        onClick = { onLogout() }
    )
}

// Lawyer Navigation Items (With Logout option)
@Composable
private fun LawyerNavigationItems(
    currentDestination: String?,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text("Casos Legales") },
        selected = currentDestination == "casos legales",
        onClick = { onNavigate("casos legales") }
    )
    NavigationDrawerItem(
        label = { Text("Perfil Abogados") },
        selected = currentDestination == "perfil abogados",
        onClick = { onNavigate("perfil abogados") }
    )
    NavigationDrawerItem(
        label = { Text("Editar Caso") },
        selected = currentDestination == "editar caso",
        onClick = { onNavigate("editar caso") }
    )
    // Logout option
    NavigationDrawerItem(
        label = { Text("Cerrar Sesión") },
        selected = false,
        onClick = { onLogout() }
    )
}

