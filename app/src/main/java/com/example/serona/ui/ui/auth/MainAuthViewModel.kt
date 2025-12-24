package com.example.serona.ui.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.serona.ui.data.repository.AuthRepository

class MainAuthViewModel (
    private val authRepo : AuthRepository = AuthRepository()
){
    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    // ini untuk ngecek UI pertama harus mulai dari mna?
    fun checkAuthStatus() {
        _authState.value = AuthState.Loading

        val user = authRepo.getCurrentUser()

        if (user == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }

        _authState.value = AuthState.Authenticated
    }

    fun logout(){
        authRepo.logout()
        _authState.value = AuthState.Unauthenticated
    }
}