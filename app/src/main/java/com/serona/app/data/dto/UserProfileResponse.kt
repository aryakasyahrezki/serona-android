package com.serona.app.data.dto

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    val success: Boolean,
    val data: UserDataResponse // Data asli ada di sini
)

data class UserDataResponse(
    val name: String,
    val email: String,
    val gender: String,
    val country: String,
    val birth_date: String,
    @SerializedName("face_shape") val face_shape: String?, // Tambahkan ini!
    @SerializedName("skin_tone") val skin_tone: String?
)