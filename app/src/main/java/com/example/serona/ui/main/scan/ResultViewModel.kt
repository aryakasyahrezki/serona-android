package com.example.serona.ui.main.scan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.dto.UpdateProfileRequest
import com.example.serona.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val userRepo: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Ambil data mentah dari navigasi
    private val rawShape: String = savedStateHandle.get<String>("shape") ?: ""
    private val rawTone: String = savedStateHandle.get<String>("skintone") ?: ""

    // Proses decoding dilakukan di sini agar UI bersih
    val decodedShape: String = try {
        URLDecoder.decode(rawShape, "UTF-8")
            .replace("+", " ")
            .substringBefore(" (")
            .trim()
    } catch (e: Exception) { rawShape }

    val decodedTone: String = try {
        URLDecoder.decode(rawTone, "UTF-8")
            .replace("+", " ")
            .trim()
    } catch (e: Exception) { rawTone }

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState = _uiState.asStateFlow()

    fun saveToProfile() {
        _uiState.update { it.copy(errorMessage = null) }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val currentUser = userRepo.userDataFlow.first()
            if (currentUser != null) {
                val request = UpdateProfileRequest(
                    name = currentUser.name,
                    email = currentUser.email,
                    country = currentUser.country,
                    birth_date = currentUser.birthDate,
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
                // Kasus jika data user di Room tiba-tiba kosong
                _uiState.update { it.copy(
                    isSaving = false,
                    errorMessage = "User session not found. Please re-login."
                ) }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(
                errorMessage = null,
            )
        }
    }
}

data class ResultUiState(
    val isSaving: Boolean = false,      // Menangani loading spinner saat klik Save
    val saveSuccess: Boolean = false,   // Trigger navigasi balik ke Home saat sukses
    val errorMessage: String? = null    // Menampilkan pesan jika API/Network error
)