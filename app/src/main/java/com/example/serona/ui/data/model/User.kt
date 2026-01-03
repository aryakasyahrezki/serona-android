package com.example.serona.ui.data.model

data class User(
    val name: String,
    val email: String,
    val gender: Gender = Gender.MALE, //default
    val country: String = "",
    val birthDate: String = ""
)

enum class Gender{MALE, FEMALE}