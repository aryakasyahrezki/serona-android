package com.example.serona.data.dto

data class RegisterResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

data class RegisterUserRequest(
    val name: String,
    val email: String
)