package com.serona.app.ui.main.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.serona.app.theme.*
import com.serona.app.ui.component.BackButton
import com.serona.app.ui.component.EmptyView
import com.serona.app.ui.component.TutorialCard
import com.serona.app.ui.component.TutorialSearchBar

@Composable
fun FavoritePage(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onTutorialClick: ((Int) -> Unit)? = null,
    onBackClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val fontSize = (maxWidth * 0.06f).value.sp
    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.0625f
    val space = maxHeight * 0.03f
    val buttonSize = maxWidth * 0.07f

    val favorites = viewModel.favoriteList
    var searchQuery by remember { mutableStateOf("") }
    var isNavigating by remember { mutableStateOf(true) }

    val safeNavigateToDetail: (Int) -> Unit = { id ->
        if (!isNavigating) {
            isNavigating = true
            if(onTutorialClick != null){
                onTutorialClick(id)
            }
        }
    }

    val safeBackClick: () -> Unit = {
        if (!isNavigating) {
            isNavigating = true
            onBackClick()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    isNavigating = false
                    viewModel.refreshFavorites()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    isNavigating = true
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Filter favorites based on search query
    val filteredFavorites = remember(favorites, searchQuery) {
        if (searchQuery.isBlank()) {
            favorites
        } else {
            favorites.filter { tutorial ->
                tutorial.title.contains(searchQuery, ignoreCase = true) ||
                        tutorial.description.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshFavorites()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(White)
                .padding(vertical = vertiPadding, horizontal = horiPadding)
        ) {
            BackButton(
                onBackClick = { safeBackClick() },
                buttonSize = buttonSize,
                fontSize = fontSize
            )
            Spacer(modifier = Modifier.height(space * 0.3f))
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = space * 0.6f)
            ) {
                Text(
                    "Favorite",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Heading,
                    fontWeight = FontWeight.Bold,
                    fontFamily = figtreeFontFamily,
                    fontSize = fontSize
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Here are the article you like!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BodyText,
                    fontFamily = figtreeFontFamily,
                    fontSize = fontSize * 0.6f
                )
            }

            Spacer(modifier = Modifier.height(space * 0.2f))
            // Search Bar
            TutorialSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                fontSize = fontSize
            )

            Spacer(modifier = Modifier.height(space * 0.5f))
            // Favorites List
            if (favorites.isEmpty()) {
                EmptyView()
            } else if (filteredFavorites.isEmpty()) {
                EmptySearchResultView()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = maxHeight * 0.12f),
                    verticalArrangement = Arrangement.spacedBy(space * 0.5f)
                ) {
                    items(filteredFavorites, key = { it.id }) { tutorial ->
                        TutorialCard(
                            tutorial = tutorial,
                            favViewModel = viewModel
                        ) {
                            safeNavigateToDetail(tutorial.id)
                        }
                    }
                }
            }
        }

        if (isNavigating) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .pointerInput(Unit) {}
            )
        }
    }

}



@Composable
fun EmptySearchResultView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                "No Tutorials!",
                style = MaterialTheme.typography.bodyLarge,
                color = BodyText,
                fontFamily = figtreeFontFamily
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Try again with other keywords",
                style = MaterialTheme.typography.bodyMedium,
                color = ParagraphLight,
                fontFamily = figtreeFontFamily
            )
        }
    }
}

