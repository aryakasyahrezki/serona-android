package com.example.serona.ui.ui.auth.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PersonalInfoViewModel @Inject constructor(
    // nanti kalo mau ke be harus ada repositorynya
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
}