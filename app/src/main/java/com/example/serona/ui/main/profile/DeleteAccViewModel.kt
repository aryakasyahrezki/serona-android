package com.example.serona.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.repository.AuthRepository
import com.example.serona.data.repository.UserRepository
import com.example.serona.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteAccViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun deleteAccount(password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            authRepo.reAuthenticate(password) { reAuthResult ->
                if (reAuthResult.isSuccess) {
                    viewModelScope.launch {
                        userRepo.deleteAccountFromBe().fold(
                            onSuccess = {
                                authRepo.deleteCurrentUser { firebaseResult ->
                                    if (firebaseResult.isSuccess) {
                                        userSession.clear()
                                        _isLoading.value = false
                                        viewModelScope.launch(kotlinx.coroutines.Dispatchers.Main) {
                                            onSuccess()
                                        }
                                    } else {
                                        _isLoading.value = false
                                        _errorMessage.value = firebaseResult.exceptionOrNull()?.message
                                            ?: "Failed to delete from Firebase"
                                    }
                                }
                            },
                            onFailure = { error ->
                                _isLoading.value = false
                                _errorMessage.value = error.message ?: "Failed to delete from server"
                            }
                        )
                    }
                } else {
                    _errorMessage.value = "Wrong Password, Verification Failed."
                    _isLoading.value = false
                }
            }
        }
    }

    fun clearError() { _errorMessage.value = null }
}