package com.example.serona.data.api

import com.example.serona.data.model.Tutorial
import okhttp3.ResponseBody
import retrofit2.http.*
import retrofit2.Response

interface TutorialApi {

    @GET("api/tutorials")
    suspend fun getTutorialsRaw(): ResponseBody

    // Get all tutorials
    @GET("api/tutorials")
    suspend fun getTutorials(): List<Tutorial>

    // Get tutorial by ID
    @GET("api/tutorials/{id}")
    suspend fun getTutorial(@Path("id") id: Int): Tutorial

    // Get user's favorite tutorials
    @GET("api/user/favorites")
    suspend fun getFavorites(): List<Tutorial>

    // Add tutorial to favorites
    @POST("api/user/favorites")
    suspend fun addFavorite(@Body request: FavoriteRequest): Response<Unit>

    // Remove tutorial from favorites
    @DELETE("api/user/favorites/{tutorialId}")
    suspend fun removeFavorite(@Path("tutorialId") tutorialId: Int): Response<Unit>
}

// Request body untuk add favorite
data class FavoriteRequest(
    val tutorial_id: Int
)