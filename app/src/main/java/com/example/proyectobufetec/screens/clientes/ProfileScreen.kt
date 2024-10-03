import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyectobufetec.R
import android.graphics.BitmapFactory
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.ui.theme.TecBlue
import com.example.proyectobufetec.viewmodel.UserViewModel
import java.io.InputStream
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, appViewModel: UserViewModel) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // Set up the Photo Picker launcher
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
            verticalArrangement = Arrangement.Center
        ) {
            // Display the selected image or a placeholder if no image is selected
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
                text = "Ignacio Herrera",
                fontSize = 32.sp,
                color = TecBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Email
            Text(
                text = "ignacio.herrera@example.com",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Button to launch the photo picker for images
            Button(onClick = {
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) {
                Text(text = "Select Profile Picture")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Change Password Option
            Text(
                text = "Cambiar Contraseña",
                color = TecBlue,
                modifier = Modifier.clickable {
                    coroutineScope.launch { sheetState.bottomSheetState.expand() }
                }
            )
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
