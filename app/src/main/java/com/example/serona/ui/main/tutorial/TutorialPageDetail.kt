//package com.example.serona.ui.main.tutorial
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.compose.AsyncImage
//import com.example.serona.data.model.Tutorial
//import com.example.serona.theme.*
//import com.example.serona.ui.main.favorite.FavoriteViewModel
//import com.example.serona.data.model.TutorialStep
//
//
//@Composable
//fun TutorialDetailPage(
//    tutorialId: Int,
//    viewModel: TutorialDetailViewModel = hiltViewModel(),
//    favViewModel: FavoriteViewModel = hiltViewModel(),
//    onBackClicked: () -> Unit
//) {
//    val steps by viewModel.steps.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    LaunchedEffect(tutorialId) {
//        viewModel.fetchTutorialAndSteps(tutorialId)
//    }
//
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
//    Column(Modifier.fillMaxSize()) {
//
//        // HEADER SIMPLE TANPA TUTORIAL OBJECT
//        DetailHeader(
//            tutorialTitle = "Tutorial",
//            onBackClicked = onBackClicked
//        )
//
//        LazyColumn(
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//
//            item {
//                Text(
//                    "Langkah-Langkah",
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            items(steps) { step ->
//                TutorialStepCard(
//                    stepNumber = step.step_number,
//                    title = step.title,
//                    description = step.description,
//                    imageUrl = step.image_url,
//                    hex = step.hex
//                )
//            }
//        }
//
//    }
//}
//
//@Composable
//private fun DetailHeader(
//    tutorialTitle: String,
//    onBackClicked: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.clickable { onBackClicked() }
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back",
//                tint = Primary
//            )
//
//            Spacer(Modifier.width(8.dp))
//
//            Text("Back", color = Primary)
//        }
//    }
//}
//
//
//@Composable
//private fun CategoryChip(
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
//            color = if (isPrimary) Primary else Secondary,
//            style = MaterialTheme.typography.bodySmall,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//// Composable for tutorial steps (to be called when backend provides TutorialStep data)
//@Composable
//fun TutorialStepCard(
//    stepNumber: Int,
//    title: String,
//    description: String,
//    imageUrl: String?,
//    hex: String?
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        // Left vertical line decoration
//        Box(
//            modifier = Modifier
//                .padding(top = 8.dp)
//                .width(4.dp)
//                .height(180.dp)
//                .background(Primary, RoundedCornerShape(4.dp))
//        )
//
//        Spacer(Modifier.width(12.dp))
//
//        // Step Card Content
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = White),
//            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                // Step number and title
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Box(
//                        modifier = Modifier
//                            .size(32.dp)
//                            .clip(CircleShape)
//                            .background(Primary),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = stepNumber.toString(),
//                            color = White,
//                            fontWeight = FontWeight.Bold,
//                            style = MaterialTheme.typography.bodyLarge
//                        )
//                    }
//
//                    Spacer(Modifier.width(12.dp))
//
//                    Text(
//                        text = title,
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold,
//                        color = Heading,
//                        fontSize = 16.sp
//                    )
//                }
//
//                Spacer(Modifier.height(12.dp))
//
//                // ==========================
//                // 🔥 BAGIAN DINAMIS
//                // ==========================
//
//                when {
//                    // 1. Kalau ada gambar
//                    !imageUrl.isNullOrEmpty() -> {
//                        AsyncImage(
//                            model = imageUrl,
//                            contentDescription = title,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(160.dp)
//                                .clip(RoundedCornerShape(12.dp)),
//                            contentScale = ContentScale.Crop
//                        )
//
//                        Spacer(Modifier.height(12.dp))
//                    }
//
//                    // 2. Kalau ada HEX → tampilkan circle warna
//                    !hex.isNullOrEmpty() && hex != "null" -> {
//                        Box(
//                            modifier = Modifier
//                                .size(80.dp)
//                                .clip(CircleShape)
//                                .background(Color(android.graphics.Color.parseColor(hex)))
//                        )
//
//                        Spacer(Modifier.height(12.dp))
//                    }
//                }
//
//                // Deskripsi
//                Text(
//                    text = description,
//                    fontSize = 14.sp,
//                    lineHeight = 20.sp,
//                    color = BodyText
//                )
//            }
//        }
//    }
//}


package com.example.serona.ui.main.tutorial

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.serona.theme.*

private fun parseColorSafely(hex: String?): Color? {
    if (hex.isNullOrEmpty() || hex == "null") return null

    return try {
        val colorHex = if (hex.startsWith("#")) hex else "#$hex"
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: IllegalArgumentException) {
        Log.e("TutorialStepCard", "Invalid hex color: $hex", e)
        null
    }
}

@Composable
fun TutorialDetailPage(
    tutorialId: Int,
    viewModel: TutorialDetailViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val tutorial by viewModel.tutorial.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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
        Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // Header with Back Button
        DetailHeader(
            tutorialTitle = tutorial?.title ?: "Tutorial",
            onBackClicked = onBackClicked
        )

        Divider(color = Grey20, thickness = 1.dp)

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        color = Heading
                    )
                }

                // Description
                item {
                    Text(
                        text = tut.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BodyText,
                        lineHeight = 24.sp
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
                        "No steps available for this tutorial.",
                        color = BodyText,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailHeader(
    tutorialTitle: String,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onBackClicked)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Back",
                color = Primary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    isPrimary: Boolean = true
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isPrimary) PrimaryContainer else SecondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (isPrimary) Primary else Secondary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TutorialStepCard(
    stepNumber: Int,
    title: String,
    description: String,
    imageUrl: String?,
    hex: String?
) {
    val parsedColor = remember(hex) { parseColorSafely(hex) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Left vertical line
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .width(4.dp)
                .height(180.dp)
                .background(Primary, RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.width(12.dp))

        // Step Card Content
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Step number and title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stepNumber.toString(),
                            color = White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Heading,
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Dynamic content: Image OR Color Circle
                when {
                    // Show image if available
                    !imageUrl.isNullOrEmpty() -> {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    // Show color circle if hex available
                    parsedColor != null -> {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(parsedColor)
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }

                // Description
                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = BodyText
                )
            }
        }
    }
}

//package com.example.serona.ui.main.tutorial
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.serona.data.model.Tutorial
//import com.example.serona.di.NetworkModule
//import kotlinx.coroutines.launch
//
//@Composable
//fun TutorialDetailPage(
//    tutorialId: Int,
//    viewModel: TutorialDetailViewModel = hiltViewModel(),
//    onBackClicked: () -> Unit
//) {
//    val tutorial by viewModel.tutorial.collectAsState()
//
//    LaunchedEffect(tutorialId) {
//        viewModel.fetchTutorial(tutorialId)
//    }
//
//    if (tutorial == null) {
//        Box(Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//        return
//    }
//
//    Column(Modifier.padding(16.dp)) {
//        Text(tutorial!!.title, style = MaterialTheme.typography.headlineSmall)
//        Spacer(Modifier.height(8.dp))
//        Text(tutorial!!.description)
//        Spacer(Modifier.height(16.dp))
//        Button(onClick = onBackClicked) {
//            Text("Back")
//        }
//    }
//}
