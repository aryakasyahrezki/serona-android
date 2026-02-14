package com.serona.app.ui.main.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.serona.app.theme.*
import com.serona.app.ui.component.*
import com.serona.app.ui.main.favorite.FavoriteViewModel

@Composable
fun TutorialDetailPage(
    tutorialId: Int,
    viewModel: TutorialDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
//    val isFavorite = favViewModel.isFavorite(tutorial.id)
    val favViewModel: FavoriteViewModel = hiltViewModel()

    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp
    val iconSize = (maxHeight * 0.03f)
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
                        .size(iconSize)
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
                        horizontalArrangement = Arrangement.spacedBy(space * 0.4f)
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
                        fontFamily = figtreeFontFamily,
                        fontSize = fontSize
                    )
                }

                // Description
                item {
                    Text(
                        text = tut.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BodyText,
                        lineHeight = fontSize,
                        fontFamily = figtreeFontFamily,
                        fontSize = fontSize* 0.6f
                    )
                }
            }

            // Steps Section
            if (steps.isNotEmpty()) {
//                item {
//                    Spacer(Modifier.height(8.dp))
//                    Text(
//                        text = "Tutorial Steps",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = figtreeFontFamily,
//                        color = Heading
//                    )
//                }

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
                        modifier = Modifier.padding(vertical = space * 0.8f),
                        fontFamily = figtreeFontFamily,
                        fontSize = fontSize * 0.6f
                    )
                }
            }
        }
    }
}