package com.hisabak.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hisabak.core.domain.AppPreferences
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val preferences: AppPreferences,
) : ViewModel() {

    /** Marks onboarding done; the app's first-launch gate then swaps to the main UI. */
    fun complete() {
        viewModelScope.launch { preferences.setOnboardingCompleted(true) }
    }
}
