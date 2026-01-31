package com.example.serona.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.serona.ui.auth.login.LoginPage
import com.example.serona.ui.auth.register.PersonalInfoPage
import com.example.serona.ui.auth.register.RegisterPage
import com.example.serona.ui.landing.LandingPageCarousel
import com.example.serona.ui.main.home.HomePage
import com.example.serona.ui.main.profile.DeleteAccPage
import com.example.serona.ui.main.profile.EditProfilePage
import com.example.serona.ui.main.profile.PrivacyPage
import com.example.serona.ui.main.profile.ProfilePage
import com.example.serona.ui.splash.SplashFullBackground

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Routes.PROFILE,
        modifier = Modifier.fillMaxSize(),

        enterTransition = {
            fadeIn(animationSpec = tween(200))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(200))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200))
        }
    ){
        composable(
            Routes.SPLASH
        ) {
            SplashFullBackground(navController)
        }
        composable(
            Routes.LANDING,
            enterTransition = { fadeIn(animationSpec = tween(800)) }
        ) {
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
        composable(
            Routes.HOME,
            enterTransition = { fadeIn(animationSpec = tween(200)) }
        ) {
            HomePage(navController)
        }

        composable(Routes.TUTORIAL){}

        composable(Routes.FAVORITE){}

        composable(Routes.PRIVACY){
            PrivacyPage(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.DELETE_PROFILE){
            DeleteAccPage(
                onDeleteConfirm = {
                    navController.navigate(Routes.SPLASH) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.EDIT_PROFILE){
            EditProfilePage(onBackClick = { navController.popBackStack() })
        }

        composable(Routes.PROFILE){
            ProfilePage(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                onPrivacyClick = { navController.navigate(Routes.PRIVACY)},
                onDeleteAccountClick = { navController.navigate(Routes.DELETE_PROFILE) }
            )
        }

        composable(Routes.SCAN){}
    }
}