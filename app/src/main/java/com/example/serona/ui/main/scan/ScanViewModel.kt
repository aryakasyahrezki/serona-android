package com.example.serona.ui.main.scan

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.model.FaceDetectionResponse
import com.example.serona.data.api.FaceAnalysisApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val apiService: FaceAnalysisApi
) : ViewModel() {

    private val _showInstructionPopup = mutableStateOf(true)
    val showInstructionPopup: State<Boolean> = _showInstructionPopup

    private val _progress = mutableFloatStateOf(0f)
    val progress: State<Float> = _progress

    private val _isScanning = mutableStateOf(false)
    val isScanning: State<Boolean> = _isScanning

    private val _showRecommendationButton = mutableStateOf(false)
    val showRecommendationButton: State<Boolean> = _showRecommendationButton

    private val _scanResult = mutableStateOf<FaceDetectionResponse?>(null)
    val scanResult: State<FaceDetectionResponse?> = _scanResult

    private var rescanJob: Job? = null

    // --- FUNGSI BARU UNTUK MENGHILANGKAN ERROR ---
    /**
     * Fungsi ini ditambahkan agar ScanScreen.kt tidak error saat mencoba memanggilnya.
     */


    fun dismissPopup() {
        _showInstructionPopup.value = false
        _isScanning.value = true
    }

    fun onFaceDetected(currentFile: File? = null) {
        if (!_isScanning.value) return
        _progress.floatValue += 0.3f
        if (_progress.floatValue >= 1.0f) {
            _progress.floatValue = 1.0f
            onFirstScanComplete(currentFile)
        }
    }

    fun onFaceLost() {
        if (_isScanning.value && !_showRecommendationButton.value) {
            _progress.floatValue = 0f
        }
    }

    fun onFirstScanComplete(file: File?) {
        viewModelScope.launch {
            try {
                if (file != null) {
                    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                    val response = apiService.detectFace(body)
                    _scanResult.value = response
                    _showRecommendationButton.value = true
                }

                delay(1000) // User sempat baca hasil
                _progress.floatValue = 0.0f // Reset bar ke nol
                _isScanning.value = true // Nyalakan lagi

            } catch (e: Exception) {
                _progress.floatValue = 0.0f
                _isScanning.value = true
            }
        }
    }

    fun uploadFaceImage(file: File) {
        viewModelScope.launch {
            try {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val response = apiService.detectFace(body)
                _scanResult.value = response
            } catch (e: Exception) {
                _scanResult.value = FaceDetectionResponse(
                    status = "failed",
                    shape = "Error: Server Offline",
                    skintone = null,
                    message = e.message
                )
                e.printStackTrace()
            }
        }
    }

    private fun startAutoRescan(file: File) {
        rescanJob?.cancel()
        rescanJob = viewModelScope.launch {
            while (true) {
                delay(1500)
                uploadFaceImage(file)
            }
        }
    }

    fun resetScanner() {
        rescanJob?.cancel()
        _isScanning.value = true
        _progress.floatValue = 0f
        _scanResult.value = null
        _showRecommendationButton.value = false
        _showInstructionPopup.value = true
    }

    override fun onCleared() {
        super.onCleared()
        rescanJob?.cancel()
    }
}