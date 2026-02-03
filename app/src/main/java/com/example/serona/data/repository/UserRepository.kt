package com.example.serona.data.repository

import com.example.serona.data.api.UserApi
import com.example.serona.data.dto.BaseResponse
import com.example.serona.data.dto.PersonalInfoRequest
import com.example.serona.data.dto.RegisterUserRequest
import com.example.serona.data.dto.UpdateProfileRequest
import com.example.serona.data.local.room.dao.UserDao
import com.example.serona.data.local.room.entity.UserEntity
import com.example.serona.data.mapper.toDomain
import com.example.serona.data.mapper.updateWith
import com.example.serona.data.model.User
import com.example.serona.data.session.UserSession
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.example.serona.data.mapper.toEntity

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val userSession: UserSession
) {
    private val gson = Gson()

    // Sumber data reaktif (UI update automatically)
    val userDataFlow: Flow<User?> = userDao.getUserFlow().map { it?.toDomain() }

    // Helper: Parse Error dari BE
    private fun parseError(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "An unknown error occurred"
        return try {
            val errorResponse = gson.fromJson(errorBody, BaseResponse::class.java)
            errorResponse.message ?: "An error occurred"
        } catch (e: Exception) { "Internal Server Error" }
    }

    private fun mapException(e: Exception): String {
        return when (e) {
            is java.net.ConnectException -> "Unable to connect to the server. Please ensure the backend is running."
            is java.net.SocketTimeoutException -> "The connection timed out. Please try again later."
            is java.net.UnknownHostException -> "No internet connection detected. Please check your network."
            else -> e.message ?: "An unexpected error occurred."
        }
    }

    suspend fun registerUser(request: RegisterUserRequest): Result<String> {
        return try {
            val response = userApi.registerUser(request)
            val body = response.body()

            if (response.isSuccessful && body?.data != null) {
                val userData = body.data

                val entity = UserEntity(
                    email = userData.email,
                    name = userData.name,
                    gender = "", country = "", birthDate = "", faceShape = null, skinTone = null
                )

                // Simpan ke Database
                userDao.insertUser(entity)

                userSession.setUser(entity.toDomain())
                Result.success("Register Success")
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception(mapException(e)))
        }
    }


    suspend fun submitPersonalInfo(request: PersonalInfoRequest): Result<Unit> {
        return try {
            val response = userApi.submitPersonalInfo(request)
            if (response.isSuccessful) {
                // Ambil data lama, update kolom tertentu, simpan lagi
                val current = userDao.getUserFlow().first()
                current?.let {
                    val updated = it.updateWith(
                        gender = request.gender,
                        country = request.country,
                        birthDate = request.birth_date
                    )
                    userDao.insertUser(updated)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    // Refresh data dari BE
    suspend fun syncFullProfile(): Result<User> {
        return try {
            val response = userApi.getProfile()
            if (response.isSuccessful && response.body()?.success == true) {
                val remote = response.body()!!.data
                val entity = remote.toEntity() // Pakai Mapper yang kita buat tadi

                userDao.insertUser(entity) // Update Database Lokal
                val user = entity.toDomain()
                userSession.setUser(user) // Update RAM Session

                Result.success(user)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) { Result.failure(Exception(mapException(e))) }
    }

    // update dari editProfile
    suspend fun updateProfile(request: UpdateProfileRequest): Result<String> {
        return try {
            val response = userApi.updateProfile(request)
            if (response.isSuccessful) {
                // Sync ulang Room seteleah update di BE berhasil
                syncFullProfile()
                Result.success(response.body()?.message ?: "Profile Updated")
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    // Delete Account
    suspend fun deleteAccountFromBe(): Result<String> {
        return try {
            val response = userApi.deleteAccount()
            if (response.isSuccessful) {
                clearAllLocalData() // Hapus semua jejak di HP
                Result.success(response.body()?.message ?: "Account Deleted")
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    // cleanup saat logout
    suspend fun clearAllLocalData() {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            userDao.clearUser()
        }
        userSession.clear()
    }
}