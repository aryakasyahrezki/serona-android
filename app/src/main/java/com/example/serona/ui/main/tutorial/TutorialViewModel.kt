package com.example.serona.ui.main.tutorial

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.model.*
import com.example.serona.data.repository.TutorialRepository
import com.example.serona.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
    private val repository: TutorialRepository,
    private val userSession: UserSession,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navFaceShape: String? = savedStateHandle.get<String>("faceShape")
        ?.takeIf { it != "none" }
    private val navSkinTone: String? = savedStateHandle.get<String>("skinTone")
        ?.takeIf { it != "none" }
    private val navOccasion: String? = savedStateHandle.get<String>("occasion")
        ?.takeIf { it != "none" }

    private val _allTutorials = MutableStateFlow<List<Tutorial>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

//    private val _query = MutableStateFlow("")
//    val query: StateFlow<String> = _query.asStateFlow()

    // Active filters (set of selected subcategory values)
    private val _activeFilters = MutableStateFlow<Set<String>>(emptySet())
    val activeFilters: StateFlow<Set<String>> = _activeFilters.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

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
            "Oblong"
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

    // Combine Filter and Search tutorials based on search query and active filters
    val filteredTutorials: StateFlow<List<Tutorial>> = combine(
        _allTutorials,
        _searchQuery,
        _activeFilters
    ) { tutorials, query, filters ->
        var result = tutorials

        if (filters.isNotEmpty()) {
            result = result.filter { tutorial ->
                filters.contains(tutorial.sub_category)
            }
        }

        if (query.isNotBlank()) {
            result = result.filter { tutorial ->
                tutorial.title.contains(query, ignoreCase = true) ||
                tutorial.description.contains(query, ignoreCase = true) ||
                tutorial.main_category.contains(query, ignoreCase = true) ||
                tutorial.sub_category.contains(query, ignoreCase = true)
            }
        }

        result
    }.stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        fetchTutorials()
        applyInitialFilters()
    }

    private fun normalizeSkinTone(tone: String?): String? {
        return tone?.trim()?.replace(" ", "-")?.split("-")?.joinToString("-") {
            it.lowercase().replaceFirstChar { char -> char.uppercase() }
        }
    }

    private fun applyInitialFilters() {
        viewModelScope.launch {
            val filters = mutableSetOf<String>()

            val user = userSession.user.first()
            android.util.Log.d("TutorialVM", "Nav Params -> Shape: $navFaceShape, Tone: $navSkinTone, Event: $navOccasion")
            android.util.Log.d("TutorialVM", "HASIL SIMPENAN -> Face Shape: ${user?.faceShape}, Skin Tone: ${user?.skinTone}")

            val faceShapeResult = navFaceShape ?: user?.faceShape
            faceShapeResult?.takeIf { it.isNotBlank() }?.let {
                filters.add(it)
                android.util.Log.d("TutorialVM", "Face Shape Filter Active: $it")
            }

            // 3. Logika Skin Tone: Prioritas Navigasi, fallback ke Profil (dengan Normalisasi)
            val skinToneResult = navSkinTone ?: user?.skinTone
            skinToneResult?.takeIf { it.isNotBlank() }?.let { rawTone ->
                normalizeSkinTone(rawTone)?.let { normalized ->
                    filters.add(normalized)
                    android.util.Log.d("TutorialVM", "Skin Tone Filter Active: $normalized")
                }
            }

            // 4. Logika Occasion: Hanya dari Navigasi (karena profil tidak menyimpan event)
            navOccasion?.takeIf { it.isNotBlank() }?.let {
                filters.add(it)
                android.util.Log.d("TutorialVM", "Occasion Filter Active: $it")
            }

            // Update state filter
            _activeFilters.value = filters
            android.util.Log.d("TutorialVM", "FINAL FILTERS: $filters")
        }
    }

    /**
     * Fetch tutorials from repository (backend)
     */
    fun fetchTutorials() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tutorials = repository.getTutorials()
                _allTutorials.value = tutorials
            } catch (e: Exception) {
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
        _searchQuery.value = newQuery
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
