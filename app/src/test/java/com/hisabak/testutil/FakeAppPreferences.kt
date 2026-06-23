package com.hisabak.testutil

import com.hisabak.core.domain.AppPreferences
import com.hisabak.core.domain.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppPreferences(
    initial: Boolean = false,
    initialThemeMode: ThemeMode = ThemeMode.SYSTEM,
) : AppPreferences {
    private val flow = MutableStateFlow(initial)
    override val onboardingCompleted: Flow<Boolean> = flow
    override suspend fun setOnboardingCompleted(value: Boolean) { flow.value = value }

    private val themeFlow = MutableStateFlow(initialThemeMode)
    override val themeMode: Flow<ThemeMode> = themeFlow
    override suspend fun setThemeMode(value: ThemeMode) { themeFlow.value = value }

    private val appLockFlow = MutableStateFlow(false)
    override val appLockEnabled: Flow<Boolean> = appLockFlow
    override suspend fun setAppLockEnabled(value: Boolean) { appLockFlow.value = value }
}
