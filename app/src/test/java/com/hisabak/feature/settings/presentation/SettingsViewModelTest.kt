package com.hisabak.feature.settings.presentation

import com.hisabak.core.domain.ThemeMode
import com.hisabak.testutil.FakeAnalytics
import com.hisabak.testutil.FakeAppPreferences
import com.hisabak.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `setThemeMode persists the choice and logs it`() = runTest {
        val prefs = FakeAppPreferences()
        val analytics = FakeAnalytics()
        val vm = SettingsViewModel(prefs, analytics)
        assertEquals(ThemeMode.SYSTEM, vm.themeMode.first())

        vm.setThemeMode(ThemeMode.DARK)
        advanceUntilIdle()

        assertEquals(ThemeMode.DARK, prefs.themeMode.first())
        assertEquals(listOf("settings_theme_changed"), analytics.names())
    }

    @Test
    fun `each theme mode round-trips through the preference`() = runTest {
        val prefs = FakeAppPreferences()
        val vm = SettingsViewModel(prefs, FakeAnalytics())

        for (mode in ThemeMode.entries) {
            vm.setThemeMode(mode)
            advanceUntilIdle()
            assertEquals(mode, prefs.themeMode.first())
        }
    }

    @Test
    fun `onLanguageSelected logs the chosen tag`() = runTest {
        val analytics = FakeAnalytics()
        val vm = SettingsViewModel(FakeAppPreferences(), analytics)

        vm.onLanguageSelected("ar")

        assertEquals(listOf("settings_language_changed"), analytics.names())
    }

    @Test
    fun `setAppLockEnabled persists the choice and logs it`() = runTest {
        val prefs = FakeAppPreferences()
        val analytics = FakeAnalytics()
        val vm = SettingsViewModel(prefs, analytics)
        assertEquals(false, vm.appLockEnabled.first())

        vm.setAppLockEnabled(true)
        advanceUntilIdle()

        assertEquals(true, prefs.appLockEnabled.first())
        assertEquals(listOf("app_lock_toggled"), analytics.names())
    }
}
