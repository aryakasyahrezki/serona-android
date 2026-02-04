package com.example.serona.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: List<Tutorial>
)

data class Tutorial(
    val id: Int,
    val title: String,
    val description: String,
    val image_url: String,

    @SerializedName("main_category")
    val main_category: String,

    @SerializedName("sub_category")
    val sub_category: String,

    @SerializedName("is_favorite")
    val is_favorite: Boolean,

    val steps: List<TutorialStep>? = null
)

data class ApiDetailResponse(
    val success: Boolean,
    val message: String,
    val data: Tutorial
)

data class TutorialStep(
    val id: Int,
    val tutorial_id: Int,
    val step_number: Int,
    val title: String,
    val description: String,
    val image_url: String,
    val hex: String
)

