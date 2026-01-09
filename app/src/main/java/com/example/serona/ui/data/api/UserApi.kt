package com.example.serona.ui.data.api

import com.example.serona.ui.data.dto.PersonalInfoRequest
import com.example.serona.ui.data.dto.RegisterUserRequest
import com.example.serona.ui.data.dto.UserDataResponse
import com.example.serona.ui.data.dto.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi{

    // ini buat ngirim ke BE setelah register
    @POST("register")
    suspend fun registerUser(
        @Body request: RegisterUserRequest
    ): Response<Unit>

    // post ke baseURL/user/personal-info
    @PUT("user/personal-info")
    // dipanggil dalam coroutine, jadi suspend func
    suspend fun submitPersonalInfo(
        @Body request: PersonalInfoRequest //request akan di serialize ke json trs di kirim ke be sbg body
    ): Response<Unit>

    // user/profile -> ini ngambil semua data user bukan personalInfo doang
    @GET("user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>

}