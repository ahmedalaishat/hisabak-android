package com.hisabak.feature.dashboard.presentation

import com.hisabak.core.presentation.ViewEffect
import com.hisabak.core.presentation.ViewIntent
import com.hisabak.core.presentation.ViewState
import com.hisabak.feature.dashboard.domain.DashboardSnapshot

data class DashboardUiState(
    val snapshot: DashboardSnapshot? = null,
    val isLoading: Boolean = true,
) : ViewState

sealed interface DashboardIntent : ViewIntent

sealed interface DashboardEffect : ViewEffect
