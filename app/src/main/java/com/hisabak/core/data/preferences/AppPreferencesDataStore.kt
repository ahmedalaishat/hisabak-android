package com.hisabak.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.hisabak.core.domain.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hisabak_prefs")

class AppPreferencesDataStore(private val context: Context) : AppPreferences {

    private val onboardingKey = booleanPreferencesKey("onboarding_completed")

    override val onboardingCompleted: Flow<Boolean> =
        context.dataStore.data.map { it[onboardingKey] ?: false }

    override suspend fun setOnboardingCompleted(value: Boolean) {
        context.dataStore.edit { it[onboardingKey] = value }
    }
}
