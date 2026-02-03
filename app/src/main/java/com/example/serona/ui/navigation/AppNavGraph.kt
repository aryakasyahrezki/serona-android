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
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.serona.ui.auth.login.LoginPage
import com.example.serona.ui.auth.register.PersonalInfoPage
import com.example.serona.ui.auth.register.RegisterPage
import com.example.serona.ui.landing.LandingPageCarousel
import com.example.serona.ui.main.favorite.FavoritePage
import com.example.serona.ui.main.tutorial.TutorialDetailPage
import com.example.serona.ui.main.tutorial.TutorialPage
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
        startDestination = Routes.SPLASH,
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
    ) {
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

//        composable(
//            Routes.TUTORIAL,
//            enterTransition = { fadeIn(animationSpec = tween(200)) }
//        ) {
//            TutorialPage(
//                onTutorialClick = { tutorialId ->
//                    navController.navigate("${Routes.DETAIL}/$tutorialId")
//                },
//                onBackClick = { navController.popBackStack() }
//            )
//        }

        composable(
            route = Routes.TUTORIAL,
            arguments = listOf(
                navArgument("faceShape") {
                    type = NavType.StringType
                    defaultValue = "none"
                },
                navArgument("skinTone") {
                    type = NavType.StringType
                    defaultValue = "none"
                },
                navArgument("occasion") {
                    type = NavType.StringType
                    defaultValue = "none"
                }
            ),
            enterTransition = { fadeIn(animationSpec = tween(200)) }
        ) {
            TutorialPage(
                onTutorialClick = { tutorialId ->
                    navController.navigate("${Routes.DETAIL}/$tutorialId")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            "${Routes.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val tutorialId = backStackEntry.arguments?.getInt("id") ?: 0
            TutorialDetailPage(
                tutorialId = tutorialId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            Routes.FAVORITE,
            enterTransition = { fadeIn(animationSpec = tween(200)) }
        ) {
            FavoritePage (
                onTutorialClick = { tutorialId ->
                    navController.navigate("${Routes.DETAIL}/$tutorialId")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.PRIVACY){
            PrivacyPage(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.DELETE_PROFILE){
            DeleteAccPage(
                onDeleteConfirm = {
                    navController.navigate(Routes.SPLASH) {
                        popUpTo(navController.graph.id) {
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

        composable(Routes.SCAN) {}
    }
    
}