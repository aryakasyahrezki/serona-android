package com.example.serona.ui.data.model

import androidx.compose.ui.graphics.Shape

data class User(
    val name: String,
    val email: String,
    val gender: Gender = Gender.MALE, //default
    val country: String = "",
    val birthDate: String = "",
    val faceShape: String? = null,
    val skinTone: String? = null
)

enum class Gender{MALE, FEMALE}