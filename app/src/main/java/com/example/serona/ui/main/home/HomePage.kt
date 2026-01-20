package com.example.serona.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Attractions
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Church
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Festival
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.serona.R
import com.example.serona.theme.BodyText
import com.example.serona.theme.CasualBg
import com.example.serona.theme.CasualCrc
import com.example.serona.theme.CasualLogo
import com.example.serona.theme.CasualText
import com.example.serona.theme.FestBg
import com.example.serona.theme.FestCrc
import com.example.serona.theme.FestLogo
import com.example.serona.theme.FestText
import com.example.serona.theme.Grey10
import com.example.serona.theme.Grey30
import com.example.serona.theme.Grey40
import com.example.serona.theme.Heading
import com.example.serona.theme.OfficeBg
import com.example.serona.theme.OfficeCrc
import com.example.serona.theme.OfficeLogo
import com.example.serona.theme.OfficeText
import com.example.serona.theme.OnPrimaryContainer
import com.example.serona.theme.PartyBg
import com.example.serona.theme.PartyCrc
import com.example.serona.theme.PartyLogo
import com.example.serona.theme.PartyText
import com.example.serona.theme.Primary
import com.example.serona.theme.PrimaryContainer
import com.example.serona.theme.WedBg
import com.example.serona.theme.WedCrc
import com.example.serona.theme.WedLogo
import com.example.serona.theme.WedText
import com.example.serona.theme.White
import com.example.serona.theme.White10
import com.example.serona.theme.figtreeFontFamily
import com.example.serona.ui.auth.AuthViewModel
import com.example.serona.ui.component.ScannedInfoCard
import com.example.serona.ui.component.eventCard
import com.example.serona.ui.component.guideCard
import com.example.serona.ui.navigation.Routes

@Composable
fun HomePage(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // Observasi data user
    val state by homeViewModel.uiState.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val fontSize = (screenWidth * 0.052f).value.sp
    val iconSize = screenWidth * 0.05f
    val space = (screenHeight * 0.04f)
    val topContentSize = screenWidth * 0.25f
    val topContentSizeImage = screenWidth * 0.4f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .padding(vertical = 50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Hi, ${state.userName}!",
                    fontSize = fontSize,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Heading
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "This is your personalized makeup preview, designed perfectly " +
                            "to match your facial features",
                    fontSize = fontSize * 0.57,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = BodyText,
                    lineHeight = fontSize * 0.9f
                )

                Spacer(modifier = Modifier.height(space * 0.5f))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    ScannedInfoCard(
                        header = state.faceHeader,
                        body = state.faceBody,
                        fontSize = fontSize * 0.55f,
                        modifier = Modifier.weight(0.5f),
                        bgColor = Primary,
                        textColor = White,
                    ) {
                        Box(
                            modifier = Modifier.size(topContentSize), // Tetap seukuran lingkaran kanan
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(if (state.hasScanned) R.drawable.face_shape_filled else R.drawable.face_shape_null),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(topContentSize)
                                    .scale(1.23f)
                                    .zIndex(1f),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    ScannedInfoCard(
                        header = state.skinHeader,
                        body = state.skinBody,
                        fontSize = fontSize * 0.55f,
                        modifier = Modifier.weight(0.5f),
                        bgColor = PrimaryContainer,
                        textColor = OnPrimaryContainer
                    ) {
                        Box(
                            modifier = Modifier
                                .size(topContentSize)
                                .shadow(
                                    elevation = if (state.hasScanned) 10.dp else 0.dp,
                                    shape = CircleShape,
                                    ambientColor = Color.Black.copy(alpha = 0.6f)
                                )
                                .clip(CircleShape)
                                .drawBehind() {
                                    val width = size.width
                                    val height = size.height
                                    val mainRadius = (size.minDimension / 2) * 0.95f

                                    // 1. DROP SHADOW (Gunakan RadialGradient agar terlihat halus/blur)
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.2f),
                                                Color.Transparent
                                            ),
                                            center = Offset(
                                                width * 0.5f,
                                                height * 0.65f
                                            ), // Titik pusat bayangan
                                            radius = mainRadius * 1.2f
                                        ),
                                        radius = mainRadius * 1.2f,
                                        center = Offset(width * 0.65f, height * 0.9f)
                                    )

                                    if (state.hasScanned) {
                                        drawCircle(
                                            color = state.skinColor,
                                            radius = mainRadius,
                                            center = Offset(width * 0.5f, height * 0.5f)
                                        )


                                        rotate(
                                            degrees = 50f,
                                            pivot = Offset(width * 0.7f, height * 0.3f)
                                        ) {
                                            drawOval(
                                                brush = Brush.radialGradient(
                                                    colors = listOf(
                                                        Color.White.copy(alpha = 0.5f),
                                                        Color.Transparent
                                                    ),
                                                    center = Offset(width * 0.7f, height * 0.3f),
                                                    radius = width * 0.25f
                                                ),
                                                topLeft = Offset(width * 0.55f, height * 0.2f),
                                                size = Size(width * 0.4f, height * 0.12f)
                                            )
                                        }

                                    } else {
                                        val graySphereBrush = Brush.radialGradient(
                                            colors = listOf(
                                                Color.White,
                                                Color(0xFFFFFFFF),
                                                Color(0xFFE7E7E7)
                                            ),
                                            center = Offset(width * 0.45f, height * 0.45f),
                                            radius = width * 0.6f
                                        )
                                        drawCircle(brush = graySphereBrush)
                                    }
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(space))

                Text(
                    text = state.guideTitle,
                    fontSize = fontSize * 0.9f,
                    fontWeight = FontWeight.SemiBold,
                    color = Heading
                )

                Spacer(modifier = Modifier.height(space * 0.3f))

                Column(
                    verticalArrangement = Arrangement.spacedBy(space * 0.3f)
                ) {
                    state.guideItems.forEach { item ->
                        guideCard(
                            Header = item.header,
                            Body = item.body,
                            fontSize = fontSize * 0.6f,
                            iconSize = iconSize,
                            boxColor = if (!state.hasScanned) PrimaryContainer else Color(0xFFFEDCC1),
                            icon = item.iconVector,
                            imageRes = item.imageRes,
                            imageSize = if (!state.hasScanned) 0.6f else 1f
                        )
                    }
                }

                Spacer(modifier = Modifier.height(space))

                Text(
                    text = "Choose Your Event",
                    fontSize = fontSize,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Heading
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Get the best makeup style recommendations for every occasion",
                    fontSize = fontSize * 0.57,
                    fontFamily = figtreeFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = BodyText
                )

                Spacer(modifier = Modifier.height(space * 0.3f))

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    eventCard(
                        label = "Office",
                        fontSize = fontSize * 0.72f,
                        iconTint = OfficeLogo,
                        textColor = OfficeText,
                        crcColor = OfficeCrc,
                        bgColor = OfficeBg,
                        icon = Icons.Outlined.Apartment
                    )

                    eventCard(
                        label = "Casual",
                        fontSize = fontSize * 0.72f,
                        iconTint = CasualLogo,
                        textColor = CasualText,
                        crcColor = CasualCrc,
                        bgColor = CasualBg,
                        icon = Icons.Outlined.Groups
                    )

                    eventCard(
                        label = "Festival",
                        fontSize = fontSize * 0.72f,
                        iconTint = FestLogo,
                        textColor = FestText,
                        crcColor = FestCrc,
                        bgColor = FestBg,
                        icon = Icons.Outlined.Attractions
                    )

                    eventCard(
                        label = "Party",
                        fontSize = fontSize * 0.72f,
                        iconTint = PartyLogo,
                        textColor = PartyText,
                        crcColor = PartyCrc,
                        bgColor = PartyBg,
                        icon = Icons.Outlined.Celebration
                    )

                    eventCard(
                        label = "Wedding",
                        fontSize = fontSize * 0.72f,
                        iconTint = WedLogo,
                        textColor = WedText,
                        crcColor = WedCrc,
                        bgColor = WedBg,
                        icon = Icons.Outlined.Church
                    )
                }

                Button(
                    onClick = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo(Routes.SPLASH) {
                                inclusive = true
                            }
                        }
                    },
                ) {
                    Text("Logout")
                }
            }
        }
    }
}



// home harus fetch data lagi kalo dia baru pertama login

//HomeViewModel
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val userRepository: UserRepository,
//    private val userSession: UserSession
//) : ViewModel() {
//
//    init {
//        if (!userSession.isInitialized()) {
//            viewModelScope.launch {
//                val result = userRepository.getProfile()
//                if (result.isSuccess) {
//                    userSession.setUser(result.getOrNull()!!)
//                }
//            }
//        }
//    }
//}