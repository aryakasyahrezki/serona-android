package com.example.serona.data.dto

// dto itu semua kumpulan data data yang bakal di terima/kirim dari/ke be (data mentah)

data class PersonalInfoResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

data class PersonalInfoRequest(
    val gender: String,
    val country: String,
    val birth_date: String
)