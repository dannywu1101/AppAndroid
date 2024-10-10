// com.example.proyectobufetec/navigation/AppNavHost.kt

package com.example.proyectobufetec.navigation

import ProfileScreen
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectobufetec.components.NavigationDrawer
import com.example.proyectobufetec.screens.clientes.BibliotecaScreen
import com.example.proyectobufetec.screens.clientes.ChatBotScreen
import com.example.proyectobufetec.screens.HomeScreen
import com.example.proyectobufetec.screens.LoginScreen
import com.example.proyectobufetec.screens.RegisterScreen
import com.example.proyectobufetec.screens.abogado.AbogadoProfileScreen
import com.example.proyectobufetec.screens.abogado.InfoCasosLegalesScreen
import com.example.proyectobufetec.screens.abogado.LegalCasesScreen
import com.example.proyectobufetec.screens.clientes.BusquedaAbogadosScreen
import com.example.proyectobufetec.screens.clientes.EstadoCasoScreen
import com.example.proyectobufetec.screens.clientes.InfoAbogadosScreen
import com.example.proyectobufetec.viewmodel.UserViewModel
import com.example.proyectobufetec.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    appViewModel: UserViewModel,
    chatViewModel: ChatViewModel,  // Added ChatViewModel
    context: Context,  // Added context for login
    padding: Modifier
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Shared scaffold and drawer logic for each screen
    @Composable
    fun ModalScaffold(contentTitle: String, content: @Composable () -> Unit) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    NavigationDrawer(navController, appViewModel) { destination ->
                        scope.launch { drawerState.close() }
                        navController.navigate(destination)
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(contentTitle) },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                content = { content() } // Simplified padding handling
            )
        }
    }

    // NavHost for handling all routes
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController, appViewModel, context)  // Pass context for login
        }

        composable("register") {
            RegisterScreen(navController, appViewModel)
        }

        composable("home") {
            ModalScaffold(contentTitle = "Pantalla Principal") {
                HomeScreen(navController, appViewModel)
            }
        }

        composable("chatbot") {
            ModalScaffold(contentTitle = "ChatBot") {
                ChatBotScreen(navController, chatViewModel)  // Use ChatViewModel here
            }
        }

        composable("biblioteca") {
            ModalScaffold(contentTitle = "Biblioteca") {
                BibliotecaScreen(navController, appViewModel)
            }
        }

        composable("profile") {
            ModalScaffold(contentTitle = "Perfil") {
                ProfileScreen(navController, appViewModel)
            }
        }

        composable("casos legales") {
            ModalScaffold(contentTitle = "Casos Legales") {
                LegalCasesScreen(navController, appViewModel)
            }
        }

        composable("busqueda abogados") {
            ModalScaffold(contentTitle = "Búsqueda Abogados") {
                BusquedaAbogadosScreen(navController, appViewModel)
            }
        }

        composable("estado caso") {
            ModalScaffold(contentTitle = "Estado del Caso") {
                EstadoCasoScreen(navController, appViewModel)
            }
        }

        // Nueva ruta: InfoCasosLegalesScreen
        composable(
            "info_casos_legales/{expediente}/{cliente}/{abogadoAsignado}/{estado}/{descripcion}",
            arguments = listOf(
                navArgument("expediente") { type = NavType.StringType },
                navArgument("cliente") { type = NavType.StringType },
                navArgument("abogadoAsignado") { type = NavType.StringType },
                navArgument("estado") { type = NavType.StringType },
                navArgument("descripcion") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            InfoCasosLegalesScreen(
                navController = navController,
                navBackStackEntry = backStackEntry
            )
        }

        // Nueva ruta: InfoAbogadosScreen
        composable(
            "info_abogado/{nombre}/{especialidad}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("especialidad") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            InfoAbogadosScreen(
                navController = navController,
                navBackStackEntry = backStackEntry
            )
        }

        composable("perfil abogados") {
            ModalScaffold(contentTitle = "Perfil Abogados") {
                AbogadoProfileScreen(navController, appViewModel)
            }
        }
    }
}
