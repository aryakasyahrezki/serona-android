package com.example.serona

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Install Splash API
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // 2. Set Fullscreen (Lakukan sebelum setContentView)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_splash)

        // 3. Pindah ke MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)

            val options = ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out)
            startActivity(intent, options.toBundle())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.fade_in, R.anim.fade_out)
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }

            finish()
        }, 1500)
    }
}