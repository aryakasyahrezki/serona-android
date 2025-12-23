package com.example.serona.ui.ui.auth

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

data class RegisterFormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isAgree: Boolean = false,

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val agreeError: String? = null
)

sealed class EmailVerificationState{
    object Idle : EmailVerificationState()
    object Sending : EmailVerificationState()
    object EmailSent : EmailVerificationState()
    object Verified : EmailVerificationState()
    object NotVerified : EmailVerificationState()
    data class Error(val message : String) : EmailVerificationState()
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",

    val emailError: String? = null,
    val passwordError: String? = null
)

sealed class ForgotPasswordState {
    object Idle : ForgotPasswordState()
    object Loading : ForgotPasswordState()
    object Success : ForgotPasswordState()
    data class Error(val message: String) : ForgotPasswordState()
}

data class ForgotPasswordFormState(
    val resetEmail: String = "",

    val resetEmailError: String? = null
)