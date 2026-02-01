package com.example.serona.ui.main.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.data.model.Tutorial
import com.example.serona.data.repository.TutorialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialDetailViewModel @Inject constructor(
    private val repository: TutorialRepository
) : ViewModel() {

    // StateFlow untuk detail tutorial
    private val _tutorial = MutableStateFlow<Tutorial?>(null)
    val tutorial: StateFlow<Tutorial?> = _tutorial

    // fetch tutorial by ID
    fun fetchTutorial(id: Int) {
        viewModelScope.launch {
            _tutorial.value = repository.getTutorialById(id)
        }
    }
}
