package com.example.proyectobufetec.service

import com.example.proyectobufetec.data.usuario.*
import retrofit2.Response

// Fake implementation of UserService for testing purposes
class FakeUserService : UsuarioApiService {

    override suspend fun register(user: RegisterRequest): Response<TokenResponse> {
        // Return a fake success response wrapped in a Retrofit Response
        return Response.success(
            TokenResponse(
                message = "Fake User registered successfully",
                token = "fake_token_for_registration"
            )
        )
    }

    override suspend fun login(user: LoginRequest): Response<TokenResponse> {
        // Return a fake success response wrapped in a Retrofit Response
        return Response.success(
            TokenResponse(
                message = "Fake login successful",
                token = "fake_token_for_login"
            )
        )
    }

    override suspend fun verifyToken(token: String): Response<VerifyTokenResponse> {
        // Return a fake success response indicating the token is valid
        return Response.success(
            VerifyTokenResponse(
                message = "Token is valid",
                user = User(id = 1, email = "test@example.com", role = "User")
            )
        )
    }
}
