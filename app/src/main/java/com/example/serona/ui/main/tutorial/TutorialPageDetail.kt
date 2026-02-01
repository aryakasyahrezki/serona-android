package com.example.serona.ui.main.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import com.example.serona.data.model.Tutorial
import com.example.serona.theme.*
import com.example.serona.ui.main.favorite.FavoriteViewModel

@Composable
fun TutorialDetailPage(
    tutorialId: Int,
    viewModel: TutorialDetailViewModel = hiltViewModel(),
    favViewModel: FavoriteViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val tutorial by viewModel.tutorial.collectAsState()

    LaunchedEffect(tutorialId) {
        viewModel.fetchTutorial(tutorialId)
    }

    if (tutorial == null) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary)
        }
        return
    }

    val currentTutorial = tutorial!!
    val isFavorite = favViewModel.favoriteList.contains(currentTutorial)

    Column(
        Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // Header with Back Button and Title
        DetailHeader(
            tutorial = currentTutorial,
            isFavorite = isFavorite,
            onBackClicked = onBackClicked,
            onFavoriteClicked = { favViewModel.toggleFavorite(currentTutorial) }
        )

        Divider(color = Grey20, thickness = 1.dp)

        // Content Section
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Image
            item {
                if (!currentTutorial.image_url.isNullOrEmpty()) {
                    AsyncImage(
                        model = currentTutorial.image_url,
                        contentDescription = currentTutorial.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Category Tag
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!currentTutorial.mainCategory.isNullOrEmpty()) {
                        CategoryChip(currentTutorial.mainCategory!!)
                    }
                    if (!currentTutorial.subCategory.isNullOrEmpty()) {
                        CategoryChip(currentTutorial.subCategory!!, isPrimary = false)
                    }
                }
            }

            // Title
            item {
                Text(
                    text = currentTutorial.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Heading
                )
            }

            // Description
            item {
                Text(
                    text = currentTutorial.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = BodyText,
                    lineHeight = 24.sp
                )
            }

            // Tutorial Steps Section (if available)
            // This will be populated from backend TutorialStep data
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Langkah-Langkah",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Heading
                )
            }
        }
    }
}

@Composable
private fun DetailHeader(
    tutorial: Tutorial,
    isFavorite: Boolean,
    onBackClicked: () -> Unit,
    onFavoriteClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onBackClicked)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                tint = Primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Kembali",
                color = Primary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Favorite Button
        IconButton(
            onClick = onFavoriteClicked,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PrimaryContainer)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "Hapus dari favorit" else "Tambah ke favorit",
                tint = Primary
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

// Composable for tutorial steps (to be called when backend provides TutorialStep data)
@Composable
fun TutorialStepCard(
    stepNumber: Int,
    title: String,
    description: String,
    imageUrl: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Left vertical line decoration
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .width(4.dp)
                .height(200.dp)
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
                        color = Heading
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Step image
                if (!imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.FillWidth
                    )

                    Spacer(Modifier.height(12.dp))
                }

                // Step description
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BodyText,
                    lineHeight = 20.sp
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
