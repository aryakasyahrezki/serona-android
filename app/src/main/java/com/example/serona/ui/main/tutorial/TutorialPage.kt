package com.example.serona.ui.main.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.serona.theme.*
import com.example.serona.ui.component.*
import com.example.serona.ui.main.favorite.FavoriteViewModel

@Composable
fun TutorialPage(
    onTutorialClick: (Int) -> Unit,
    onBackClicked: (() -> Unit)? = null
) {
    val vm: TutorialViewModel = hiltViewModel()
    val favVM: FavoriteViewModel = hiltViewModel()

    val tutorials by vm.filteredTutorials.collectAsState()
    val query by vm.query.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val isFilterActive by vm.isFilterActive.collectAsState()
    val activeFilters by vm.activeFilters.collectAsState()

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
                "Tutorial",
                style = MaterialTheme.typography.headlineSmall,
                color = Heading,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "Ini dia kumpulan tutorial untukmu!",
                style = MaterialTheme.typography.bodyMedium,
                color = BodyText
            )
        }

        Spacer(Modifier.height(8.dp))

        // Search Bar
        TutorialSearchBar(query, vm::onQueryChange)

        Spacer(Modifier.height(12.dp))

        // Filter Row
        FilterRow(
            mainCategoryOptions = vm.mainCategoryOptions,
            subCategoryOptions = vm.subCategoryOptions,
            activeFilters = activeFilters,
            isFilterActive = isFilterActive,
            onFilterSelected = vm::onFilterSelected,
            onClearAllFilters = vm::clearAllFilters
        )

        Spacer(Modifier.height(16.dp))
        // Active Filters Section
        if (activeFilters.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activeFilters.forEach { filter ->
                    ActiveFilterChip(
                        text = filter,
                        onRemove = { vm.onFilterSelected(filter) }
                    )
                }

                if (activeFilters.size > 1) {
                    TextButton(
                        onClick = vm::clearAllFilters,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            "Clear All",
                            color = Primary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Content Section
        when {
            isLoading -> LoadingView()
            tutorials.isEmpty() -> EmptyView()
            else -> {
                // Group tutorials by main category
                val faceShapeTutorials = tutorials.filter {
                    it.mainCategory == "Face Shape"
                }

                val occasionTutorials = tutorials.filter {
                    it.mainCategory == "Occasion"
                }

                val skinToneTutorials = tutorials.filter {
                    it.mainCategory == "Skin Tone"
                }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Face Shape Section
                    if (faceShapeTutorials.isNotEmpty()) {
                        item {
                            SectionTitle("Make Up Placement")
                        }

                        items(faceShapeTutorials, key = { it.id }) { tutorial ->
                            TutorialCard(tutorial, favVM) {
                                onTutorialClick(tutorial.id)
                            }
                        }
                    }

                    // Occasion Section
                    if (occasionTutorials.isNotEmpty()) {
                        item {
                            if (faceShapeTutorials.isNotEmpty()) {
                                Spacer(Modifier.height(16.dp))
                            }
                            SectionTitle("Make Up Style For Your Occasion")
                        }

                        items(occasionTutorials, key = { it.id }) { tutorial ->
                            TutorialCard(tutorial, favVM) {
                                onTutorialClick(tutorial.id)
                            }
                        }
                    }

                    // Skin Tone Section
                    if (skinToneTutorials.isNotEmpty()) {
                        item {
                            if (faceShapeTutorials.isNotEmpty() || occasionTutorials.isNotEmpty()) {
                                Spacer(Modifier.height(16.dp))
                            }
                            SectionTitle("Blush and Lipstick Shade Recommendation")
                        }

                        items(skinToneTutorials, key = { it.id }) { tutorial ->
                            TutorialCard(tutorial, favVM) {
                                onTutorialClick(tutorial.id)
                            }
                        }
                    }
                }
            }
        }
    }
}



//package com.example.serona.ui.main.tutorial
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.serona.ui.component.*
//import com.example.serona.ui.main.favorite.FavoriteViewModel
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.collectAsState
//
//@Composable
//fun TutorialPage(onTutorialClick: (Int) -> Unit) {
//
//    val vm: TutorialViewModel = hiltViewModel()
//    val favVM: FavoriteViewModel = hiltViewModel()
//
//    val tutorials by vm.filteredTutorials.collectAsState()
//    val query by vm.query.collectAsState()
//    val isLoading by vm.isLoading.collectAsState()
//    val isFilterActive by vm.isFilterActive.collectAsState()
//    val activeFilters by vm.activeFilters.collectAsState(initial = emptySet())
//    val selectedMainCategory by vm.mainCategory.collectAsState()
//    val selectedSubCategory by vm.subCategory.collectAsState()
//
//
//    Column(Modifier
//        .fillMaxSize()
//        .background(MaterialTheme.colorScheme.surfaceVariant)
//    ) {
//
//        Text(
//            "Tutorial",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.padding(16.dp)
//        )
//
//        Text(
//            "Ini dia kumpulan tutorial untukmu!",
//            modifier = Modifier.padding(horizontal = 16.dp)
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        TutorialSearchBar(query, vm::onQueryChange)
//
//        Spacer(Modifier.height(12.dp))
//
//        FilterRow(
//            // Berikan semua opsi yang ada di ViewModel
//            mainCategoryOptions = vm.mainCategoryOptions,
//            subCategoryOptions = vm.subCategoryOptions,
//
//            // Berikan Set filter yang sedang aktif
//            activeFilters = activeFilters,
//            isFilterActive = isFilterActive,
//
//            // Teruskan fungsi-fungsi dari ViewModel
//            onFilterSelected = vm::onFilterSelected,
//            onClearAllFilters = vm::clearAllFilters
//        )
//
//        if (activeFilters.isNotEmpty()) {
//            Spacer(Modifier.height(8.dp))
//
//            LazyRow(
//                contentPadding = PaddingValues(horizontal = 16.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(activeFilters.toList()) { filter ->
//                    ActiveFilterChip(
//                        text = filter,
//                        onRemove = { vm.onFilterSelected(filter) }
//                    )
//                }
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        when {
//            isLoading -> LoadingView()
//            tutorials.isEmpty() -> EmptyView()
//            else -> {
//
//                // 🔥 GROUP tutorial berdasarkan kategori utama
//                val faceShapeTutorials = tutorials.filter {
//                    it.mainCategory == "Face Shape"
//                }
//
//                val occasionTutorials = tutorials.filter {
//                    it.mainCategory == "Occasion"
//                }
//
//                val skinToneTutorials = tutorials.filter {
//                    it.mainCategory == "Skintone"
//                }
//
//                LazyColumn(
//                    contentPadding = PaddingValues(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//
//                    // ===============================
//                    // Penempatan Rias Wajah
//                    // ===============================
//                    if (faceShapeTutorials.isNotEmpty()) {
//                        item {
//                            SectionTitle("Penempatan Rias Wajah")
//                        }
//
//                        items(faceShapeTutorials, key = { it.id }) { tutorial ->
//                            TutorialCard(tutorial, favVM) {
//                                onTutorialClick(tutorial.id)
//                            }
//                        }
//                    }
//
//                    // ===============================
//                    // Makeup Terbaik untuk Acaramu
//                    // ===============================
//                    if (occasionTutorials.isNotEmpty()) {
//                        item {
//                            Spacer(Modifier.height(12.dp))
//                            SectionTitle("Makeup Terbaik untuk Acaramu")
//                        }
//
//                        items(occasionTutorials, key = { it.id }) { tutorial ->
//                            TutorialCard(tutorial, favVM) {
//                                onTutorialClick(tutorial.id)
//                            }
//                        }
//                    }
//
//                    // ===============================
//                    // Rekomendasi Warna Riasan
//                    // ===============================
//                    if (skinToneTutorials.isNotEmpty()) {
//                        item {
//                            Spacer(Modifier.height(12.dp))
//                            SectionTitle("Rekomendasi Warna Riasan")
//                        }
//
//                        items(skinToneTutorials, key = { it.id }) { tutorial ->
//                            TutorialCard(tutorial, favVM) {
//                                onTutorialClick(tutorial.id)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun LoadingView() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = androidx.compose.ui.Alignment.Center
//    ) {
//        CircularProgressIndicator()
//    }
//}
//
//@Composable
//fun EmptyView() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = androidx.compose.ui.Alignment.Center
//    ) {
//        Text("Belum ada tutorial")
//    }
//}
//
//@Composable
//fun SectionTitle(title: String) {
//    Text(
//        text = title,
//        style = MaterialTheme.typography.titleMedium,
//        modifier = Modifier.padding(bottom = 8.dp)
//    )
//}


//@Composable
//fun TutorialPage(onTutorialClick: (Int) -> Unit) {
//
//    val tutorialVM: TutorialViewModel = hiltViewModel()
//    val favVM: FavoriteViewModel = hiltViewModel()
//
//    val tutorials by tutorialVM.filteredTutorials.collectAsState()
//    val isLoading by tutorialVM.isLoading.collectAsState()
//    val query by tutorialVM.query.collectAsState()
//    val hasActiveFilter by tutorialVM.isFilterActive.collectAsState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(top = 16.dp)
//    ) {
//
//        // 🔹 HEADER
//        Text(
//            text = "Tutorial",
//            style = MaterialTheme.typography.headlineSmall,
//            modifier = Modifier.padding(horizontal = 16.dp)
//        )
//
//        Text(
//            text = "Ini dia kumpulan tutorial untukmu!",
//            style = MaterialTheme.typography.bodyMedium,
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        // 🔹 SEARCH BAR
//        TutorialSearchBar(
//            query = query,
//            onQueryChange = tutorialVM::onQueryChange
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        // 🔹 FILTER (placeholder dulu)
//        FilterRow(
//            hasActiveFilter = hasActiveFilter,
//            onMainCategoryClick = {
//                category -> tutorialVM.setMainCategory(category)
//            },
//            onClearFilter = {tutorialVM.clearFilter()}
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        when {
//            isLoading -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//
//            tutorials.isEmpty() -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Belum ada tutorial")
//                }
//            }
//
//            else -> {
//                LazyColumn(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.spacedBy(12.dp),
//                    contentPadding = PaddingValues(
//                        start = 16.dp,
//                        end = 16.dp,
//                        bottom = 16.dp
//                    )
//                ) {
//                    item {
//                        Text(
//                            text = "Penempatan Rias Wajah",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//                    }
//                    items(tutorials, key = { it.id }) {
//                        TutorialCard(
//                            tutorial = it,
//                            favViewModel = favVM,
//                            onClick = { onTutorialClick(it.id) }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

//package com.example.serona.ui.main
////@file:OptIn(ExperimentalMaterial3Api::class)
//
//import android.util.Log
//import com.example.serona.R
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.combinedClickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.foundation.combinedClickable
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//
//
//// Kategori utama untuk membedakan jenis tutorial
//enum class MainCategory {
//    FACE_SHAPE, EVENT, SKIN_TONE
//}
//
//// Sub-kategori untuk filtering yang lebih spesifik
//enum class SubCategory {
//    // Face Shapes
//    OVAL, SQUARE, HEART, RECTANGLE, ROUND,
//    // Events
//    OFFICE, PARTY, FESTIVAL, CASUAL, WEDDING,
//    // Skin Tones
//    FAIRLIGHT, MEDIUMTAN, DEEP
//}
//
//data class TutorialItem(
//    val id: Int,
//    val title: String,
//    val description: String,
//    val imageUrl: String,
//    val mainCategory: MainCategory,
//    val subCategory: SubCategory,
//    val tag: String
//)
//
//sealed interface TutorialDetail {
//    data class FaceShapeDetail(
//        val steps: List<StepItem>
//    ) : TutorialDetail
//
//    data class SkinToneDetail(
//        val title: String,
//        val topsub: String,
//        val bottomsub: String,
//        val shades: List<ShadeItem>
//    ) : TutorialDetail
//
//    data class EventDetail(
//        val steps: List<StepItem>
//    ) : TutorialDetail
//}
//
////data class ShadeGroup(
////    val groupTitle: String, // e.g., "Untuk Fair-Light Skintone"
////    val subtitle: String,
////    val shades: List<ShadeItem> // Daftar shade di dalam grup ini
////)
//data class ShadeItem(
//    val name: String,
//    val description: String,
//    val color: Color, // Menggunakan objek Color dari Compose
//)
//data class StepItem(
//    val stepNum: Int,
//    val title: String,
//    val description: String,
//    val image: String
//)
//
//// --- 2. DATA DUMMY: Kumpulan data tutorial sebagai contoh ---
//
//fun getDummyTutorials(): List<TutorialItem> {
//    return listOf(
//        // Penempatan Rias Wajah
//        TutorialItem(1, "Oval Face Makeup Placement", "Panduan ini dibuat untuk membantu Anda menempatkan riasan agar sesuai dengan bentuk wajah Anda.", imageUrl = "https://res.cloudinary.com/dpo7ufrlc/image/upload/v1767931842/c1917a95-38b3-4d77-a32b-8b91ae19679a.png", MainCategory.FACE_SHAPE, SubCategory.OVAL, "Lonjong"),
//        TutorialItem(2, "Square Face Makeup Placement", "Panduan ini dibuat untuk membantu Anda menempatkan riasan agar sesuai dengan bentuk wajah Anda.", imageUrl = "https://res.cloudinary.com/dpo7ufrlc/image/upload/v1767931842/c1917a95-38b3-4d77-a32b-8b91ae19679a.png", MainCategory.FACE_SHAPE, SubCategory.SQUARE, "Persegi"),
//        TutorialItem(3, "Heart Face Makeup Placement", "Panduan ini dibuat untuk membantu Anda menempatkan riasan agar sesuai dengan bentuk wajah Anda.", imageUrl = "https://res.cloudinary.com/dpo7ufrlc/image/upload/v1767931842/c1917a95-38b3-4d77-a32b-8b91ae19679a.png", MainCategory.FACE_SHAPE, SubCategory.HEART, "Hati"),
//        TutorialItem(4, "Rectangle Face Makeup Placement", "Panduan ini dibuat untuk membantu Anda menempatkan riasan agar sesuai dengan bentuk wajah Anda.", "https://res.cloudinary.com/dpo7ufrlc/image/upload/v1767931842/c1917a95-38b3-4d77-a32b-8b91ae19679a.png", MainCategory.FACE_SHAPE, SubCategory.RECTANGLE, "Persegi Panjang"),
//        TutorialItem(5, "Round Face Makeup Placement", "Panduan ini dibuat untuk membantu Anda menempatkan riasan agar sesuai dengan bentuk wajah Anda.", "https://res.cloudinary.com/dpo7ufrlc/image/upload/v1767931842/c1917a95-38b3-4d77-a32b-8b91ae19679a.png", MainCategory.FACE_SHAPE, SubCategory.ROUND, "Bulat"),
//
//        // Makeup Terbaik untuk Acaramu
//        TutorialItem(6, "Soft Western Makeup Style", "Tampilan rias pengantin ini mengutamakan kulit yang tampak bersih, halus, dan bercahaya alami.", "https://res.cloudinary.com/dpo7ufrlc/image/upload/v1767931842/c1917a95-38b3-4d77-a32b-8b91ae19679a.png", MainCategory.EVENT, SubCategory.WEDDING, "Pernikahan"),
//        TutorialItem(7, "Old Money Makeup Style", "Tampilan rias pengantin ini mengutamakan kulit yang tampak bersih, halus, dan bercahaya alami.", "https://i.pinimg.com/564x/44/e9/79/44e979e2a7de365a6f2062f43475f922.jpg", MainCategory.EVENT, SubCategory.WEDDING, "Pernikahan"),
//
//        TutorialItem(8, "Korean Natural Makeup Style", "Tampilan rias ini menonjolkan kesan segar, bersih, dan effortless dengan fokus pada kulit yang tampak sehat.", "https://i.pinimg.com/564x/22/41/54/2241542f5399a933f3889c02555d8f63.jpg", MainCategory.EVENT, SubCategory.CASUAL, "Santai"),
//        TutorialItem(9, "Igari Makeup Style", "Tampilan rias ini menonjolkan kesan segar, bersih, dan effortless dengan fokus pada kulit yang tampak sehat.", "https://i.pinimg.com/564x/22/41/54/2241542f5399a933f3889c02555d8f63.jpg", MainCategory.EVENT, SubCategory.CASUAL, "Santai"),
//
//        TutorialItem(10, "Clean Girl Makeup Style", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://i.pinimg.com/564x/11/4a/0b/114a0b2d355b85093e0b86a344933a36.jpg", MainCategory.EVENT, SubCategory.OFFICE, "Kantor"),
//        TutorialItem(11, "Latte Makeup Style", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://i.pinimg.com/564x/11/4a/0b/114a0b2d355b85093e0b86a344933a36.jpg", MainCategory.EVENT, SubCategory.OFFICE, "Kantor"),
//
//        TutorialItem(12, "Euphoria Makeup Style", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://i.pinimg.com/564x/11/4a/0b/114a0b2d355b85093e0b86a344933a36.jpg", MainCategory.EVENT, SubCategory.FESTIVAL, "Festival"),
//        TutorialItem(13, "E-girl Makeup Style", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://i.pinimg.com/564x/11/4a/0b/114a0b2d355b85093e0b86a344933a36.jpg", MainCategory.EVENT, SubCategory.FESTIVAL, "Festival"),
//
//        TutorialItem(14, "Western Glam Makeup Style", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://i.pinimg.com/564x/11/4a/0b/114a0b2d355b85093e0b86a344933a36.jpg", MainCategory.EVENT, SubCategory.PARTY, "Pesta"),
//        TutorialItem(15, "Douyin Glam Makeup Style", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://i.pinimg.com/564x/11/4a/0b/114a0b2d355b85093e0b86a344933a36.jpg", MainCategory.EVENT, SubCategory.PARTY, "Pesta"),
//
//        // Rekomendasi Warna
//        TutorialItem(16, "Shade Lipstick for Fair to Light Skintone", "Ini adalah panduan untuk menemukan warna lipstik yang paling cocok untukmu.", "https://i.pinimg.com/564x/5a/f1/a8/5af1a85041a76295b9e0f6b7c12643a6.jpg", MainCategory.SKIN_TONE, SubCategory.FAIRLIGHT, "Fair-Light Skintone"),
//        TutorialItem(17, "Shade Lipstick for Medium Tan Skintone", "Ini adalah panduan untuk menemukan warna lipstik yang paling cocok untukmu.", "https://i.pinimg.com/564x/5a/f1/a8/5af1a85041a76295b9e0f6b7c12643a6.jpg", MainCategory.SKIN_TONE, SubCategory.MEDIUMTAN, "Medium-Tan Skintone"),
//        TutorialItem(18, "Shade Lipstick for Deep Skintone", "Ini adalah panduan untuk menemukan warna lipstik yang paling cocok untukmu.", "https://i.pinimg.com/564x/5a/f1/a8/5af1a85041a76295b9e0f6b7c12643a6.jpg", MainCategory.SKIN_TONE, SubCategory.DEEP, "Deep Skintone"),
//
//        TutorialItem(19, "Shade Foundation for Fair to Light Skintone", "Tampilan rias ini menonjolkan kesan segar, bersih, dan effortless dengan fokus pada kulit yang tampak sehat.", "https://i.pinimg.com/564x/0f/f8/e3/0ff8e364a93c7ac9986333333793f773.jpg", MainCategory.SKIN_TONE, SubCategory.FAIRLIGHT, "Fair-Light Skintone"),
//        TutorialItem(20, "Shade Foundation for Medium Tan Skintone", "Tampilan rias ini menonjolkan kesan segar, bersih, dan effortless dengan fokus pada kulit yang tampak sehat.", "https://i.pinimg.com/564x/0f/f8/e3/0ff8e364a93c7ac9986333333793f773.jpg", MainCategory.SKIN_TONE, SubCategory.MEDIUMTAN, "Medium-Tan Skintone"),
//        TutorialItem(21, "Shade Foundation for Deep Skintone", "Tampilan rias ini menonjolkan kesan segar, bersih, dan effortless dengan fokus pada kulit yang tampak sehat.", "https://i.pinimg.com/564x/0f/f8/e3/0ff8e364a93c7ac9986333333793f773.jpg", MainCategory.SKIN_TONE, SubCategory.DEEP, "Deep Skintone"),
//
//        TutorialItem(22, "Shade Blush Terbaik untuk Fair to Light Skintone", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://res.cloudinary.com/dpo7ufrlc/image/upload/v1767930483/shade_blush_fair_preview_papmgq.jpg", MainCategory.SKIN_TONE, SubCategory.FAIRLIGHT, "Fair-Light Skintone"),
//        TutorialItem(23, "Shade Blush Terbaik untuk Medium Tan Skintone", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://i.pinimg.com/564x/6c/1c/b5/6c1cb50a7c493c0411d04427b3de62ab.jpg", MainCategory.SKIN_TONE, SubCategory.MEDIUMTAN, "Medium-Tan Skintone"),
//        TutorialItem(24, "Shade Blush Terbaik untuk Deep Skintone", "Tampilan rias ini menghadirkan kesan glamor yang lembut dengan kulit halus dan bercahaya alami.", "https://i.pinimg.com/564x/6c/1c/b5/6c1cb50a7c493c0411d04427b3de62ab.jpg", MainCategory.SKIN_TONE, SubCategory.DEEP, "Deep Skintone")
//    )
//}
//
//fun getDummyDetailData(): Map<Int, TutorialDetail>{
//    return mapOf(
//        1 to TutorialDetail.FaceShapeDetail(
//            steps = listOf(
//                StepItem(1, "Cara Mengaplikasikan Contour Wajah Oval", "Deskripsi", "url"),
//                StepItem(2, "Cara Mengaplikasikan Blush Wajah Oval", "Deskripsi", "url"),
//                StepItem(3, "Cara Mengaplikasikan Highlight Wajah Oval","Deskripsi", "url"),
//            )
//        ),
//        2 to TutorialDetail.FaceShapeDetail(
//            steps = listOf(
//                StepItem(1, "Cara Mengaplikasikan Contour Wajah Square", "Deskripsi", "url"),
//                StepItem(2, "Cara Mengaplikasikan Blush Wajah Square", "Deskripsi", "url"),
//                StepItem(3, "Cara Mengaplikasikan Highlight Wajah Square","Deskripsi", "url"),
//            )
//        ),
//        3 to TutorialDetail.FaceShapeDetail(
//            steps = listOf(
//                StepItem(1, "Cara Mengaplikasikan Contour Wajah Heart", "Deskripsi", "url"),
//                StepItem(2, "Cara Mengaplikasikan Blush Wajah Heart", "Deskripsi", "url"),
//                StepItem(3, "Cara Mengaplikasikan Highlight Wajah Heart","Deskripsi", "url"),
//            )
//        ),
//        4 to TutorialDetail.FaceShapeDetail(
//            steps = listOf(
//                StepItem(1, "Cara Mengaplikasikan Contour Wajah Rectangle", "Deskripsi", "url"),
//                StepItem(2, "Cara Mengaplikasikan Blush Wajah Rectangle", "Deskripsi", "url"),
//                StepItem(3, "Cara Mengaplikasikan Highlight Wajah Rectangle","Deskripsi", "url"),
//            )
//        ),
//        5 to TutorialDetail.FaceShapeDetail(
//            steps = listOf(
//                StepItem(1, "Cara Mengaplikasikan Contour Wajah Round", "Deskripsi", "url"),
//                StepItem(2, "Cara Mengaplikasikan Blush Wajah Round", "Deskripsi", "url"),
//                StepItem(3, "Cara Mengaplikasikan Highlight Wajah Round","Deskripsi", "url"),
//            )
//        ),
//        6 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Soft Western Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Soft Western Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Soft Western Style","Deskripsi", "url"),
//            )
//        ),
//        7 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Old Money Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Old Money Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Old Money Style","Deskripsi", "url"),
//            )
//        ),
//        8 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Korean Natural Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Korean Natural Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Korean Natural Style","Deskripsi", "url"),
//            )
//        ),
//        9 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Igari Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Igari Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Igari Style","Deskripsi", "url"),
//            )
//        ),
//        10 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Clean Girl Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Clean Girl Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Clean Girl Style","Deskripsi", "url"),
//            )
//        ),
//        11 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Latte Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Latte Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Latte Style","Deskripsi", "url"),
//            )
//        ),
//        12 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Euphoria Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Euphoria Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Euphoria Style","Deskripsi", "url"),
//            )
//        ),
//        13 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk E-Girl Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk E-Girl Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk E-Girl Style","Deskripsi", "url"),
//            )
//        ),
//        14 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Western Glam Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Western Glam Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Western Glam Style","Deskripsi", "url"),
//            )
//        ),
//        15 to TutorialDetail.EventDetail(
//            steps = listOf(
//                StepItem(1, "Eye Make Up Untuk Douyin Glam Style", "Deskripsi", "url"),
//                StepItem(2, "Lip Technique Untuk Douyin Glam Style", "Deskripsi", "url"),
//                StepItem(3, "Tips untuk Douyin Glam Style","Deskripsi", "url"),
//            )
//        ),
//        16 to TutorialDetail.SkinToneDetail(
//            title = "Shade Lipstick for Fair to Light Skintone",
//            topsub = "Yeay! Kamu cocok banget sama warna-warna manis dan fresh ini.",
//            bottomsub = "Warna-warna ini bakal bikin wajah kamu kelihatan cerah dan tetap terlihat cantik natural!",
//            shades = listOf(
//                    ShadeItem("Baby Pink", "Memberi tampilan segar dan manis, membuat wajah terlihat natural tapi tetap hidup.", Color(0xFFFBC4D2)),
//                    ShadeItem("Light Coral", "Warna cerah hangat yang langsung memberi efek fresh dan membuat wajah tampak lebih bercahaya.", Color(0xFFF7A08C)),
//                    ShadeItem("Peach", "Lembut dan natural, cocok untuk mencerahkan wajah secara halus tanpa terlihat berlebihan.", Color(0xFFF37F73)),
//                    ShadeItem("Light Pink", "Memberikan kesan feminim dan lembut, membuat wajah tampak cerah dengan sentuhan natural.", Color(0xFFE85A9B)),
//                    ShadeItem("Plum", "Elegan dengan nuansa sedikit bold, namun tetap memberi efek cerah dan anggun pada wajah.", Color(0xFF8E4A83))
//            )
//        ),
//        17 to TutorialDetail.SkinToneDetail(
//            title = "Shade Lipstick for Medium Tan Skintone",
//            topsub = "Yeay! Kamu cocok banget sama warna-warna manis dan fresh ini.",
//            bottomsub = "Warna-warna ini bakal bikin wajah kamu kelihatan cerah dan tetap terlihat cantik natural!",
//            shades = listOf(
//                    ShadeItem("Baby Pink", "Memberi tampilan segar dan manis, membuat wajah terlihat natural tapi tetap hidup.", Color(0xFFFBC4D2)),
//                    ShadeItem("Light Coral", "Warna cerah hangat yang langsung memberi efek fresh dan membuat wajah tampak lebih bercahaya.", Color(0xFFF7A08C)),
//                    ShadeItem("Peach", "Lembut dan natural, cocok untuk mencerahkan wajah secara halus tanpa terlihat berlebihan.", Color(0xFFF37F73)),
//                    ShadeItem("Light Pink", "Memberikan kesan feminim dan lembut, membuat wajah tampak cerah dengan sentuhan natural.", Color(0xFFE85A9B)),
//                    ShadeItem("Plum", "Elegan dengan nuansa sedikit bold, namun tetap memberi efek cerah dan anggun pada wajah.", Color(0xFF8E4A83))
//            )
//        ),
//        18 to TutorialDetail.SkinToneDetail(
//            title = "Shade Lipstick for Deep Skintone",
//            topsub = "Yeay! Kamu cocok banget sama warna-warna manis dan fresh ini.",
//            bottomsub = "Warna-warna ini bakal bikin wajah kamu kelihatan cerah dan tetap terlihat cantik natural!",
//            shades = listOf(
//                ShadeItem("Baby Pink", "Memberi tampilan segar dan manis, membuat wajah terlihat natural tapi tetap hidup.", Color(0xFFFBC4D2)),
//                ShadeItem("Light Coral", "Warna cerah hangat yang langsung memberi efek fresh dan membuat wajah tampak lebih bercahaya.", Color(0xFFF7A08C)),
//                ShadeItem("Peach", "Lembut dan natural, cocok untuk mencerahkan wajah secara halus tanpa terlihat berlebihan.", Color(0xFFF37F73)),
//                ShadeItem("Light Pink", "Memberikan kesan feminim dan lembut, membuat wajah tampak cerah dengan sentuhan natural.", Color(0xFFE85A9B)),
//                ShadeItem("Plum", "Elegan dengan nuansa sedikit bold, namun tetap memberi efek cerah dan anggun pada wajah.", Color(0xFF8E4A83))
//            )
//        )
//    )
//}
//
//// --- 3. UI COMPONENTS: Membangun setiap bagian dari tampilan ---
///**
// * Composable utama yang membangun seluruh halaman tutorial.
// */
//@Composable
//fun TutorialPage(
//     onTutorialClick: (Int) -> Unit
//) {
//    // State untuk menyimpan teks pencarian dan filter yang aktif
//    var searchQuery by remember { mutableStateOf("") }
//    var activeFilters by remember { mutableStateOf(setOf<SubCategory>()) }
//    val allTutorials = remember { getDummyTutorials() }
//
//    Scaffold(
//        topBar = { TopSection(onBackClicked = { /* TODO: Implement back navigation */ }) },
//    ) { paddingValues ->
//        // LazyColumn adalah container utama yang bisa di-scroll ke bawah
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(Color(0xFFF5F5F5))
//        ) {
//            SearchAndFilterSection(
//                searchQuery = searchQuery,
//                onSearchQueryChange = { searchQuery = it },
//                activeFilters = activeFilters,
//                onFilterChanged = { subCategory ->
//                    activeFilters = if (activeFilters.contains(subCategory)) {
//                        activeFilters - subCategory
//                    } else {
//                        activeFilters + subCategory
//                    }
//                },
//            )
//        }
//            // 1. Lakukan filter DAN pengelompokan seperti sebelumnya
//            val groupedAndFiltered = allTutorials.filter { tutorial ->
//                val matchesSearch = searchQuery.isEmpty() ||
//                        tutorial.title.contains(searchQuery, ignoreCase = true) ||
//                        tutorial.description.contains(searchQuery, ignoreCase = true)
//
//                val matchesFilter =
//                    activeFilters.isEmpty() || activeFilters.contains(tutorial.subCategory)
//                matchesSearch && matchesFilter
//            }.groupBy { it.mainCategory }
//
//            // 2. Hapus `forEach` dan iterasi melalui keys dari map
//            groupedAndFiltered.keys.forEach { category ->
//                // Dapatkan list tutorial untuk kategori ini
//                val tutorialsInCategory = groupedAndFiltered[category]
//
//                if (!tutorialsInCategory.isNullOrEmpty()) {
//                    // Tampilkan Judul Kategori sebagai satu 'item'
//                    item {
//                        Text(
//                            text = when (category) {
//                                MainCategory.FACE_SHAPE -> "Penempatan Rias Wajah"
//                                MainCategory.EVENT -> "Makeup Terbaik untuk Acaramu"
//                                MainCategory.SKIN_TONE -> "Rekomendasi Warna untuk Skintone"
//                            },
//                            style = MaterialTheme.typography.titleLarge,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
//                        )
//                    }
//
//                    // Tampilkan SEMUA tutorial dalam kategori ini menggunakan 'items'
//                    items(
//                        items = tutorialsInCategory,
//                        key = { it.id } // Memberikan key unik untuk performa yang lebih baik
//                    ) { tutorial ->
//                        TutorialCard(
//                            tutorial = tutorial,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                            onClick = { onTutorialClick(tutorial.id) }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun SearchAndFilterSection(
//    searchQuery: String,
//    onSearchQueryChange: (String) -> Unit,
//    onFilterChanged: (SubCategory) -> Unit,
//    activeFilters: Set<SubCategory>
//) {
//    Column(modifier = Modifier
//        .background(Color.White)
//        .padding(bottom = 16.dp)) {
//        // Search Bar
//        OutlinedTextField(
//            value = searchQuery,
//            onValueChange = onSearchQueryChange,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            placeholder = { Text("Pencarian") },
//            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
//            shape = RoundedCornerShape(12.dp),
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedBorderColor = Color(0xFFE91E63),
//                unfocusedBorderColor = Color.LightGray
//            )
//        )
//        // Filter Buttons
//        LazyRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            item { FilterChip(Icons.Default.FilterList, "Filters", onClick = { }) }
//            item { FilterDropDown("Bentuk Wajah", SubCategory.entries.filter { it in setOf(
//                SubCategory.OVAL, SubCategory.SQUARE, SubCategory.HEART,
//                SubCategory.RECTANGLE, SubCategory.ROUND) }, onFilterChanged, activeFilters) }
//            item { FilterDropDown("Warna Kulit", SubCategory.entries.filter { it in setOf(
//                SubCategory.FAIRLIGHT, SubCategory.MEDIUMTAN, SubCategory.DEEP) }, onFilterChanged, activeFilters) }
//            item { FilterDropDown("Acara", SubCategory.entries.filter { it in setOf(
//                SubCategory.WEDDING,
//                SubCategory.PARTY,
//                SubCategory.FESTIVAL,
//                SubCategory.OFFICE,
//                SubCategory.CASUAL,
//            ) }, onFilterChanged, activeFilters) }
//        }
//        if (activeFilters.isNotEmpty()) {
//            LazyRow(
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(activeFilters.toList()) { filter ->
//                    ActiveFilterChip(
//                        text = filter.name.replaceFirstChar { it.titlecase() },
//                        onDismiss = { onFilterChanged(filter) } // Memanggil fungsi yang sama untuk menghapus
//                    )
//                }
//            }
//        }
//    }
//}
//
////@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun FilterChip(icon: ImageVector, text: String, onClick: () -> Unit) {
//    AssistChip(
//        onClick = onClick,
//        label = { Text(text) },
//        leadingIcon = { Icon(icon, contentDescription = null) },
//        colors = AssistChipDefaults.assistChipColors(
//            containerColor = Color(0xFFFFF1F5),
//            labelColor = Color(0xFFE91E63),
//            leadingIconContentColor = Color(0xFFE91E63)
//        ),
//
//        // Ganti assistChipBorder dengan BorderStroke secara langsung.
//        // Parameter: ketebalan garis (1.dp) dan warnanya.
//        border = BorderStroke(1.dp, Color(0xFFE91E63))
//    )
//}
//@Composable
//fun ActiveFilterChip(
//    text: String,
//    onDismiss: () -> Unit
//) {
//    InputChip(
//        selected = true, // Selalu terpilih karena ini adalah filter aktif
//        onClick = { onDismiss() }, // Aksi saat chip diklik adalah menghapus dirinya sendiri
//        label = { Text(text, fontSize = 12.sp) },
//        trailingIcon = {
//            Icon(
//                imageVector = Icons.Default.Close,
//                contentDescription = "Hapus filter",
//                modifier = Modifier
//                    .size(18.dp)
//                    .clickable { onDismiss() },
//                tint = Color(0xFFE91E63)
//            )
//        },
//        colors = InputChipDefaults.inputChipColors(
//            selectedContainerColor = Color(0xFFFFF1F5), // Warna background pink muda
//            selectedLabelColor = Color(0xFFE91E63), // Warna teks pink tua
//        ),
//        border = InputChipDefaults.inputChipBorder(
//            borderColor = Color(0xFFE91E63),
//            borderWidth = 1.dp,
//            enabled = TODO(),
//            selected = TODO(),
//            selectedBorderColor = TODO(),
//            disabledBorderColor = TODO(),
//            disabledSelectedBorderColor = TODO(),
//            selectedBorderWidth = TODO()
//        )
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FilterDropDown(
//    title: String,
//    options: List<SubCategory>,
//    onFilterChanged: (SubCategory) -> Unit,
//    activeFilters: Set<SubCategory>
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val isAnyFilterActive = options.any { it in activeFilters }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//        Row(
//            modifier = Modifier
//                .menuAnchor()
//                .width(IntrinsicSize.Min)
//                .border(
//                    1.dp,
//                    if (isAnyFilterActive) Color(0xFFE91E63) else Color.LightGray,
//                    RoundedCornerShape(8.dp)
//                )
//                .padding(horizontal = 12.dp, vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            Text(
//                text = title,
//                color = if (isAnyFilterActive) Color(0xFFE91E63) else Color.Black,
//                maxLines = 1, //biar baris ga wrap ke baris baru
//                overflow = TextOverflow.Visible //biar teks ga kepotong
//            )
//            Icon(
//                Icons.Default.ArrowDropDown,
//                contentDescription = null,
//                tint = if (isAnyFilterActive) Color(0xFFE91E63) else Color.Gray)
//        }
//
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            options.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option.name.replaceFirstChar { it.uppercase() }) },
//                    onClick = {
//                        onFilterChanged(option)
//                        expanded = false
//                    },
//                    leadingIcon = {
//                        if (activeFilters.contains(option)) {
//                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color(0xFFE91E63))
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun TopSection(onBackClicked: () -> Unit) {
//    Column(modifier = Modifier
//        .background(Color.White)
//        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
//        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable(onClick = onBackClicked)) {
//            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color(0xFFE91E63))
//            Spacer(Modifier.width(8.dp))
//            Text("Kembali", color = Color(0xFFE91E63), fontWeight = FontWeight.SemiBold)
//        }
//        Spacer(Modifier.height(16.dp))
//        Text("Tutorial", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
//        Text("Ini dia kumpulan tutorial untukmu!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun TutorialCard(
//    tutorial: TutorialItem,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit) {
//    var isFavorite by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(12.dp),
//            verticalAlignment = Alignment.Top
//        ) {
//            // Gambar
//            AsyncImage(
//                model = tutorial.imageUrl,
//                contentDescription = tutorial.title,
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(12.dp)),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(Modifier.width(16.dp))
//
//            // Kolom Teks
//            Column(modifier = Modifier.weight(1f)) {
//                // Tag
//                Text(
//                    text = tutorial.tag,
//                    color = Color(0xFFE91E63),
//                    fontSize = 10.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .background(Color(0xFFFFF1F5), RoundedCornerShape(6.dp))
//                        .padding(horizontal = 8.dp, vertical = 4.dp)
//                )
//
//                Spacer(Modifier.height(8.dp))
//
//                // Judul
//                Text(
//                    text = tutorial.title,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp,
//                    maxLines = 2
//                )
//
//                Spacer(Modifier.height(4.dp))
//
//                // Deskripsi
//                Text(
//                    text = tutorial.description,
//                    fontSize = 12.sp,
//                    color = Color.Gray,
//                    maxLines = 2
//                )
//
//                Spacer(Modifier.height(8.dp))
//
//                // Tombol "Lihat lebih lanjut"
//                Text(
//                    text = "Lihat lebih lanjut",
//                    color = Color(0xFFE91E63),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 12.sp,
//                    )
//            }
//
//            // Tombol Favorit
//            Icon(
//                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
//                contentDescription = "Favorite",
//                tint = Color(0xFFE91E63),
//                modifier = Modifier
//                    .size(24.dp)
//                    .clickable (indication = null, interactionSource = remember { MutableInteractionSource() }
//                    ){
//                        isFavorite = !isFavorite
//                    }
//            )
//        }
//    }
//}
//
//// --- 4. PREVIEW: Untuk melihat tampilan langsung di Android Studio ---
//@Preview(showBackground = true, device = "id:pixel_4")
//@Composable
//fun TutorialPagePreview() {
//    // Anda bisa mengganti dengan tema aplikasi Anda jika sudah ada
//    MaterialTheme {
//        TutorialPage(onTutorialClick = {
//
//        })
//    }
//}
//
//@Composable
//private fun ShadeDescriptionItem(shade: ShadeItem) {
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        // Garis vertikal berwarna sebagai dekorasi
//        Box(
//            modifier = Modifier
//                .width(4.dp)
//                .height(50.dp)
//                .background(
//                    Color(0xFFE91E63), RoundedCornerShape(4.dp)
//                )
//        )
//        Spacer(Modifier.width(12.dp))
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)) // Warna abu-abu sangat muda
//        ) {
//            // Column untuk menyusun teks secara vertikal
//            Column(
//                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
//            ) {
//                Text(
//                    text = shade.name,
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.bodyLarge
//                )
//                Spacer(modifier = Modifier.height(2.dp))
//
//                Text(
//                    text = shade.description,
//                    fontSize = 12.sp,
//                    color = Color.Gray,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}
//@Composable
//private fun SkinToneContent(detailData: TutorialDetail.SkinToneDetail) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Subtitle atas hitam
//        Text(
//            text = detailData.topsub,
//            style = MaterialTheme.typography.bodyMedium,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(Modifier.height(8.dp))
//
//        // Subtitle bawh merah
//        Text(
//            text = detailData.bottomsub,
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color(0xFFE91E63),
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(Modifier.height(24.dp))
//
//        // palet warna
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            detailData.shades.forEach { shade ->
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Box(
//                        modifier = Modifier
//                            .size(56.dp)
//                            .clip(CircleShape)
//                            .background(shade.color)
//                    )
//                    Text(
//                        text = shade.name,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.SemiBold,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            }
//        }
//
//        Spacer(Modifier.height(32.dp))// Bagian daftar kartu deskripsi
//        Column(
//            modifier = Modifier.fillMaxWidth(), // Kembali ke lebar penuh
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            detailData.shades.forEach { shade -> ShadeDescriptionItem(shade = shade)}
//
//        }
//    }
//}
//
//@Composable
//fun TutorialDetailPage(tutorialId: Int, onBackClicked: () -> Unit) {
//    // Ambil data tutorial dan data detail berdasarkan ID
//    val tutorial = getDummyTutorials().find { it.id == tutorialId }
//    val detailData = getDummyDetailData()[tutorialId]
//
//    // 1. Validasi Data: Pastikan data tidak kosong (null)
//    // Ini penting untuk mencegah aplikasi crash jika ID yang dikirim tidak valid.
//    if (tutorial == null || detailData == null) {
//        // Jika data tidak ditemukan, tampilkan pesan error atau layar loading.
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Maaf, konten tutorial tidak ditemukan.")
//        }
//        // Hentikan eksekusi fungsi lebih lanjut jika data tidak ada.
//        return
//    }
//
//    // 2. Struktur Utama Halaman: Gunakan LazyColumn
//    // Menggunakan LazyColumn memungkinkan konten yang sangat panjang (seperti langkah-langkah)
//    // untuk bisa di-scroll dengan efisien.
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White) // Atur warna background halaman
//            .verticalScroll(scrollState)
//    ) {
//        DetailHeader(
//            tutorial = tutorial,
//            onBackClicked = onBackClicked
//        )
//
//        when (detailData) {
//            is TutorialDetail.FaceShapeDetail -> {
//                FaceShapeContent(detailData = detailData)
//            }
//            is TutorialDetail.SkinToneDetail -> {
//                SkinToneContent(detailData = detailData)
//            }
//            // Anda bisa menambahkan kategori lain di sini di masa depan
//            is TutorialDetail.EventDetail -> {
//                EventContent(detailData = detailData)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//
//    }
//}
//
//@Composable
//private fun DetailHeader(tutorial: TutorialItem, onBackClicked: () -> Unit) {
//    Column(modifier = Modifier.padding(16.dp)) {
//        // Baris untuk tombol "Kembali"
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.clickable(onClick = onBackClicked) // Membuat seluruh baris bisa diklik
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Kembali",
//                tint = Color(0xFFE91E63) // Warna pink
//            )
//            Spacer(Modifier.width(8.dp))
//            Text("Kembali", color = Color(0xFFE91E63), fontWeight = FontWeight.SemiBold)
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // Tag kategori (misal: "Persegi")
//        Text(
//            text = tutorial.tag,
//            color = Color(0xFFE91E63),
//            modifier = Modifier
//                .background(
//                    Color(0xFFFFF1F5),
//                    RoundedCornerShape(6.dp)
//                )
//                .padding(horizontal = 8.dp, vertical = 4.dp)
//        )
//
//        Spacer(Modifier.height(8.dp))
//
//        // Judul Utama Tutorial
//        Text(
//            text = tutorial.title,
//            style = MaterialTheme.typography.headlineSmall,
//            fontWeight = FontWeight.Bold
//        )
//
//        Spacer(Modifier.height(4.dp))
//
//        // Deskripsi singkat di bawah judul
//        Text(
//            text = tutorial.description,
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color.Gray
//        )
//    }
//}
//
//
////=================================================================//
//// Bagian untuk Konten "Face Shape" (Langkah-langkah)              //
////=================================================================//
//
///**
// * Composable yang bertanggung jawab untuk me-layout semua langkah
// * tutorial Face Shape.
// */
//@Composable
//private fun FaceShapeContent(detailData: TutorialDetail.FaceShapeDetail) {
//    // Column untuk menampung semua kartu langkah
//    Column(
//        modifier = Modifier.padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp) // Memberi jarak antar kartu
//    ) {
//        // Looping untuk setiap langkah di dalam data
//        detailData.steps.forEach { step ->
//            StepCard(step = step)
//        }
//    }
//}
//
//@Composable
//private fun EventContent(detailData: TutorialDetail.EventDetail) {
//    // Column untuk menampung semua kartu langkah
//    Column(
//        modifier = Modifier.padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp) // Memberi jarak antar kartu
//    ) {
//        // Looping untuk setiap langkah di dalam data
//        detailData.steps.forEach { step ->
//            StepCard(step = step)
//        }
//    }
//}
//
//@Composable
//private fun StepCard(step: StepItem) {
//    // Row utama untuk membungkus garis vertikal dan Card
//    Row(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//    //  Garis vertikal berwarna sebagai dekorasi
//        Box(
//            modifier = Modifier
//                .padding(top = 8.dp) // Beri sedikit padding atas agar sejajar dengan judul
//                .width(4.dp)
//                .height(200.dp) // Sesuaikan tinggi garis sesuai kebutuhan
//                .background(Color(0xFFE91E63), RoundedCornerShape(4.dp))
//        )
//        Spacer(Modifier.width(12.dp))
//
//        // Card yang berisi semua konten langkah
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White),
//            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                // Baris untuk nomor dan judul langkah
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    // Lingkaran untuk nomor langkah
//                    Box(
//                        modifier = Modifier
//                            .size(24.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFFE91E63)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = step.stepNum.toString(),
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//                    Spacer(Modifier.width(12.dp))
//
//                    Text(
//                        text = step.title,
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                Spacer(Modifier.height(12.dp))
//
//                // Gambar ilustrasi langkah
//                AsyncImage(
//                    model = step.image, // Gunakan URL dari data                    contentDescription = step.title,
//                    contentDescription = step.title,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(12.dp)),
//                    contentScale = ContentScale.FillWidth
//                )
//
//                Spacer(Modifier.height(12.dp))
//                Text(
//                    text = step.description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.DarkGray,
//                    lineHeight = 20.sp // Membuat jarak antar baris lebih nyaman dibaca
//                )
//            }
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
