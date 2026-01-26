package com.example.serona.data.dto

data class BaseResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)
data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val country: String,
    val birth_date: String,
    val gender: String,
    val face_shape : String,
    val skin_tone : String
)