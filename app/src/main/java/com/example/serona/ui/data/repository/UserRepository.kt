package com.example.serona.ui.data.repository

import com.example.serona.ui.data.api.UserApi
import com.example.serona.ui.data.dto.PersonalInfoRequest
import com.example.serona.ui.data.dto.RegisterUserRequest
import com.example.serona.ui.data.model.Gender
import com.example.serona.ui.data.model.User
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

}