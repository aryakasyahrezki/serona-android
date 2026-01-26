package com.example.serona.ui.main.profile

import com.example.serona.data.model.Gender

data class EditProfileUiState(
    val name: String = "",
    val email: String = "",
    val country: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val gender: Gender = Gender.FEMALE,

    val isChanged: Boolean = false,
    val isLoading: Boolean = false,
    val updateSuccess: Boolean = false,

    val nameError: String? = null,
    val countryError: String? = null,
    val dobError: String? = null,
    val errorMessage: String? = null
)

sealed class EditProfileEvent {
    data class NameChanged(val name: String) : EditProfileEvent()
    data class CountryChanged(val country: String) : EditProfileEvent()
    data class DayChanged(val day: String) : EditProfileEvent()
    data class MonthChanged(val month: String) : EditProfileEvent()
    data class YearChanged(val year: String) : EditProfileEvent()
    data class GenderChanged(val gender: Gender) : EditProfileEvent()
    object ClearError : EditProfileEvent()
    object Save : EditProfileEvent()
}