package com.example.serona.ui.data.dto

// dto itu semua kumpulan data data yang bakal di terima/kirim dari/ke be (data mentah)

data class PersonalInfoRequest(
    val gender: String,
    val country: String,
    val birth_date: String
)