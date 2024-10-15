// com.example.proyectobufetec/screens/HomeScreen.kt

package com.example.proyectobufetec.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.R
import com.example.proyectobufetec.components.NavigationDrawer
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, appViewModel: UserViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    title = { Text("Bufetec") },
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagen del logo de Bufetec (sin margen extra)
                    Image(
                        painter = painterResource(id = R.drawable.bufetec),
                        contentDescription = "Bufetec Logo",
                        modifier = Modifier
                            .size(300.dp)
                    )

                    // Eliminar o reducir el `Spacer` entre la imagen y el título
                    // Spacer(modifier = Modifier.height(16.dp)) // <--- Eliminar este `Spacer`

                    // Título principal sin espacio extra
                    Text(
                        text = "Bienvenidos a Bufetec",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TecBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Descripción
                    Text(
                        text = "Descubre nuestros servicios legales personalizados para resolver tus problemas con la mejor atención.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Servicios destacados
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ServiceItem(
                            iconId = R.drawable.mercantil,
                            title = "Mercantil"
                        )
                        ServiceItem(
                            iconId = R.drawable.family,
                            title = "Familiar"
                        )
                        ServiceItem(
                            iconId = R.drawable.civil,
                            title = "Civil"
                        )
                    }
                }
            }
        )
    }
}


@Composable
fun ServiceItem(iconId: Int, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = title,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TecBlue
        )
    }
}
