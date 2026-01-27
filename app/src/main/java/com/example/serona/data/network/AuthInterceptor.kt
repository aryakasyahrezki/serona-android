package com.example.serona.data.network

import android.util.Log
import com.example.serona.data.repository.AuthRepository
import com.google.android.gms.tasks.Tasks
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authRepo: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Content-Type", "application/json")

        val user = authRepo.getCurrentUser()
        if (user != null) {
            try {
                // force = false, artinya dia langsung ngambil dari cache di firebase
                val tokenResult = Tasks.await(user.getIdToken(false))
                val token = tokenResult.token

                if (!token.isNullOrBlank()) {
                    requestBuilder.addHeader(
                        "Authorization",
                        "Bearer $token"
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthInterceptor", "Failed to get token", e)
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}

