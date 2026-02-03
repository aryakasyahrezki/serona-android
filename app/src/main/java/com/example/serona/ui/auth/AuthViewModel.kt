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

//    fun checkAuthStatus() {
//        _authState.value = AuthState.Loading
//
//        val currentUser = authRepo.getCurrentUser()
//        if (currentUser == null) {
//            _authState.value = AuthState.Unauthenticated
//            return
//        }
//
//        // Langsung set Authenticated jika Firebase User ada
//        _authState.value = AuthState.Authenticated
//
//        viewModelScope.launch {
//            try {
//                loadUserSync()
//                // Data profil akan terisi di userSession secara diam-diam
//            } catch (e: Exception) {
//                // Jika gagal ambil profil, bisa ditangani di halaman Home (misal: minta logout)
//            }
//        }
//    }

    fun checkAuthStatus() {
        val currentUser = authRepo.getCurrentUser()

        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                // Kita coba ambil profil dari Backend Azure
                val result = userRepo.syncFullProfile()

                if (result.isSuccess) {
                    val user = result.getOrNull()

                    // Cek apakah data wajib (Personal Info) sudah diisi
                    // Gunakan properti yang paling mencerminkan profile sudah lengkap
                    if (user?.gender == null || user?.country.isNullOrBlank()) {
                        _authState.value = AuthState.NeedPersonalInfo
                    } else {
                        _authState.value = AuthState.Authenticated
                    }
                } else {
                    // Jika API Azure gagal (misal user belum terdaftar di DB Azure)
                    _authState.value = AuthState.NeedPersonalInfo
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Connection failed: ${e.message}")
            }
        }
    }

    private suspend fun loadUserSync() {
        if (userSession.isInitialized.value) return

        val result = userRepo.syncFullProfile()
        if (result.isSuccess) {
            result.getOrNull()?.let { userSession.setUser(it) }
        } else {
            userSession.markInitialized()
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