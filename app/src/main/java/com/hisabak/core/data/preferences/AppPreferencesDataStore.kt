package com.hisabak.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hisabak.core.domain.AppPreferences
import com.hisabak.core.domain.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hisabak_prefs")

class AppPreferencesDataStore(private val context: Context) : AppPreferences {

    private val onboardingKey = booleanPreferencesKey("onboarding_completed")
    private val themeModeKey = stringPreferencesKey("theme_mode")
    private val appLockKey = booleanPreferencesKey("app_lock_enabled")

    override val onboardingCompleted: Flow<Boolean> =
        context.dataStore.data.map { it[onboardingKey] ?: false }

    override suspend fun setOnboardingCompleted(value: Boolean) {
        context.dataStore.edit { it[onboardingKey] = value }
    }

    override val themeMode: Flow<ThemeMode> =
        context.dataStore.data.map { prefs ->
            prefs[themeModeKey]?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() } ?: ThemeMode.SYSTEM
        }

    override suspend fun setThemeMode(value: ThemeMode) {
        context.dataStore.edit { it[themeModeKey] = value.name }
    }

    override val appLockEnabled: Flow<Boolean> =
        context.dataStore.data.map { it[appLockKey] ?: false }

    override suspend fun setAppLockEnabled(value: Boolean) {
        context.dataStore.edit { it[appLockKey] = value }
    }
}
