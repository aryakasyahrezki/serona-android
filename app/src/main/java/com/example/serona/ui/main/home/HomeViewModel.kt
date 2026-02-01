package com.example.serona.ui.main.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serona.R
import com.example.serona.data.local.PreferencesManager
import com.example.serona.data.repository.UserRepository
import com.example.serona.data.session.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val mapper: HomeContentMapper,
    private val prefs: PreferencesManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _manualTooltipTrigger = MutableStateFlow(false)
    private val _dialogDismissed = MutableStateFlow(false)


    val uiState: StateFlow<HomeUiState> = combine(
        userRepo.userDataFlow,
        _manualTooltipTrigger ,
        _dialogDismissed
    ){ user, showTooltip, dismissed->
            val scanned = !user?.faceShape.isNullOrEmpty() && !user?.skinTone.isNullOrEmpty()
            val userEmail = user?.email ?: ""

            val shouldShowDialog = !scanned && prefs.isFirstLaunch(userEmail) && !dismissed

            HomeUiState(
                userName = user?.name ?: "Guest",
                userEmail = userEmail,
                showScanDialog = shouldShowDialog,
                showScanTooltip = showTooltip,
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
        refreshDataFromApi()
    }

    private fun refreshDataFromApi() {
        viewModelScope.launch {
            userRepo.syncFullProfile()
        }
    }

    fun dismissScanDialog() {
        val currentUser = uiState.value.userEmail
        prefs.setFirstLaunchCompleted(currentUser)
        _dialogDismissed.value = true
    }

    fun triggerScanTooltip() {
        val email = uiState.value.userEmail
        if (email.isNotEmpty()) {
            prefs.setFirstLaunchCompleted(email)
        }
        viewModelScope.launch {
            _manualTooltipTrigger.value = true
            _dialogDismissed.value = true
        }
    }

    fun dismissTooltip() {
        _manualTooltipTrigger.value = false
    }
}