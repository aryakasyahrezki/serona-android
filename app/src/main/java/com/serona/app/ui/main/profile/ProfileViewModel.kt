package com.serona.app.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serona.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
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