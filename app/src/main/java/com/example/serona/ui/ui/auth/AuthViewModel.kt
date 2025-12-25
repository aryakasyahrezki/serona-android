package com.example.serona.ui.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.serona.ui.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (
    private val authRepo : AuthRepository
): ViewModel(){
    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    // ini untuk ngecek UI pertama harus mulai dari mna?
//    fun checkAuthStatus() {
//        _authState.value = AuthState.Loading
//
//        val user = authRepo.getCurrentUser()
//
//        if (user == null) {
//            _authState.value = AuthState.Unauthenticated
//            return
//        }
//
////        kalo user belum verified, kita kasi dia masuk aja, jadi nanti di cek lagi di dalem dia bisa minta sen email verif lagi buat verifikasi email dia
////        karena kalo misalnya kita kasih !user.isEmailVerified -> AuthState.Unauthenticated, user jadi malah gabisa pake email itu samsek, karna mau di register ulang pun firebase gaakan ijinin karena sudah terdaftar
//
//        _authState.value = AuthState.Authenticated
//    }

    fun checkAuthStatus() {
        _authState.value = AuthState.Loading

        try {
            val user = authRepo.getCurrentUser()

            if (user == null) {
                _authState.value = AuthState.Unauthenticated
                return
            }

            _authState.value = AuthState.Authenticated
        } catch (e: Exception) {
            // PENTING: jangan crash app
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun logout(){
        authRepo.logout()
        _authState.value = AuthState.Unauthenticated
    }
}