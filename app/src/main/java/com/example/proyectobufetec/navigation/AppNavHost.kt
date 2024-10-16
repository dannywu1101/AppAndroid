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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectobufetec.components.NavigationDrawer
import com.example.proyectobufetec.screens.*
import com.example.proyectobufetec.screens.clientes.*
import com.example.proyectobufetec.screens.abogado.*
import com.example.proyectobufetec.viewmodel.*
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    appViewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    abogadoViewModel: AbogadoViewModel,
    bibliotecaViewModel: BibliotecaViewModel,
    casoViewModel: CasoViewModel,
    context: Context,
    padding: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Reusable scaffold with navigation drawer logic
    @Composable
    fun ModalScaffold(contentTitle: String, content: @Composable () -> Unit) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    NavigationDrawer(navController, appViewModel) { destination ->
                        scope.launch {
                            drawerState.close()
                            navController.navigate(destination) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(contentTitle) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                content = { content() }
            )
        }
    }

    // Define the navigation graph
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = padding
    ) {
        composable("login") {
            LoginScreen(navController, appViewModel, context)
        }

        composable("register") {
            RegisterScreen(navController, appViewModel)
        }

        composable("chatbot") {
            ModalScaffold(contentTitle = "ChatBot") {
                ChatBotScreen(navController, chatViewModel)
            }
        }

        composable("biblioteca") {
            ModalScaffold(contentTitle = "Biblioteca") {
                BibliotecaScreen(navController, bibliotecaViewModel)
            }
        }

        composable("profile") {
            ModalScaffold(contentTitle = "Perfil") {
                ProfileScreen(navController, appViewModel)
            }
        }

        composable("casos legales") {
            ModalScaffold(contentTitle = "Casos Legales") {
                LegalCasesScreen(navController, casoViewModel)
            }
        }

        composable("busqueda abogados") {
            ModalScaffold(contentTitle = "Búsqueda Abogados") {
                BusquedaAbogadosScreen(navController, abogadoViewModel)
            }
        }

        composable("estado caso") {
            ModalScaffold(contentTitle = "Estado del Caso") {
                EstadoCasoScreen(navController, casoViewModel)
            }
        }

        composable(
            route = "info_casos_legales/{caseID}",
            arguments = listOf(
                navArgument("caseID") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val caseID = backStackEntry.arguments?.getInt("caseID") ?: -1
            InfoCasosLegalesScreen(
                navController = navController,
                casoViewModel = casoViewModel,
                caseID = caseID
            )
        }



        composable(
            route = "info_abogado/{nombre}/{especialidad}",
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

        composable("home") {
            ModalScaffold(contentTitle = "Pantalla de Inicio") {
                HomeScreen(navController, appViewModel)
            }
        }

    }
}
