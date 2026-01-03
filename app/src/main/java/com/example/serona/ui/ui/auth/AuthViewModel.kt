package com.example.serona.ui.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.ui.data.repository.AuthRepository
import com.example.serona.ui.data.repository.UserRepository
import com.example.serona.ui.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (
    private val authRepo : AuthRepository,
    private val userRepo : UserRepository,
    private val userSession : UserSession
): ViewModel(){
    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    fun checkAuthStatus() {
        _authState.value = AuthState.Loading

        try {
            val user = authRepo.getCurrentUser()

            if (user == null) {
                _authState.value = AuthState.Unauthenticated
                return
            }

            viewModelScope.launch {
                loadUserIfLoggedIn()
                if (userSession.user.value != null) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Unauthenticated // ini terjadi kalo dia register tapi belom verified trs dia lgsg login
                }
            }

        } catch (e: Exception) {
            // PENTING: jangan crash app
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun loadUserIfLoggedIn() {
        // kalo belom login gausa ambil data
        if (authRepo.getCurrentUser() == null) return

        // kalo uda pernah login dan user session uda keisi gausa ambil ulang lagi
        if (userSession.isInitialized.value) return

        viewModelScope.launch {
            val result = userRepo.getProfile()
            if (result.isSuccess) {
                userSession.setUser(result.getOrNull()!!)
            }else{
                userSession.markInitialized()
            }
        }
    }

    fun logout(){
        authRepo.logout()
        _authState.value = AuthState.Unauthenticated
        userSession.clear()
    }
}