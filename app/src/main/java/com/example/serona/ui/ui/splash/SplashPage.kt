package com.example.serona.ui.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.serona.R
import com.example.serona.ui.theme.LandingPageGrad
import com.example.serona.ui.theme.leagueSpartanFontFamily
import com.example.serona.ui.ui.auth.AuthState
import com.example.serona.ui.ui.auth.AuthViewModel
import com.example.serona.ui.ui.navigation.Routes

@Composable
fun SplashFullBackground(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    
    val authState by viewModel.authState.observeAsState(AuthState.Loading)

    LaunchedEffect(Unit){
        viewModel.checkAuthStatus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LandingPageGrad),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ellips_splash_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.serona_logo),
                contentDescription = null,
                alignment = Alignment.Center
            )
            Text(
                "The art of your own glow",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.White,
                fontFamily = leagueSpartanFontFamily,
                fontSize = 13.sp,
                letterSpacing = 5.sp
            )
        }
    }

    LaunchedEffect(authState) {
        when (authState) {

            AuthState.Loading,
            AuthState.Idle -> {}

            AuthState.Authenticated -> {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }

            AuthState.Unauthenticated -> {
                navController.navigate(Routes.LANDING) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }

            else -> Unit
        }
    }
}