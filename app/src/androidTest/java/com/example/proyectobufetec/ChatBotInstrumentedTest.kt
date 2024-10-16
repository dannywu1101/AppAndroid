import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.proyectobufetec.screens.clientes.ChatBotScreen
import com.example.proyectobufetec.viewmodel.UserViewModel
import com.example.proyectobufetec.viewmodel.ChatViewModel
import com.example.proyectobufetec.service.FakeUserService
import org.junit.Rule
import org.junit.Test

class ChatBotInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testChatBotInteraction() {
        // Set up fake dependencies
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val fakeNavController = FakeNavController(context)
        val fakeUserViewModel = UserViewModel(FakeUserService())
        val fakeChatViewModel = ChatViewModel() // Provide ChatViewModel
        val fakeAppViewModel = AppViewModel() // Provide AppViewModel
        val fakeTokenManager =
            com.example.proyectobufetec.data.network.TokenManager(context) // Provide TokenManager

        // Set content with all required parameters
        composeTestRule.setContent {
            ChatBotScreen(
                navController = fakeNavController,
                appViewModel = fakeAppViewModel,
                chatViewModel = fakeChatViewModel,
                tokenManager = fakeTokenManager
            )
        }

        // Verify if initial chatbot message exists
        composeTestRule.onNodeWithText("¡Hola! Soy tu asistente virtual, ¿en qué puedo ayudarte hoy?")
            .assertExists()

        // Enter input in the chatbot
        composeTestRule.onNodeWithTag("UserInputField")
            .performTextInput("Hola")

        // Click the send button
        composeTestRule.onNodeWithTag("SendButton").performClick()

        composeTestRule.waitForIdle()

        // Ensure the error message does not exist after sending
        composeTestRule.onNodeWithText("Failed to get a response. Try again.")
            .assertDoesNotExist()

        // Verify the chatbot messages list has two entries (initial message + user input)
        composeTestRule.onNodeWithTag("ChatMessagesList").onChildren().assertCountEquals(2)
    }

    // FakeNavController for handling navigation in tests
    class FakeNavController(context: Context) : NavController(context) {
        override fun popBackStack(): Boolean {
            return true // Always return true for testing purposes
        }
    }
}
