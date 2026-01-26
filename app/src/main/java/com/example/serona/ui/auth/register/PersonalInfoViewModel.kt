package com.example.serona.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.dto.PersonalInfoRequest
import com.example.serona.data.model.Gender
import com.example.serona.data.repository.AuthRepository
import com.example.serona.data.repository.UserRepository
import com.example.serona.data.session.UserSession
import com.example.serona.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _state = MutableStateFlow(PersonalInfoState())
    val state: StateFlow<PersonalInfoState> = _state.asStateFlow()

    fun selectGender(gender: Gender){
        _state.value = _state.value.copy(gender = gender)
    }

    fun selectCountry(country: String){
        _state.value = _state.value.copy(country = country)
    }

    fun selectDOB(day: String, month: String, year: String){
        _state.value = _state.value.copy(day = day, month = month, year = year)
    }

    fun submitPersonalInfo(onSuccess: () -> Unit){
        val gender = state.value.gender ?: return

        val request = PersonalInfoRequest(
            gender = when(state.value.gender) {
                Gender.MALE -> "Male"
                else -> "Female"
            },
            country = state.value.country,
            birth_date = DateUtils.formatBirthDate(state.value.day, state.value.month, state.value.year)
        )

        viewModelScope.launch {

            _state.value = _state.value.copy(
                errorMessage = null
            )

            val result = userRepo.submitPersonalInfo(request)

            result.onSuccess { dataFromBe ->
                userSession.updatePersonalInfo(
                    gender = if (dataFromBe.gender.lowercase() == "male") Gender.MALE else Gender.FEMALE,
                    country = dataFromBe.country,
                    birthDate = dataFromBe.birth_date
                )
                onSuccess()
            }.onFailure { exception ->
                _state.value = _state.value.copy(
                    errorMessage = exception.message ?: "Failed to submit personal info"
                )
            }
        }
    }

    fun clearError(){
        _state.value = _state.value.copy(errorMessage = null)
    }


}