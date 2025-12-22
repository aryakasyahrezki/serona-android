package com.example.serona.ui.ui.auth.login

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.serona.R
import com.example.serona.ui.theme.AuthPageGrad
import com.example.serona.ui.theme.Primary
import com.example.serona.ui.theme.White
import com.example.serona.ui.theme.figtreeFontFamily
import com.example.serona.ui.theme.glassColor
import com.example.serona.ui.theme.leagueSpartanFontFamily
import com.example.serona.ui.ui.auth.AuthState
import com.example.serona.ui.ui.auth.LoginFormState
import com.example.serona.ui.ui.component.AuthPasswordField
import com.example.serona.ui.ui.component.AuthTextField

@Composable
fun LoginPage(loginViewModel: LoginViewModel) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(brush = AuthPageGrad)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ellips_auth_bg),
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

            LoginCard(loginViewModel)
        }
    }
}

@Composable
fun LoginCard(loginViewModel: LoginViewModel) {

    val form = loginViewModel.loginFormState.observeAsState(LoginFormState()).value
    val authState = loginViewModel.loginState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Error) {
            Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
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
                .background(
                    brush = glassColor,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Login",
                    fontSize = 20.sp,
                    color = White,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(20.dp))

                AuthTextField(
                    value = form.email,
                    onValueChange = loginViewModel::onEmailChanged,
                    label = "Email",
                    error = form.emailError
                )

                Spacer(modifier = Modifier.height(12.dp))

                AuthPasswordField(
                    value = form.password,
                    onValueChange = loginViewModel::onPasswordChange,
                    label = "Password",
                    error = form.passwordError
                )

                TextButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "Forgot Password",
                        color = Primary,

                        )
                }

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        loginViewModel.submit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE05757)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Login",
                        color = White,
                        fontSize = 16.sp,
                        fontFamily = figtreeFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

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
                        modifier = Modifier.clickable{}
                    )
                }
            }
        }
    }
}