package com.hisabak.feature.metrics.domain.usecase

import com.hisabak.feature.metrics.domain.BrandBreakdown
import com.hisabak.feature.metrics.domain.MetricsRepository
import com.hisabak.feature.metrics.domain.Period

class GetSpendingByBrandUseCase(private val repository: MetricsRepository) {
    suspend operator fun invoke(period: Period): List<BrandBreakdown> =
        repository.spendingByBrand(period)
}
