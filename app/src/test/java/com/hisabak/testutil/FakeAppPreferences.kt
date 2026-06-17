package com.hisabak.testutil

import com.hisabak.core.domain.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppPreferences(initial: Boolean = false) : AppPreferences {
    private val flow = MutableStateFlow(initial)
    override val onboardingCompleted: Flow<Boolean> = flow
    override suspend fun setOnboardingCompleted(value: Boolean) { flow.value = value }
}
