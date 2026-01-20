package com.example.serona.ui.component

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class NavBarShape(
    private val notchWidthRatio: Float = 0.4f,
    private val notchDepthRatio: Float = 0.4f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val width = size.width
        val height = size.height

        val notchWidth = width * notchWidthRatio
        val notchDepth = height * notchDepthRatio

        val centerX = width / 2f
        val leftNotch = centerX - notchWidth / 2
        val rightNotch = centerX + notchWidth / 2

        val path = Path().apply {

            moveTo(0f, 0f)

            // kiri → sebelum lekukan
            lineTo(leftNotch, 0f)

            // curve turun
            cubicTo(
                leftNotch + notchWidth * 0.35f,   // CP1 X (jauh dari edge)
                0f,               // CP1 Y (turun pelan)

                centerX - notchWidth * 0.2f,      // CP2 X (dekat tengah)
                notchDepth,                       // CP2 Y (kedalaman max)

                centerX,                          // End X
                notchDepth                       // End Y
            )

            // curve naik
            cubicTo(
                centerX + notchWidth * 0.2f,
                notchDepth,
                rightNotch - notchWidth * 0.35f,
                0f,
                rightNotch,
                0f
            )

            // kanan
            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        return Outline.Generic(path)
    }
}