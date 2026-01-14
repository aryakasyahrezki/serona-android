package com.example.serona.data.model

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