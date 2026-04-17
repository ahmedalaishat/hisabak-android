package com.hisabak.feature.dashboard.presentation

import com.hisabak.core.presentation.ViewEffect
import com.hisabak.core.presentation.ViewIntent
import com.hisabak.core.presentation.ViewState
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.dashboard.domain.DashboardSnapshot

data class DashboardUiState(
    val snapshot: DashboardSnapshot? = null,
    val isLoading: Boolean = true,
    val overallTrendCategoryId: CategoryId? = null,
    val dailyTrendCategoryId: CategoryId? = null,
) : ViewState

sealed interface DashboardIntent : ViewIntent {
    data class OverallTrendCategoryChanged(val id: CategoryId) : DashboardIntent
    data class DailyTrendCategoryChanged(val id: CategoryId) : DashboardIntent
}

sealed interface DashboardEffect : ViewEffect
