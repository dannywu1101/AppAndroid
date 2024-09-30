package com.example.proyectobufetec

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.proyectobufetec.screens.clientes.BusquedaAbogadosScreen
import com.example.proyectobufetec.service.FakeUserService
import com.example.proyectobufetec.viewmodel.UserViewModel
import org.junit.Rule
import org.junit.Test

class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchAndViewLawyerInfo() {
        // Use the fake UserService for testing
        val fakeUserService = FakeUserService()

        // Creating an instance of UserViewModel with the fake UserService
        val userViewModel = UserViewModel(usuarioApiService = fakeUserService)

        // Get the real context from InstrumentationRegistry
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val fakeNavController = FakeNavController(context)

        // Set up the initial UI with the fake UserService and fakeNavController
        composeTestRule.setContent {
            BusquedaAbogadosScreen(navController = fakeNavController, appViewModel = userViewModel)
        }

        // Simulate typing "Juan" in the search field
        composeTestRule.onNodeWithTag("Buscar abogados").performTextInput("Juan")

        // Check if the filtered lawyer "Juan Pérez" is displayed
        composeTestRule.onNodeWithText("Juan Pérez").assertExists()

        // Click on "Juan Pérez"
        composeTestRule.onNodeWithText("Juan Pérez").performClick()

        // Check if the lawyer's info is shown correctly in the bottom sheet
        composeTestRule.onNodeWithText("Información del Abogado").assertExists()
        composeTestRule.onNodeWithText("Nombre: Juan Pérez").assertExists()
        composeTestRule.onNodeWithText("Especialidad: Divorcios").assertExists()
    }

    // FakeNavController for handling navigation in tests
    class FakeNavController(context: Context) : NavController(context) {
        override fun popBackStack(): Boolean {
            return true // Always return true for testing purposes
        }
    }
}
