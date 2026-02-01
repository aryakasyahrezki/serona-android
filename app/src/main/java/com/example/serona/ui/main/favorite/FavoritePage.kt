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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.serona.theme.*
import com.example.serona.ui.component.EmptyView
import com.example.serona.ui.component.TutorialCard
import com.example.serona.ui.component.TutorialSearchBar

@Composable
fun FavoritePage(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onTutorialClick: ((Int) -> Unit)? = null,
    onBackClicked: (() -> Unit)? = null
) {
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

    Column(
        Modifier
            .fillMaxSize()
            .background(White)
    ) {
        // Back Button - Pink circular button with "Kembali" text
        if (onBackClicked != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onBackClicked)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Back",
                        color = Primary,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                "Favorite",
                style = MaterialTheme.typography.headlineSmall,
                color = Heading,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "Here are the article you like!",
                style = MaterialTheme.typography.bodyMedium,
                color = BodyText
            )
        }

        Spacer(Modifier.height(8.dp))

        // Search Bar
        TutorialSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(Modifier.height(16.dp))

        // Favorites List
        if (favorites.isEmpty()) {
            EmptyView()
        } else if (filteredFavorites.isEmpty()) {
            EmptySearchResultView()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

//@Composable
//fun EmptyFavoriteView() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.padding(32.dp)
//        ) {
//            Text(
//                "Belum ada tutorial favorit",
//                style = MaterialTheme.typography.titleMedium,
//                color = Heading,
//                fontWeight = FontWeight.SemiBold
//            )
//            Spacer(Modifier.height(8.dp))
//            Text(
//                "Yuk, temukan tutorial favoritmu!",
//                style = MaterialTheme.typography.bodyMedium,
//                color = BodyText,
//                textAlign = androidx.compose.ui.text.style.TextAlign.Center
//            )
//        }
//    }
//}

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
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

//package com.example.serona.ui.main.favorite
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.serona.theme.*
//import com.example.serona.ui.component.TutorialCard
//import androidx.navigation.compose.rememberNavController
//
//@Composable
//fun FavoritePage(
//    viewModel: FavoriteViewModel = hiltViewModel(),
//    onTutorialClick: ((Int) -> Unit)? = null
//) {
//    val favorites = viewModel.favoriteList
//
//    Column(
//        Modifier
//            .fillMaxSize()
//            .background(White)
//    ) {
//        // Header Section
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 16.dp)
//        ) {
//            Text(
//                "Favorit",
//                style = MaterialTheme.typography.headlineSmall,
//                color = Heading
//            )
//            Spacer(Modifier.height(4.dp))
//            Text(
//                "Tutorial yang kamu simpan",
//                style = MaterialTheme.typography.bodyMedium,
//                color = BodyText
//            )
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        // Content Section
//        if (favorites.isEmpty()) {
//            EmptyFavoriteView()
//        } else {
//            LazyColumn(
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//                verticalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                items(favorites, key = { it.id }) { tutorial ->
//                    TutorialCard(
//                        tutorial = tutorial,
//                        favViewModel = viewModel
//                    ) {
//                        onTutorialClick?.invoke(tutorial.id)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EmptyFavoriteView() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.padding(32.dp)
//        ) {
//            Text(
//                "Belum ada tutorial favorit",
//                style = MaterialTheme.typography.titleMedium,
//                color = Heading
//            )
//            Spacer(Modifier.height(8.dp))
//            Text(
//                "Tap ikon ❤️ pada tutorial untuk menambahkannya ke favorit",
//                style = MaterialTheme.typography.bodyMedium,
//                color = BodyText,
//                textAlign = androidx.compose.ui.text.style.TextAlign.Center
//            )
//        }
//    }
//}

//package com.example.serona.ui.main.favorite
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.serona.ui.component.TutorialCard
//import com.example.serona.ui.main.favorite.FavoriteViewModel
//
//@Composable
//fun FavoritePage() {
//
//    val favViewModel: FavoriteViewModel = viewModel()
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        items(favViewModel.favoriteList) {
//            TutorialCard(
//                tutorial = it,
//                favViewModel = favViewModel,
//                onClick = {}
//            )
//        }
//    }
//}
