// com.example.proyectobufetec/screens/abogado/AbogadoProfileScreen.kt

package com.example.proyectobufetec.screens.abogado

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.R
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.io.InputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbogadoProfileScreen(navController: NavController, appViewModel: UserViewModel) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // Campos adicionales para el abogado
    var experiencia by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var resumen by remember { mutableStateOf("") }

    // Configurar el selector de fotos
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetContent = {
            ChangePasswordPanel(
                onClose = {
                    coroutineScope.launch { sheetState.bottomSheetState.hide() }
                }
            )
        },
        sheetPeekHeight = 0.dp // Ocultar panel hasta que sea llamado
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Mostrar la imagen seleccionada o un marcador de posición si no hay ninguna
            if (selectedImageUri != null) {
                val context = LocalContext.current
                val inputStream: InputStream? = context.contentResolver.openInputStream(selectedImageUri!!)

                bitmap = BitmapFactory.decodeStream(inputStream)

                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .shadow(8.dp, CircleShape) // Añadir sombra
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Placeholder Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Perfil del Abogado",
                fontSize = 32.sp,
                color = TecBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Campos para experiencia, especialidad y resumen
            OutlinedTextField(
                value = especialidad,
                onValueChange = { especialidad = it },
                label = { Text("Especialidad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = experiencia,
                onValueChange = { experiencia = it },
                label = { Text("Experiencia (en años)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = resumen,
                onValueChange = { resumen = it },
                label = { Text("Resumen Personal") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Botón para cambiar la imagen de perfil
            Button(onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Text(text = "Seleccionar Imagen de Perfil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Opción para cambiar la contraseña
            Text(
                text = "Cambiar Contraseña",
                color = TecBlue,
                modifier = Modifier.clickable {
                    coroutineScope.launch { sheetState.bottomSheetState.expand() }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar la información
            Button(onClick = {
                // Aquí podrías guardar la información ingresada por el abogado
            }) {
                Text(text = "Guardar Información")
            }
        }
    }
}

@Composable
fun ChangePasswordPanel(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cambiar Contraseña", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TecBlue)
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para contraseña actual
        OutlinedTextField(
            value = "",
            onValueChange = { /* Manejar cambio */ },
            label = { Text("Contraseña Actual") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para nueva contraseña
        OutlinedTextField(
            value = "",
            onValueChange = { /* Manejar cambio */ },
            label = { Text("Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo para confirmar nueva contraseña
        OutlinedTextField(
            value = "",
            onValueChange = { /* Manejar cambio */ },
            label = { Text("Confirmar Nueva Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Botón para cerrar el panel
        Button(onClick = onClose) {
            Text(text = "Confirmar")
        }
    }
}
