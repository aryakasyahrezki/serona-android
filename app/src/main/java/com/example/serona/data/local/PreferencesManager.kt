package com.example.serona.data.local

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

    fun isFirstLaunch(): Boolean {
        // Defaultnya true, artinya dianggap pertama kali
        return sharedPreferences.getBoolean("is_first_launch", true)
    }

    fun setFirstLaunchCompleted() {
        sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
    }

    fun setFirstLaunchUncompleted() {
        sharedPreferences.edit().putBoolean("is_first_launch", true).apply()
    }
}