package com.example.serona.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.repository.AuthRepository
import com.example.serona.data.repository.UserRepository
import com.example.serona.data.session.UserSession
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

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val currentUser = authRepo.getCurrentUser()

        if (currentUser == null) {
            viewModelScope.launch {
                userRepo.clearAllLocalData()
                _authState.value = AuthState.Unauthenticated
            }
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val result = userRepo.syncFullProfile()

                if (result.isSuccess) {
                    val user = result.getOrNull()

                    if (user?.gender == null || user?.country.isNullOrBlank()) {
                        _authState.value = AuthState.NeedPersonalInfo
                    } else {
                        _authState.value = AuthState.Authenticated
                    }
                } else {
                    userRepo.clearAllLocalData()
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Connection failed: ${e.message}")
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            authRepo.logout()
            userRepo.clearAllLocalData()
            _authState.value = AuthState.Unauthenticated
        }
    }
}