// com.example.proyectobufetec/components/NavigationDrawer.kt

package com.example.proyectobufetec.components

import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proyectobufetec.viewmodel.UserViewModel

@Composable
fun NavigationDrawer(navController: NavController, appViewModel: UserViewModel, onNavigate: (String) -> Unit) {
    val isUserLogged by appViewModel.isUserLogged.collectAsState() // Extract the Boolean value using 'by'

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route

    ModalDrawerSheet(modifier = Modifier) {
        if (isUserLogged) {  // Use the extracted Boolean value here
            NavigationDrawerItem(
                label = { Text("Ruta 1") },
                selected = currentDestination == "Ruta 1",
                onClick = { onNavigate("route1") }
            )
            NavigationDrawerItem(
                label = { Text("Ruta 2") },
                selected = currentDestination == "Ruta 2",
                onClick = { onNavigate("route2") }
            )
        }

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
}
