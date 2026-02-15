package com.serona.app.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serona.app.data.dto.RegisterUserRequest
import com.serona.app.data.repository.AuthRepository
import com.serona.app.data.repository.UserRepository
import com.serona.app.data.session.UserSession
import com.serona.app.ui.auth.EmailVerificationState
import com.serona.app.ui.auth.RegisterFormState
import com.serona.app.ui.auth.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val userRepo: UserRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _formState = MutableLiveData(RegisterFormState())
    val formState: LiveData<RegisterFormState> = _formState

    private val _registerState = MutableLiveData<RegisterState>(RegisterState.Idle)
    val registerState: LiveData<RegisterState> = _registerState

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

    fun sendVerificationEmail(){
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
        val nameErr = if (state.name.isBlank()) {
                "Name can't be empty"
            } else if (state.name.length < 3){
                "Name mus be at least 3 characters"
            } else null
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
            else if(state.password.length < 6) "Minimal 6 character"
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
        _registerState.value = RegisterState.Loading

        repo.register(state.name, state.email, state.password) { result ->
            if (result.isSuccess) {
                _registerState.postValue(RegisterState.Success)
                sendVerificationEmail()
            }else {
                val exception = result.exceptionOrNull()

                if (exception is com.google.firebase.auth.FirebaseAuthUserCollisionException) {

                    repo.login(state.email, state.password) { loginResult ->
                        if (loginResult.isSuccess) {
                            repo.reloadUser { isVerified ->
                                if (!isVerified) {
                                    repo.deleteCurrentUser { deleteResult ->
                                        if (deleteResult.isSuccess) {
                                            submit()
                                        } else {
                                            _registerState.postValue(RegisterState.Error("Failed to reset unverified account"))
                                        }
                                    }
                                } else {
                                    _registerState.postValue(RegisterState.Error("Email is already in use by another account"))
                                }
                            }
                        } else {
                            _registerState.postValue(RegisterState.Error("Email is already in use"))
                        }
                    }
                } else {
                    _registerState.postValue(RegisterState.Error(exception?.message ?: "Register failed"))
                }
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = RegisterState.Idle
    }

    fun checkEmailVerification() {
        repo.reloadUser { verified ->
            if (!verified) {
                _emailVerificationState.postValue(EmailVerificationState.NotVerified)
                return@reloadUser
            }

            val (nameFirebase, emailFirebase) = repo.getCurrentUserInfo()
                ?: return@reloadUser

            viewModelScope.launch {
                val apiResult = userRepo.registerUser(
                    RegisterUserRequest(
                        name = nameFirebase!!,
                        email = emailFirebase!!
                    )
                )

                apiResult.onSuccess {
                    userSession.markInitialized()
                    _emailVerificationState.postValue(EmailVerificationState.Verified)
                }.onFailure { error ->
                    _emailVerificationState.postValue(
                        EmailVerificationState.Error(error.message ?: "Failed save user to server")
                    )
                }
            }
        }
    }

    fun resetEmailVerificationState() {
        _emailVerificationState.value = EmailVerificationState.Idle
    }

    fun deleteAccount() {
        resetEmailVerificationState() //jadi kan kalo gajadi user bakal coba email baru, jadi state nya ulang lagi

        repo.deleteCurrentUser { result ->
            if (result.isSuccess) {
                viewModelScope.launch {
                    userRepo.clearAllLocalData()
                }
            } else {
                println("Firebase account deletion failed: ${result.exceptionOrNull()?.message}")
            }
        }
    }
}