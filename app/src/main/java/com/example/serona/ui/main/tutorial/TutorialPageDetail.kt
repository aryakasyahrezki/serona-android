//package com.example.serona.ui.main.tutorial
//
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.outlined.FavoriteBorder
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.compose.AsyncImage
//import com.example.serona.theme.*
//import com.example.serona.ui.component.BackButton
//import com.example.serona.ui.main.favorite.FavoriteViewModel
//
//private fun parseColorSafely(hex: String?): Color? {
//    if (hex.isNullOrEmpty() || hex == "null") return null
//
//    return try {
//        val colorHex = if (hex.startsWith("#")) hex else "#$hex"
//        Color(android.graphics.Color.parseColor(colorHex))
//    } catch (e: IllegalArgumentException) {
//        Log.e("TutorialStepCard", "Invalid hex color: $hex", e)
//        null
//    }
//}
//
//@Composable
//fun TutorialDetailPage(
//    tutorialId: Int,
//    viewModel: TutorialDetailViewModel = hiltViewModel(),
//    favViewModel: FavoriteViewModel = hiltViewModel(),
//    onClick: () -> Unit = {},
//    onBackClick: () -> Unit
//) {
//    val configuration = LocalConfiguration.current
//    val maxWidth = configuration.screenWidthDp.dp
//    val maxHeight = configuration.screenHeightDp.dp
//
//    val fontSize = (maxWidth * 0.06f).value.sp
//    val horiPadding = maxWidth * 0.05f
//    val vertiPadding = maxHeight * 0.0625f
//    val space = maxHeight * 0.03f
//    val buttonSize = maxWidth * 0.07f
//
//    val tutorial by viewModel.tutorial.collectAsState()
//    val steps by viewModel.steps.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    // Check favorite status after tutorial is loaded
//    val isFavorite = tutorial?.let { favViewModel.isFavorite(it.id) } ?: false
//
//    LaunchedEffect(tutorialId) {
//        viewModel.fetchTutorialAndSteps(tutorialId)
//    }
//
//    // Loading state
//    if (isLoading) {
//        Box(
//            Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator(color = Primary)
//        }
//        return
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(White)
//            .padding(horizontal = horiPadding)
//    ) {
//        // ================= HEADER WITH BACK & FAVORITE =================
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = vertiPadding),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            // Back Button
//            BackButton(
//                onBackClick = onBackClick,
//                buttonSize = buttonSize,
//                fontSize = fontSize
//            )
//
//            // Favorite Icon
//
//        }
//
//        Spacer(modifier = Modifier.height(space * 0.5f))
//
//        // ================= CONTENT =================
//        LazyColumn(
//            contentPadding = PaddingValues(bottom = maxHeight * 0.05f),
//            verticalArrangement = Arrangement.spacedBy(space * 0.5f)
//        ) {
//            tutorial?.let { tut ->
//                // Category Tags
//                item {
//                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                        CategoryChip(tut.main_category, isPrimary = true)
//                        CategoryChip(tut.sub_category, isPrimary = false)
//                    }
//                }
//
//                // Title
//                item {
//                    Text(
//                        text = tut.title,
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold,
//                        color = Heading,
//                        fontFamily = figtreeFontFamily
//                    )
//                }
//
//                // Description
//                item {
//                    Text(
//                        text = tut.description,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = BodyText,
//                        lineHeight = 24.sp,
//                        fontFamily = figtreeFontFamily
//                    )
//                }
//            }
//
//            // Tutorial Steps Section
//            if (steps.isNotEmpty()) {
//                item {
//                    Spacer(Modifier.height(space * 0.5f))
//                    Text(
//                        "Tutorial Steps",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        color = Heading,
//                        fontFamily = figtreeFontFamily
//                    )
//                }
//
//                items(steps) { step ->
//                    TutorialStepCard(
//                        stepNumber = step.step_number,
//                        title = step.title,
//                        description = step.description,
//                        imageUrl = step.image_url,
//                        hex = step.hex
//                    )
//                }
//            } else {
//                item {
//                    Text(
//                        "No content available for this tutorial.",
//                        color = BodyText,
//                        modifier = Modifier.padding(vertical = 16.dp),
//                        fontFamily = figtreeFontFamily
//                    )
//                }
//            }
//        }
//    }
//}
//
//// ================= CATEGORY CHIP COMPONENT =================
//@Composable
//fun CategoryChip(
//    text: String,
//    isPrimary: Boolean = true
//) {
//    Surface(
//        shape = RoundedCornerShape(20.dp),
//        color = if (isPrimary) PrimaryContainer else SecondaryContainer
//    ) {
//        Text(
//            text = text,
//            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
//            color = if (isPrimary) Primary else Tertiary,
//            style = MaterialTheme.typography.bodySmall,
//            fontWeight = FontWeight.Medium,
//            fontFamily = figtreeFontFamily
//        )
//    }
//}
//
//// ================= TUTORIAL STEP CARD COMPONENT =================
//@Composable
//fun TutorialStepCard(
//    stepNumber: Int,
//    title: String,
//    description: String,
//    imageUrl: String?,
//    hex: String?
//) {
//    val configuration = LocalConfiguration.current
//    val maxWidth = configuration.screenWidthDp.dp
//    val maxHeight = configuration.screenHeightDp.dp
//    val space = maxHeight * 0.03f
//
//    val parsedColor = remember(hex) { parseColorSafely(hex) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = space * 0.2f),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Box(modifier = Modifier.fillMaxWidth()) {
//            // ================= PINK GRADIENT STRIP (LEFT) =================
//            Box(
//                modifier = Modifier
//                    .width(14.dp)
//                    .fillMaxHeight()
//                    .background(
//                        brush = Brush.horizontalGradient(
//                            colors = listOf(
//                                Primary.copy(alpha = 0.6f),
//                                Primary.copy(alpha = 0.2f),
//                                Color.Transparent
//                            )
//                        ),
//                        shape = RoundedCornerShape(
//                            topStart = 16.dp,
//                            bottomStart = 16.dp
//                        )
//                    )
//            )
//
//            // ================= VERSION 1: COLOR (HORIZONTAL) =================
//            if (parsedColor != null && imageUrl.isNullOrEmpty()) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Color circle
//                    Box(
//                        modifier = Modifier
//                            .size(56.dp)
//                            .clip(CircleShape)
//                            .background(parsedColor)
//                    )
//
//                    Spacer(Modifier.width(space))
//
//                    // Text content
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(
//                            text = title,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.SemiBold,
//                            color = Heading,
//                            fontFamily = figtreeFontFamily
//                        )
//
//                        Spacer(Modifier.height(4.dp))
//
//                        Text(
//                            text = description,
//                            fontSize = 14.sp,
//                            lineHeight = 20.sp,
//                            color = BodyText,
//                            fontFamily = figtreeFontFamily
//                        )
//                    }
//                }
//            }
//            // ================= VERSION 2: IMAGE (VERTICAL) =================
//            else if (!imageUrl.isNullOrEmpty()) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    // Step number + title
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Box(
//                            modifier = Modifier
//                                .size(28.dp)
//                                .clip(CircleShape)
//                                .background(Primary),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = stepNumber.toString(),
//                                color = White,
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 14.sp,
//                                fontFamily = figtreeFontFamily
//                            )
//                        }
//
//                        Spacer(Modifier.width(12.dp))
//
//                        Text(
//                            text = title,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Heading,
//                            fontFamily = figtreeFontFamily
//                        )
//                    }
//
//                    Spacer(Modifier.height(12.dp))
//
//                    // Image
//                    AsyncImage(
//                        model = imageUrl,
//                        contentDescription = title,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(maxHeight * 0.25f)
//                            .align(Alignment.CenterHorizontally)
//                            .clip(RoundedCornerShape(12.dp)),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    Spacer(Modifier.height(12.dp))
//
//                    // Description
//                    Text(
//                        text = description,
//                        fontSize = 14.sp,
//                        lineHeight = 20.sp,
//                        color = BodyText,
//                        fontFamily = figtreeFontFamily
//                    )
//                }
//            }
//        }
//    }
//}



package com.example.serona.ui.main.tutorial

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.serona.theme.*
import com.example.serona.ui.component.*
import com.example.serona.ui.main.favorite.FavoriteViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun TutorialDetailPage(
    tutorialId: Int,
    viewModel: TutorialDetailViewModel = hiltViewModel(),
//    favViewModel: FavoriteViewModel,
//    onClick: () -> Unit,
    onBackClick: () -> Unit
) {
//    val isFavorite = favViewModel.isFavorite(tutorial.id)
    val favViewModel: FavoriteViewModel = hiltViewModel()

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val fontSize = (maxWidth * 0.06f).value.sp
    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.0625f
    val space = maxHeight * 0.03f
    val buttonSize = maxWidth * 0.07f

    val tutorial by viewModel.tutorial.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFavorite = tutorial?.let {
        favViewModel.isFavorite(it.id)
    } ?: false

//    val isFavorite = favViewModel.isFavorite(tutorial.id)

    LaunchedEffect(tutorialId) {
        viewModel.fetchTutorialAndSteps(tutorialId)
    }

    // Loading state
    if (isLoading) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(vertical = vertiPadding, horizontal = horiPadding),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            BackButton(
                onBackClick = { onBackClick() },
                buttonSize = buttonSize,
                fontSize = fontSize
            )

            tutorial?.let { tut ->

                val favIcon: ImageVector =
                    if (isFavorite) Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder

                Icon(
                    imageVector = favIcon,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color(0xFFDC143C) else BodyText,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            favViewModel.toggleFavorite(tut)
                        }
                )
            }
        }


        Spacer(modifier = Modifier.height(space))

        // Content
        LazyColumn(
//            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(space * 0.3f)
        ) {
            // Tutorial Info
            tutorial?.let { tut ->
                // Category Tags
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CategoryChip(tut.main_category, isPrimary = true)
                        CategoryChip(tut.sub_category, isPrimary = false)
                    }
                }

                // Title
                item {
                    Text(
                        text = tut.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Heading,
                        fontFamily = figtreeFontFamily
                    )
                }

                // Description
                item {
                    Text(
                        text = tut.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BodyText,
                        lineHeight = 24.sp,
                        fontFamily = figtreeFontFamily
                    )
                }
            }

            // Steps Section
            if (steps.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Tutorial Steps",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = figtreeFontFamily,
                        color = Heading
                    )
                }

                items(steps) { step ->
                    TutorialStepCard(
                        stepNumber = step.step_number,
                        title = step.title,
                        description = step.description,
                        imageUrl = step.image_url,
                        hex = step.hex
                    )
                }
            } else {
                item {
                    Text(
                        "No content available for this tutorial.",
                        color = BodyText,
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontFamily = figtreeFontFamily
                    )
                }
            }
        }
    }
}



//        // ================= HEADER =================
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = vertiPadding),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//
//            BackButton(
//                onBackClick = onBackClick,
//                buttonSize = buttonSize,
//                fontSize = fontSize
//            )
//
//            Icon(
//                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
//                contentDescription = "Favorite",
//                tint = Color(0xFFDC143C),
//                modifier = Modifier
//                    .align(Alignment.Top)
//                    .padding(space * 0.1f)
//                    .size(24.dp)
//                    .clickable(
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() }
//                    ) {
//                        favViewModel.toggleFavorite(tutorial)
//                    }
//            )        }
//
//        // ================= CONTENT =================
//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(space * 0.3f)
//        ) {
//
//            tutorial?.let { tut ->
//                item {
//                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                        CategoryChip(tut.main_category, true)
//                        CategoryChip(tut.sub_category, false)
//                    }
//                }
//
//                item {
//                    Text(
//                        text = tut.title,
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold,
//                        color = Heading,
//                        fontFamily = figtreeFontFamily
//                    )
//                }
//
//                item {
//                    Text(
//                        text = tut.description,
//                        style = MaterialTheme.typography.bodyLarge,
//                        color = BodyText,
//                        lineHeight = 24.sp,
//                        fontFamily = figtreeFontFamily
//                    )
//                }
//            }
//
//            if (steps.isNotEmpty()) {
//                item {
//                    Text(
//                        "Tutorial Steps",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        color = Heading,
//                        fontFamily = figtreeFontFamily
//                    )
//                }
//
//                items(steps) { step ->
//                    TutorialStepCard(
//                        stepNumber = step.step_number,
//                        title = step.title,
//                        description = step.description,
//                        imageUrl = step.image_url,
//                        hex = step.hex
//                    )
//                }
//            }
//        }
//    }
//}