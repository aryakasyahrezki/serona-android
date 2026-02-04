package com.serona.app.ui.main.scan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serona.app.data.dto.UpdateProfileRequest
import com.serona.app.data.repository.UserRepository
import com.serona.app.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

/**
 * ViewModel for the Result Screen.
 * It handles the retrieval of scan data from navigation arguments,
 * data decoding, and the process of saving results to the user's profile.
 */
@HiltViewModel
class ResultViewModel @Inject constructor(
    private val userRepo: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Retrieve raw data passed from the navigation route
    private val rawShape: String = savedStateHandle.get<String>("shape") ?: ""
    private val rawTone: String = savedStateHandle.get<String>("skintone") ?: ""

    /**
     * Decoded Face Shape string.
     * Handles URL decoding and removes extra formatting (e.g., probability percentages)
     * to provide a clean string for the UI.
     */
    val decodedShape: String = try {
        URLDecoder.decode(rawShape, "UTF-8")
            .replace("+", " ")
            .substringBefore(" (")
            .trim()
    } catch (e: Exception) { rawShape }

    /**
     * Decoded Skin Tone string.
     * Standardizes the skin tone result for display and database storage.
     */
    val decodedTone: String = try {
        URLDecoder.decode(rawTone, "UTF-8")
            .replace("+", " ")
            .trim()
    } catch (e: Exception) { rawTone }

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Sends the analysis results to the server to update the user's beauty profile.
     * Uses current user session data from the local repository to build the request.
     */
    fun saveToProfile() {
        _uiState.update { it.copy(errorMessage = null) }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            // Get current user session from the local data flow
            val currentUser = userRepo.userDataFlow.first()
            if (currentUser != null) {
                val parsedDate = DateUtils.parseBirthDate(currentUser.birthDate)

                // Ubah Triple tersebut menjadi string "2025-01-21"
                val formattedDate = if (parsedDate != null) {
                    DateUtils.formatBirthDate(
                        day = parsedDate.first,
                        month = parsedDate.second,
                        year = parsedDate.third
                    )
                } else {
                    currentUser.birthDate // Fallback jika parsing gagal
                }

                val request = UpdateProfileRequest(
                    name = currentUser.name,
                    email = currentUser.email,
                    country = currentUser.country,
                    birth_date = formattedDate,
                    gender = currentUser.gender.name.lowercase().replaceFirstChar { it.uppercase() },
                    face_shape = decodedShape,
                    skin_tone = decodedTone
                )

                val result = userRepo.updateProfile(request)

                result.onSuccess { msg ->
                    _uiState.update { it.copy(
                        isSaving = false,
                        saveSuccess = true,
                        errorMessage = msg
                    ) }
                }.onFailure { exception ->
                    _uiState.update { it.copy(
                        isSaving = false,
                        saveSuccess = false,
                        errorMessage = exception.message ?: "Failed to Update Profile"
                    ) }
                }
            } else {
                _uiState.update { it.copy(
                    isSaving = false,
                    errorMessage = "User session not found. Please re-login."
                ) }
            }
        }
    }

    /**
     * Resets the error message state. Used by the UI to clear Toasts or Dialogs.
     */
    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}

/**
 * Represents the UI state for the Result Screen.
 * @property isSaving Controls the loading indicator during the network request.
 * @property saveSuccess Triggers navigation or success feedback.
 * @property errorMessage Holds the message for Toast/Snackbar notifications.
 */
data class ResultUiState(
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)