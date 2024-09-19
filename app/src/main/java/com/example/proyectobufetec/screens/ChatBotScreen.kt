package com.example.proyectobufetec.screens
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proyectobufetec.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.concurrent.thread
@Composable
fun ChatBotScreen(navController: NavController, appViewModel: UserViewModel, modifier: Modifier = Modifier) {
    var userInput by remember { mutableStateOf("") }
    val messageList = remember { mutableStateListOf<Message>() } // List to store chat history

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)  // Solo padding horizontal
    ) {
        // LazyColumn para mostrar el historial de mensajes
        LazyColumn(
            modifier = Modifier
                .weight(1f)  // Para ocupar todo el espacio disponible antes del input
                .padding(top = 8.dp)  // Separar un poco las burbujas del top
        ) {
            items(messageList) { message ->
                ChatBubble(message)
            }
        }

        // Input field y botón para enviar
        val interactionSource = remember { MutableInteractionSource() }

        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Your question") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1E30),
                unfocusedContainerColor = Color(0xFF1E1E30),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            interactionSource = interactionSource
        )



        Button(
            onClick = {
                if (userInput.isNotEmpty()) {
                    val userMessage = Message(userInput, isUser = true)
                    messageList.add(userMessage)

                    getResponse(userInput) { response ->
                        val botMessage = Message(response, isUser = false)
                        messageList.add(botMessage)
                    }

                    userInput = "" // Limpiar el input después de enviar
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3C3F58)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)  // Agrega un padding inferior para que el botón no toque el borde de la pantalla
        ) {
            Text(text = "Ask", color = Color.White)
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val alignment = if (message.isUser) Arrangement.End else Arrangement.Start
    val backgroundBrush = if (message.isUser) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFFFB6C1), // Light Pink
                Color(0xFFFF69B4)  // Hot Pink
            )
        )
    } else {
        SolidColor(Color(0xFF33354B))
    }
    val textColor = if (message.isUser) Color.White else Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = alignment
    ) {
        Box(
            modifier = Modifier
                .background(brush = backgroundBrush, shape = RoundedCornerShape(16.dp))
                .padding(12.dp)
                .widthIn(max = 300.dp)
        ) {
            Text(text = message.text, color = textColor)
        }
    }
}

data class Message(val text: String, val isUser: Boolean)

fun getResponse(userMessage: String, callback: (String) -> Unit) {

}
