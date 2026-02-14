package com.serona.app.ui.main.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.serona.app.theme.*
import com.serona.app.ui.component.*
import com.serona.app.ui.main.favorite.FavoriteViewModel

@Composable
fun TutorialPage(
    onTutorialClick: (Int) -> Unit,
    onBackClick: () -> Unit = {},
    vm: TutorialViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val fontSize = (maxWidth * 0.06f).value.sp
    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.055f
    val space = maxHeight * 0.03f
    val buttonSize = maxWidth * 0.07f

    val favVM: FavoriteViewModel = hiltViewModel()
    var isNavigating by remember { mutableStateOf(false) }

    val safeNavigateToDetail: (Int) -> Unit = { id ->
        if (!isNavigating) {
            isNavigating = true
            onTutorialClick(id)
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
                    vm.refreshTutorials()
                    favVM.refreshFavorites()
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

    val tutorials by vm.filteredTutorials.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val isFilterActive by vm.isFilterActive.collectAsState()
    val activeFilters by vm.activeFilters.collectAsState()

    LaunchedEffect(Unit) {
        vm.refreshTutorials()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(White)
                .padding(vertical = vertiPadding, horizontal = horiPadding)
        ) {
            Spacer(modifier = Modifier.height(space * 0.25f))

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
                    "Tutorial",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Heading,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = figtreeFontFamily
                )
                Spacer(Modifier.height(space * 0.1f))
                Text(
                    "Here are the tutorials for you!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BodyText,
                    fontSize = fontSize * 0.6f,
                    fontFamily = figtreeFontFamily
                )
            }

            Spacer(modifier = Modifier.height(space * 0.2f))
            // Search Bar
            TutorialSearchBar(
                query = searchQuery,
                onQueryChange = vm::onQueryChange,
                fontSize = fontSize
            )

            Spacer(modifier = Modifier.height(space * 0.5f))

            // Filter Row
            FilterRow(
                mainCategoryOptions = vm.mainCategoryOptions,
                subCategoryOptions = vm.subCategoryOptions,
                activeFilters = activeFilters,
                isFilterActive = isFilterActive,
                onFilterSelected = vm::onFilterSelected,
                fontSize = fontSize
            )

            Spacer(modifier = Modifier.height(space * 0.6f))
            // Active Filters Section
            if (activeFilters.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(space * 1.8f)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(space * 0.2f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    activeFilters.forEach { filter ->
                        ActiveFilterChip(
                            text = filter,
                            onRemove = { vm.onFilterSelected(filter) },
                            fontSize = fontSize
                        )
                    }

                    if (activeFilters.size > 1) {
                        TextButton(
                            onClick = vm::clearAllFilters
                        ) {
                            Text(
                                "Clear All",
                                color = Primary,
                                fontSize = fontSize * 0.5f,
                                fontFamily = figtreeFontFamily,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(space * 0.6f))

            // Content Section lsg pakai tutorial dari viewmodel
            when {
                isLoading -> LoadingView()
                tutorials.isEmpty() -> EmptyView()
                else -> {
                    // Group tutorials by main category
                    val faceShapeTutorials = tutorials.filter {
                        it.main_category == "Face Shape"
                    }

                    val occasionTutorials = tutorials.filter {
                        it.main_category == "Occasion"
                    }

                    val skinToneTutorials = tutorials.filter {
                        it.main_category == "Skin Tone"
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(
//                        start = 8.dp,
//                        top = 8.dp,
//                        end = 8.dp,
                            bottom = maxHeight * 0.12f
                        ),
                        verticalArrangement = Arrangement.spacedBy(space * 0.5f)
                    ) {

                        // Face Shape Section
                        if (faceShapeTutorials.isNotEmpty()) {
                            item {
                                SectionTitle("Face Make Up Placement")
                            }

                            items(faceShapeTutorials, key = { it.id }) { tutorial ->
                                TutorialCard(tutorial, favVM) {
                                    safeNavigateToDetail(tutorial.id)
                                }
                            }
                        }

                        // Occasion Section
                        if (occasionTutorials.isNotEmpty()) {
                            item {
                                if (faceShapeTutorials.isNotEmpty()) {
                                    Spacer(Modifier.height(space))
                                }
                                SectionTitle("The Best Make Up Style For Your Occasion")
                            }

                            items(occasionTutorials, key = { it.id }) { tutorial ->
                                TutorialCard(tutorial, favVM) {
                                    safeNavigateToDetail(tutorial.id)
                                }
                            }
                        }

                        // Skin Tone Section
                        if (skinToneTutorials.isNotEmpty()) {
                            item {
                                if (faceShapeTutorials.isNotEmpty() || occasionTutorials.isNotEmpty()) {
                                    Spacer(Modifier.height(space))
                                }
                                SectionTitle("Recommended Make Up Colors")
                            }

                            items(skinToneTutorials, key = { it.id }) { tutorial ->
                                TutorialCard(tutorial, favVM) {
                                    safeNavigateToDetail(tutorial.id)
                                }
                            }
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
                    // pointerInput(Unit) {} akan memakan semua input sentuhan agar tidak tembus ke bawah
                    .pointerInput(Unit) {}
            )
        }
    }
}
