package com.example.serona.ui.component

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BottomWaveShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val baseHeight = size.height * 0.18f
        val upAmplitude = size.height * 0.05f
        val downAmplitude = size.height * 0.12f

        val path = Path().apply {

            // mulai dari kiri
            moveTo(0f, baseHeight - (upAmplitude * 5f))

            // S-curve: naik -> turun -> naik
            cubicTo(
                size.width * 0.25f, baseHeight - (upAmplitude * 15f),   // control 1 (naik)
                size.width * 0.75f, baseHeight + downAmplitude, // control 2 (turun)
                size.width, baseHeight                           // end point
            )

            // tutup shape
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        return Outline.Generic(path)
    }
}


