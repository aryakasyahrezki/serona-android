package com.example.serona.data.repository

import com.example.serona.data.api.UserApi
import com.example.serona.data.dto.PersonalInfoRequest
import com.example.serona.data.dto.RegisterUserRequest
import com.example.serona.data.dto.UpdateProfileRequest
import com.example.serona.data.model.Gender
import com.example.serona.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
){

    suspend fun registerUser(
        request: RegisterUserRequest
    ): Result<Unit> {
        return try {
            val response = userApi.registerUser(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Register user failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ini func yang berbeda dengan yg di UserAPI, karena dia yang ngirim request ke API dan translate response yang direturn sama fun tersebut untuk di pake di UI nanti
    suspend fun submitPersonalInfo(
        request: PersonalInfoRequest
    ): Result<Unit>{
        return try {
            val response = userApi.submitPersonalInfo(request)
            if(response.isSuccessful){
                Result.success(Unit)
            }else{
                Result.failure(Exception("Failed: ${response.code()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }

    }

    suspend fun getProfile(): Result<User>{
        return try{
            val response = userApi.getProfile()
            if(response.isSuccessful){
                val body = response.body()
                    ?: return Result.failure(Exception("Empty profile response"))

                if (body != null && body.success) {
                    val userData = body.data
                    Result.success(
                        User(
                            name = userData.name,
                            email = userData.email,
                            gender = if (userData.gender == "male") Gender.MALE else Gender.FEMALE,
                            country = userData.country,
                            birthDate = userData.birth_date,
                            faceShape = userData.face_shape_id,
                            skinTone = userData.skin_tone_id
                        )
                    )
                } else {
                    Result.failure(Exception("API returned success false"))
                }
            }else{
                Result.failure(Exception("Failed to load Profile"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun updateProfile(request: UpdateProfileRequest): Result<Pair<String, User>> {
        return try {
            val response = userApi.updateProfile(request)
            val body = response.body()

            if (response.isSuccessful && body != null && body.data != null) {
                val data = body.data
                // Map dari DTO ke Model User aplikasi kamu
                val updatedUser = User(
                    name = data.name,
                    email = data.email,
                    gender = if (data.gender.lowercase() == "male") Gender.MALE else Gender.FEMALE,
                    country = data.country,
                    birthDate = data.birth_date,
                    faceShape = data.face_shape,
                    skinTone = data.skin_tone
                )
                Result.success(Pair(body.message, updatedUser))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "An error occurred"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}