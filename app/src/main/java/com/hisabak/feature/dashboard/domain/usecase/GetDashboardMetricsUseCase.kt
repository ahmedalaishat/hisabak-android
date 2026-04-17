package com.hisabak.feature.dashboard.domain.usecase

import com.hisabak.core.common.Clock
import com.hisabak.core.common.Currency
import com.hisabak.core.common.Money
import com.hisabak.feature.brand.domain.Brand
import com.hisabak.feature.brand.domain.usecase.ObserveBrandsUseCase
import com.hisabak.feature.category.domain.Category
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.category.domain.usecase.ObserveCategoriesUseCase
import com.hisabak.feature.dashboard.domain.BrandShare
import com.hisabak.feature.dashboard.domain.CategoryShare
import com.hisabak.feature.dashboard.domain.DashboardSnapshot
import com.hisabak.feature.dashboard.domain.DayPoint
import com.hisabak.feature.dashboard.domain.MonthPoint
import com.hisabak.feature.transaction.domain.Transaction
import com.hisabak.feature.transaction.domain.usecase.ObserveTransactionsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class GetDashboardMetricsUseCase(
    private val observeTransactions: ObserveTransactionsUseCase,
    private val observeCategories: ObserveCategoriesUseCase,
    private val observeBrands: ObserveBrandsUseCase,
    private val currency: Currency,
    private val clock: Clock,
) {
    operator fun invoke(): Flow<DashboardSnapshot> =
        combine(observeTransactions(), observeCategories(), observeBrands()) { txs, cats, brands ->
            compute(txs, cats, brands)
        }

    private fun compute(
        transactions: List<Transaction>,
        categories: List<Category>,
        brands: List<Brand>,
    ): DashboardSnapshot {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.ofInstant(clock.now(), zone)
        val currentMonth = YearMonth.from(today)
        val prevMonth = currentMonth.minusMonths(1)

        val catById = categories.associateBy { it.id }
        val brandById = brands.associateBy { it.id }

        fun typeOf(tx: Transaction): CategoryType? =
            brandById[tx.brandId]?.categoryId?.let { catById[it]?.type }

        val byType = transactions.groupBy { typeOf(it) }
        fun sumMinorFor(type: CategoryType?): Long =
            byType[type].orEmpty().sumOf { it.amount.amountMinor }

        val totalIncome = sumMinorFor(CategoryType.INCOME)
        val totalExpense = sumMinorFor(CategoryType.EXPENSES)
        val totalSavings = sumMinorFor(CategoryType.SAVINGS)
        val totalInvestment = sumMinorFor(CategoryType.INVESTMENT)
        val totalCash = totalIncome - totalExpense - totalSavings - totalInvestment
        val netWorth = totalIncome - totalExpense

        val netWorthSeries = buildMonthlyRunningTotal(transactions, zone) { tx ->
            when (typeOf(tx)) {
                CategoryType.INCOME -> tx.amount.amountMinor
                CategoryType.EXPENSES -> -tx.amount.amountMinor
                else -> 0L
            }
        }

        val monthTxs = transactions.filter {
            YearMonth.from(LocalDate.ofInstant(it.occurredAt, zone)) == currentMonth
        }
        val prevMonthTxs = transactions.filter {
            YearMonth.from(LocalDate.ofInstant(it.occurredAt, zone)) == prevMonth
        }

        val incomeMonth = monthTxs.filter { typeOf(it) == CategoryType.INCOME }.sumOf { it.amount.amountMinor }
        val expenseMonth = monthTxs.filter { typeOf(it) == CategoryType.EXPENSES }.sumOf { it.amount.amountMinor }
        val incomePrev = prevMonthTxs.filter { typeOf(it) == CategoryType.INCOME }.sumOf { it.amount.amountMinor }
        val expensePrev = prevMonthTxs.filter { typeOf(it) == CategoryType.EXPENSES }.sumOf { it.amount.amountMinor }

        val incomeDaily = dailySeriesForMonth(monthTxs, zone, currentMonth) { typeOf(it) == CategoryType.INCOME }
        val expenseDaily = dailySeriesForMonth(monthTxs, zone, currentMonth) { typeOf(it) == CategoryType.EXPENSES }

        val incomeByCategory = breakdown(
            transactions = monthTxs.filter { typeOf(it) == CategoryType.INCOME },
            categoryOf = { brandById[it.brandId]?.categoryId?.let(catById::get) },
            currency = currency,
        )
        val expenseByCategory = breakdown(
            transactions = monthTxs.filter { typeOf(it) == CategoryType.EXPENSES },
            categoryOf = { brandById[it.brandId]?.categoryId?.let(catById::get) },
            currency = currency,
        )

        val overallIncomeTrend = buildMonthlySum(
            transactions.filter { typeOf(it) == CategoryType.INCOME },
            zone,
        )
        val dailyExpenseTrend = dailySeriesForMonth(monthTxs, zone, currentMonth) { typeOf(it) == CategoryType.EXPENSES }

        val expenseByBrand = brandBreakdown(
            transactions = monthTxs.filter { typeOf(it) == CategoryType.EXPENSES },
            brandById = brandById,
            catById = catById,
            currency = currency,
        )
        val topBrand = expenseByBrand.firstOrNull()
        val topBrandTrend = if (topBrand != null) {
            buildMonthlySum(transactions.filter { it.brandId == topBrand.id }, zone)
        } else emptyList()

        return DashboardSnapshot(
            netWorth = Money(netWorth, currency),
            netWorthSeries = netWorthSeries,
            totalCash = Money(totalCash, currency),
            totalSavings = Money(totalSavings, currency),
            totalInvestment = Money(totalInvestment, currency),
            incomeMonth = Money(incomeMonth, currency),
            incomeTrendPct = pctChange(incomePrev, incomeMonth),
            expenseMonth = Money(expenseMonth, currency),
            expenseTrendPct = pctChange(expensePrev, expenseMonth),
            incomeDaily = incomeDaily,
            expenseDaily = expenseDaily,
            incomeByCategory = incomeByCategory,
            expenseByCategory = expenseByCategory,
            overallIncomeTrend = overallIncomeTrend,
            dailyExpenseTrend = dailyExpenseTrend,
            expenseByBrand = expenseByBrand,
            topBrandTrend = topBrandTrend,
            topBrandName = topBrand?.name,
        )
    }

    private fun pctChange(prev: Long, current: Long): Double? {
        if (prev == 0L) return null
        return ((current - prev).toDouble() / prev.toDouble()) * 100.0
    }

    private fun buildMonthlyRunningTotal(
        transactions: List<Transaction>,
        zone: ZoneId,
        signedMinor: (Transaction) -> Long,
    ): List<MonthPoint> {
        if (transactions.isEmpty()) return emptyList()
        val byMonth = transactions.groupBy { YearMonth.from(LocalDate.ofInstant(it.occurredAt, zone)) }
            .mapValues { (_, list) -> list.sumOf(signedMinor) }
        val sorted = byMonth.keys.sorted()
        if (sorted.isEmpty()) return emptyList()
        val start = sorted.first()
        val end = sorted.last()
        val points = mutableListOf<MonthPoint>()
        var running = 0L
        var cursor = start
        while (!cursor.isAfter(end)) {
            running += byMonth[cursor] ?: 0L
            points += MonthPoint(cursor.atDay(1), running)
            cursor = cursor.plusMonths(1)
        }
        return points
    }

    private fun buildMonthlySum(transactions: List<Transaction>, zone: ZoneId): List<MonthPoint> {
        if (transactions.isEmpty()) return emptyList()
        val byMonth = transactions.groupBy { YearMonth.from(LocalDate.ofInstant(it.occurredAt, zone)) }
            .mapValues { (_, list) -> list.sumOf { it.amount.amountMinor } }
        val sorted = byMonth.keys.sorted()
        val start = sorted.first()
        val end = sorted.last()
        val points = mutableListOf<MonthPoint>()
        var cursor = start
        while (!cursor.isAfter(end)) {
            points += MonthPoint(cursor.atDay(1), byMonth[cursor] ?: 0L)
            cursor = cursor.plusMonths(1)
        }
        return points
    }

    private fun dailySeriesForMonth(
        transactions: List<Transaction>,
        zone: ZoneId,
        month: YearMonth,
        predicate: (Transaction) -> Boolean,
    ): List<DayPoint> {
        val byDay = transactions.filter(predicate)
            .groupBy { LocalDate.ofInstant(it.occurredAt, zone) }
            .mapValues { (_, list) -> list.sumOf { it.amount.amountMinor } }
        val days = (1..month.lengthOfMonth()).map { month.atDay(it) }
        return days.map { DayPoint(it, byDay[it] ?: 0L) }
    }

    private fun breakdown(
        transactions: List<Transaction>,
        categoryOf: (Transaction) -> Category?,
        currency: Currency,
    ): List<CategoryShare> {
        val grouped = transactions.groupBy { categoryOf(it) }
        val total = grouped.values.sumOf { list -> list.sumOf { it.amount.amountMinor } }
        if (total == 0L) return emptyList()
        return grouped.mapNotNull { (cat, list) ->
            if (cat == null) return@mapNotNull null
            val sum = list.sumOf { it.amount.amountMinor }
            CategoryShare(
                id = cat.id,
                name = cat.name,
                color = cat.color,
                amount = Money(sum, currency),
                pct = sum.toDouble() / total.toDouble(),
            )
        }.sortedByDescending { it.amount.amountMinor }
    }

    private fun brandBreakdown(
        transactions: List<Transaction>,
        brandById: Map<com.hisabak.feature.brand.domain.BrandId, Brand>,
        catById: Map<com.hisabak.feature.category.domain.CategoryId, Category>,
        currency: Currency,
    ): List<BrandShare> {
        val grouped = transactions.groupBy { it.brandId }
        val total = grouped.values.sumOf { list -> list.sumOf { it.amount.amountMinor } }
        if (total == 0L) return emptyList()
        return grouped.mapNotNull { (brandId, list) ->
            val brand = brandById[brandId] ?: return@mapNotNull null
            val sum = list.sumOf { it.amount.amountMinor }
            BrandShare(
                id = brand.id,
                name = brand.name,
                color = brand.categoryId?.let { catById[it]?.color },
                amount = Money(sum, currency),
                pct = sum.toDouble() / total.toDouble(),
            )
        }.sortedByDescending { it.amount.amountMinor }
    }
}
