package com.example.serona.data.api

import com.example.serona.data.dto.BaseResponse
import com.example.serona.data.dto.PersonalInfoRequest
import com.example.serona.data.dto.PersonalInfoResponse
import com.example.serona.data.dto.RegisterResponse
import com.example.serona.data.dto.RegisterUserRequest
import com.example.serona.data.dto.UpdateProfileRequest
import com.example.serona.data.dto.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi{

    // ini buat ngirim ke BE setelah register
    @POST("register")
    suspend fun registerUser(
        @Body request: RegisterUserRequest
    ): Response<RegisterResponse<RegisterUserRequest>>

    // post ke baseURL/user/personal-info
    @PUT("user/personal-info")
    // dipanggil dalam coroutine, jadi suspend func
    suspend fun submitPersonalInfo(
        @Body request: PersonalInfoRequest //request akan di serialize ke json trs di kirim ke be sbg body
    ): Response<PersonalInfoResponse<PersonalInfoRequest>>

    // user/profile -> ini ngambil semua data user bukan personalInfo doang
    @GET("user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>

    @PUT("user/update-profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<BaseResponse<UpdateProfileRequest>>

    @DELETE("user/delete-account")
    suspend fun deleteAccount(): Response<BaseResponse<Unit>>

}