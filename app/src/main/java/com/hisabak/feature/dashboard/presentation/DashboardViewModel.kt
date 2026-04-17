package com.hisabak.feature.dashboard.presentation

import androidx.lifecycle.viewModelScope
import com.hisabak.core.presentation.BaseViewModel
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
                setState { copy(snapshot = snapshot, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: DashboardIntent) {
        // No intents yet — dashboard is read-only in v1.
    }
}
