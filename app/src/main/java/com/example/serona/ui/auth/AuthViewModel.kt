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
        viewModelScope.launch {
            userSession.user.collect { user ->
                if (user == null && authRepo.getCurrentUser() == null) {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    fun checkAuthStatus() {
        _authState.value = AuthState.Loading

        val currentUser = authRepo.getCurrentUser()
        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }

        // Langsung set Authenticated jika Firebase User ada
        // Sambil jalan, kita tetap panggil loadUserSync di background
        _authState.value = AuthState.Authenticated

        viewModelScope.launch {
            try {
                loadUserSync()
                // Data profil akan terisi di userSession secara diam-diam
            } catch (e: Exception) {
                // Jika gagal ambil profil, bisa ditangani di halaman Home (misal: minta logout)
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