package com.example.serona.ui.ui.navigation

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.serona.ui.ui.auth.AuthViewModel
//import com.example.serona.ui.ui.splash.
import com.example.serona.ui.ui.auth.login.LoginPage
import com.example.serona.ui.ui.auth.login.LoginViewModel
import com.example.serona.ui.ui.auth.register.PersonalInfoPage
import com.example.serona.ui.ui.auth.register.RegisterPage
import com.example.serona.ui.ui.auth.register.RegisterViewModel
import com.example.serona.ui.ui.landing.LandingPageCarousel
import com.example.serona.ui.ui.main.HomePage
import com.example.serona.ui.ui.splash.SplashFullBackground
import kotlin.getValue

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
//    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ){
        composable(Routes.SPLASH) {
            SplashFullBackground(navController)
        }
        composable(Routes.LANDING) {
            LandingPageCarousel(navController)
        }
        composable(Routes.LOGIN) {
            LoginPage(navController)
        }
        composable(Routes.REGISTER) {
            RegisterPage(navController)
        }
        composable(Routes.PERSONALINFO) {
            PersonalInfoPage(navController)
        }
        composable(Routes.HOME) {
            HomePage(navController)
        }
    }
    
}