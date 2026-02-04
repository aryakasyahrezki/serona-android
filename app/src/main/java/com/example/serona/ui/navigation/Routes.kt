package com.example.serona.ui.navigation

object Routes{
    const val SPLASH = "splash"
    const val LANDING = "landing"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PERSONALINFO = "personalInfo"

    //main tab
    const val HOME = "home"

    const val TUTORIAL = "tutorial/{faceShape}/{skinTone}/{occasion}"

    fun navigateToTutorial(
        faceShape: String? = null,
        skinTone: String? = null,
        occasion: String? = null
    ): String {
        return "tutorial/${faceShape ?: "none"}/${skinTone ?: "none"}/${occasion ?: "none"}"
    }

    const val DETAIL = "detail"
    const val FAVORITE = "favorite"
    const val PROFILE = "profile"
    const val PRIVACY = "privacyPolicy"
    const val DELETE_PROFILE = "deleteProfile"
    const val EDIT_PROFILE = "editProfile"

    const val SCAN_MENU = "faceScanMenu"
    const val SCAN = "scan"

    const val RESULT = "result/{shape}/{skintone}" // Menggunakan parameter agar data bisa dikirim

}