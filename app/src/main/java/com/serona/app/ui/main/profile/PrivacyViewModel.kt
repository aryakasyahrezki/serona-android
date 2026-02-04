package com.serona.app.ui.main.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.serona.app.data.model.PrivacyFaq
import com.serona.app.data.source.PrivacyDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    // Kelola data FAQ di sini
    private val _faqList = MutableStateFlow<List<PrivacyFaq>>(emptyList())
    val faqList: StateFlow<List<PrivacyFaq>> = _faqList

    init {
        // Ambil data saat ViewModel dibuat
        _faqList.value = PrivacyDataProvider(context).getFaqs()
    }
}