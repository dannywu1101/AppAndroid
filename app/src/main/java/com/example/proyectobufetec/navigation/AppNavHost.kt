// com.example.proyectobufetec/navigation/AppNavHost.kt

package com.example.proyectobufetec.navigation

import ProfileScreen
import android.annotation.SuppressLint
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
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(appViewModel: UserViewModel, padding: Modifier) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Create a single NavHost for handling all routes
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController, appViewModel)
        }

        composable("register") {
            RegisterScreen(navController, appViewModel)
        }

        composable("home") {
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
                            title = { Text("Pantalla Principal") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { padding ->
                        HomeScreen(navController, appViewModel)
                    }
                )
            }
        }

        composable("chatbot") {
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
                            title = { Text("ChatBot") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { paddingValues ->
                        ChatBotScreen(navController, appViewModel, modifier = Modifier.padding(paddingValues))
                    }
                )
            }
        }

        composable("biblioteca") {
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
                            title = { Text("Biblioteca") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { padding ->
                        BibliotecaScreen(navController, appViewModel)
                    }
                )
            }
        }

        composable("profile") {
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
                            title = { Text("Perfil") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { padding ->
                        ProfileScreen(navController, appViewModel)
                    }
                )
            }
        }

        composable("casos legales") {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    NavigationDrawer(navController, appViewModel) { destination ->
                        scope.launch { drawerState.close() }
                        navController.navigate(destination)
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Casos Legales") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { padding ->
                        LegalCasesScreen(navController, appViewModel)
                    }
                )
            }
        }

        // Nueva Ruta: Búsqueda Abogados
        composable("busqueda abogados") {
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
                            title = { Text("Búsqueda Abogados") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { padding ->
                        BusquedaAbogadosScreen(navController, appViewModel)
                    }
                )
            }
        }

        composable("perfil abogados") {
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
                            title = { Text("Perfil Abogados") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { padding ->
                        AbogadoProfileScreen(navController, appViewModel)
                    }
                )
            }
        }



        // Nueva Ruta: Estado Caso
        composable("estado caso") {
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
                            title = { Text("Estado del Caso") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            }
                        )
                    },
                    content = { padding ->
                        EstadoCasoScreen(navController, appViewModel)
                    }
                )
            }
        }


        composable("legal_cases_screen") { LegalCasesScreen(navController, appViewModel) }

        composable(
            "info_casos_legales/{expediente}/{cliente}/{abogadoAsignado}/{estado}/{descripcion}",
            arguments = listOf(
                navArgument("expediente") { type = NavType.StringType },
                navArgument("cliente") { type = NavType.StringType },
                navArgument("abogadoAsignado") { type = NavType.StringType },
                navArgument("estado") { type = NavType.StringType },
                navArgument("descripcion") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            InfoCasosLegalesScreen(
                navController = navController,
                navBackStackEntry = backStackEntry
            )
        }


        // Nueva ruta para la pantalla InfoAbogadosScreen
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


    }
}
