package com.hisabak.feature.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.hisabak.core.presentation.BaseViewModel
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.dashboard.domain.usecase.GetDashboardMetricsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DashboardViewModel(
    private val getMetrics: GetDashboardMetricsUseCase,
) : BaseViewModel<DashboardIntent, DashboardUiState, DashboardEffect>() {

    override fun initialState() = DashboardUiState()

    init {
        getMetrics()
            .onEach { snapshot ->
                setState {
                    copy(
                        snapshot = snapshot,
                        isLoading = false,
                        overallTrendCategoryId = overallTrendCategoryId
                            ?: snapshot.categoryOptions.firstOrNull { it.type == CategoryType.INCOME }?.id
                            ?: snapshot.categoryOptions.firstOrNull()?.id,
                        dailyTrendCategoryId = dailyTrendCategoryId
                            ?: snapshot.categoryOptions.firstOrNull { it.type == CategoryType.EXPENSES }?.id
                            ?: snapshot.categoryOptions.firstOrNull()?.id,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.OverallTrendCategoryChanged ->
                setState { copy(overallTrendCategoryId = intent.id) }
            is DashboardIntent.DailyTrendCategoryChanged ->
                setState { copy(dailyTrendCategoryId = intent.id) }
        }
    }
}
