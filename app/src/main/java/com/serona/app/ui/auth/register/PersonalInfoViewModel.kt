package com.serona.app.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serona.app.data.dto.PersonalInfoRequest
import com.serona.app.data.dto.RegisterUserRequest
import com.serona.app.data.model.Gender
import com.serona.app.data.repository.AuthRepository
import com.serona.app.data.repository.UserRepository
import com.serona.app.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PersonalInfoState())
    val state: StateFlow<PersonalInfoState> = _state.asStateFlow()

    fun selectGender(gender: Gender){
        _state.value = _state.value.copy(gender = gender, dobError = null, errorMessage = null)
    }

    fun selectCountry(country: String){
        _state.value = _state.value.copy(country = country, dobError = null, errorMessage = null)
    }

    fun selectDOB(day: String, month: String, year: String){
        _state.value = _state.value.copy(day = day, month = month, year = year, dobError = null, errorMessage = null)
    }

    private fun validateForm(): Boolean {
        val currentState = _state.value
        var isValid = true


        // Cek validitas tanggal menggunakan DateUtils
        val dobError = if (!DateUtils.isValidDate(currentState.day, currentState.month, currentState.year)) {
            isValid = false
            "The date is invalid for the selected month"
        } else null

        _state.value = _state.value.copy(dobError = dobError)
        return isValid
    }

    fun submitPersonalInfo(onSuccess: () -> Unit) : Boolean{
        _state.value = _state.value.copy(dobError = null, errorMessage = null)
        if (!validateForm()) return false
        val currentState = state.value

        val request = PersonalInfoRequest(
            gender = when(currentState.gender) {
                Gender.MALE -> "Male"
                else -> "Female"
            },
            country = state.value.country,
            birth_date = DateUtils.formatBirthDate(currentState.day, currentState.month, currentState.year)
        )

        viewModelScope.launch {

            _state.value = _state.value.copy(
                errorMessage = null
            )

            val result = userRepo.submitPersonalInfo(request)

            result.onSuccess {
                onSuccess()
            }.onFailure { exception ->
                val errorMsg = exception.message ?: ""

                // JIKA GAGAL karena user belum ada di BE (Ghost User)
                if (errorMsg.contains("401") || errorMsg.contains("unauthenticated", true) || errorMsg.contains("method", true)) {

                    // Ambil info dari Firebase dan daftarkan ke Laravel dulu
                    val (nameFb, emailFb) = authRepo.getCurrentUserInfo() ?: (null to null)
                    if (nameFb != null && emailFb != null) {
                        val regResult = userRepo.registerUser(RegisterUserRequest(nameFb, emailFb))

                        if (regResult.isSuccess) {
                            // Jika registrasi sukses, coba kirim Personal Info-nya lagi
                            submitPersonalInfo(onSuccess)
                        } else {
                            _state.value = _state.value.copy(errorMessage = "Sync failed. Try re-logging.")
                        }
                    }
                } else {
                    _state.value = _state.value.copy(errorMessage = errorMsg)
                }
            }
        }

        return true
    }

    fun clearError(){
        _state.value = _state.value.copy(errorMessage = null, dobError = null)
    }

}