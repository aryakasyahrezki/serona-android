package com.example.serona.ui.main.tutorial

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.model.Tutorial
import com.example.serona.data.model.TutorialStep
import com.example.serona.data.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialDetailViewModel @Inject constructor(
    private val repo: TutorialRepository
) : ViewModel() {

    // StateFlow untuk detail tutorial
//    private val _tutorial = MutableStateFlow<Tutorial?>(null)
//    val tutorial = _tutorial.asStateFlow()

    private val _tutorial = MutableStateFlow<Tutorial?>(null)
    val tutorial: StateFlow<Tutorial?> = _tutorial.asStateFlow()

    private val _steps = MutableStateFlow<List<TutorialStep>>(emptyList())
    val steps: StateFlow<List<TutorialStep>> = _steps.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchTutorialAndSteps(tutorialId: Int) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                Log.d("TutorialDetailVM", "🔄 Fetching tutorial ID: $tutorialId")

                // Fetch tutorial detail (sudah include steps dari API)
                val tutorialData = repo.getTutorialById(tutorialId)

                _tutorial.value = tutorialData

                // 🔥 AMBIL STEPS DARI TUTORIAL OBJECT, BUKAN DARI API TERPISAH
                _steps.value = tutorialData.steps ?: emptyList()

                Log.d("TutorialDetailVM", "✅ Tutorial: ${tutorialData.title}")
                Log.d("TutorialDetailVM", "✅ Steps: ${_steps.value.size}")

            } catch (e: Exception) {
                Log.e("TutorialDetailVM", "❌ Error: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
