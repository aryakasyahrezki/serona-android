package com.example.serona.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFDFDFD)
val White10 = Color(0xFFFFFFFF)

// Primary Brand Color
val Primary = Color(0xFFEF4E5E)
val OnPrimary = White
val PrimaryContainer = Color(0xFFFFE5E5)
val OnPrimaryContainer = Color(0xFF784141)


// Primary Tonal Palette
val Primary20 = Color(0xFFFA748D)
val Primary50 = Color(0xFFE1233C)
val Primary70 = Color(0xFFAE1C34)
val Primary80 = Color(0xFF6F132A)


// Secondary Colors
val Secondary = Color(0xFFFD9793)
val OnSecondary = White
val SecondaryContainer = Color(0xFFFDDDDD)
val OnSecondaryContainer = Color(0xFF5E5F60)


// Secondary Tonal Palette
val Secondary10 = Color(0xFFFFD7E7)
val Secondary20 = Color(0xFFFDCFDF)
val Secondary40 = Color(0xFFFEBFD2)
val Secondary30 = Color(0xFFFDEBD1)
val Secondary50 = Color(0xFFFDA1BA)
val Secondary60 = Color(0xFFFDA9C0)
val Secondary70 = Color(0xFFFD8EAE)
val Secondary80 = Color(0xFFFD90AD)


// Tertiary Colors
val Tertiary = Color(0xFFB9212D)
val OnTertiary = White
val TertiaryContainer = Color(0xFFFFC6D5)
val OnTertiaryContainer = OnSecondaryContainer


// Tertiary Tonal Palette
val Tertiary80 = Color(0xFFE2293B)
val Tertiary40 = Color(0xFFF76275)
val Tertiary20 = Color(0xFFFD9D91)

// Dark Accent
val DarkAccent80 = Color(0xFF370318)
val DarkAccent70 = Color(0xFF47081D)
val DarkAccent60 = Color(0xFF580D22)
val DarkAccent50 = Color(0xFF9B2940)

// Warm Accent
val WarmLightPink = Color(0xFFFF568D)
val WarmRedPinkSoft = Color(0xFFFD7F94)
val WarmPeach = Tertiary20
val WarmRedOrange = Color(0xFFFFAA96)
val WarmSoftCoral = Color(0xFFFFD4CA)

//success scan
val SuccessScan = Color(0xFFB0F45E)

// Occasion Office Color
val OfficeText = Color(0xFF4FBEDB)
val OfficeCrc = Color(0xFFB4EFFF)
val OfficeBg = Color(0xFFE9F6FB)
val OfficeLogo = Color(0xFF3D646E)

// Occasion Casual Color
val CasualText = Color(0xFFFE9F54)
val CasualCrc = Color(0xFFB4EFFF)
val CasualBg = Color(0xFFFEC191)
val CasualLogo = Color(0xFFD88340)

// Occasion Festival Color
val FestText = Color(0xFF7BDE9F)
val FestCrc = Color(0xFFAFF4C8)
val FestBg = Color(0xFFE7FBEF)
val FestLogo = Color(0xFF46895E)

// Occasion Party Color
val PartyText = Color(0xFFEB5A81)
val PartyCrc = Color(0xFFF88EAA)
val PartyBg = TertiaryContainer
val PartyLogo = Color(0xFFE03160)

// Occasion Wedding Color
val WedText = Color(0xFFD070FE)
val WedCrc = Color(0xFFD989FF)
val WedBg = Color(0xFFEBBFFF)
val WedLogo = Color(0xFF60048B)

// Gradient Background
val LandingPageGrad = Brush.verticalGradient(
    0.0f to WarmRedOrange,
    0.29f to Tertiary20,
    0.52f to WarmRedPinkSoft,
    1f to WarmLightPink
)

val BgGrad = Brush.verticalGradient(
    0.4f to White10,
    1f to PrimaryContainer
)

val GenderButtonGrad = Brush.verticalGradient(
    0.0f to Tertiary20,
    1f to Color(0xFFF45E70)
)

// Glass Color
val glassColor = Brush.horizontalGradient(
    0.0f to White10,
    0.37f to Color(0xFFFF598D),
    0.70f to Color(0xFFFD9492),
    1f to Color(0xFFFD9592)
)

// Text Color
val Heading = Color(0xFF00000)
val BodyText = Color(0xFF5B5B5B)
val ParagraphLight = Color(0xFF8B8B8B)
val MutedLight = Color(0xFFC5C5C5)
val Disabled = Color(0xFFD9D9D9)

