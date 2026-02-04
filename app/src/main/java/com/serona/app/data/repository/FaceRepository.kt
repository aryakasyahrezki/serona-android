package com.serona.app.data.repository

import com.serona.app.data.api.FaceAnalysisApi
import com.serona.app.data.model.FaceDetectionResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class FaceRepository @Inject constructor(
    private val apiService: FaceAnalysisApi
) {
    suspend fun uploadFaceImage(body: MultipartBody.Part): FaceDetectionResponse {
        return apiService.detectFace(body)
    }
}