package com.example.serona.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.repository.UserRepository
import com.example.serona.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val userSession: UserSession,
    private val userRepo : UserRepository
) : ViewModel() {

    val user = userRepo.userDataFlow


    init {
        refreshProfile()
    }

    private fun refreshProfile() {
        viewModelScope.launch {
            userRepo.syncFullProfile()
        }
    }
}