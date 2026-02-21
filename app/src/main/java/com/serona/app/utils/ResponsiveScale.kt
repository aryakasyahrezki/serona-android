package com.serona.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun ResponsiveScale(
    maxFontScale: Float = 1.15f,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val cappedDensity = Density(
        density = density.density,
        fontScale = density.fontScale.coerceAtMost(maxFontScale)
    )

    CompositionLocalProvider(LocalDensity provides cappedDensity) {
        content()
    }
}