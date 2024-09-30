// com.example.proyectobufetec/screens/clientes/ChatBotScreen.kt

package com.example.proyectobufetec.screens.clientes
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
@Composable
fun ChatBotScreen(navController: NavController, appViewModel: UserViewModel, modifier: Modifier = Modifier) {
    val messageList = remember { mutableStateListOf<Message>(
        Message("¡Hola! Soy tu asistente virtual, ¿en qué puedo ayudarte hoy?", isUser = false)
    ) }

    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Usamos un `Column` que permite hacer scroll en toda la pantalla
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Permite el scroll vertical
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(350.dp)  // Aumenta la altura del área del chatbot
                .background(Color(0xFFF0F0F5), shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("ChatMessagesList")  // Agregamos un testTag para la lista de mensajes
            ) {
                items(messageList) { message ->
                    ChatBubble(message)
                }
            }
        }

        // Ajustamos el espaciado para evitar el gran espacio entre el botón de enviar y la sección siguiente
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("ESCRIBE TU PREGUNTA AQUÍ") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = Color(0xFF003366),
                unfocusedLabelColor = Color(0xFF666666),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF003366)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("UserInputField")  // Agregamos un testTag para el campo de texto
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (userInput.isNotEmpty()) {
                    val userMessage = Message(userInput, isUser = true)
                    messageList.add(userMessage)

                    getResponse(userInput) { response ->
                        val botMessage = Message(response, isUser = false)
                        messageList.add(botMessage)

                        coroutineScope.launch {
                            listState.animateScrollToItem(messageList.size - 1)
                        }
                    }

                    userInput = ""

                    coroutineScope.launch {
                        listState.animateScrollToItem(messageList.size - 1)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("SendButton")  // Agregamos un testTag para el botón de enviar
        ) {
            Text(text = "ENVIAR", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        // Eliminar el espaciador extra entre el botón y el título
        Spacer(modifier = Modifier.height(16.dp))

        // Título más destacado para la sección de "Descubre Nuevas Posibilidades"
        Text(
            text = "DESCUBRE NUEVAS POSIBILIDADES",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            color = Color(0xFF003366)
        )

        // Sección de botones con menos espacio y scroll permitido
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                SquareButtonWithIcon(
                    title = "BIBLIOTECA",
                    icon = Icons.Default.Book,
                    description = "Consulta libros y documentos disponibles en nuestra biblioteca.",
                    onClick = {
                        navController.navigate("biblioteca")
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                SquareButtonWithIcon(
                    title = "CASOS LEGALES",
                    icon = Icons.Default.Gavel,
                    description = "Accede a información sobre casos legales relevantes.",
                    onClick = {
                        navController.navigate("casos legales")
                    }
                )
            }
        }
    }
}

@Composable
fun SquareButtonWithIcon(title: String, icon: ImageVector, description: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(80.dp)
                .padding(end = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)),
            shape = RoundedCornerShape(0.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier.weight(2f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF003366),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val alignment = if (message.isUser) Arrangement.End else Arrangement.Start
    val backgroundBrush = if (message.isUser) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF003366),
                Color(0xFF004080)
            )
        )
    } else {
        SolidColor(Color(0xFFD1D1E0))
    }
    val textColor = if (message.isUser) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = alignment
    ) {
        Box(
            modifier = Modifier
                .background(brush = backgroundBrush, shape = RoundedCornerShape(16.dp))
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(text = message.text, color = textColor)
        }
    }
}

data class Message(val text: String, val isUser: Boolean)


fun getResponse(userMessage: String, callback: (String) -> Unit) {
    val client = OkHttpClient()

    val url = "https://bufetec-postgres.onrender.com/api/chat"

    val requestBody = """
        {
            "message": "$userMessage"
        }
    """.trimIndent()

    val request = Request.Builder()
        .url(url)
        .addHeader("Content-Type", "application/json")
        .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
        .build()

    thread {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "API failed", e)
                callback("Failed to get a response. Try again.")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("error", "Unexpected code $response")
                    callback("Error: ${response.code}")
                    return
                }

                val body = response.body?.string()
                if (body != null) {
                    try {
                        val jsonObject = JSONObject(body)
                        val textResult = jsonObject.getString("response")
                        callback(textResult.trim())
                    } catch (e: Exception) {
                        Log.e("error", "JSON parsing error", e)
                        callback("Error parsing response. Please try again.")
                    }
                } else {
                    callback("No response from the server.")
                }
            }
        })
    }
}
