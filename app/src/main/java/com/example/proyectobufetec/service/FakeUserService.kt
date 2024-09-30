package com.example.proyectobufetec.service

import com.example.proyectobufetec.data.LoginUserRequest
import com.example.proyectobufetec.data.LoginUserResponse
import com.example.proyectobufetec.data.RegisterUserRequest
import com.example.proyectobufetec.data.RegisterUserResponse

// Fake implementation of UserService for testing purposes
class FakeUserService : UserService {
    override suspend fun addUser(user: RegisterUserRequest): RegisterUserResponse {
        // Return a fake success response with required fields
        return RegisterUserResponse(
            message = "Fake User registered successfully",
            token = "fake_token_for_registration"
        )
    }

    override suspend fun loginUser(user: LoginUserRequest): LoginUserResponse {
        // Return a fake success response with required fields
        return LoginUserResponse(
            message = "Fake login successful",
            token = "fake_token_for_login"
        )
    }
}
