package com.hisabak.core.domain

import kotlinx.coroutines.flow.Flow

/** Lightweight on-device app preferences (DataStore-backed). */
interface AppPreferences {
    /** Whether the user has finished the first-launch onboarding. */
    val onboardingCompleted: Flow<Boolean>

    suspend fun setOnboardingCompleted(value: Boolean)

    /** The chosen appearance; defaults to [ThemeMode.SYSTEM]. */
    val themeMode: Flow<ThemeMode>

    suspend fun setThemeMode(value: ThemeMode)
}
