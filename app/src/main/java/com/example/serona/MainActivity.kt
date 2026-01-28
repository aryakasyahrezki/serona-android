package com.example.serona

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.getValue
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.serona.theme.SeronaTheme
import com.example.serona.ui.component.NavBar
import com.example.serona.ui.navigation.AppNavGraph
import com.example.serona.ui.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            SeronaTheme() {
                val navController = rememberNavController()

                // Ambil rute saat ini
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Daftar rute yang menampilkan NavBar
                val mainRoutes = listOf(Routes.HOME, Routes.TUTORIAL, Routes.FAVORITE, Routes.PROFILE)
                val shouldShowNavBar = currentRoute in mainRoutes

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(),
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        // Gunakan Box untuk menumpuk AppNavGraph dan NavBar
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // 1. Konten utama (paling belakang)
                            AppNavGraph(navController = navController)

                            AnimatedVisibility (
                                visible = shouldShowNavBar,
                                modifier = Modifier.align(Alignment.BottomCenter),
                                enter = slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = tween(durationMillis = 200) // Sesuaikan durasi dengan AppNavGraph
                                ) + fadeIn(animationSpec = tween(200)),
                                exit = slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(durationMillis = 200)
                                ) + fadeOut(animationSpec = tween(200))
                            ) {
                                if (shouldShowNavBar) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .navigationBarsPadding()
                                    ) {
                                        NavBar(
                                            selectedIndex = when (currentRoute) {
                                                Routes.HOME -> 0
                                                Routes.TUTORIAL -> 1
                                                Routes.FAVORITE -> 2
                                                Routes.PROFILE -> 3
                                                else -> 0
                                            },
                                            onItemSelected = { index ->
                                                val targetRoute = when (index) {
                                                    0 -> Routes.HOME
                                                    1 -> Routes.TUTORIAL
                                                    2 -> Routes.FAVORITE
                                                    3 -> Routes.PROFILE
                                                    else -> Routes.HOME
                                                }
                                                if (currentRoute != targetRoute) {
                                                    navController.navigate(targetRoute) {
                                                        // Memastikan Home selalu ada di dasar stack
                                                        popUpTo(Routes.HOME) {
                                                            saveState = true
                                                        }
                                                        // Menghindari duplikasi instance halaman
                                                        launchSingleTop = true
                                                        // Mengembalikan state (posisi scroll, dll)
                                                        restoreState = true
                                                    }
                                                }
                                            },
                                            onCenterClick = {
                                                navController.navigate(Routes.SCAN)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
