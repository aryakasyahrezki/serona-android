package com.example.serona.data.model

import com.google.gson.annotations.SerializedName



data class Tutorial(
    val id: Int,
    val title: String,
    val description: String,
    val image_url: String,

    @SerializedName("main_category")
    val mainCategory: String,

    @SerializedName("sub_category")
    val subCategory: String,

    @SerializedName("is_favorite")
    val isFavorite: Boolean
)

data class TutorialStep(
    val id: Int,
    val tutorial_id: Int,
    val step_number: Int,
    val type: String,
    val title: String,
    val description: String,
    val image_url: String,
    val extra: String
)

