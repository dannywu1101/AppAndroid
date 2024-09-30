// com.example.proyectobufetec/ChatBotInstrumentedTest

package com.example.proyectobufetec

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.proyectobufetec.screens.clientes.ChatBotScreen
import com.example.proyectobufetec.viewmodel.UserViewModel
import org.junit.Rule
import org.junit.Test
import androidx.compose.runtime.mutableStateListOf
import com.example.proyectobufetec.service.FakeUserService

class ChatBotInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testChatBotInteraction() {
        val fakeNavController = FakeNavController(InstrumentationRegistry.getInstrumentation().targetContext)
        val fakeUserViewModel = UserViewModel(userService = FakeUserService())

        composeTestRule.setContent {
            ChatBotScreen(navController = fakeNavController, appViewModel = fakeUserViewModel)
        }

        composeTestRule.onNodeWithText("¡Hola! Soy tu asistente virtual, ¿en qué puedo ayudarte hoy?")
            .assertExists()

        composeTestRule.onNodeWithTag("UserInputField")
            .performTextInput("Hola")

        composeTestRule.onNodeWithTag("SendButton").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Failed to get a response. Try again.")
            .assertDoesNotExist()

        composeTestRule.onNodeWithTag("ChatMessagesList").onChildren().assertCountEquals(2)
    }



    class FakeNavController(context: Context) : NavController(context) {
        override fun popBackStack(): Boolean {
            return true // Always return true for testing purposes
        }
    }
}
