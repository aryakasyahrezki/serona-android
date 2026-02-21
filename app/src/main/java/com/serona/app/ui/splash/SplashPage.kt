package com.serona.app.ui.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.serona.app.R
import com.serona.app.theme.Primary
import com.serona.app.theme.Tertiary
import com.serona.app.theme.White10
import com.serona.app.theme.montserratFontFamily
import com.serona.app.ui.auth.AuthState
import com.serona.app.ui.auth.AuthViewModel
import com.serona.app.ui.component.BottomWaveShape
import com.serona.app.ui.navigation.Routes
import com.serona.app.utils.ResponsiveScale
import kotlinx.coroutines.delay


@Composable
fun screenWidthDp(): Int =
    LocalConfiguration.current.screenWidthDp

@Composable
fun screenHeightDp(): Int =
    LocalConfiguration.current.screenHeightDp

@Composable
fun scaleFont(base: Float): Float {
    val width = screenWidthDp()
    return base * (width / 360f)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedBoxWithConstraintsScope")
@Composable
fun SplashFullBackground(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    
    val authState by viewModel.authState.observeAsState(AuthState.Loading)

    LaunchedEffect(Unit){
        viewModel.checkAuthStatus()
    }

    val screenHeight = screenHeightDp()
    val screenWidth = screenWidthDp()

    ResponsiveScale(maxFontScale = 1f) {
        Box(modifier = Modifier.fillMaxSize()) {

            // IMAGE BACKGROUND
            Image(
                painter = painterResource(id = R.drawable.splash_page_bg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f), // 👈 image hanya 75% layar
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter
            )

            // SOFT PINK OVERLAY
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color(0xFFEF4E5E).copy(0.3f),
                            0.34f to Color(0xFFF45E70).copy(0.1f),
                            0.50f to Color(0xFFF66779).copy(0.2f),
                            0.79f to Color(0xFFFD7F94).copy(0.15f),
                            0.99f to Color(0xFFFFB2BF).copy(0.15f)
                        )
                    )
            )

            // BOTTOM WHITE SHAPE
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height((screenHeight * 0.28).dp)
                    .background(
                        brush = Brush.verticalGradient(
                            0f to White10,
                            1f to Color(0xFFFFE5E5)
                        ),
                        shape = BottomWaveShape()
                    )
                    .padding(
                        horizontal = (screenWidth * 0.08).dp,
                        vertical = 24.dp
                    ),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Hello,\nGorgeous !",
                    fontSize = scaleFont(32f).sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = montserratFontFamily,
                    color = Tertiary,
                    lineHeight = scaleFont(36f).sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Your space to explore beauty, express yourself, and shine every day.",
                    fontSize = scaleFont(14f).sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = montserratFontFamily,
                    color = Primary,
                    lineHeight = scaleFont(20f).sp
                )
            }
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated, AuthState.Unauthenticated, AuthState.NeedPersonalInfo -> {
                delay(1000)

                if (authState == AuthState.Authenticated) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                } else if(authState == AuthState.NeedPersonalInfo){
                    navController.navigate(Routes.PERSONALINFO) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }else {
                    navController.navigate(Routes.LANDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
            AuthState.Loading, AuthState.Idle -> {
                // Tetap di sini kalau masih loading
            }
            else -> Unit
        }
    }
}