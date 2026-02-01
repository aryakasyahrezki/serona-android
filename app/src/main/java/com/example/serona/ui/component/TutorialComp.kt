package com.example.serona.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.serona.data.model.Tutorial
import com.example.serona.theme.*
import com.example.serona.ui.main.favorite.FavoriteViewModel

/* -----------------------------------------------------
   TUTORIAL CARD
----------------------------------------------------- */

@Composable
fun TutorialCard(
    tutorial: Tutorial,
    favViewModel: FavoriteViewModel,
    onClick: () -> Unit
) {
    val isFavorite = favViewModel.isFavorite(tutorial.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Image Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = tutorial.image_url,
                    contentDescription = tutorial.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(12.dp))

            // Content Section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Tag
                Text(
                    text = tutorial.subCategory,
                    color = White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = Color(0xFFDC143C), // Dark red/maroon color
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(Modifier.height(8.dp))

                // Title
                Text(
                    text = tutorial.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(6.dp))

                // Description
                Text(
                    text = tutorial.description,
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                Spacer(Modifier.height(8.dp))

                // "Lihat lebih lanjut" button
                Text(
                    text = "See More Detail",
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            // Favorite Icon
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite",
                tint = Primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        favViewModel.toggleFavorite(tutorial)
                    }
            )
        }
    }
}

/* -----------------------------------------------------
   LOADING VIEW
----------------------------------------------------- */

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Primary)
    }
}

/* -----------------------------------------------------
   SECTION TITLE
----------------------------------------------------- */

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = Heading,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

/* -----------------------------------------------------
   SEARCH BAR
----------------------------------------------------- */

@Composable
fun TutorialSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text(
                "Search",
                color = ParagraphLight
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = BodyText
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = Grey20,
            focusedContainerColor = White,
            unfocusedContainerColor = White
        )
    )
}

/* -----------------------------------------------------
   FILTER ROW
----------------------------------------------------- */

@Composable
fun FilterRow(
    mainCategoryOptions: List<String>,
    subCategoryOptions: Map<String, List<String>>,
    activeFilters: Set<String>,
    isFilterActive: Boolean,
    onFilterSelected: (String) -> Unit,
    onClearAllFilters: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Filters button with icon
        item {
            FilterButton(
                text = "Filters",
                isActive = isFilterActive,
                showIcon = true,
                onClick = { /* TODO: Show filter dialog */ }
            )
        }

        // Main category filters with dropdown arrow
        items(mainCategoryOptions.size) { index ->
            val category = mainCategoryOptions[index]
            FilterDropDownButton(
                categoryName = category,
                subOptions = subCategoryOptions[category] ?: emptyList(),
                activeFilters = activeFilters,
                onFilterSelected = onFilterSelected
            )
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    isActive: Boolean,
    showIcon: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isActive) Color(0xFFFFF0F5) else White
            )
            .border(
                width = 1.dp,
                color = if (isActive) Primary else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (text == "Filters" && showIcon) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = Primary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = text,
                color = if (isActive) Primary else Color(0xFF666666),
                fontSize = 14.sp,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun FilterDropDownButton(
    categoryName: String,
    subOptions: List<String>,
    activeFilters: Set<String>,
    onFilterSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    // Check if any sub-option from this category is active
    val hasActiveFilter = subOptions.any { activeFilters.contains(it) }

    Box {
        // Main category button
        Box(
            modifier = Modifier
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (hasActiveFilter) Color(0xFFFFF0F5) else White
                )
                .border(
                    width = 1.dp,
                    color = if (hasActiveFilter) Primary else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = categoryName,
                    color = if (hasActiveFilter) Primary else Color(0xFF666666),
                    fontSize = 14.sp,
                    fontWeight = if (hasActiveFilter) FontWeight.SemiBold else FontWeight.Normal
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = if (hasActiveFilter) Primary else Color(0xFF666666),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(White)
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
        ) {
            subOptions.forEach { subOption ->
                val isSelected = activeFilters.contains(subOption)

                DropdownMenuItem(
                    text = {
                        Text(
                            text = subOption,
                            color = if (isSelected) Primary else Color(0xFF333333),
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onFilterSelected(subOption) // Toggle filter
                        expanded = false // ✅ TUTUP DROPDOWN SETELAH PILIH
                    },
                    modifier = Modifier.background(
                        if (isSelected) Color(0xFFFFF0F5) else White
                    )
                )
            }
        }
    }
}

/* -----------------------------------------------------
   ACTIVE FILTER CHIP
----------------------------------------------------- */

@Composable
fun ActiveFilterChip(
    text: String,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFF0F5))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = text,
                color = Primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove filter",
                tint = Primary,
                modifier = Modifier
                    .size(14.dp)
                    .clickable(onClick = onRemove)
            )
        }
    }
}

@Composable
fun EmptyView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "No Tutorials!",
                style = MaterialTheme.typography.bodyLarge,
                color = BodyText
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Try again with other keywords",
                style = MaterialTheme.typography.bodyMedium,
                color = ParagraphLight
            )
        }
    }
}

////package com.example.serona.ui.component
////
////import androidx.compose.foundation.background
////import androidx.compose.foundation.border
////import androidx.compose.foundation.clickable
////import androidx.compose.foundation.horizontalScroll
////import androidx.compose.foundation.interaction.MutableInteractionSource
////import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.lazy.LazyRow
////import androidx.compose.foundation.rememberScrollState
////import androidx.compose.foundation.shape.RoundedCornerShape
////import androidx.compose.material.icons.Icons
////import androidx.compose.material.icons.filled.Close
////import androidx.compose.material.icons.filled.Favorite
////import androidx.compose.material.icons.filled.FavoriteBorder
////import androidx.compose.material.icons.filled.FilterList
////import androidx.compose.material.icons.filled.KeyboardArrowDown
////import androidx.compose.material.icons.filled.Search
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.draw.clip
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.layout.ContentScale
////import androidx.compose.ui.text.font.FontWeight
////import androidx.compose.ui.text.style.TextOverflow
////import androidx.compose.ui.unit.dp
////import androidx.compose.ui.unit.sp
////import coil.compose.AsyncImage
////import com.example.serona.data.model.Tutorial
////import com.example.serona.theme.*
////import com.example.serona.ui.main.favorite.FavoriteViewModel
////
////data class FilterOption<T>(
////    val label: String,
////    val value: T
////)
////
/////* -----------------------------------------------------
////   TUTORIAL CARD
////----------------------------------------------------- */
////
////@Composable
////fun TutorialCard(
////    tutorial: Tutorial,
////    favViewModel: FavoriteViewModel,
////    onClick: () -> Unit
////) {
////    val isFavorite = favViewModel.isFavorite(tutorial.id)
////
////    Card(
////        modifier = Modifier
////            .fillMaxWidth()
////            .clickable(onClick = onClick),
////        shape = RoundedCornerShape(16.dp),
////        colors = CardDefaults.cardColors(containerColor = White),
////        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
////    ) {
////        Row(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(12.dp),
////            verticalAlignment = Alignment.Top
////        ) {
////            // Image Section
////            Box(
////                modifier = Modifier
////                    .size(100.dp)
////                    .clip(RoundedCornerShape(12.dp))
////            ) {
////                AsyncImage(
////                    model = tutorial.image_url,
////                    contentDescription = tutorial.title,
////                    modifier = Modifier.fillMaxSize(),
////                    contentScale = ContentScale.Crop
////                )
////            }
////
////            Spacer(Modifier.width(12.dp))
////
////            // Content Section
////            Column(
////                modifier = Modifier
////                    .weight(1f)
////                    .fillMaxHeight()
////            ) {
////                // Tag
////                Text(
////                    text = tutorial.subCategory,
////                    color = White,
////                    fontSize = 10.sp,
////                    fontWeight = FontWeight.Bold,
////                    modifier = Modifier
////                        .background(
////                            color = Color(0xFFDC143C), // Dark red/maroon color
////                            shape = RoundedCornerShape(4.dp)
////                        )
////                        .padding(horizontal = 8.dp, vertical = 4.dp)
////                )
////
////                Spacer(Modifier.height(8.dp))
////
////                // Title
////                Text(
////                    text = tutorial.title,
////                    fontWeight = FontWeight.Bold,
////                    fontSize = 15.sp,
////                    color = Color.Black,
////                    maxLines = 2,
////                    overflow = TextOverflow.Ellipsis,
////                    lineHeight = 20.sp
////                )
////
////                Spacer(Modifier.height(6.dp))
////
////                // Description
////                Text(
////                    text = tutorial.description,
////                    fontSize = 12.sp,
////                    color = Color(0xFF666666),
////                    maxLines = 2,
////                    overflow = TextOverflow.Ellipsis,
////                    lineHeight = 16.sp
////                )
////
////                Spacer(Modifier.height(8.dp))
////
////                // "Lihat lebih lanjut" button
////                Text(
////                    text = "Lihat lebih lanjut",
////                    color = Primary,
////                    fontWeight = FontWeight.Bold,
////                    fontSize = 12.sp
////                )
////            }
////
////            Spacer(Modifier.width(8.dp))
////
////            // Favorite Icon
////            Icon(
////                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
////                contentDescription = "Favorite",
////                tint = Primary,
////                modifier = Modifier
////                    .size(24.dp)
////                    .clickable(
////                        indication = null,
////                        interactionSource = remember { MutableInteractionSource() }
////                    ) {
////                        favViewModel.toggleFavorite(tutorial)
////                    }
////            )
////        }
////    }
////}
////@Composable
////fun TutorialCard(
////    tutorial: Tutorial,
////    favViewModel: FavoriteViewModel,
////    onClick: () -> Unit
////) {
////    val isFavorite = favViewModel.isFavorite(tutorial.id)
////
////    Card(
////        modifier = Modifier
////            .fillMaxWidth()
////            .clickable(onClick = onClick),
////        shape = RoundedCornerShape(16.dp),
////        colors = CardDefaults.cardColors(containerColor = White),
////        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
////    ) {
////        Row(
////            modifier = Modifier
////                .fillMaxWidth()
////                .padding(12.dp),
////            verticalAlignment = Alignment.Top
////        ) {
////            // Thumbnail Image
////            AsyncImage(
////                model = tutorial.image_url,
////                contentDescription = tutorial.title,
////                modifier = Modifier
////                    .size(100.dp)
////                    .clip(RoundedCornerShape(12.dp)),
////                contentScale = ContentScale.Crop
////            )
////
////            // Content
////            Column(
////                modifier = Modifier
////                    .weight(1f)
////                    .padding(vertical = 4.dp)
////            ) {
////                // Category Badge
////                if (!tutorial.subCategory.isNullOrEmpty()) {
////                    Surface(
////                        shape = RoundedCornerShape(6.dp),
////                        color = PrimaryContainer,
////                        modifier = Modifier.padding(bottom = 4.dp)
////                    ) {
////                        Text(
////                            text = tutorial.subCategory!!,
////                            color = Primary,
////                            style = MaterialTheme.typography.labelSmall,
////                            fontWeight = FontWeight.Medium,
////                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
////                        )
////                    }
////                }
////
////                Spacer(Modifier.height(4.dp))
////
////                // Title
////                Text(
////                    text = tutorial.title,
////                    style = MaterialTheme.typography.titleMedium,
////                    fontWeight = FontWeight.Bold,
////                    color = Heading,
////                    maxLines = 2,
////                    overflow = TextOverflow.Ellipsis
////                )
////
////                Spacer(Modifier.height(4.dp))
////
////                // Description
////                Text(
////                    text = tutorial.description,
////                    maxLines = 2,
////                    overflow = TextOverflow.Ellipsis,
////                    style = MaterialTheme.typography.bodySmall,
////                    color = BodyText
////                )
////            }
////
////            // Favorite Button
////            IconButton(
////                onClick = { favViewModel.toggleFavorite(tutorial) },
////                modifier = Modifier.align(Alignment.Top)
////            ) {
////                Icon(
////                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
////                    contentDescription = if (isFavorite) "Hapus dari favorit" else "Tambah ke favorit",
////                    tint = Primary
////                )
////            }
////        }
////    }
////}
//

//
//
///* -----------------------------------------------------
//   SEARCH BAR
//----------------------------------------------------- */
//
//@Composable
//fun TutorialSearchBar(
//    query: String,
//    onQueryChange: (String) -> Unit
//) {
//    OutlinedTextField(
//        value = query,
//        onValueChange = onQueryChange,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        placeholder = {
//            Text(
//                "Pencarian",
//                color = ParagraphLight
//            )
//        },
//        singleLine = true,
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Search",
//                tint = BodyText
//            )
//        },
//        shape = RoundedCornerShape(12.dp),
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedBorderColor = Primary,
//            unfocusedBorderColor = Grey20,
//            focusedContainerColor = White,
//            unfocusedContainerColor = White
//        )
//    )
//}
//
///* -----------------------------------------------------
//   FILTER ROW
//----------------------------------------------------- */
//
//@Composable
//fun FilterRow(
//    mainCategoryOptions: List<String>,
//    subCategoryOptions: List<String>,
//    activeFilters: Set<String>,
//    isFilterActive: Boolean,
//    onFilterSelected: (String) -> Unit,
//    onClearAllFilters: () -> Unit
//) {
//    LazyRow(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        // Filters button with icon
//        item {
//            FilterButton(
//                text = "Filters",
//                isActive = isFilterActive,
//                showIcon = true,
//                iconOnly = false,
//                onClick = { /* TODO: Show filter dialog */ }
//            )
//        }
//
//        // Main category filters with dropdown arrow
//        mainCategoryOptions.forEach { category ->
//            item {
//                FilterButton(
//                    text = category,
//                    isActive = activeFilters.contains(category),
//                    showIcon = true,
//                    iconOnly = false,
//                    onClick = { onFilterSelected(category) }
//                )
//            }
//        }
//
//        // Sub category filters
//        subCategoryOptions.forEach { subCategory ->
//            item {
//                FilterButton(
//                    text = subCategory,
//                    isActive = activeFilters.contains(subCategory),
//                    showIcon = false,
//                    iconOnly = false,
//                    onClick = { onFilterSelected(subCategory) }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun FilterButton(
//    text: String,
//    isActive: Boolean,
//    showIcon: Boolean = false,
//    iconOnly: Boolean = false,
//    onClick: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .height(40.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .background(
//                if (isActive) Color(0xFFFFF0F5) else White
//            )
//            .border(
//                width = 1.dp,
//                color = if (isActive) Primary else Color(0xFFE0E0E0),
//                shape = RoundedCornerShape(12.dp)
//            )
//            .clickable(onClick = onClick)
//            .padding(horizontal = 16.dp, vertical = 10.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(6.dp)
//        ) {
//            // Icon for "Filters" button
//            if (text == "Filters" && showIcon) {
//                Icon(
//                    imageVector = Icons.Default.FilterList,
//                    contentDescription = "Filter",
//                    tint = Primary,
//                    modifier = Modifier.size(18.dp)
//                )
//            }
//
//            if (!iconOnly) {
//                Text(
//                    text = text,
//                    color = if (isActive) Primary else Color(0xFF666666),
//                    fontSize = 14.sp,
//                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
//                )
//            }
//
//            // Dropdown arrow for category filters
//            if (showIcon && text != "Filters") {
//                Icon(
//                    imageVector = Icons.Default.KeyboardArrowDown,
//                    contentDescription = "Dropdown",
//                    tint = if (isActive) Primary else Color(0xFF666666),
//                    modifier = Modifier.size(18.dp)
//                )
//            }
//        }
//    }
//}
////@Composable
////fun FilterRow(
////    mainCategoryOptions: List<FilterOption<String>>,
////    subCategoryOptions: Map<String, List<FilterOption<String>>>,
////    activeFilters: Set<String>,
////    isFilterActive: Boolean,
////    onFilterSelected: (String) -> Unit,
////    onClearAllFilters: () -> Unit
////) {
////    Column {
////        // Main filter buttons row
////        Row(
////            modifier = Modifier
////                .fillMaxWidth()
////                .horizontalScroll(rememberScrollState())
////                .padding(horizontal = 16.dp),
////            horizontalArrangement = Arrangement.spacedBy(8.dp)
////        ) {
////            // Filters label chip
////            AssistChip(
////                onClick = { },
////                enabled = false,
////                label = {
////                    Text(
////                        text = "Filters",
////                        color = if (isFilterActive) Primary else BodyText
////                    )
////                },
////                colors = AssistChipDefaults.assistChipColors(
////                    containerColor = if (isFilterActive) PrimaryContainer else Grey30,
////                    disabledContainerColor = if (isFilterActive) PrimaryContainer else Grey30
////                ),
////                shape = RoundedCornerShape(20.dp)
////            )
////
////            // Filter dropdowns
////            mainCategoryOptions.forEach { mainCategory ->
////                FilterDropdown(
////                    label = mainCategory.label,
////                    options = subCategoryOptions[mainCategory.value] ?: emptyList(),
////                    activeFilters = activeFilters,
////                    onSelected = onFilterSelected
////                )
////            }
////        }
////
////        // Active filters chips
////        if (isFilterActive && activeFilters.isNotEmpty()) {
////            Spacer(modifier = Modifier.height(12.dp))
////
////            Row(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .horizontalScroll(rememberScrollState())
////                    .padding(horizontal = 16.dp),
////                horizontalArrangement = Arrangement.spacedBy(8.dp)
////            ) {
////                activeFilters.forEach { filter ->
////                    ActiveFilterChip(
////                        text = filter,
////                        onRemove = { onFilterSelected(filter) }
////                    )
////                }
////
////                // Clear all button
////                TextButton(
////                    onClick = onClearAllFilters,
////                    colors = ButtonDefaults.textButtonColors(
////                        contentColor = Primary
////                    )
////                ) {
////                    Text("Clear")
////                }
////            }
////        }
////    }
////}
//
//@Composable
//fun FilterDropdown(
//    label: String,
//    options: List<FilterOption<String>>,
//    activeFilters: Set<String>,
//    onSelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val hasActiveFilter = options.any { activeFilters.contains(it.value) }
//
//    Box {
//        AssistChip(
//            onClick = { expanded = true },
//            label = {
//                Text(
//                    label,
//                    color = if (hasActiveFilter) Primary else BodyText
//                )
//            },
//            colors = AssistChipDefaults.assistChipColors(
//                containerColor = if (hasActiveFilter) PrimaryContainer else Grey30
//            ),
//            shape = RoundedCornerShape(20.dp)
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            options.forEach { option ->
//                DropdownMenuItem(
//                    text = {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(option.label)
//                            if (activeFilters.contains(option.value)) {
//                                Spacer(Modifier.width(8.dp))
//                                Icon(
//                                    imageVector = Icons.Default.Favorite,
//                                    contentDescription = null,
//                                    tint = Primary,
//                                    modifier = Modifier.size(16.dp)
//                                )
//                            }
//                        }
//                    },
//                    onClick = {
//                        onSelected(option.value)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun ActiveFilterChip(
//    text: String,
//    onRemove: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .height(32.dp)
//            .clip(RoundedCornerShape(8.dp))
//            .background(Color(0xFFFFF0F5))
//            .padding(horizontal = 10.dp, vertical = 6.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(6.dp)
//        ) {
//            Text(
//                text = text,
//                color = Primary,
//                fontSize = 12.sp,
//                fontWeight = FontWeight.Medium
//            )
//            Icon(
//                imageVector = Icons.Default.Close,
//                contentDescription = "Remove filter",
//                tint = Primary,
//                modifier = Modifier
//                    .size(14.dp)
//                    .clickable(onClick = onRemove)
//            )
//        }
//    }
//}
////@Composable
////fun ActiveFilterChip(
////    text: String,
////    onRemove: () -> Unit
////) {
////    AssistChip(
////        onClick = onRemove,
////        label = {
////            Row(
////                horizontalArrangement = Arrangement.spacedBy(6.dp),
////                verticalAlignment = Alignment.CenterVertically
////            ) {
////                Text(
////                    text = text,
////                    color = Primary,
////                    style = MaterialTheme.typography.bodySmall,
////                    fontWeight = FontWeight.Medium
////                )
////                Text(
////                    text = "×",
////                    color = Primary,
////                    style = MaterialTheme.typography.bodyMedium,
////                    fontWeight = FontWeight.Bold
////                )
////            }
////        },
////        colors = AssistChipDefaults.assistChipColors(
////            containerColor = PrimaryContainer
////        ),
////        shape = RoundedCornerShape(20.dp)
////    )
////}
//

//
////package com.example.serona.ui.component
////
////import androidx.compose.foundation.clickable
////import androidx.compose.foundation.horizontalScroll
////import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.lazy.LazyColumn
////import androidx.compose.foundation.lazy.LazyRow
////import androidx.compose.foundation.rememberScrollState
////import androidx.compose.foundation.shape.RoundedCornerShape
////import androidx.compose.material.icons.Icons
////import androidx.compose.material.icons.filled.Favorite
////import androidx.compose.material.icons.filled.FavoriteBorder
////import androidx.compose.material.icons.filled.Search
////import androidx.compose.material3.*
////import androidx.compose.runtime.*
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.draw.clip
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.unit.dp
////import coil.compose.AsyncImage
////import com.example.serona.data.model.Tutorial
////import com.example.serona.ui.main.favorite.FavoriteViewModel
////import com.example.serona.ui.component.FilterOption
////
////data class FilterOption<T>(
////    val label: String,   // buat UI
////    val value: T    // buat logic filter
////)
////
////
/////* -----------------------------------------------------
////   TUTORIAL CARD
////----------------------------------------------------- */
////
////@Composable
////fun TutorialCard(
////    tutorial: Tutorial,
////    favViewModel: FavoriteViewModel,
////    onClick: () -> Unit
////) {
////    val isFavorite = favViewModel.favoriteList.contains(tutorial)
////
////    Card(
////        modifier = Modifier
////            .fillMaxWidth()
////            .clickable(onClick = onClick),
////        shape = RoundedCornerShape(16.dp)
////    ) {
////        Row(
////            modifier = Modifier.padding(12.dp),
////            horizontalArrangement = Arrangement.spacedBy(12.dp)
////        ) {
////
////            AsyncImage(
////                model = tutorial.image_url,
////                contentDescription = null,
////                modifier = Modifier
////                    .size(100.dp)
////                    .clip(RoundedCornerShape(12.dp))
////            )
////
////            Spacer(Modifier.width(12.dp))
////
////            Column(
////                modifier = Modifier.weight(1f)
////            ) {
////                Text(
////                    text = tutorial.title,
////                    style = MaterialTheme.typography.titleMedium
////                )
////                Text(
////                    text = tutorial.description,
////                    maxLines = 2,
////                    style = MaterialTheme.typography.bodyMedium
////                )
////            }
////
////            IconButton(
////                onClick = { favViewModel.toggleFavorite(tutorial) }
////            ) {
////                Icon(
////                    imageVector = if (isFavorite)
////                        Icons.Default.Favorite
////                    else
////                        Icons.Default.FavoriteBorder,
////                    contentDescription = null,
////                    tint = Color(0xFFE91E63)
////                )
////            }
////        }
////    }
////}
////
/////* -----------------------------------------------------
////   SEARCH BAR
////----------------------------------------------------- */
////
////@Composable
////fun TutorialSearchBar(
////    query: String,
////    onQueryChange: (String) -> Unit
////) {
////    OutlinedTextField(
////        value = query,
////        onValueChange = onQueryChange,
////        modifier = Modifier
////            .fillMaxWidth()
////            .padding(horizontal = 16.dp),
////        placeholder = { Text("Search") },
////        singleLine = true,
////        leadingIcon = {
////            Icon(
////                imageVector = Icons.Default.Search,
////                contentDescription = null
////            )
////        },
////        shape = RoundedCornerShape(12.dp)
////    )
////}
////
/////* -----------------------------------------------------
////   FILTER ROW
////----------------------------------------------------- */
////@Composable
////fun FilterRow(
////    mainCategoryOptions: List<FilterOption<String>>,
////    subCategoryOptions: Map<String, List<FilterOption<String>>>,
////    activeFilters: Set<String>,
////    isFilterActive: Boolean,
////    onFilterSelected: (String) -> Unit,
////    onClearAllFilters: () -> Unit
////) {
////    Column {
////        LazyRow(
////            modifier = Modifier.padding(horizontal = 16.dp),
////            horizontalArrangement = Arrangement.spacedBy(12.dp)
////        ) {
////            items(mainCategoryOptions.size) { index ->
////                val mainCategory = mainCategoryOptions[index]
////
////                FilterDropdown(
////                    label = mainCategory.label,
////                    options = subCategoryOptions[mainCategory.value] ?: emptyList(),
//////                    selectedValue = null,
////                    onSelected = onFilterSelected
////                )
////            }
////        }
////
////        if (isFilterActive && activeFilters.isNotEmpty()) {
////            Spacer(modifier = Modifier.height(8.dp))
////
////            Row(
////                modifier = Modifier
////                    .padding(horizontal = 16.dp)
////                    .horizontalScroll(rememberScrollState()),
////                Arrangement.spacedBy(8.dp)
////            ) {
////                activeFilters.forEach { filter ->
////                    ActiveFilterChip(
////                        text = filter,
////                        onRemove = { onFilterSelected(filter) }
////                    )
////                }
////
////                TextButton(onClick = onClearAllFilters) {
////                    Text("Clear")
////                }
////            }
////        }
////    }
//////    Row(
//////        modifier = Modifier
//////            .horizontalScroll(rememberScrollState())
//////            .padding(horizontal = 16.dp),
//////        horizontalArrangement = Arrangement.spacedBy(8.dp)
//////    ) {
//////
//////        AssistChip(
//////            onClick = {},
//////            enabled = false,
//////            label = {
//////                Text(
//////                    text = "Filters",
//////                    color = if (hasActiveFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
////////                    color = if (hasActiveFilter) Color(0xFF6200EE) else Color.Gray
//////                )
//////            },
//////            colors = AssistChipDefaults.assistChipColors(
//////                containerColor = if (hasActiveFilter) Color(0xFFEDE7F6) else Color(0xFFF0F0F0)
//////            )
//////        )
//////
//////        // Main Category Dropdown
//////        FilterDropdown("Main Category", mainCategoryOptions, selectedMainCategory, onMainCategorySelected)
//////
//////        // Sub Category Dropdown
//////        val subOptions = selectedMainCategory?.let { subCategoryOptions[it] } ?: emptyList()
//////        FilterDropdown("Sub Category", subOptions, selectedSubCategory, onSubCategorySelected)
//////
//////        // Active filter tags
//////        activeFilters.forEach { value ->
//////            AssistChip(
//////                onClick = onClearFilter,
//////                label = { Text("$value ×", color = Color(0xFF6200EE)) },
//////                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFEDE7F6))
//////            )
//////        }
//////    }
////}
////
////@Composable
////fun FilterDropdown(
////    label: String,
////    options: List<FilterOption<String>>,
//////    selectedValue: String?,
////    onSelected: (String) -> Unit
////) {
////    var expanded by remember { mutableStateOf(false) }
////    Box {
////        AssistChip(
////            onClick = { expanded = true },
////            label = { Text(label) }
////        )
////
////        DropdownMenu(
////            expanded = expanded,
////            onDismissRequest = { expanded = false }
////        ) {
////            options.forEach { option ->
////                DropdownMenuItem(
////                    text = { Text(option.label) },
////                    onClick = {
////                        onSelected(option.value)
////                        expanded = false
////                    }
////                )
////            }
////        }
////    }
////}
////
////@Composable
////fun ActiveFilterChip(
////    text: String,
////    onRemove: () -> Unit
////) {
////    AssistChip(
////        onClick = onRemove,
////        label = {
////            Row(
////                horizontalArrangement = Arrangement.spacedBy(6.dp)
////            ) {
////                Text(
////                    text = text,
////                    color = Color(0xFFE91E63)
////                )
////                Text(
////                    text = "×",
////                    color = Color(0xFFE91E63)
////                )
////            }
////        },
////        colors = AssistChipDefaults.assistChipColors(
////            containerColor = Color(0xFFFFE4EC)
////        ),
////        shape = RoundedCornerShape(20.dp)
////    )
////}
