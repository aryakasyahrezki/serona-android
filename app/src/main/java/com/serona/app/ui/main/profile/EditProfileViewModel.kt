package com.serona.app.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serona.app.data.dto.UpdateProfileRequest
import com.serona.app.data.model.Gender
import com.serona.app.data.model.User
import com.serona.app.data.repository.AuthRepository
import com.serona.app.data.repository.UserRepository
import com.serona.app.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : ViewModel(){

    private var originalUser: User? = null


    init {
        viewModelScope.launch {
            userRepo.userDataFlow.collect { user ->
                if (user != null && originalUser == null) {
                    originalUser = user
                    val parsedDate = DateUtils.parseBirthDate(user.birthDate)
                    _uiState.update { it.copy(
                        name = user.name,
                        country = user.country,
                        gender = user.gender,
                        day = parsedDate?.first ?: "",
                        month = parsedDate?.second ?: "",
                        year = parsedDate?.third ?: ""
                    )}
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(EditProfileUiState())
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
            email = originalUser?.email ?: "",
            country = state.country,
            birth_date = fullDate,
            gender = formattedGender,
            face_shape = originalUser?.faceShape ?: "",
            skin_tone = originalUser?.skinTone ?: ""
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = userRepo.updateProfile(request)

            result.onSuccess { msg ->

                authRepo.updateFirebaseName(state.name)

                _uiState.update { it.copy(
                    updateSuccess = true,
                    isLoading = false,
                    errorMessage = msg
                ) }

                originalUser = null
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

        val dobError = when {
            state.day.isBlank() || state.month.isBlank() || state.year.isBlank() -> {
                isValid = false
                "Please complete your date of birth"
            }

            !DateUtils.isValidDate(state.day, state.month, state.year) -> {
                isValid = false
                "The date is invalid for the selected month"
            }
            else -> null
        }

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
        val user = originalUser ?: return

        // Parse tanggal secara dinamis dari originalUser yang ada saat ini
        val parsedOriginalDate = DateUtils.parseBirthDate(user.birthDate)

        // Bandingkan setiap field dengan data asli dari session
        val isNameChanged = state.name != (originalUser?.name ?: "")
        val isCountryChanged = state.country != (originalUser?.country ?: "")
        val isGenderChanged = state.gender != (originalUser?.gender ?: Gender.MALE)
        val isDayChanged = state.day != (parsedOriginalDate?.first ?: "")
        val isMonthChanged = state.month != (parsedOriginalDate?.second ?: "")
        val isYearChanged = state.year != (parsedOriginalDate?.third ?: "")

        val totalChanged = isNameChanged || isCountryChanged || isGenderChanged ||
                isDayChanged || isMonthChanged || isYearChanged

        _uiState.update { it.copy(isChanged = totalChanged) }
    }

    private fun resetChangeStatus(){
        _uiState.update { it.copy(isChanged = false) }
    }
}

