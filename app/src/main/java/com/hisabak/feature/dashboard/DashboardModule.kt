package com.hisabak.feature.dashboard

import com.hisabak.feature.dashboard.domain.usecase.GetDashboardMetricsUseCase
import com.hisabak.feature.dashboard.presentation.DashboardViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dashboardModule = module {
    factory {
        GetDashboardMetricsUseCase(
            observeTransactions = get(),
            observeCategories = get(),
            observeBrands = get(),
            currency = get(),
            clock = get(),
        )
    }
    viewModel { DashboardViewModel(getMetrics = get()) }
}
