package com.serona.app.ui.main.scan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.serona.app.data.repository.FaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

/**
 * ViewModel for the Face Scan Menu, handling image uploads from the gallery.
 * It manages the loading state, error messages, and data sanitization before navigation.
 */
@HiltViewModel
class FaceScanMenuViewModel @Inject constructor(
    private val repository: FaceRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    /**
     * Clears the current error message to reset the UI state.
     */
    fun clearError() { _errorMessage.value = null }

    /**
     * Uploads the selected image to the server and processes the analysis results.
     * It includes data cleaning (Regex) to ensure the result is ready for display.
     */
    fun uploadAndAnalyzeImage(file: File, navController: NavController) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Prepare the image file for multipart upload
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                // Call the repository to handle the network request
                val result = repository.uploadFaceImage(body)

                if (result.shape != null && result.skintone != null) {
                    /**
                     * DATA SANITIZATION LOGIC
                     * Cleans the server output by removing parentheses, percentages, and special characters.
                     * Example: "Heart+(96%)" becomes "Heart".
                     */
                    val cleanShape = result.shape
                        .substringBefore("(")
                        .replace(Regex("[^a-zA-Z]"), "")
                        .trim()

                    val cleanTone = result.skintone
                        .substringBefore("(")
                        .replace(Regex("[^a-zA-Z\\s]"), "")
                        .trim()

                    val encodedShape = URLEncoder.encode(cleanShape, StandardCharsets.UTF_8.toString())
                    val encodedTone = URLEncoder.encode(cleanTone, StandardCharsets.UTF_8.toString())

                    withContext(Dispatchers.Main) {
                        Log.d("SCAN_DEBUG", "Navigating with: $cleanShape and $cleanTone")
                        navController.navigate("result/$encodedShape/$encodedTone")
                    }
                } else {
                    // Handles cases where the server returns empty data (no face detected)
                    _errorMessage.value = "Face not detected. Make sure the photo is a close-up, well-lit, and not too far away."
                }
            } catch (e: Exception) {
                Log.e("SCAN_ERROR", "Error detail: ${e.message}")
                _errorMessage.value = "Failed to connect to the server. Please check your internet connection."
            } finally {
                _isLoading.value = false
            }
        }
    }
}