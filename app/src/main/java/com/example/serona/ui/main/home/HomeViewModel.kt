package com.example.serona.ui.main.home

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.R
import com.example.serona.data.repository.UserRepository
import com.example.serona.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userSession: UserSession,
    private val userRepo: UserRepository,
    private val mapper: HomeContentMapper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = userSession.user
        .map { user ->
            val scanned = !user?.faceShape.isNullOrEmpty() && !user?.skinTone.isNullOrEmpty()

            HomeUiState(
                userName = user?.name ?: "Guest",
                hasScanned = scanned,
                faceHeader = if (scanned) context.getString(R.string.face_detected_header, user?.faceShape)
                            else context.getString(R.string.face_not_detected),
                faceBody = mapper.getFaceDescription(user?.faceShape),
                skinHeader = if (scanned) context.getString(R.string.skin_detected_header, user?.skinTone)
                            else context.getString(R.string.skin_not_detected),
                skinBody = mapper.getSkinDescription(user?.skinTone),
                skinColor = mapper.getSkinColor(user?.skinTone),
                guideTitle = if (scanned) context.getString(R.string.guide_title_post_scan)
                            else context.getString(R.string.guide_title_pre_scan),
                guideItems = mapper.getGuideItems(scanned)
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        checkAndRefreshSession()
    }

    private fun checkAndRefreshSession() {
        viewModelScope.launch {
            // 1. Cek apakah di memori sudah ada data user?
            if (userSession.user.value == null) {
                // 2. Jika kosong (misal baru buka app), paksa ambil dari API
                val result = userRepo.getProfile()
                if (result.isSuccess) {
                    val user = result.getOrNull()
                    if (user != null) {
                        userSession.setUser(user)
                    }
                } else {
                    // 3. Jika gagal fetch (misal internet mati), tandai initialized
                    // agar UI tidak nunggu terus
                    userSession.markInitialized()
                }
            }
        }
    }
}