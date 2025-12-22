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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serona.R
import com.example.serona.ui.theme.AuthPageGrad
import com.example.serona.ui.theme.Primary
import com.example.serona.ui.theme.White
import com.example.serona.ui.theme.figtreeFontFamily
import com.example.serona.ui.theme.glassColor
import com.example.serona.ui.theme.leagueSpartanFontFamily
import com.example.serona.ui.ui.auth.AuthState
import com.example.serona.ui.ui.auth.RegisterFormState
import com.example.serona.ui.ui.component.AuthPasswordField
import com.example.serona.ui.ui.component.AuthTextField
import com.example.serona.ui.ui.component.RoundedCheckbox

@Composable
fun RegisterPage(registerViewModel: RegisterViewModel) {

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

            RegisterCard(registerViewModel)
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
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    state.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is AuthState.Authenticated -> {
                // TODO: navigate ke home / login
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
                                color = Primary, // warna link kamu
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

//@Composable
//fun RegisterPage(authViewModel: AuthViewModel) {
//
//    val authState = authViewModel.authState.observeAsState()
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(brush = AuthPageGrad)
//    ) {
//        Image(
//            painter = painterResource(id = com.example.serona.R.drawable.ellips_auth_bg),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 150.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.serona_logo),
//                contentDescription = "Serona Logo"
//            )
//            Text(
//                text = "The art of your own glow",
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                color = Color.White,
//                fontFamily = leagueSpartanFontFamily,
//                fontSize = 13.sp,
//                letterSpacing = 5.sp
//            )
//
//            RegisterCard(authState = authState, authViewModel)
//        }
//
//
//
//    }
//}
//
//@Composable
//fun RegisterCard(authState : State<AuthState?>, authViewModel: AuthViewModel) {
//
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var checked by remember { mutableStateOf(false) }
//
//    val context = LocalContext.current
//
//    LaunchedEffect(authState) {
//        when(authState.value){
////            is AuthState.Authenticated ->
//            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message,
//                Toast.LENGTH_SHORT).show()
//            else -> Unit
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ){
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
////            .shadow(
////                elevation = 20.dp,
////                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
////                clip = false
////            )
//                .background(
//                    brush = glassColor,
//                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
//                )
//                .padding(24.dp)
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text(
//                    text = "Register",
//                    fontSize = 20.sp,
//                    color = Color.White,
//                    fontFamily = figtreeFontFamily,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                AuthTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = "Full Name"
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                AuthTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = "Email"
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                AuthPasswordField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = "Password"
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                AuthPasswordField(
//                    value = confirmPassword,
//                    onValueChange = { confirmPassword = it },
//                    label = "Confirmation Password"
//
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(
//                        checked = checked,
//                        onCheckedChange = { checked = it }
//                    )
//                    Text(
//                        text = "I agree that my personal data will be processed in accordance with the ",
//                        fontSize = 12.sp,
//                        color = Color.White
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(
//                    onClick = {
//                        authViewModel.register(name, email, password, confirmPassword, checked)
//                    },
//                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp)
//                ) {
//                    Text(
//                        text = "Register",
//                        color = Color.White,
//                        fontSize = 16.sp,
//                        fontFamily = figtreeFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AuthTextField(value: String, onValueChange: (String) -> Unit, label: String) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = {
//            Text(label,
//                fontSize = 15.sp,
//                fontFamily = figtreeFontFamily,
//                fontWeight = FontWeight.Medium
//            )
//        },
//        singleLine = true,
//        shape = RoundedCornerShape(15.dp),
//        colors = TextFieldDefaults.colors(
//            unfocusedContainerColor = Color.Transparent,
//            focusedContainerColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.White.copy(alpha = 0.4f),
//            focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
//            unfocusedLabelColor = White10.copy(alpha = 0.8f),
//            focusedLabelColor = White10,
//            cursorColor = Color.White
//        ),
//        modifier = Modifier
//            .fillMaxWidth()
//    )
//}
//
//@Composable
//fun AuthPasswordField(value: String, onValueChange: (String) -> Unit, label: String) {
//    var visible by remember { mutableStateOf(false) }
//
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = {
//            Text(label,
//                fontSize = 15.sp,
//                fontFamily = figtreeFontFamily,
//                fontWeight = FontWeight.Medium)
//        },
//        singleLine = true,
//        shape = RoundedCornerShape(15.dp),
//        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
//        trailingIcon = {
//            IconButton(onClick = { visible = !visible }) {
//                Icon(
//                    painter = painterResource(
//                        if (visible) R.drawable.blind_icon
//                        else R.drawable.blind_icon
//                    ),
//                    contentDescription = null
//                )
//            }
//        },
//        colors = TextFieldDefaults.colors(
//            unfocusedContainerColor = Color.Transparent,
//            focusedContainerColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.White.copy(alpha = 0.4f),
//            focusedIndicatorColor = Color.White.copy(alpha = 0.5f),
//            unfocusedLabelColor = White10.copy(alpha = 0.8f),
//            focusedLabelColor = White10,
//            cursorColor = Color.White
//        ),
//        modifier = Modifier
//            .fillMaxWidth()
//    )
//}
