package com.example.serona.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.dto.UpdateProfileRequest
import com.example.serona.data.model.Gender
import com.example.serona.data.repository.AuthRepository
import com.example.serona.data.repository.UserRepository
import com.example.serona.data.session.UserSession
import com.example.serona.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.compareTo

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository,
    private val userSession: UserSession
) : ViewModel(){

    private val currentUser = userSession.user.value

    private val parsedDate = DateUtils.parseBirthDate(currentUser?.birthDate)

    init {
        println("DEBUG: Original BirthDate from Session: ${currentUser?.birthDate}")
    }

    private val _uiState = MutableStateFlow(
        EditProfileUiState(
            name = currentUser?.name ?: "",
            country = currentUser?.country ?: "",
            gender = currentUser?.gender ?: Gender.MALE,
            day = parsedDate?.first ?: "",
            month = parsedDate?.second ?: "",
            year = parsedDate?.third ?: "",
        )
    )
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.name, nameError = null) }
                updateChangeStatus()
            }
            is EditProfileEvent.CountryChanged -> {
                _uiState.update { it.copy(country = event.country, countryError = null) }
                updateChangeStatus()
            }
            is EditProfileEvent.DayChanged -> {
                _uiState.update { it.copy(day = event.day, dobError = null) }
                updateChangeStatus()
            }
            is EditProfileEvent.MonthChanged -> {
                _uiState.update { it.copy(month = event.month, dobError = null) }
                updateChangeStatus()
            }
            is EditProfileEvent.YearChanged -> {
                _uiState.update { it.copy(year = event.year, dobError = null) }
                updateChangeStatus()
            }
            is EditProfileEvent.GenderChanged -> {
                _uiState.update { it.copy(gender = event.gender) }
                updateChangeStatus()
            }
            is EditProfileEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            EditProfileEvent.Save -> saveChanges()
        }
    }

    fun saveChanges() {
        _uiState.update { it.copy(errorMessage = null) }

        if (!validateForm()) return

        val state = _uiState.value
        val fullDate = DateUtils.formatBirthDate(state.day, state.month, state.year)

        val formattedGender = state.gender.name.lowercase().replaceFirstChar {
            it.uppercase()
        }

        val request = UpdateProfileRequest(
            name = state.name,
            email = currentUser?.email ?: "",
            country = state.country,
            birth_date = fullDate,
            gender = formattedGender,
            face_shape = currentUser?.faceShape ?: "",
            skin_tone = currentUser?.skinTone ?: ""
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = userRepo.updateProfile(request)

            result.onSuccess { (msg, updatedUser) ->
                userSession.setUser(updatedUser)

                authRepo.updateFirebaseName(updatedUser.name)

                _uiState.update { it.copy(
                    updateSuccess = true,
                    isLoading = false,
                    errorMessage = msg
                ) }

                resetChangeStatus()
            } .onFailure {exception ->
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Failed to Update"
                ) }
            }
        }
    }

    private fun validateForm(): Boolean {
        val state = _uiState.value
        var isValid = true

        val nameError = if (state.name.length < 3) {
            isValid = false
            "Name must be at least 3 characters"
        } else null

        val countryError = if (state.country.isBlank()) {
            isValid = false
            "Please select your country"
        } else null

        val dobError = if (state.day.isBlank() || state.month.isBlank() || state.year.isBlank()) {
            isValid = false
            "Please complete your date of birth"
        } else null

        _uiState.update {
            it.copy(
                nameError = nameError,
                countryError = countryError,
                dobError = dobError
            )
        }

        return isValid
    }

    private fun updateChangeStatus() {
        val state = _uiState.value

        // Bandingkan setiap field dengan data asli dari session
        val isNameChanged = state.name != (currentUser?.name ?: "")
        val isCountryChanged = state.country != (currentUser?.country ?: "")
        val isGenderChanged = state.gender != (currentUser?.gender ?: Gender.MALE)
        val isDayChanged = state.day != (parsedDate?.first ?: "")
        val isMonthChanged = state.month != (parsedDate?.second ?: "")
        val isYearChanged = state.year != (parsedDate?.third ?: "")

        val totalChanged = isNameChanged || isCountryChanged || isGenderChanged ||
                isDayChanged || isMonthChanged || isYearChanged

        _uiState.update { it.copy(isChanged = totalChanged) }
    }

    private fun resetChangeStatus(){
        _uiState.update { it.copy(isChanged = false) }
    }
}

