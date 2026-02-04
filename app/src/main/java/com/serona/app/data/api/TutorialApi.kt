package com.serona.app.data.api

import com.serona.app.data.dto.FavoriteResponse
import com.serona.app.data.model.ApiDetailResponse
import com.serona.app.data.model.ApiResponse
import retrofit2.http.*
import retrofit2.Response

interface TutorialApi {

//    @GET("api/tutorials")
//    suspend fun getTutorialsRaw(): ResponseBody

    // Get all tutorials
    @GET("tutorials")
    suspend fun getTutorials(): ApiResponse

    // Get tutorial by ID
    @GET("tutorials/{id}")
    suspend fun getTutorial(@Path("id") id: Int): ApiDetailResponse

//    @GET("steps")
//    suspend fun getTutorialSteps(
//        @Query("tutorial_id") id: Int
//    ): List<TutorialStep>

    // Get user's favorite tutorials
    @GET("user/favorites")
    suspend fun getFavorites(): FavoriteResponse

    // Add tutorial to favorites
    @POST("user/favorites")
    suspend fun addFavorite(@Body request: FavoriteRequest): Response<Void>

    // Remove tutorial from favorites
    @DELETE("user/favorites/{tutorialId}")
    suspend fun removeFavorite(@Path("tutorialId") tutorialId: Int): Response<Void>
}

// Request body untuk add favorite
data class FavoriteRequest(
    val tutorial_id: Int
)