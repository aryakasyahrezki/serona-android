package com.example.serona.ui.ui.auth.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serona.R
import com.example.serona.ui.theme.AuthPageGrad
import com.example.serona.ui.theme.ForgotPasswordBorderGrad
import com.example.serona.ui.theme.Primary
import com.example.serona.ui.theme.White
import com.example.serona.ui.theme.figtreeFontFamily
import com.example.serona.ui.theme.glassColor
import com.example.serona.ui.theme.leagueSpartanFontFamily
import com.example.serona.ui.ui.auth.EmailVerificationState
import com.example.serona.ui.ui.auth.RegisterFormState
import com.example.serona.ui.ui.auth.RegisterState
import com.example.serona.ui.ui.component.AuthPasswordField
import com.example.serona.ui.ui.component.AuthTextField
import com.example.serona.ui.ui.component.RoundedCheckbox
import kotlinx.coroutines.delay

@Composable
fun RegisterPage(registerViewModel: RegisterViewModel) {

    val emailState by registerViewModel.emailVerificationState.observeAsState(EmailVerificationState.Idle)

    LaunchedEffect(emailState) {
        if(emailState == EmailVerificationState.Verified){
            delay(1500)
            registerViewModel.resetEmailVerificationState()
            // nanti ini ganti buat lgsg ke Personal Info Page
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(brush = AuthPageGrad)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ellips_splash_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.serona_logo),
                contentDescription = "Serona Logo"
            )
            Text(
                text = "The art of your own glow",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                fontFamily = leagueSpartanFontFamily,
                fontSize = 13.sp,
                letterSpacing = 5.sp
            )

            RegisterCard(registerViewModel)

            if(emailState != EmailVerificationState.Idle) {
                EmailVerificationDialog(
                    state = emailState,
                    onDismiss = {
                        registerViewModel.resetEmailVerificationState()
                    },
                    viewModel = registerViewModel
                )
            }
        }
    }
}

@Composable
fun RegisterCard(registerViewModel: RegisterViewModel) {

    val form = registerViewModel.formState.observeAsState(RegisterFormState()).value
    val registerState = registerViewModel.registerState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(registerState.value) {
        when (val state = registerState.value) {
            is RegisterState.Error -> {
                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_SHORT
                ).show()
                registerViewModel.resetRegisterState()
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
//            )
                .background(
                    brush = glassColor,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Register",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(20.dp))

                AuthTextField(
                    value = form.name,
                    onValueChange = registerViewModel::onNameChanged,
                    label = "Full Name",
                    error = form.nameError
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthTextField(
                    value = form.email,
                    onValueChange = registerViewModel::onEmailChanged,
                    label = "Email",
                    error = form.emailError
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthPasswordField(
                    value = form.password,
                    onValueChange = registerViewModel::onPasswordChanged,
                    label = "Password",
                    error = form.passwordError
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthPasswordField(
                    value = form.confirmPassword,
                    onValueChange = registerViewModel::onConfirmChanged,
                    label = "Confirmation Password",
                    error = form.confirmPasswordError
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundedCheckbox(
                        checked = form.isAgree,
                        onCheckedChange = registerViewModel::onAgreeChange
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    val annotatedText = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = White,
                                fontSize = 14.sp,
                                fontFamily = figtreeFontFamily
                            )
                        ) {
                            append("I agree that my personal data will be processed in accordance with the ")
                        }

                        pushStringAnnotation(
                            tag = "PRIVACY_POLICY",
                            annotation = "privacy"
                        )
                        withStyle(
                            style = SpanStyle(
                                color = Primary,
                                textDecoration = TextDecoration.Underline,
                                fontSize = 14.sp,
                                fontFamily = figtreeFontFamily
                            )
                        ) {
                            append("privacy policy")
                        }
                        pop()
                    }

                    ClickableText(
                        text = annotatedText,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp
                        ),
                        onClick = { offset ->
                            annotatedText
                                .getStringAnnotations("PRIVACY_POLICY", offset, offset)
                                .firstOrNull()
                                ?.let {
                                    // nanri tambahin mau kemananya
                                }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    enabled = form.isAgree,
                    onClick = {
                        registerViewModel.submit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Register",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun EmailVerificationDialog(
    state: EmailVerificationState,
    viewModel: RegisterViewModel,
    onDismiss: () -> Unit
) {
    val dialogText = TextStyle(
        fontFamily = figtreeFontFamily,
        fontWeight = FontWeight.Normal,
        color = Primary,
        textAlign = TextAlign.Center
    )

    val buttonText = TextStyle(
        fontFamily = figtreeFontFamily,
        fontWeight = FontWeight.SemiBold
    )

    AlertDialog(
        modifier = Modifier.drawBehind {
            drawRoundRect(
                brush = ForgotPasswordBorderGrad,
                style = Stroke(width = 3.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx())
            )
        },
        containerColor = Color(0xFFECD3D4).copy(alpha = 0.9f),
        onDismissRequest = {},
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Email Verification",
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary,
                )
            }
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    EmailVerificationState.Sending ->
                        Text(
                            "Sending Verification Email...",
                            style = dialogText
                        )

                    EmailVerificationState.EmailSent ->
                        Text(
                            "Verification Email has been sent. Please check your inbox or spam",
                            style = dialogText
                        )

                    EmailVerificationState.Verified ->
                        Text(
                            "Your email has been verified successfully!",
                            style = dialogText
                        )

                    EmailVerificationState.NotVerified ->
                        Text(
                            "Email not verified yet. Please check your inbox or spam email.",
                            style = dialogText
                        )

                    else -> {}
                }
            }
        },
        confirmButton = {
            Column(){
                if(state == EmailVerificationState.EmailSent || state == EmailVerificationState.NotVerified){
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Primary.copy(alpha = 0.8f)),
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.checkEmailVerification()
                        }
                    ) {
                        Text(
                            "I've Verified My Email",
                            style = buttonText,
                            color = White
                        )
                    }

                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.resetEmailVerificationState()
                            viewModel.deleteAccount()
                            onDismiss()
                        }
                    ) {
                        Text(
                            "Use another email",
                            style = buttonText,
                            color = Primary
                        )
                    }
                }
            }
        },
        dismissButton = {}
    )


}

