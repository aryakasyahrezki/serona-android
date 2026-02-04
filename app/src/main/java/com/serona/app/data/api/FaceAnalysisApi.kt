package com.serona.app.data.api

import com.serona.app.data.model.FaceDetectionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FaceAnalysisApi {
    /**
     * Mengirimkan file gambar ke endpoint /predict di server Python.
     */
    @Multipart
    @POST("predict")
    suspend fun detectFace(
        @Part file: MultipartBody.Part
    ): FaceDetectionResponse
}