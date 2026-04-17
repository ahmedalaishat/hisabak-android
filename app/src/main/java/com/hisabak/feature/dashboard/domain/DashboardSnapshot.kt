package com.hisabak.feature.dashboard.domain

import com.hisabak.core.common.Money
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryType
import java.time.LocalDate

data class DashboardSnapshot(
    val netWorth: Money,
    val netWorthSeries: List<MonthPoint>,
    val totalCash: Money,
    val totalSavings: Money,
    val totalInvestment: Money,
    val incomeMonth: Money,
    val incomeTrendPct: Double?,
    val expenseMonth: Money,
    val expenseTrendPct: Double?,
    val incomeDaily: List<DayPoint>,
    val expenseDaily: List<DayPoint>,
    val incomeByCategory: List<CategoryShare>,
    val expenseByCategory: List<CategoryShare>,
    val categoryOptions: List<CategoryOption>,
    val overallTrendByCategory: Map<CategoryId, List<MonthPoint>>,
    val dailyTrendByCategory: Map<CategoryId, List<DayPoint>>,
    val expenseByBrand: List<BrandShare>,
    val topBrandTrend: List<MonthPoint>,
    val topBrandName: String?,
)

data class CategoryOption(
    val id: CategoryId,
    val name: String,
    val color: String,
    val type: CategoryType,
)

data class MonthPoint(val monthStart: LocalDate, val amountMinor: Long)

data class DayPoint(val day: LocalDate, val amountMinor: Long)

data class CategoryShare(
    val id: CategoryId,
    val name: String,
    val color: String,
    val amount: Money,
    val pct: Double,
)

data class BrandShare(
    val id: BrandId,
    val name: String,
    val color: String?,
    val amount: Money,
    val pct: Double,
)
