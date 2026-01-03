package com.example.serona.ui.data.network

import android.util.Log
import com.example.serona.ui.data.repository.AuthRepository
import com.google.android.gms.tasks.Tasks
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authRepo: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val user = authRepo.getCurrentUser()
        if (user != null) {
            try {
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