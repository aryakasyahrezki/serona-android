package com.example.serona.data.session

import com.example.serona.data.model.Gender
import com.example.serona.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class UserSession @Inject constructor(){
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized

    fun setBasicInfo(name: String, email: String){
        _user.value = User(
            name = name,
            email = email
        )
    }

    fun updatePersonalInfo(
        gender: Gender,
        country: String,
        birthDate: String
    ){
        _user.value = _user.value?.copy(
            gender = gender,
            country = country,
            birthDate = birthDate
        )

    }

    fun setUser(user: User){
        _user.value = user
        _isInitialized.value = true
    }

    fun clear() {
        _user.value = null
        _isInitialized.value = false
    }

    // ini func biar kalo fetch nya gagal, dia ngga nyoba berulang" lagi
    fun markInitialized() {
        _isInitialized.value = true
    }

}