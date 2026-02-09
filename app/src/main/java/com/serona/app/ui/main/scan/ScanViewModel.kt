package com.serona.app.ui.main.scan

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serona.app.data.model.FaceDetectionResponse
import com.serona.app.data.api.FaceAnalysisApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for the Live Camera Scanning interface.
 * Coordinates real-time face detection progress and handles automated image
 * submission once the detection threshold is met.
 */
@HiltViewModel
class ScanViewModel @Inject constructor(
    private val apiService: FaceAnalysisApi
) : ViewModel() {

    // --- UI STATES ---
    private val _isFaceInFrame = mutableStateOf(false)
    val isFaceInFrame: State<Boolean> = _isFaceInFrame
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

    private var _isCurrentlyUploading = mutableStateOf(false)
    val isCurrentlyUploading: State<Boolean> = _isCurrentlyUploading

    /**
     * Closes the initial instruction guide and activates the camera analysis.
     */
    fun dismissPopup() {
        _showInstructionPopup.value = false
        _isScanning.value = true
    }

    /**
     * Handles the automated upload process once a stable face is captured.
     * Implements multi-tier performance logging for thesis evaluation.
     */
    fun onFirstScanComplete(file: File?) {
        if (file == null || _isCurrentlyUploading.value) return

        // TIMER 3 START: Captures the timestamp before initiating the network request.
        val startTime = System.currentTimeMillis()
        _isCurrentlyUploading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                val response = apiService.detectFace(body  )

                withContext(Dispatchers.Main) {
                    if (response != null &&
                        !response.shape.isNullOrBlank() &&
                        !response.skintone.isNullOrBlank()) {

                        // TIMER 3 END: Calculates the total User Experience latency.
                        val totalTime = System.currentTimeMillis() - startTime

                        /**
                         * PERFORMANCE LOGS
                         * Essential for analyzing the impact of network latency vs model inference.
                         */
                        // This log tracks the total round-trip time (End-to-End Latency)
                        // from the moment the request is initiated until the result is received.
                        Log.d("PERFORMANCE_TEST", ">>> Total Response Time (UX): $totalTime ms")

                        // This log tracks the pure mathematical prediction time on the server,
                        // excluding network latency and image decoding.
                        Log.d("PERFORMANCE_TEST", ">>> Server Model Classification: ${response.serverInferenceMs} ms")

                        _scanResult.value = response
                        _showRecommendationButton.value = true
                    }

                    _progress.floatValue = 0.0f
                    _isScanning.value = true
                    _isCurrentlyUploading.value = false
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ScanViewModel", "Network Failure: ${e.message}")
                    _progress.floatValue = 0.0f
                    _isScanning.value = true
                    _isCurrentlyUploading.value = false
                }
            }
        }
    }

    /**
     * Logic for incremental face detection.
     * Increments the progress bar and triggers upload when the scan is "complete" (1.0f).
     */
    fun onFaceDetected(currentFile: File? = null) {
        _isFaceInFrame.value = true
        viewModelScope.launch(Dispatchers.Main) {
            if (!_isScanning.value || _isCurrentlyUploading.value) return@launch

            // Jika belum penuh, kita jalankan animasi progres (misal +0.2f per frame)
            if (_progress.floatValue < 1.0f) {
                val nextProgress = _progress.floatValue + 0.35f
                _progress.floatValue = nextProgress.coerceAtMost(1.0f)

                // Jika baru saja menyentuh 1.0f, panggil upload pertama kali
                if (_progress.floatValue >= 1.0f) {
                    onFirstScanComplete(currentFile)
                }
            }
        }
    }

        /**
         * Resets scan progress if the subject moves out of the camera frame.
         */
    fun onFaceLost() {
        _isFaceInFrame.value = false
        if (_isScanning.value && !_showRecommendationButton.value) {
            // LANGSUNG KOSONG: Begitu wajah hilang, bar balik ke 0
            _progress.floatValue = 0f
        }
    }

    /**
     * Stops the camera processing and resets the progress state.
     */
    fun stopScanning() {
        _isScanning.value = false
        _progress.floatValue = 0f
    }
}