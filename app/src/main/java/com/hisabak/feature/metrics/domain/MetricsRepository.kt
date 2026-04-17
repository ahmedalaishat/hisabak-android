package com.hisabak.feature.metrics.domain

import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryId

interface MetricsRepository {
    suspend fun totalIncome(period: Period): ScalarMetric
    suspend fun totalExpenses(period: Period): ScalarMetric
    suspend fun totalSavings(period: Period): ScalarMetric
    suspend fun totalInvestment(period: Period): ScalarMetric
    suspend fun totalCash(period: Period): ScalarMetric
    suspend fun netWorth(period: Period): ScalarMetric

    suspend fun netWorthTrend(period: Period): Trend
    suspend fun totalIncomeTrend(period: Period): Trend
    suspend fun totalExpensesTrend(period: Period): Trend
    suspend fun categoryTrend(period: Period, categoryId: CategoryId): Trend
    suspend fun categoryDailyTrend(period: Period, categoryId: CategoryId): Trend
    suspend fun brandTrend(period: Period, brandId: BrandId): Trend
    suspend fun brandChangeRate(period: Period, brandId: BrandId): ScalarMetric

    suspend fun expensesByCategory(period: Period): List<CategoryBreakdown>
    suspend fun incomeByCategory(period: Period): List<CategoryBreakdown>
    suspend fun spendingByBrand(period: Period): List<BrandBreakdown>

    suspend fun transactionsCount(period: Period): Long
    suspend fun transactionsByCategory(period: Period): List<CategoryBreakdown>
    suspend fun transactionsByBrand(period: Period): List<BrandBreakdown>

    suspend fun highestTransaction(period: Period): ScalarMetric
    suspend fun lowestTransaction(period: Period): ScalarMetric
    suspend fun averageTransaction(period: Period): ScalarMetric
    suspend fun transactionsStdDev(period: Period): Double

    suspend fun brandStats(period: Period, brandId: BrandId): TransactionStats
    suspend fun categoryStats(period: Period, categoryId: CategoryId): TransactionStats

    suspend fun circlePack(period: Period): CirclePackNode
}
