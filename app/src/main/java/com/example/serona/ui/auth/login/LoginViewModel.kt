package com.example.serona.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.repository.AuthRepository
import com.example.serona.data.repository.UserRepository
import com.example.serona.data.session.UserSession
import com.example.serona.ui.auth.AuthState
import com.example.serona.ui.auth.ForgotPasswordFormState
import com.example.serona.ui.auth.ForgotPasswordState
import com.example.serona.ui.auth.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo : AuthRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _loginFormState = MutableLiveData(LoginFormState())
    val loginFormState : LiveData<LoginFormState> = _loginFormState

    //ini state hanya untuk login aja, state untuk tau user sebelumnya uda login atau belum (session), ada di AuthViewModel
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
        val emailErr =
            when {
                !state.email.contains("@") ->
                    "Email must contain '@'"

                !state.email.contains(".") ->
                    "Email must contain '.'"

                else -> null
            }
        val passErr = if (state.password.length < 6) "Minimal 6 character" else null

        if (emailErr != null || passErr != null) {
            _loginFormState.value = state.copy(
                emailError = emailErr,
                passwordError = passErr
            )
            return
        }

        _loginState.value = AuthState.Loading

        repo.login(state.email, state.password) { result ->
            if (result.isSuccess) {
                viewModelScope.launch {
                    val syncResult = userRepo.syncFullProfile()
                    if (syncResult.isSuccess) {
                        val user = syncResult.getOrNull()
                        if (user == null) { // Tambahkan pengecekan null extra
                            _loginState.postValue(AuthState.Error("Profile not found"))
                        } else if (user.country.isNullOrBlank()) {
                            _loginState.postValue(AuthState.NeedPersonalInfo)
                        } else {
                            _loginState.postValue(AuthState.Authenticated)
                        }
                    } else {
                        _loginState.postValue(AuthState.NeedPersonalInfo)
                    }
                }
            } else {
                _loginState.postValue(AuthState.Error("Email or password is incorrect"))
            }
        }
    }

    fun resetLoginState(){
        _loginState.value = AuthState.Idle
    }

    // Forgot Password Func
    private val _forgotPasswordFormState = MutableLiveData(ForgotPasswordFormState())
    val forgotPasswordFormState : LiveData<ForgotPasswordFormState> = _forgotPasswordFormState

    private val _forgotPasswordState = MutableLiveData<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState : LiveData<ForgotPasswordState> = _forgotPasswordState

    fun onResetEmailChanged(value : String){
        _forgotPasswordFormState.value = forgotPasswordFormState.value?.copy(
            resetEmail = value,
            resetEmailError = null
        )

        if (_forgotPasswordState.value is ForgotPasswordState.Success) {
            _forgotPasswordState.value = ForgotPasswordState.Idle
        }
    }

    fun sendResetPasswordEmail(){
        val state = _forgotPasswordFormState.value ?: return

        val emailErr = if (state.resetEmail.isBlank()) "Email can't be empty" else null

        if (emailErr != null){
            _forgotPasswordFormState.value = state.copy(
                resetEmailError = emailErr
            )
            return
        }

        _forgotPasswordState.value = ForgotPasswordState.Loading

        repo.forgotPassword(state.resetEmail){ result ->
            if (result.isSuccess){
                _forgotPasswordState.value = ForgotPasswordState.Success
            }else{
                _forgotPasswordState.value = ForgotPasswordState.Error(
                    result.exceptionOrNull()?.message ?: "Unknown Error"
                )
            }
        }
    }

    fun resetForgotPasswordState(){
        _forgotPasswordFormState.value = ForgotPasswordFormState()
        _forgotPasswordState.value = ForgotPasswordState.Idle
    }


}