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

        val currentUser = authRepo.getCurrentUser()
        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }

        // Gunakan satu coroutine saja agar urutannya sinkron (top-to-bottom)
        viewModelScope.launch {
            try {
                // Kita pastikan data user dimuat dulu sampai beres
                loadUserSync()

                if (userSession.user.value != null) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    private suspend fun loadUserSync() {
        if (userSession.isInitialized.value) return

        val result = userRepo.getProfile()
        if (result.isSuccess) {
            result.getOrNull()?.let { userSession.setUser(it) }
        } else {
            userSession.markInitialized()
        }
    }

    fun logout(){
        authRepo.logout()
        _authState.value = AuthState.Unauthenticated
        userSession.clear()
    }
}