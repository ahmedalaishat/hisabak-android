package com.hisabak.feature.metrics.domain

import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.core.common.Money
import java.time.LocalDate

data class ScalarMetric(val value: Money, val previousValue: Money? = null) {
    val changeRatio: Double?
        get() = previousValue?.let {
            if (it.isZero) null else (value.amountMinor - it.amountMinor) / it.amountMinor.toDouble()
        }
}

data class TrendPoint(val date: LocalDate, val value: Money)

data class Trend(val points: List<TrendPoint>)

data class CategoryBreakdown(val categoryId: CategoryId, val name: String, val total: Money, val count: Long)

data class BrandBreakdown(val brandId: BrandId, val name: String, val total: Money, val count: Long)

data class TransactionStats(
    val highest: Money,
    val lowest: Money,
    val average: Money,
    val stdDev: Double,
    val count: Long,
)

data class CirclePackNode(
    val id: String,
    val label: String,
    val value: Money,
    val children: List<CirclePackNode> = emptyList(),
)
