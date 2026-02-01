package com.example.serona.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Attractions
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Church
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.example.serona.theme.Heading
import com.example.serona.theme.OfficeBg
import com.example.serona.theme.OfficeCrc
import com.example.serona.theme.OfficeLogo
import com.example.serona.theme.OfficeText
import com.example.serona.theme.OnPrimaryContainer
import com.example.serona.theme.ParagraphLight
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
import com.example.serona.theme.figtreeFontFamily
import com.example.serona.ui.component.ScannedInfoCard
import com.example.serona.ui.component.eventCard
import com.example.serona.ui.component.guideCard

@Composable
fun HomePage(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    // Observasi data user
    val state by homeViewModel.uiState.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val horiPadding = screenWidth * 0.05f
    val vertiPadding = screenHeight * 0.08f
    val fontSize = (screenWidth * 0.052f).value.sp
    val iconSize = screenWidth * 0.05f
    val space = (screenHeight * 0.04f)
    val topContentSize = screenWidth * 0.25f
    val fabSize = screenHeight * 0.08f

    if(state.showScanDialog){
        ScanDialog(homeViewModel, fontSize, screenWidth, screenHeight, space)
    }

    DisposableEffect(Unit) {
        onDispose {
            // Memastikan tooltip di-reset ke false saat meninggalkan halaman ini
            homeViewModel.dismissTooltip()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(top = vertiPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horiPadding)
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

                Spacer(modifier = Modifier.height(space * 0.3f))

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
                    horizontalArrangement = Arrangement.spacedBy(space * 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    ScannedInfoCard(
                        header = state.faceHeader,
                        body = state.faceBody,
                        fontSize = fontSize * 0.55f,
                        modifier = Modifier.weight(0.5f),
                        bgColor = Primary,
                        textColor = White,
                        screenHeight = screenHeight
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
                        textColor = OnPrimaryContainer,
                        screenHeight = screenHeight
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
                            imageSize = if (!state.hasScanned) 0.6f else 1f,
                            screenHeight = screenHeight,
                            screenWidth = screenWidth
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

                Spacer(modifier = Modifier.height(space * 0.3f))

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
                        icon = Icons.Outlined.Apartment,
                        screenHeight = screenHeight,
                        screenWidth = screenWidth
                    )

                    eventCard(
                        label = "Casual",
                        fontSize = fontSize * 0.72f,
                        iconTint = CasualLogo,
                        textColor = CasualText,
                        crcColor = CasualCrc,
                        bgColor = CasualBg,
                        icon = Icons.Outlined.Groups,
                        screenHeight = screenHeight,
                        screenWidth = screenWidth
                    )

                    eventCard(
                        label = "Festival",
                        fontSize = fontSize * 0.72f,
                        iconTint = FestLogo,
                        textColor = FestText,
                        crcColor = FestCrc,
                        bgColor = FestBg,
                        icon = Icons.Outlined.Attractions,
                        screenHeight = screenHeight,
                        screenWidth = screenWidth
                    )

                    eventCard(
                        label = "Party",
                        fontSize = fontSize * 0.72f,
                        iconTint = PartyLogo,
                        textColor = PartyText,
                        crcColor = PartyCrc,
                        bgColor = PartyBg,
                        icon = Icons.Outlined.Celebration,
                        screenHeight = screenHeight,
                        screenWidth = screenWidth
                    )

                    eventCard(
                        label = "Wedding",
                        fontSize = fontSize * 0.72f,
                        iconTint = WedLogo,
                        textColor = WedText,
                        crcColor = WedCrc,
                        bgColor = WedBg,
                        icon = Icons.Outlined.Church,
                        screenHeight = screenHeight,
                        screenWidth = screenWidth
                    )
                }

                Spacer(modifier = Modifier.height(fabSize * 2f))
            }
        }
    }

    if (state.showScanTooltip) {
        Box(
            modifier = Modifier
                .width(screenWidth)
                .height(screenHeight)
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // Opsional: Kalau mau klik di luar tooltip menutup tooltipnya
                     homeViewModel.dismissTooltip()
                }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(fabSize)
            .background(Color.Transparent)
            .zIndex(2f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            if (state.showScanTooltip) {
                ScanTooltip(
                    text = "Click here to scan your face",
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .size(fabSize)
                        .background(Primary, CircleShape)
                        .clickable() {
                            homeViewModel.dismissTooltip()
                            navController.navigate("scan")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ar_on_you),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(iconSize *1.25f)
                    )
                }
            }
        }
    }
}

@Composable
fun ScanDialog(
    homeViewModel: HomeViewModel,
    fontSize: TextUnit,
    width: Dp,
    height: Dp,
    space: Dp
) {
    Dialog (
        onDismissRequest = { homeViewModel.dismissScanDialog() }
    ) {
        Card (
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            modifier = Modifier
                .width(width * 1.2f)
                .height(height * 0.48f)
                .padding(5.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp),

            ) {
                Image(
                    painter = painterResource(R.drawable.scan_your_face_first),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(0.4f)
                )

                Text(
                    "Scan Your Face First",
                    fontWeight = FontWeight.Bold,
                    fontFamily = figtreeFontFamily,
                    textAlign = TextAlign.Center,
                    color = Heading,
                    fontSize = fontSize * 0.9f
                )

                Spacer(modifier = Modifier.height(space * 0.2f))

                Text(
                    "To receive personalized recommendations, please scan your face first.",
                    fontWeight = FontWeight.Medium,
                    fontFamily = figtreeFontFamily,
                    textAlign = TextAlign.Center,
                    fontSize = fontSize * 0.6f
                )

                Spacer(modifier = Modifier.height(space * 1.5f))

                Row {
                    TextButton(
                        onClick = { homeViewModel.dismissScanDialog() },
                        modifier = Modifier
                            .weight(0.8f)
                            .height(48.dp)
                            .border(
                                width = 0.8f.dp,
                                color = ParagraphLight,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = ParagraphLight
                        )
                    ) {
                        Text(
                            text = "Skip",
                            fontFamily = figtreeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = fontSize * 0.65f
                        )
                    }

                    Spacer(modifier = Modifier.width(space * 0.2f))

                    Button(
                        onClick = { homeViewModel.triggerScanTooltip() },
                        modifier = Modifier
                            .weight(1f) // KUNCI: Membuat lebar tombol sama besar
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary // Warna merah/pink sesuai tema kamu
                        )
                    ) {
                        Text(
                            text = "Scan Your Face",
                            fontFamily = figtreeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = fontSize * 0.65f,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScanTooltip(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = figtreeFontFamily
            )
        }

        Box(
            modifier = Modifier
                .size(16.dp, 8.dp)
                .drawBehind() {
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.width, 0f)
                        lineTo(size.width / 2, size.height)
                        close()
                    }
                    drawPath(path, Color.White)
                }
        )
    }
}

