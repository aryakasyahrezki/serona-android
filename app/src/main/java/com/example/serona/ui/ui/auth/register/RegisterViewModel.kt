package com.example.serona.ui.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.serona.ui.data.repository.AuthRepository
import com.example.serona.ui.ui.auth.AuthState
import com.example.serona.ui.ui.auth.EmailVerificationState
import com.example.serona.ui.ui.auth.RegisterFormState

class RegisterViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _formState = MutableLiveData(RegisterFormState())
    val formState: LiveData<RegisterFormState> = _formState

    private val _registerState = MutableLiveData<AuthState>()
    val registerState: LiveData<AuthState> = _registerState

    private val _emailVerificationState = MutableLiveData<EmailVerificationState>(
        EmailVerificationState.Idle)
    val emailVerificationState: LiveData<EmailVerificationState> = _emailVerificationState


    fun onNameChanged(value: String) {
        _formState.value = _formState.value?.copy(
            name = value,
            nameError = null
        )
    }

    fun onEmailChanged(value: String) {
        _formState.value = _formState.value?.copy(
            email = value,
            emailError = null
        )
    }

    fun onPasswordChanged(value: String) {
        _formState.value = _formState.value?.copy(
            password = value,
            passwordError = null
        )
    }

    fun onConfirmChanged(value: String) {
        _formState.value = _formState.value?.copy(
            confirmPassword = value,
            confirmPasswordError = null
        )
    }

    fun onAgreeChange(value: Boolean) {
        _formState.value = _formState.value?.copy(
            isAgree = value,
            agreeError = null
        )
    }

    private fun sendVerificationEmail(){
        _emailVerificationState.value = EmailVerificationState.Sending

        repo.emailVerification { result ->
            if(result.isSuccess) {
                _emailVerificationState.postValue(EmailVerificationState.EmailSent)
            }else{
                _emailVerificationState.postValue(
                    EmailVerificationState.Error(
                        result.exceptionOrNull()?.message?: "Failed to send email"
                    )
                )
            }
        }
    }


    fun submit() {
        val state = _formState.value ?: return

        // VALIDASI PER FIELD
        val nameErr = if (state.name.isBlank()) "Nama can't be empty" else null
        val emailErr =
            when {
                state.email.isBlank() ->
                    "Email can't be empty"

                !state.email.contains("@") ->
                    "Email must contain '@'"

                !state.email.contains(".") ->
                    "Email must contain '.'"

                else -> null
            }
        val passErr = if (state.password.length < 6) "Minimal 6 character" else null
        val confirmErr =
            if (state.password != state.confirmPassword) "Password and Confirmation Password must be same"
            else if (state.confirmPassword.isBlank()) "Confirmation Password can't be empty"
            else null

        val agreeErr = if (!state.isAgree) "You must agree with privacy policy to register"
        else null

        if (nameErr != null || emailErr != null || passErr != null || confirmErr != null || agreeErr != null) {
            _formState.value = state.copy(
                nameError = nameErr,
                emailError = emailErr,
                passwordError = passErr,
                confirmPasswordError = confirmErr,
                agreeError = agreeErr
            )
            return
        }

        // Kalo tidak ada error
        _registerState.value = AuthState.Loading

        repo.register(state.name, state.email, state.password) { result ->
            if (result.isSuccess)
                sendVerificationEmail()
            else
                _registerState.postValue(
                    AuthState.Error(result.exceptionOrNull()?.message ?: "Error")
                )
        }
    }

    fun checkEmailVerification(){
        repo.reloadUser { verified ->
            if (verified) {
                _emailVerificationState.postValue(EmailVerificationState.Verified)
            } else {
                _emailVerificationState.postValue(
                    EmailVerificationState.NotVerified
                )
            }
        }
    }

    fun resetEmailVerificationState() {
        _emailVerificationState.value = EmailVerificationState.Idle
    }
}