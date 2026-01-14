package com.example.serona

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.serona.theme.SeronaTheme
import com.example.serona.ui.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
        setContent {
            SeronaTheme() {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent
                ) { padding ->
                    val navController = rememberNavController()

                    Box(modifier = Modifier.fillMaxSize()) {
                        AppNavGraph(
                            navController = navController
                            // modifier = Modifier.padding(innerPadding) <- Hapus padding ini jika ingin fullscreen tembus status bar
                        )
                    }
                }
            }
        }
    }
}
