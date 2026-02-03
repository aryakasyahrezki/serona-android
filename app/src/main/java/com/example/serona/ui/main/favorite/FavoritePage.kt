package com.example.serona.ui.main.favorite

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.serona.theme.*
import com.example.serona.ui.component.BackButton
import com.example.serona.ui.component.EmptyView
import com.example.serona.ui.component.TutorialCard
import com.example.serona.ui.component.TutorialSearchBar

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
    val iconSize = (maxHeight * 0.03f)
    val upperBoxHeight = maxHeight * 0.44f
    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.0625f
    val space = maxHeight * 0.03f
    val buttonSize = maxWidth * 0.07f

    val favorites = viewModel.favoriteList
    var searchQuery by remember { mutableStateOf("") }

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

    Column(
        Modifier
            .fillMaxSize()
            .background(White)
            .padding(vertical = vertiPadding, horizontal = horiPadding)
    ) {
//
        BackButton(
            onBackClick = { onBackClick() },
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
                fontFamily = figtreeFontFamily
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "Here are the article you like!",
                style = MaterialTheme.typography.bodyMedium,
                color = BodyText,
                fontFamily = figtreeFontFamily
            )
        }

        Spacer(modifier = Modifier.height(space * 0.2f))
        // Search Bar
        TutorialSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredFavorites, key = { it.id }) { tutorial ->
                    TutorialCard(
                        tutorial = tutorial,
                        favViewModel = viewModel
                    ) {
                        onTutorialClick?.invoke(tutorial.id)
                    }
                }
            }
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
                "Tidak ada hasil",
                style = MaterialTheme.typography.titleMedium,
                color = Heading,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Coba cari dengan kata kunci lain",
                style = MaterialTheme.typography.bodyMedium,
                color = BodyText,
                fontFamily = figtreeFontFamily,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

