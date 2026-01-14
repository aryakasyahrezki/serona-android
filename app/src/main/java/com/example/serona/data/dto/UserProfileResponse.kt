package com.example.serona.data.dto

//data class UserProfileResponse(
//    val name: String,
//    val email:String,
//    val gender: String,
//    val country: String,
//    val birth_date: String,
//    val face_shape_id: String,
//    val skin_tone_id: String
//)

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
    val face_shape_id: String?,
    val skin_tone_id: String?
)