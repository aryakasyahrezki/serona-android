package com.example.serona.ui.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.serona.ui.data.repository.AuthRepository

class MainAuthViewModel (
    private val authRepo : AuthRepository = AuthRepository()
){
    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    fun checkAuthStatus(){
        _authState.value =
            if (authRepo.isLoggedIn()) AuthState.Authenticated
            else AuthState.Unauthenticated
    }

    fun logout(){
        authRepo.logout()
        _authState.value = AuthState.Unauthenticated
    }
}