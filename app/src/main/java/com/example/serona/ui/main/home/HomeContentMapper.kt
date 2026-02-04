package com.example.serona.ui.main.home

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.ui.graphics.Color
import com.example.serona.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HomeContentMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getFaceDescription(faceShape: String?): String {
        return when (faceShape?.lowercase()) {
            "oval" -> context.getString(R.string.desc_face_oval)
            "square" -> context.getString(R.string.desc_face_square)
            "round" -> context.getString(R.string.desc_face_round)
            "diamond" -> context.getString(R.string.desc_face_diamond)
            "rectangle" -> context.getString(R.string.desc_face_rectangle)
            "heart" -> context.getString(R.string.desc_face_heart)
            else -> context.getString(R.string.face_not_detected_body)
        }
    }

    fun getSkinDescription(skinTone: String?): String {
        return when (skinTone?.lowercase()) {
            "fair light" -> context.getString(R.string.desc_skin_light)
            "medium tan" -> context.getString(R.string.desc_skin_medium)
            "deep" -> context.getString(R.string.desc_skin_deep)
            else -> context.getString(R.string.skin_not_detected_body)
        }
    }

    fun getSkinColor(skinTone: String?): Color {
        return when (skinTone?.lowercase()) {
            "fair light" -> Color(0xFFF5E1C8)
            "medium tan" -> Color(0xFFD4A373)
            "deep" -> Color(0xFF8D5524)
            else -> Color(0xFFFFD7A8)
        }
    }

    fun getGuideItems(scanned: Boolean) = if (scanned) {
        listOf(
            GuideUiItem(
                context.getString(R.string.guide_post_1_title),
                context.getString(R.string.guide_post_1_body),
                imageRes = R.drawable.guide_card_hydrate
            ),
            GuideUiItem(
                context.getString(R.string.guide_post_2_title),
                context.getString(R.string.guide_post_2_body),
                imageRes = R.drawable.guide_card_primer
            ),
            GuideUiItem(
                context.getString(R.string.guide_post_3_title),
                context.getString(R.string.guide_post_3_body),
                imageRes = R.drawable.guide_card_protect
            )
        )
    } else {
        listOf(
            GuideUiItem(context.getString(R.string.guide_pre_1_title), context.getString(R.string.guide_pre_1_body), imageRes = R.drawable.ar_on_you),
            GuideUiItem(context.getString(R.string.guide_pre_2_title), context.getString(R.string.guide_pre_2_body), Icons.Outlined.Check),
            GuideUiItem(context.getString(R.string.guide_pre_3_title), context.getString(R.string.guide_pre_3_body), Icons.Outlined.Palette)
        )
    }
}