@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.serona.ui.screens.landing

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.serona.R
import com.example.serona.ui.theme.OnPrimary
import com.example.serona.ui.theme.Primary
import com.example.serona.ui.theme.Primary70
import com.example.serona.ui.theme.WarmRedPinkSoft
import com.example.serona.ui.theme.montserratFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LandingPageCarousel() {
//    var currentPage by remember { mutableStateOf(0) }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val half = size.width / 2f

                        if (offset.x < half) {
                            // Tap kiri → prev
                            scope.launch {
                                val prev = pagerState.currentPage - 1
                                if (prev >= 0) pagerState.animateScrollToPage(prev)
                            }
                        } else {
                            // Tap kanan → next
                            scope.launch {
                                val next = pagerState.currentPage + 1
                                if (next <= 2) pagerState.animateScrollToPage(next)
                            }
                        }
                    }
                }
        ) { page ->
            when (page) {
                0 -> FirstLandingPage()
                1 -> SecondLandingPage()
                2 -> ThirdLandingPage()
            }
        }

        // --- Dots Indicator for LandingPage ---
        AnimatedVisibility(
            visible = pagerState.currentPage < 2,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier.padding(bottom = 50.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    val selected = pagerState.currentPage == index

                    Box(
                        modifier = Modifier
                            .padding(7.dp)
                            .size(14.dp)
                            .background(
                                if (selected) Primary70 else Primary70.copy(0.3f),
                                CircleShape
                            )
                    )
                }
            }
        }

        // --- Button for ThirdLandingPage ---
        AnimatedVisibility(
            visible = pagerState.currentPage == 2,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            Button(
                onClick = { }, //ini nanti ke LoginPage
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .width(170.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Primary70)
            ) {
                Text(
                    "Start",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

// Data model for labels
data class LabelData(val text: String, val color: Color, val offsetX: Float, val offsetY: Float)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LandingPageTemplate(
    imageRes: Int,
    gradient: Brush,
    title1: String,
    title2: String,
    subtitle: String,
    labels: List<LabelData>,
    scaleImage: Float = 1f
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleImage)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
        )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            labels.forEach { label ->
                CircleLabel(
                    text = label.text,
                    color = label.color,
                    modifier = Modifier.offset(
                        x = maxWidth * label.offsetX,
                        y = maxHeight * label.offsetY
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(0.26f)
                .align(Alignment.BottomStart)
                .padding(24.dp, bottom = 30.dp)
        ) {
            Text(text = title1,
                fontSize = 28.sp,
                fontFamily = montserratFontFamily,
                fontWeight =  FontWeight.Bold,
                color = OnPrimary
            )
            Text(text = title2,
                fontSize = 28.sp,
                fontFamily = montserratFontFamily,
                fontWeight =  FontWeight.Bold,
                color = Primary70
            )
            Text(
                text = subtitle,
                fontSize = 15.sp,
                fontFamily = montserratFontFamily,
                fontWeight =  FontWeight.Medium,
                color = OnPrimary.copy(alpha = 0.9922f),
                lineHeight = 17.sp,
                modifier = Modifier.widthIn(max = 300.dp)

            )
        }
    }
}

@Composable
fun CircleLabel(text: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(color)
    ) {
        Text(text = text, color = Color.White, fontSize = 10.sp)
    }
}

@Composable
fun FirstLandingPage() {
    LandingPageTemplate(
        imageRes = R.drawable.landing_page1_bg,
        gradient = Brush.verticalGradient(
            0.5f to Color(0xFFF66779).copy(0f),
            0.66f to Color(0xFFF45E70).copy(0.31f),
            1f to Primary.copy(0.6f)
        ),
        title1 = "Find Your",
        title2 = "Perfect Look",
        subtitle = "Discover makeup styles that match your face shape",
        labels = listOf(
            LabelData("Heart", Primary, 0.76f, 0.35f),
            LabelData("Square", Primary, 0.1f, 0.51f),
            LabelData("Oval", Primary70, 0.68f, 0.58f)
        ),
        scaleImage = 1.12f
    )
}

@Composable
fun SecondLandingPage() {
    LandingPageTemplate(
        imageRes = R.drawable.landing_page2_bg,
        gradient = Brush.verticalGradient(
            0.07f to WarmRedPinkSoft.copy(0.21f),
            0.5f to Color(0xFFF66779).copy(0.16f),
            0.66f to Color(0xFFF45E70).copy(0.33f),
            1f to Primary.copy(1f)
        ),
        title1 = "Achieve Your",
        title2 = "Perfect Look ",
        subtitle = "A personalized AI makeup guide made exclusively for you",
        labels = emptyList()
    )
}

@Composable
fun ThirdLandingPage() {
    LandingPageTemplate(
        imageRes = R.drawable.landing_page3_bg,
        gradient = Brush.verticalGradient(
            0.01f to Color(0xFFFFB2BF).copy(0.3f),
            0.21f to WarmRedPinkSoft.copy(0.25f),
            0.50f to Color(0xFFF66779).copy(0.2f),
            0.66f to Color(0xFFF45E70).copy(0.6f),
            1f to Primary.copy(0.6f)
        ),
        title1 = "Start Your",
        title2 = "Beauty Journey",
        subtitle = "Scan, match, and discover your best makeup style",
        labels = emptyList()
    )
}