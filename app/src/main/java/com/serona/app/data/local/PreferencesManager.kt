package com.serona.app.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// PreferencesManager.kt
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("serona_prefs", Context.MODE_PRIVATE)

    fun isFirstLaunch(userEmail: String): Boolean {
        // Defaultnya true, artinya dianggap pertama kali
        return sharedPreferences.getBoolean("is_first_launch$userEmail", true)
    }

    fun setFirstLaunchCompleted(userEmail: String) {
        sharedPreferences.edit().putBoolean("is_first_launch$userEmail", false).apply()
    }
}