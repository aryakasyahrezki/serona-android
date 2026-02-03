package com.example.serona.ui.main.favorite

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.model.Tutorial
import com.example.serona.data.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: TutorialRepository
) : ViewModel() {

    // Mutable state list untuk menyimpan tutorial favorit
    private val _favoriteList = mutableStateListOf<Tutorial>()
    val favoriteList: List<Tutorial> get() = _favoriteList

    init {
        loadFavorites()
    }

    /**
     * Toggle favorite status untuk tutorial
     * Jika tutorial sudah ada di favorit, akan dihapus
     * Jika belum ada, akan ditambahkan
     */
    fun toggleFavorite(tutorial: Tutorial) {
        viewModelScope.launch {
            if (isFavorite(tutorial.id)) {
                // Remove from favorites
                val success = repository.removeFavorite(tutorial.id)
                if (success) {
                    _favoriteList.removeIf { it.id == tutorial.id }
                }
            } else {
                // Add to favorites
                val success = repository.addFavorite(tutorial.id)
                if (success) {
                    _favoriteList.add(tutorial)
                }
            }
        }
    }

    /**
     * Menambahkan tutorial ke favorit
     */
    fun addFavorite(tutorial: Tutorial) {
        viewModelScope.launch {
            if (!isFavorite(tutorial.id)) {
                val success = repository.addFavorite(tutorial.id)
                if (success) {
                    _favoriteList.add(tutorial)
                }
            }
        }
    }

    /**
     * Menghapus tutorial dari favorit
     */
    fun removeFavorite(tutorial: Tutorial) {
        viewModelScope.launch {
            val success = repository.removeFavorite(tutorial.id)
            if (success) {
                _favoriteList.removeIf { it.id == tutorial.id }
            }
        }
    }

    /**
     * Mengecek apakah tutorial ada di favorit
     */
    fun isFavorite(tutorialId: Int): Boolean {
        return _favoriteList.any { it.id == tutorialId }
    }

    /**
     * Load favorites from backend
     */
    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favorites = repository.getFavorites()
                _favoriteList.clear()
                _favoriteList.addAll(favorites)
            } catch (e: Exception) {
                Log.e("FavoriteVM", "Error loading favorites: ${e.message}")
            }
        }
    }

    /**
     * Refresh favorites (untuk pull-to-refresh)
     */
    fun refreshFavorites() {
        loadFavorites()
    }
}


//@HiltViewModel
//class FavoriteViewModel @Inject constructor() : ViewModel() {
//
//    // Mutable state list untuk menyimpan tutorial favorit
//    private val _favoriteList = mutableStateListOf<Tutorial>()
//    val favoriteList: List<Tutorial> get() = _favoriteList
//
//    /**
//     * Toggle favorite status untuk tutorial
//     * Jika tutorial sudah ada di favorit, akan dihapus
//     * Jika belum ada, akan ditambahkan
//     */
//    fun toggleFavorite(tutorial: Tutorial) {
//        viewModelScope.launch {
//            if (_favoriteList.contains(tutorial)) {
//                _favoriteList.remove(tutorial)
//                // TODO: Call API to remove from favorites
//                // Example: repository.removeFavorite(tutorial.id)
//            } else {
//                _favoriteList.add(tutorial)
//                // TODO: Call API to add to favorites
//                // Example: repository.addFavorite(tutorial.id)
//            }
//        }
//    }
//
//    /**
//     * Menambahkan tutorial ke favorit
//     */
//    fun addFavorite(tutorial: Tutorial) {
//        viewModelScope.launch {
//            if (!_favoriteList.contains(tutorial)) {
//                _favoriteList.add(tutorial)
//                // TODO: Call API to add to favorites
//            }
//        }
//    }
//
//    /**
//     * Menghapus tutorial dari favorit
//     */
//    fun removeFavorite(tutorial: Tutorial) {
//        viewModelScope.launch {
//            _favoriteList.remove(tutorial)
//            // TODO: Call API to remove from favorites
//        }
//    }
//
//    /**
//     * Mengecek apakah tutorial ada di favorit
//     */
//    fun isFavorite(tutorialId: Int): Boolean {
//        return _favoriteList.any { it.id == tutorialId }
//    }
//
//    /**
//     * Load favorites from backend
//     * TODO: Implement when backend API is ready
//     */
//    fun loadFavorites() {
//        viewModelScope.launch {
//            // TODO: Call API to get user's favorite tutorials
//            // Example:
//            // val favorites = repository.getFavorites()
//            // _favoriteList.clear()
//            // _favoriteList.addAll(favorites)
//        }
//    }
//}

//package com.example.serona.ui.main.favorite
//
//import androidx.compose.runtime.mutableStateListOf
//import androidx.lifecycle.ViewModel
//import com.example.serona.data.model.Tutorial
//
//class FavoriteViewModel : ViewModel() {
//
//    val favoriteList = mutableStateListOf<Tutorial>()
//
//    fun toggleFavorite(tutorial: Tutorial) {
//        if (favoriteList.contains(tutorial)) {
//            favoriteList.remove(tutorial)
//        } else {
//            favoriteList.add(tutorial)
//        }
//    }
//}