package com.example.serona.data.dto

import com.example.serona.data.model.Tutorial
import com.google.gson.annotations.SerializedName

data class FavoriteResponse(
    val success: String,
    val message: String,
    val data: List<FavoriteItem>
)

data class FavoriteItem(
    val id: Int,
    val user_id: Int,
    val tutorial_id: Int,

    @SerializedName("tutorial")
    val tutorial: Tutorial
)
