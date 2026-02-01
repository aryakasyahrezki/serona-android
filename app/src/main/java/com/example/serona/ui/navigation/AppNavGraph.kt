package com.example.serona.ui.navigation

import android.util.Log
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
import com.example.serona.ui.main.scan.FaceScanMenuScreen
import com.example.serona.ui.main.scan.ScanScreen
import com.example.serona.ui.splash.SplashFullBackground
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.serona.ui.main.scan.ResultScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Routes.RESULT,
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

        composable(Routes.PROFILE){}

        composable(Routes.SCAN_MENU){
            FaceScanMenuScreen(navController)
        }
        composable(Routes.SCAN){
            ScanScreen(navController = navController)
        }

        composable(
            route = Routes.RESULT,
            arguments = listOf(
                navArgument("shape") { type = NavType.StringType },
                navArgument("skintone") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Mengambil data yang dikirim dari ViewModel
//            val shape = backStackEntry.arguments?.getString("shape") ?: "Unknown"
//            val skintone = backStackEntry.arguments?.getString("skintone") ?: "Unknown"
//
//            // Menampilkan layar hasil
//            ResultScreen(navController = navController, shape = shape, tone = skintone)
            val shapeArg = backStackEntry.arguments?.getString("shape")
            val toneArg = backStackEntry.arguments?.getString("skintone")

            Log.d("NAV_CHECK", "Data diterima: shape=$shapeArg, tone=$toneArg")

            ResultScreen(
                navController = navController,
                shape = shapeArg ?: "Unknown",
                tone = toneArg ?: "Unknown"
            )
        }

    }
    
}