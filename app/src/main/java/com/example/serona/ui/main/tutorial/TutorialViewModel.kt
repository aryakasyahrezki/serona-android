package com.example.serona.ui.main.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.model.*
import com.example.serona.data.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
    private val repository: TutorialRepository
) : ViewModel() {

    private val _allTutorials = MutableStateFlow<List<Tutorial>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // Active filters (set of selected subcategory values)
    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
    val activeFilters: StateFlow<Set<String>> = _activeFilters

    // Main Category options - UBAH JADI List<String>
    val mainCategoryOptions: List<String> = listOf(
        "Face Shape",
        "Skin Tone",
        "Occasion"
    )

    // Sub Category options - UBAH JADI Map<String, List<String>>
    val subCategoryOptions: Map<String, List<String>> = mapOf(
        "Face Shape" to listOf(
            "Oval",
            "Heart",
            "Round",
            "Square",
            "Rectangle"
        ),
        "Skin Tone" to listOf(
            "Fair-Light",
            "Medium-Tan",
            "Deep"
        ),
        "Occasion" to listOf(
            "Casual",
            "Office",
            "Party",
            "Wedding",
            "Festival"
        )
    )

    // Check if any filter is active
    val isFilterActive: StateFlow<Boolean> = _activeFilters.map { it.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    // Filtered tutorials based on search query and active filters
    val filteredTutorials: StateFlow<List<Tutorial>> =
        combine(_allTutorials, _query, _activeFilters) { tutorials, q, filters ->
            if (q.isBlank() && filters.isEmpty()) {
                // No query or filter, show all tutorials
                tutorials
            } else {
                tutorials.filter { tutorial ->
                    // Match search query
                    val matchQuery = q.isBlank() ||
                            tutorial.title.contains(q, ignoreCase = true) ||
                            tutorial.description.contains(q, ignoreCase = true)

                    // Match active filters (check if tutorial's subcategory is in active filters)
                    val matchFilter = filters.isEmpty() ||
                            (filters.contains(tutorial.sub_category))

                    matchQuery && matchFilter
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        fetchTutorials()
    }

    /**
     * Fetch tutorials from repository (backend)
     */
    private fun fetchTutorials() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tutorials = repository.getTutorials()
                _allTutorials.value = tutorials
            } catch (e: Exception) {
                // Handle error - you might want to show error state to user
                e.printStackTrace()
//                _allTutorials.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Toggle filter selection
     * If filter is already active, remove it
     * If not active, add it
     */
    fun onFilterSelected(filterValue: String) {
        _activeFilters.update { currentFilters ->
            if (currentFilters.contains(filterValue)) {
                // Remove filter if already active (toggle off)
                currentFilters - filterValue
            } else {
                // Add filter if not active (toggle on)
                currentFilters + filterValue
            }
        }
    }

    /**
     * Update search query
     */
    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    /**
     * Clear all active filters
     */
    fun clearAllFilters() {
        _activeFilters.value = emptySet()
    }

    /**
     * Refresh tutorials from backend
     */
    fun refreshTutorials() {
        fetchTutorials()
    }
}

//package com.example.serona.ui.main.tutorial
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.serona.data.model.*
//import com.example.serona.ui.component.*
//import com.example.serona.data.repository.TutorialRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class TutorialViewModel @Inject constructor(
//    private val repository: TutorialRepository
//) : ViewModel() {
//
//    private val _allTutorials = MutableStateFlow<List<Tutorial>>(emptyList())
//    private val _isLoading = MutableStateFlow(true)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _query = MutableStateFlow("")
//    val query: StateFlow<String> = _query.asStateFlow()
//
//    // Active filters (set of selected subcategory values)
//    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
//    val activeFilters: StateFlow<Set<String>> = _activeFilters
//
//    // For backward compatibility (not actively used but kept in case)
////    private val _mainCategory = MutableStateFlow<String?>(null)
////    private val _subCategory = MutableStateFlow<String?>(null)
////    val mainCategory: StateFlow<String?> = _mainCategory
////    val subCategory: StateFlow<String?> = _subCategory
//
//    // Main Category options
//    val mainCategoryOptions: List<FilterOption<String>> = listOf(
//        FilterOption("Face Shape", "Face Shape"),
//        FilterOption("Skintone", "Skintone"),
//        FilterOption("Occasion", "Occasion")
//    )
//
//    // Sub Category options (mapped to main categories)
//    val subCategoryOptions: Map<String, List<FilterOption<String>>> = mapOf(
//        "Face Shape" to listOf(
//            FilterOption("Oval", "Oval"),
//            FilterOption("Heart", "Heart"),
//            FilterOption("Round", "Round"),
//            FilterOption("Square", "Square"),
//            FilterOption("Rectangle", "Rectangle")
//        ),
//        "Skintone" to listOf(
//            FilterOption("Fair-Light", "Fair-Light"),
//            FilterOption("Light Vanilla", "Light Vanilla"),
//            FilterOption("Medium-Tan", "Medium-Tan"),
//            FilterOption("Deep", "Deep")
//        ),
//        "Occasion" to listOf(
//            FilterOption("Casual", "Casual"),
//            FilterOption("Office", "Office"),
//            FilterOption("Party", "Party"),
//            FilterOption("Wedding", "Wedding"),
//            FilterOption("Festival", "Festival")
//        )
//    )
//
//    // Check if any filter is active
//    val isFilterActive: StateFlow<Boolean> = _activeFilters.map { it.isNotEmpty() }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = false
//        )
//
//    // Filtered tutorials based on search query and active filters
//    val filteredTutorials: StateFlow<List<Tutorial>> =
//        combine(_allTutorials, _query, _activeFilters) { tutorials, q, filters ->
//            if (q.isBlank() && filters.isEmpty()) {
//                // No query or filter, show all tutorials
//                tutorials
//            } else {
//                tutorials.filter { tutorial ->
//                    // Match search query
//                    val matchQuery = q.isBlank() ||
//                            tutorial.title.contains(q, ignoreCase = true) ||
//                            tutorial.description.contains(q, ignoreCase = true)
//
//                    // Match active filters (check if tutorial's subcategory is in active filters)
//                    val matchFilter = filters.isEmpty() ||
//                            (!tutorial.subCategory.isNullOrEmpty() && filters.contains(tutorial.subCategory))
//
//                    matchQuery && matchFilter
//                }
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = emptyList()
//        )
//
//    init {
//        fetchTutorials()
//    }
//
//    /**
//     * Fetch tutorials from repository (backend)
//     */
//    private fun fetchTutorials() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val tutorials = repository.getTutorials()
//                _allTutorials.value = tutorials
//            } catch (e: Exception) {
//                // Handle error - you might want to show error state to user
//                e.printStackTrace()
//                _allTutorials.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    /**
//     * Toggle filter selection
//     * If filter is already active, remove it
//     * If not active, add it
//     */
//    fun onFilterSelected(filterValue: String) {
//        _activeFilters.update { currentFilters ->
//            if (currentFilters.contains(filterValue)) {
//                // Remove filter if already active (toggle off)
//                currentFilters - filterValue
//            } else {
//                // Add filter if not active (toggle on)
//                currentFilters + filterValue
//            }
//        }
//    }
//
//    /**
//     * Update search query
//     */
//    fun onQueryChange(newQuery: String) {
//        _query.value = newQuery
//    }
//
//    /**
//     * Clear all active filters
//     */
//    fun clearAllFilters() {
//        _activeFilters.value = emptySet()
//    }
//
//    /**
//     * Refresh tutorials from backend
//     */
//    fun refreshTutorials() {
//        fetchTutorials()
//    }
//}

//package com.example.serona.ui.main.tutorial
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.serona.data.model.*
//import com.example.serona.ui.component.*
//import com.example.serona.data.repository.TutorialRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class TutorialViewModel @Inject constructor(
//    private val repository: TutorialRepository
//) : ViewModel() {
//
//    private val _allTutorials = MutableStateFlow<List<Tutorial>>(emptyList())
//    private val _isLoading = MutableStateFlow(true)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _query = MutableStateFlow("")
//    val query: StateFlow<String> = _query.asStateFlow()
//
//    // Filters
//    private val _mainCategory = MutableStateFlow<String?>(null)
//    private val _subCategory = MutableStateFlow<String?>(null)
//    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
//    val activeFilters: StateFlow<Set<String>> = _activeFilters
//    val mainCategory: StateFlow<String?> = _mainCategory
//    val subCategory: StateFlow<String?> = _subCategory
//
//    // Main Category → kategori utama
//    val mainCategoryOptions: List<FilterOption<String>> = listOf(
//        FilterOption("Face Shape", "Face Shape"),
//        FilterOption("Skintone", "Skintone"),
//        FilterOption("Occasion", "Occasion")
//    )
//
//    // Sub Category → semua opsi dari ketiga kategori
//    val subCategoryOptions: Map<String, List<FilterOption<String>>> = mapOf(
//        "Face Shape" to listOf(
//            FilterOption("Oval", "Oval"),
//            FilterOption("Heart", "Heart"),
//            FilterOption("Round", "Round"),
//            FilterOption("Square", "Square"),
//            FilterOption("Rectangle", "Rectangle")
//        ),
//        "Skin Tone" to listOf(
//            FilterOption("Fair-Light", "Fair-Light"),
//            FilterOption("Medium-Tan", "Medium-Tan"),
//            FilterOption("Deep", "Deep")
//        ),
//        "Occasion" to listOf(
//            FilterOption("Casual", "Casual"),
//            FilterOption("Office", "Office"),
//            FilterOption("Party", "Party"),
//            FilterOption("Wedding", "Wedding"),
//            FilterOption("Festival", "Festival")
//        )
//    )
//
//
//    // Check if any filter active
//    val isFilterActive: StateFlow<Boolean> = _activeFilters.map { it.isNotEmpty() }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = false
//        )
////    val isFilterActive: StateFlow<Boolean> =
////        combine(
////            _mainCategory,
////            _subCategory
////        ) { main, sub ->
////            main != null || sub != null
////        }.stateIn(
////            viewModelScope,
////            SharingStarted.WhileSubscribed(5_000),
////            false
////        )
//
//    // Filtered tutorials
//    val filteredTutorials: StateFlow<List<Tutorial>> =
//        combine(_allTutorials, _query, _activeFilters) { tutorials, q, filters ->
//            if (q.isBlank() && filters.isEmpty()) {
//                // Jika tidak ada query atau filter, tampilkan semua tutorial
//                tutorials
//            } else {
//                tutorials.filter { tutorial ->
//                    // Mencocokkan dengan query (logika tetap sama)
//                    val matchQuery = q.isBlank() ||
//                            tutorial.title.contains(q, true) ||
//                            tutorial.description.contains(q, true)
//                    val matchFilter = filters.isEmpty() || filters.contains(tutorial.subCategory)
//
//                    matchQuery && matchFilter
//                }
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = emptyList()
//        )
////    val filteredTutorials: StateFlow<List<Tutorial>> =
////        combine(
////            _allTutorials,
////            _query,
////            _mainCategory,
////            _subCategory
////        )
////        { tutorials, q, main, sub ->
////            tutorials.filter { tutorial ->
////                val matchQuery = q.isBlank() || tutorial.title.contains(
////                    q, true) || tutorial.description.contains(q, true)
////                val matchMain = main == null || tutorial.mainCategory == main
////                val matchSub = sub == null || tutorial.subCategory == sub
////
////                matchQuery && matchMain && matchSub
////            }
////        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
//
//    init {
//        fetchTutorials()
//    }
//
//    private fun fetchTutorials() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            repository.getTutorials().also {
//                _allTutorials.value = it
//                _isLoading.value = false
//            }
//            _isLoading.value = false
//        }
//    }
//    //  Fungsi baru ini akan menambah atau menghapus filter dari Set `_activeFilters`.
//    /**
//     * Mengelola state filter. Jika filter sudah ada, maka akan dihapus.
//     * Jika belum ada, akan ditambahkan.
//     * @param filterValue Nilai dari filter yang dipilih, misal: "Oval".
//     */
//    fun onFilterSelected(filterValue: String) {
//        _activeFilters.update { currentFilters ->
//            if (currentFilters.contains(filterValue)) {
//                // Hapus filter jika sudah ada (toggle off)
//                currentFilters - filterValue
//            } else {
//                // Tambah filter jika belum ada (toggle on)
//                currentFilters + filterValue
//            }
//        }
//    }
//
//    // Query
//    fun onQueryChange(newQuery: String) {
//        _query.value = newQuery
//    }
//
//    // Main & Sub category
//    fun setMainCategory(value: String?) {
//        _mainCategory.value = value
//    }
//
//    fun setSubCategory(value: String?) {
//        _subCategory.value = value
//    }
//
//    fun clearAllFilters() {
//        _activeFilters.value = emptySet()
//    }
//}