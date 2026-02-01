package com.example.serona.data.repository

import com.example.serona.data.api.FaceAnalysisApi
import com.example.serona.data.model.FaceDetectionResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class FaceRepository @Inject constructor( // Tambahkan @Inject di sini
    private val apiService: FaceAnalysisApi
) {
    suspend fun uploadFaceImage(body: MultipartBody.Part): FaceDetectionResponse {
        return apiService.detectFace(body)
    }
}