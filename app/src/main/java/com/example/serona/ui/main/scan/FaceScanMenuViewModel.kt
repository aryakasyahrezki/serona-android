package com.example.serona.ui.main.scan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.serona.data.repository.FaceRepository
import com.example.serona.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@HiltViewModel
class FaceScanMenuViewModel @Inject constructor(
    private val repository: FaceRepository // Sekarang menggunakan Repository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun clearError() { _errorMessage.value = null }

    fun uploadAndAnalyzeImage(file: File, navController: NavController) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Siapkan file untuk dikirim
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                // 2. Panggil fungsi di repository
                val result = repository.uploadFaceImage(body)

                // 3. Jika berhasil, pindah ke layar hasil
                if (result.shape != null && result.skintone != null) {
                    // FUNGSI PEMBERSIH:
                    // 1. Ambil teks sebelum kurung (jika ada)
                    // 2. Hapus semua karakter yang bukan huruf (seperti + atau %)
                    // 3. Trim spasi
                    val cleanShape = result.shape
                        .substringBefore("(")       // "Heart+(96%)" -> "Heart+"
                        .replace(Regex("[^a-zA-Z]"), "") // "Heart+" -> "Heart"
                        .trim()

                    val cleanTone = result.skintone
                        .substringBefore("(")
                        .replace(Regex("[^a-zA-Z]"), "")
                        .trim()

                    val encodedShape = URLEncoder.encode(cleanShape, StandardCharsets.UTF_8.toString())
                    val encodedTone = URLEncoder.encode(cleanTone, StandardCharsets.UTF_8.toString())

                    withContext(Dispatchers.Main) {
                        Log.d("SCAN_DEBUG", "Navigating with: $cleanShape and $cleanTone")
                        navController.navigate("result/$encodedShape/$encodedTone")
                    }
                }else {
                    // KONDISI: Server membalas tapi data kosong (Wajah tidak terdeteksi)
                    _errorMessage.value = "Face not detected. Make sure the photo is a close-up, well-lit, and not too far away."
                }
            } catch (e: Exception) {
                Log.e("SCAN_ERROR", "Detail Error: ${e.message}")
                // Opsional: Kamu bisa set State baru untuk nampilin pesan error di UI
            } finally {
                _isLoading.value = false
            }
        }
    }
}