package com.example.serona.ui.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.serona.ui.data.repository.AuthRepository
import com.example.serona.ui.ui.auth.AuthState
import com.example.serona.ui.ui.auth.LoginFormState

class LoginViewModel(
    private val repo : AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loginFormState = MutableLiveData(LoginFormState())
    val loginFormState : LiveData<LoginFormState> = _loginFormState

    private val _loginState = MutableLiveData<AuthState>()
    val loginState : LiveData<AuthState> = _loginState

    fun onEmailChanged(value : String){
        _loginFormState.value = _loginFormState.value?.copy(
            email = value,
            emailError = null
        )
    }

    fun onPasswordChange(value : String){
        _loginFormState.value = _loginFormState.value?.copy(
            password = value,
            passwordError = null
        )
    }

    fun submit() {
        val state = _loginFormState.value ?: return

        // VALIDASI PER FIELD
        val emailErr = if (state.email.isBlank()) "Email can't be empty" else null
        val passErr = if (state.password.isBlank()) "Password can't be empty" else null


        if (emailErr != null || passErr != null) {
            _loginFormState.value = state.copy(
                emailError = emailErr,
                passwordError = passErr
            )
            return
        }

        // Kalo tidak ada error
        _loginState.value = AuthState.Loading

        repo.login(state.email, state.password) { result ->
            if (result.isSuccess)
               _loginState.postValue(AuthState.Authenticated)
            else
               _loginState.postValue(
                    AuthState.Error("Email or password is incorrect")
                )
        }
    }
}