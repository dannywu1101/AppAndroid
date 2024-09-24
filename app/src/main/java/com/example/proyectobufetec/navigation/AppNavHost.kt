// com.example.proyectobufetec/navigation/AppNavHost.kt

package com.example.proyectobufetec.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectobufetec.components.NavigationDrawer

import com.example.proyectobufetec.screens.BibliotecaScreen
import com.example.proyectobufetec.screens.ChatBotScreen
import com.example.proyectobufetec.screens.HomeScreen
import com.example.proyectobufetec.screens.LoginScreen
import com.example.proyectobufetec.screens.ProfileScreen
import com.example.proyectobufetec.screens.RegisterScreen
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(appViewModel: UserViewModel, padding: Modifier) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Crear un solo NavHost para manejar todas las rutas
    NavHost(
        navController = navController,
        startDestination = "chatbot"
        //startDestination = if (appViewModel.isUserLogged) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(navController, appViewModel)
        }

        composable("register") {
            RegisterScreen(navController)
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
                        // Aquí pasamos los padding que vienen del Scaffold
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
                        NavigationDrawer(navController,appViewModel) { destination ->
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
                        NavigationDrawer(navController,appViewModel) { destination ->
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

    }
}