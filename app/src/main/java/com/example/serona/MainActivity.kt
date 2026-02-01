package com.example.serona

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
        setContent {
            SeronaTheme() {
                val navController = rememberNavController()

                // 1. Ambil rute saat ini
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 2. Daftar rute yang menampilkan NavBar
                val mainRoutes = listOf(Routes.HOME, Routes.TUTORIAL, Routes.FAVORITE, Routes.PROFILE)
                val shouldShowNavBar = currentRoute in mainRoutes

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { padding ->
                    // Kirim padding ke NavHost agar konten tidak tertutup NavBar
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
                                // Animasi masuk: slide dari bawah + fade in
                                enter = slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = tween(durationMillis = 200) // Sesuaikan durasi dengan AppNavGraph
                                ) + fadeIn(animationSpec = tween(200)),
                                // Animasi keluar: slide ke bawah + fade out
                                exit = slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(durationMillis = 200)
                                ) + fadeOut(animationSpec = tween(200))
                            ) {
                                // 2. NavBar (melayang di depan konten)
                                if (shouldShowNavBar) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
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
                                                navController.navigate(targetRoute) {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                            onCenterClick = {
                                                navController.navigate(Routes.SCAN_MENU)
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
