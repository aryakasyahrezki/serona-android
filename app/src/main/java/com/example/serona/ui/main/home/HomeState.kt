package com.example.serona.ui.main.home

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class HomeUiState(
    val userName: String = "",
    val hasScanned: Boolean = false,
    val faceHeader: String = "",
    val faceBody: String = "",
    val skinHeader: String = "",
    val skinBody: String = "",
    val skinColor: Color = Color.Gray,
    val guideTitle: String = "",
    val guideItems: List<GuideUiItem> = emptyList()
)

data class GuideUiItem(
    val header: String,
    val body: String,
    val iconVector: ImageVector? = null,
    @DrawableRes val imageRes: Int? = null
)