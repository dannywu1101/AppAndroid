// com.example.proyectobufetec/screens/ProfileScreen.kt

package com.example.proyectobufetec.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.proyectobufetec.viewmodel.UserViewModel


@Composable
fun ProfileScreen(navController: NavController, appViewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Centra verticalmente
    ) {
        // Profile Picture
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Foto de Perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = "Nombre del Usuario",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        Text(
            text = "correo@example.com",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Edit Profile Button
        Button(onClick = { /* Acción para editar el perfil */ }) {
            Icon(Icons.Filled.Edit, contentDescription = "Editar")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Editar Perfil")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Change Password Option
        Text(
            text = "Cambiar Contraseña",
            color = Color.Blue,
            modifier = Modifier.clickable { /* Acción para cambiar contraseña */ }
        )
    }
}