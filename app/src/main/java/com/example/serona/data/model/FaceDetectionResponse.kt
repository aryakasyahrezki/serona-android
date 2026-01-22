package com.example.serona.data.model

import com.google.gson.annotations.SerializedName

data class FaceDetectionResponse(
    @SerializedName("status")
    val status: String,

    // Kita ubah agar sesuai dengan output Python: "shape" dan "skintone"
    @SerializedName("shape")
    val shape: String?, // Contoh: "Oval (95%)"

    @SerializedName("skintone")
    val skintone: String?, // Contoh: "Medium Tan"

    @SerializedName("message")
    val message: String? // Untuk menangkap pesan error jika status "failed"
)