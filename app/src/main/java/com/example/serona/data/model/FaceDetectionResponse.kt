package com.example.serona.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data model for the face analysis API response.
 * Maps JSON keys from the Python server to Kotlin properties.
 */
data class FaceDetectionResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("shape")
    val shape: String?,             // Example: "Oval (95%)"

    @SerializedName("skintone")
    val skintone: String?,          // Example: "Medium Tan"

    @SerializedName("message")
    val message: String?,           // Error message if status is "failed"

    @SerializedName("server_inference_ms")
    val serverInferenceMs: String?  // Pure ML model processing time from server
)