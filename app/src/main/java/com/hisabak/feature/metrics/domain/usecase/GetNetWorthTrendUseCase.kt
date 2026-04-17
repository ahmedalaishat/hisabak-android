package com.hisabak.feature.metrics.domain.usecase

import com.hisabak.feature.metrics.domain.MetricsRepository
import com.hisabak.feature.metrics.domain.Period
import com.hisabak.feature.metrics.domain.Trend

class GetNetWorthTrendUseCase(private val repository: MetricsRepository) {
    suspend operator fun invoke(period: Period): Trend = repository.netWorthTrend(period)
}
