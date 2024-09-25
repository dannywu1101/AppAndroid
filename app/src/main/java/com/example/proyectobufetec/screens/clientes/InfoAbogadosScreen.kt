// com.example.proyectobufetec/screens/InfoAbogadosScreen.kt

package com.example.proyectobufetec.screens.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel

@Composable
fun InfoAbogadosScreen(navController: NavController, appViewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 16.dp, end = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Información sobre servicios legales y otros detalles relevantes.",
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = """
                Aqui vamos a explicar que tipos de abogados ofrecen servicio y todos los detalles de 
                bufetec en general.
                
                en busqueda abogados se van a poder encontrar los datos de cada abogado en especifico
            """.trimIndent(),
            fontSize = 14.sp
        )
    }
}



