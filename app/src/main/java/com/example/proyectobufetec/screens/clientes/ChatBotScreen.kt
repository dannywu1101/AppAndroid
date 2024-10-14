package com.example.proyectobufetec.screens.clientes

import androidx.compose.foundation.background
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
import com.example.proyectobufetec.viewmodel.ChatViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ChatBotScreen(
    navController: NavController,
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val messageList = remember {
        mutableStateListOf(
            Message("¡Hola! Soy tu asistente virtual, ¿en qué puedo ayudarte hoy?", isUser = false)
        )
    }

    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val chatResponse by chatViewModel.chatBotResponse.collectAsState()

    // Observe chat responses and add to the message list
    LaunchedEffect(chatResponse) {
        if (chatResponse.isNotEmpty()) {
            messageList.add(Message(chatResponse, isUser = false))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF9F9F9))
            .padding(16.dp)
    ) {
        // Display chat messages
        Box(
            modifier = Modifier
                .height(350.dp)
                .background(Color(0xFFF0F0F5), shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                items(messageList) { message ->
                    ChatBubble(message)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input field for user message
        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("ESCRIBE TU PREGUNTA AQUÍ") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = Color(0xFF003366),
                cursorColor = Color(0xFF003366)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Send button to send message
        Button(
            onClick = {
                if (userInput.isNotEmpty()) {
                    messageList.add(Message(userInput, isUser = true))
                    chatViewModel.sendMessage(userInput)
                    userInput = ""
                    coroutineScope.launch { listState.animateScrollToItem(messageList.size - 1) }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("ENVIAR", color = Color.White)
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
            modifier = Modifier.size(80.dp).padding(end = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)),
            shape = RoundedCornerShape(0.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.White)
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
            colors = listOf(Color(0xFF003366), Color(0xFF004080))
        )
    } else {
        SolidColor(Color(0xFFD1D1E0))
    }
    val textColor = if (message.isUser) Color.White else Color.Black

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
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