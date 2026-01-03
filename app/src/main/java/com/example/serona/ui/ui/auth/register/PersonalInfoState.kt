package com.example.serona.ui.ui.auth.register

import com.example.serona.ui.data.model.Gender

data class PersonalInfoState(
    val gender: Gender? = null,
    val country: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val errorMessage: String? = null
){
    val answeredCount: Int
        get() {
            var count = 0
            if (gender != null) count++
            if (country.isNotBlank()) count++
            if (day.isNotBlank() && month.isNotBlank() && year.isNotBlank()) count++
            return count
        }

    val canContinue: Boolean
        get() = answeredCount == 3
}