package com.example.serona.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.serona.R
import com.example.serona.ui.theme.SeronaTheme
import com.example.serona.ui.ui.auth.login.LoginPage
import com.example.serona.ui.ui.auth.login.LoginViewModel
import com.example.serona.ui.ui.auth.register.PersonalInfoPage
import com.example.serona.ui.ui.auth.register.RegisterPage
import com.example.serona.ui.ui.auth.register.RegisterViewModel

class MainActivity : ComponentActivity() {

//    private val loginViewModel: LoginViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen() // wajib
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeronaTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                LoginPage(loginViewModel)
                RegisterPage(registerViewModel)
//                }
            }
        }
    }
}
