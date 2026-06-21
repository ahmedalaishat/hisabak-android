package com.hisabak.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hisabak.core.domain.AppPreferences
import com.hisabak.core.domain.ThemeMode
import com.hisabak.core.domain.analytics.Analytics
import com.hisabak.core.domain.analytics.AnalyticsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferences: AppPreferences,
    private val analytics: Analytics,
) : ViewModel() {

    val themeMode: Flow<ThemeMode> = preferences.themeMode

    fun setThemeMode(value: ThemeMode) {
        analytics.log(AnalyticsEvent.SettingsThemeChanged(value.name.lowercase()))
        viewModelScope.launch { preferences.setThemeMode(value) }
    }

    /** The locale switch itself happens in the UI layer (AppCompatDelegate); this only records it. */
    fun onLanguageSelected(tag: String) {
        analytics.log(AnalyticsEvent.SettingsLanguageChanged(tag))
    }
}
