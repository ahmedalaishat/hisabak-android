package com.hisabak.feature.metrics.domain.usecase

import com.hisabak.feature.metrics.domain.CategoryBreakdown
import com.hisabak.feature.metrics.domain.MetricsRepository
import com.hisabak.feature.metrics.domain.Period

class GetExpensesByCategoryUseCase(private val repository: MetricsRepository) {
    suspend operator fun invoke(period: Period): List<CategoryBreakdown> =
        repository.expensesByCategory(period)
}
