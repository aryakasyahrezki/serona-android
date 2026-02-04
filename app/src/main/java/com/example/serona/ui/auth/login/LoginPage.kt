package com.example.serona.ui.auth.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.serona.R
import com.example.serona.theme.AuthPageGrad
import com.example.serona.theme.ForgotPasswordBorderGrad
import com.example.serona.theme.Primary
import com.example.serona.theme.White
import com.example.serona.theme.figtreeFontFamily
import com.example.serona.theme.glassColor
import com.example.serona.theme.leagueSpartanFontFamily
import com.example.serona.ui.auth.AuthState
import com.example.serona.ui.auth.ForgotPasswordFormState
import com.example.serona.ui.auth.ForgotPasswordState
import com.example.serona.ui.auth.LoginFormState
import com.example.serona.ui.component.AuthPasswordField
import com.example.serona.ui.component.AuthTextField
import com.example.serona.ui.navigation.Routes

@Composable
fun LoginPage(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val fontSize = (maxWidth * 0.052f).value.sp
    val topPadding = maxWidth * 0.3f
    val space = maxHeight * 0.07f

    var forgotPasswordDialogBox by remember { mutableStateOf(false) }
    val authState by loginViewModel.loginState.observeAsState(AuthState.Idle)
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when(authState){
            is AuthState.Authenticated-> {
                navController.navigate("home") {
                    popUpTo(Routes.LOGIN) {
                        inclusive = true
                    }
                }

                loginViewModel.resetLoginState()
            }

            is AuthState.NeedPersonalInfo -> {
                navController.navigate(Routes.PERSONALINFO) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
                loginViewModel.resetLoginState()
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                loginViewModel.resetLoginState()
            }

            else -> Unit
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
                letterSpacing = fontSize * 0.25f
            )

            LoginCard(
                navController,
                loginViewModel,
                onForgotPasswordClick = {
                    forgotPasswordDialogBox = true
                },
                fontSize = fontSize,
                space = space
            )

            if (forgotPasswordDialogBox) {
                ForgotPasswordDialog(
                    viewModel = loginViewModel,
                    onDismiss = { forgotPasswordDialogBox = false },
                    fontSize = fontSize,
                    space = space * 0.6f
                )
            }
        }
    }
}

@Composable
fun LoginCard(
    navController: NavController,
    loginViewModel: LoginViewModel,
    onForgotPasswordClick: () -> Unit = {},
    fontSize: TextUnit,
    space: Dp
) {

    val form = loginViewModel.loginFormState.observeAsState(LoginFormState()).value

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    brush = glassColor,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(space* 0.5f)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Login",
                    fontSize = fontSize,
                    color = White,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(space * 0.5f))

                AuthTextField(
                    value = form.email,
                    onValueChange = loginViewModel::onEmailChanged,
                    label = "Email",
                    error = form.emailError,
                    fontSize = fontSize,
                    space = space
                )

                Spacer(modifier = Modifier.height(space * 0.2f))

                AuthPasswordField(
                    value = form.password,
                    onValueChange = loginViewModel::onPasswordChange,
                    label = "Password",
                    error = form.passwordError,
                    fontSize = fontSize,
                    space = space
                )

                TextButton(
                    onClick = onForgotPasswordClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "Forgot Password",
                        color = Primary,
                        fontFamily = figtreeFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(space * 0.4f))

                Button(
                    enabled = (form.email != "") && (form.password != ""),
                    onClick = {
                        loginViewModel.submit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE05757)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(space * 0.8f)
                ) {
                    Text(
                        text = "Login",
                        color = White,
                        fontSize = 16.sp,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(space * 0.1f))

                Row() {
                    Text(
                        text = "Don’t have an Account ? ",
                        color = White,
                        fontSize = 13.sp,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "Register",
                        color = Primary,
                        fontSize = 13.sp,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable{
                            navController.navigate("register") {
                                popUpTo("login") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordDialog(
    viewModel: LoginViewModel,
    onDismiss: () -> Unit,
    fontSize: TextUnit,
    space: Dp
) {
    val form = viewModel.forgotPasswordFormState.observeAsState(ForgotPasswordFormState()).value
    val state by viewModel.forgotPasswordState
        .observeAsState(ForgotPasswordState.Idle)

    AlertDialog(
        modifier = Modifier.drawBehind {
            drawRoundRect(
                brush = ForgotPasswordBorderGrad,
                style = Stroke(width = 3.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx())
            )
        },
        containerColor = Color(0xFFFFF2F2).copy(alpha = 0.9f),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Forgot Password",
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Enter your email to reset your password",
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Primary
                    )

                    Spacer(Modifier.height(space * 0.08f))

                    AuthTextField(
                        value = form.resetEmail,
                        onValueChange = viewModel::onResetEmailChanged,
                        label = "Email",
                        error = form.resetEmailError,
                        color = Primary,
                        fontSize = fontSize,
                        space = space
                    )
                }

                if (state is ForgotPasswordState.Success) {
                    Spacer(Modifier.height(space * 0.08f))
                    Text(
                        text = "Email has been sent",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = space * 0.08f),
                        color = Primary,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                }
            }
        },
        confirmButton = {
            Column{
                Button(
                    onClick = { viewModel.sendResetPasswordEmail() },
                    enabled = state !is ForgotPasswordState.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary.copy(alpha = 0.8f)),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        "Send Email",
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.resetForgotPasswordState()
                        onDismiss()
                    }
                ) {
                    Text(
                        "Cancel",
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }
            }

        },
        dismissButton = {},
        onDismissRequest = {
            viewModel.resetForgotPasswordState()
            onDismiss()
        }
    )

}