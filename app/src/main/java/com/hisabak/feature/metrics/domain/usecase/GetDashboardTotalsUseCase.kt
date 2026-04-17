package com.hisabak.feature.metrics.domain.usecase

import com.hisabak.feature.metrics.domain.MetricsRepository
import com.hisabak.feature.metrics.domain.Period
import com.hisabak.feature.metrics.domain.ScalarMetric

data class DashboardTotals(
    val income: ScalarMetric,
    val expenses: ScalarMetric,
    val savings: ScalarMetric,
    val investment: ScalarMetric,
    val cash: ScalarMetric,
    val netWorth: ScalarMetric,
)

class GetDashboardTotalsUseCase(private val repository: MetricsRepository) {
    suspend operator fun invoke(period: Period): DashboardTotals = DashboardTotals(
        income = repository.totalIncome(period),
        expenses = repository.totalExpenses(period),
        savings = repository.totalSavings(period),
        investment = repository.totalInvestment(period),
        cash = repository.totalCash(period),
        netWorth = repository.netWorth(period),
    )
}
