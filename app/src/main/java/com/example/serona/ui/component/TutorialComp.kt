package com.example.serona.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.serona.data.model.Tutorial
import com.example.serona.theme.*
import com.example.serona.ui.main.favorite.FavoriteViewModel
//import com.example.serona.ui.main.tutorial.parseColorSafely

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
    val configuration = LocalConfiguration.current
    val maxWidth = configuration.screenWidthDp.dp
    val maxHeight = configuration.screenHeightDp.dp

    val fontSize = (maxWidth * 0.06f).value.sp
    val iconSize = (maxHeight * 0.03f)
    val upperBoxHeight = maxHeight * 0.44f
    val horiPadding = maxWidth * 0.05f
    val vertiPadding = maxHeight * 0.055f
    val space = maxHeight * 0.03f
    val buttonSize = maxWidth * 0.07f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Grey30)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space * 0.05f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(space * 0.2f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image Section
                Box(
                    modifier = Modifier
//                        .size(100.dp)
                        .width(maxWidth * 0.18f)
                        .height(maxHeight * 0.11f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = tutorial.image_url,
                        contentDescription = tutorial.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.width(space * 0.3f))

                // Content Section
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    // Tag
                    Text(
                        text = tutorial.sub_category,
                        color = White,
                        fontSize = fontSize * 0.4f,
                        fontWeight = FontWeight.Bold,
                        fontFamily = figtreeFontFamily,
                        lineHeight = fontSize * 0.5f,
                        modifier = Modifier
                            .background(
                                color = Color(0xFFDC143C), // Dark red/maroon color
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = space * 0.3f)
                    )

                    Spacer(Modifier.height(space * 0.2f))

                    // Title
                    Text(
                        text = tutorial.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = fontSize * 0.6f,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = fontSize * 0.6f,
                        fontFamily = figtreeFontFamily
                    )

                    Spacer(Modifier.height(space * 0.1f))

                    // Description
                    Text(
                        text = tutorial.description,
                        fontSize = fontSize * 0.5f,
                        fontFamily = figtreeFontFamily,
                        color = Color(0xFF666666),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = fontSize * 0.6f
                    )

                    Spacer(Modifier.height(space * 0.1f))

                    Text(
                        text = "Read more",
                        color = Color(0xFFDC143C),
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSize * 0.5f,
                        fontFamily = figtreeFontFamily
                    )
                }

                Spacer(Modifier.width(space * 0.05f))

                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color(0xFFDC143C),
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(space * 0.1f)
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
        fontWeight = FontWeight.SemiBold,
        fontFamily = figtreeFontFamily,
    )
}

/* -----------------------------------------------------
   SEARCH BAR
----------------------------------------------------- */

@Composable
fun TutorialSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    fontSize: TextUnit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = {
            Text(
                "Search",
                color = ParagraphLight,
                fontFamily = figtreeFontFamily,
                fontSize = fontSize * 0.8f
            )
        },

        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = fontSize * 0.5f,
            fontFamily = figtreeFontFamily,
            fontWeight = FontWeight.Medium,
            color = MutedLight
        ),

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
    fontSize: TextUnit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
//            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Filters button with icon
        item {
            FilterButton(
                text = "Filters",
                isActive = isFilterActive,
                showIcon = true,
                onClick = { /* TODO: Show filter dialog */ },
                fontSize = fontSize
            )
        }

        // Main category filters with dropdown arrow
        items(mainCategoryOptions.size) { index ->
            val category = mainCategoryOptions[index]
            FilterDropDownButton(
                categoryName = category,
                subOptions = subCategoryOptions[category] ?: emptyList(),
                activeFilters = activeFilters,
                onFilterSelected = onFilterSelected,
                fontSize = fontSize
            )
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    isActive: Boolean,
    showIcon: Boolean = false,
    onClick: () -> Unit,
    fontSize: TextUnit
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
                color = if (isActive) Primary50 else Color(0xFFE0E0E0),
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
                    modifier = Modifier.size(18.dp),
                )
            }

            Text(
                text = text,
                color = if (isActive) Primary else Color(0xFF666666),
                fontSize = fontSize * 0.6f,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                fontFamily = figtreeFontFamily
            )
        }
    }
}

@Composable
fun FilterDropDownButton(
    categoryName: String,
    subOptions: List<String>,
    activeFilters: Set<String>,
    onFilterSelected: (String) -> Unit,
    fontSize: TextUnit
) {

    var expanded by remember { mutableStateOf(false) }
    var buttonWidth by remember { mutableStateOf(0) }
    val density = LocalDensity.current

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
                .onSizeChanged() {
                    // 2. Tangkap lebar tombol saat ukurannya berubah
                    buttonWidth = it.width
                }
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
                    fontSize = fontSize * 0.6f,
                    fontWeight = if (hasActiveFilter) FontWeight.SemiBold else FontWeight.Normal,
                    fontFamily = figtreeFontFamily
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
                .width(with(density) { buttonWidth.toDp() })
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
                            fontSize = fontSize * 0.6f,
                            fontFamily = figtreeFontFamily,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onFilterSelected(subOption) // Toggle filter
                        expanded = false
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
    onRemove: () -> Unit,
    fontSize: TextUnit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFF0F5))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = text,
                color = Primary,
                fontSize = fontSize * 0.55f,
                fontWeight = FontWeight.Medium,
                fontFamily = figtreeFontFamily
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove filter",
                tint = Primary,
                modifier = Modifier
                    .size(16.dp)
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


@Composable
fun CategoryChip(
    text: String,
    isPrimary: Boolean = true
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isPrimary) PrimaryContainer else SecondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = if (isPrimary) Primary else Tertiary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            fontFamily = figtreeFontFamily
        )
    }
}



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
fun TutorialStepCard(
    stepNumber: Int,
    title: String,
    description: String,
    imageUrl: String?,
    hex: String?
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
    val parsedColor = remember(hex) { parseColorSafely(hex) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = space * 0.2f)
    ) {

        Spacer(modifier = Modifier.height(space * 0.5f))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .width(14.dp)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Primary.copy(alpha = 0.6f),
                                    Primary.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                bottomStart = 16.dp
                            )
                        )
                )

                if (parsedColor != null && imageUrl.isNullOrEmpty()) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Color circle + step badge
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(parsedColor)
                            )
                        }

                        Spacer(Modifier.width(space * 0.3f))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = title,
                                fontSize = fontSize * 0.6f,
                                fontWeight = FontWeight.SemiBold,
                                color = Heading,
                                fontFamily = figtreeFontFamily
                            )

                            Text(
                                text = description,
                                fontSize = fontSize * 0.6f,
                                lineHeight = fontSize * 0.5f,
                                color = BodyText,
                                fontFamily = figtreeFontFamily
                            )
                        }
                    }
                }

                else if (!imageUrl.isNullOrEmpty()) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(space * 0.5f)
                    ) {

                        // Step number + title
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stepNumber.toString(),
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    fontFamily = figtreeFontFamily,
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Heading,
                                fontFamily = figtreeFontFamily
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Image
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = title,
                            modifier = Modifier
                                .width(maxWidth * 0.7f)
                                .height(maxHeight * 0.3f)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.height(12.dp))

                        // Description
                        Text(
                            text = description,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            color = Heading,
                            fontFamily = figtreeFontFamily
                        )
                    }
                }

            }
        }
    }
}
