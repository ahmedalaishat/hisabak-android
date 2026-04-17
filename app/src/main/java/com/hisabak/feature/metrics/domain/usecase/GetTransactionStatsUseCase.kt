package com.hisabak.feature.metrics.domain.usecase

import com.hisabak.feature.metrics.domain.MetricsRepository
import com.hisabak.feature.metrics.domain.Period
import com.hisabak.feature.metrics.domain.TransactionStats

data class PeriodTransactionStats(
    val count: Long,
    val highest: com.hisabak.feature.metrics.domain.ScalarMetric,
    val lowest: com.hisabak.feature.metrics.domain.ScalarMetric,
    val average: com.hisabak.feature.metrics.domain.ScalarMetric,
    val stdDev: Double,
)

class GetTransactionStatsUseCase(private val repository: MetricsRepository) {
    suspend operator fun invoke(period: Period): PeriodTransactionStats = PeriodTransactionStats(
        count = repository.transactionsCount(period),
        highest = repository.highestTransaction(period),
        lowest = repository.lowestTransaction(period),
        average = repository.averageTransaction(period),
        stdDev = repository.transactionsStdDev(period),
    )
}
