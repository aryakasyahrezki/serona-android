package com.serona.app.ui.auth.register

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.serona.app.theme.AuthPageGrad
import com.serona.app.theme.ForgotPasswordBorderGrad
import com.serona.app.theme.Primary
import com.serona.app.theme.White
import com.serona.app.theme.figtreeFontFamily
import com.serona.app.theme.glassColor
import com.serona.app.theme.leagueSpartanFontFamily
import com.serona.app.ui.auth.EmailVerificationState
import com.serona.app.ui.auth.RegisterFormState
import com.serona.app.ui.auth.RegisterState
import com.serona.app.ui.component.AuthPasswordField
import com.serona.app.ui.component.AuthTextField
import com.serona.app.ui.component.RoundedCheckbox
import com.serona.app.ui.navigation.Routes
import com.serona.app.R
import com.serona.app.utils.ResponsiveScale
import com.serona.app.utils.rememberNavigationGuard

@Composable
fun RegisterPage(
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {

    val emailState by registerViewModel.emailVerificationState.observeAsState(EmailVerificationState.Idle)
    val registerState by registerViewModel.registerState.observeAsState()
    val context = LocalContext.current

    val (isNavigating, safeAction, resetNavigation) = rememberNavigationGuard()

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is RegisterState.Error -> {
                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_SHORT
                ).show()
                registerViewModel.resetRegisterState()

                resetNavigation()
            }
            else -> Unit
        }
    }

    LaunchedEffect(emailState) {
        when (emailState) {
            is EmailVerificationState.Verified -> {
                navController.navigate("personalInfo") {
                    popUpTo("register") { inclusive = true }
                    launchSingleTop = true
                }
            }
            is EmailVerificationState.EmailSent,
            is EmailVerificationState.NotVerified -> {
                resetNavigation()
            }
            is EmailVerificationState.Error -> {
                resetNavigation()
            }
            else -> Unit
        }
    }

    val form by registerViewModel.formState.observeAsState()

    LaunchedEffect(
        form?.nameError,
        form?.emailError,
        form?.passwordError,
        form?.confirmPasswordError,
        form?.agreeError
    ) {
        if (form?.nameError != null || form?.emailError != null ||
            form?.passwordError != null || form?.confirmPasswordError != null ||
            form?.agreeError != null) {
            resetNavigation()
        }
    }

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val fontSize = (maxWidth * 0.052f).value.sp
    val topPadding = maxWidth * 0.3f
    val space = maxHeight * 0.07f

    ResponsiveScale(maxFontScale = 1f) {
        Box(
            modifier = Modifier
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
                    .padding(top = topPadding),
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
                    fontSize = fontSize * 0.8f,
                    letterSpacing = fontSize * 0.25f,
                )

                RegisterCard(navController, registerViewModel, fontSize, space, safeAction)

                if (emailState != EmailVerificationState.Idle) {
                    EmailVerificationDialog(
                        state = emailState,
                        onDismiss = {
                            registerViewModel.resetEmailVerificationState()
                            resetNavigation()
                        },
                        onCheckVerification = { registerViewModel.checkEmailVerification() },
                        onUseAnotherEmail = {
                            registerViewModel.resetEmailVerificationState()
                            registerViewModel.deleteAccount()
                        },
                        fontSize = fontSize,
                        space = space,
                        safeAction = safeAction,
                        resetNavigation = resetNavigation
                    )
                }
            }

            if (isNavigating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(100f)
                        .background(Color.Transparent)
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                }
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun RegisterCard(
    navController: NavController,
    registerViewModel: RegisterViewModel,
    fontSize: TextUnit,
    space: Dp,
    safeAction: (action: () -> Unit) -> Unit
) {

    val form = registerViewModel.formState.observeAsState(RegisterFormState()).value

    ResponsiveScale(maxFontScale = 1f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
//            )
                    .background(
                        brush = glassColor,
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
                    .padding(space * 0.5f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Register",
                        fontSize = fontSize,
                        color = Color.White,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(space * 0.5f))

                    AuthTextField(
                        value = form.name,
                        onValueChange = registerViewModel::onNameChanged,
                        label = "Full Name",
                        error = form.nameError,
                        fontSize = fontSize,
                        space = space
                    )

                    Spacer(modifier = Modifier.height(space * 0.2f))

                    AuthTextField(
                        value = form.email,
                        onValueChange = registerViewModel::onEmailChanged,
                        label = "Email",
                        error = form.emailError,
                        fontSize = fontSize,
                        space = space
                    )

                    Spacer(modifier = Modifier.height(space * 0.2f))

                    AuthPasswordField(
                        value = form.password,
                        onValueChange = registerViewModel::onPasswordChanged,
                        label = "Password",
                        error = form.passwordError,
                        fontSize = fontSize,
                        space = space
                    )

                    Spacer(modifier = Modifier.height(space * 0.2f))

                    AuthPasswordField(
                        value = form.confirmPassword,
                        onValueChange = registerViewModel::onConfirmChanged,
                        label = "Confirmation Password",
                        error = form.confirmPasswordError,
                        fontSize = fontSize,
                        space = space
                    )

                    Spacer(modifier = Modifier.height(space * 0.3f))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedCheckbox(
                            checked = form.isAgree,
                            onCheckedChange = registerViewModel::onAgreeChange,
                            fontSize = fontSize
                        )

                        Spacer(modifier = Modifier.width(space * 0.1f))

                        val annotatedText = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = White,
                                    fontSize = fontSize * 0.7f,
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
                                    fontSize = fontSize * 0.7f,
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
                                fontSize = fontSize * 0.4f
                            ),
                            onClick = { offset ->
                                annotatedText
                                    .getStringAnnotations("PRIVACY_POLICY", offset, offset)
                                    .firstOrNull()
                                    ?.let {
                                        safeAction {
                                            navController.navigate("privacyPolicy") {
                                                popUpTo(Routes.REGISTER) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(space * 0.4f))

                    Button(
                        enabled = form.isAgree && (form.name != "") && (form.email != "") && (form.password != "") && (form.confirmPassword != ""),
                        onClick = {
                            safeAction { registerViewModel.submit() }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(space * 0.8f)
                    ) {
                        Text(
                            text = "Register",
                            color = Color.White,
                            fontSize = fontSize * 0.8f,
                            fontFamily = figtreeFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(space * 0.1f))

                    Row() {
                        Text(
                            text = "Already have an Account ? ",
                            color = White,
                            fontSize = fontSize * 0.6f,
                            fontFamily = figtreeFontFamily,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "Login",
                            color = Primary,
                            fontSize = fontSize * 0.6f,
                            fontFamily = figtreeFontFamily,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {
                                safeAction { navController.popBackStack() }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmailVerificationDialog(
    state: EmailVerificationState,
    fontSize: TextUnit,
    space: Dp,
    onDismiss: () -> Unit,
    onCheckVerification: () -> Unit,
    onUseAnotherEmail: () -> Unit,
    safeAction: (action: () -> Unit) -> Unit,
    resetNavigation: () -> Unit
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

    ResponsiveScale(maxFontScale = 1f) {
        AlertDialog(
            modifier = Modifier.drawBehind {
                drawRoundRect(
                    brush = ForgotPasswordBorderGrad,
                    style = Stroke(width = 3.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx())
                )
            },
            containerColor = Color(0xFFFFF2F2).copy(alpha = 0.9f),
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
                                style = dialogText,
                                fontFamily = figtreeFontFamily
                            )

                        EmailVerificationState.EmailSent ->
                            Text(
                                "Verification Email has been sent. Please check your inbox or spam",
                                style = dialogText,
                                fontFamily = figtreeFontFamily
                            )

                        EmailVerificationState.Verified ->
                            Text(
                                "Your email has been verified successfully!",
                                style = dialogText,
                                fontFamily = figtreeFontFamily
                            )

                        EmailVerificationState.NotVerified ->
                            Text(
                                "Email not verified yet. Please check your inbox or spam email.",
                                style = dialogText,
                                fontFamily = figtreeFontFamily
                            )

                        is EmailVerificationState.Error ->
                            Text(state.message, fontFamily = figtreeFontFamily, style = dialogText)

                        else -> {}
                    }
                }
            },
            confirmButton = {
                Column() {
                    if (state == EmailVerificationState.EmailSent || state == EmailVerificationState.NotVerified) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Primary.copy(alpha = 0.8f)),
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                safeAction { onCheckVerification() }
                            }
                        ) {
                            Text(
                                "I've Verified My Email",
                                style = buttonText,
                                color = White,
                                fontFamily = figtreeFontFamily
                            )
                        }

                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                safeAction {
                                    onUseAnotherEmail()
                                    onDismiss()
                                }
                            }
                        ) {
                            Text(
                                "Use another email",
                                style = buttonText,
                                color = Primary
                            )
                        }
                    }

                    if (state is EmailVerificationState.Error) {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                resetNavigation()
                                safeAction {
                                    onUseAnotherEmail()
                                    onDismiss()
                                }
                            }
                        ) {
                            Text("Back to Register", style = buttonText, color = White)
                        }

                        Text(
                            text = "Note: This will reset your registration so you can try again.",
                            fontSize = fontSize * 0.5f,
                            fontFamily = figtreeFontFamily,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = space * 0.2f),
                            color = Primary.copy(alpha = 0.7f),
                            lineHeight = fontSize * 0.4f
                        )
                    }
                }
            },
            dismissButton = {}
        )

    }
}

