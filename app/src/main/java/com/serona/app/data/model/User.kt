package com.serona.app.data.model

data class User(
    val name: String,
    val email: String,
    val gender: Gender = Gender.FEMALE,
    val country: String = "",
    val birthDate: String = "",
    val faceShape: String? = null,
    val skinTone: String? = null
)

enum class Gender{MALE, FEMALE}