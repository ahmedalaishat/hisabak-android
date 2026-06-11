package com.hisabak.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hisabak.core.common.Money
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.presentation.CategoryStyle
import com.hisabak.feature.dashboard.domain.BrandShare
import com.hisabak.feature.dashboard.domain.CategoryOption
import com.hisabak.feature.dashboard.domain.CategoryShare
import com.hisabak.feature.dashboard.domain.DashboardSnapshot
import com.hisabak.feature.dashboard.domain.DayPoint
import com.hisabak.feature.dashboard.domain.MonthPoint
import com.hisabak.feature.dashboard.presentation.components.AreaLineChart
import com.hisabak.feature.dashboard.presentation.components.BarSparkline
import com.hisabak.feature.dashboard.presentation.components.DonutChart
import com.hisabak.feature.dashboard.presentation.components.DonutSlice
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.theme.HisabakTheme
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onOverallCategoryChanged: (CategoryId) -> Unit,
    onDailyCategoryChanged: (CategoryId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snap = state.snapshot
    if (snap == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                "Dashboard",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        item { NetWorthCard(snap) }
        item { TotalsRow(snap) }
        item { IncomeExpenseRow(snap) }
        item { OverTimeRow(snap) }
        item { SectionHeader(title = "Categories Analytics") }
        item { CategoryDonutsRow(snap) }
        item {
            CategoryTrendsRow(
                snap = snap,
                overallCategoryId = state.overallTrendCategoryId,
                dailyCategoryId = state.dailyTrendCategoryId,
                onOverallCategoryChanged = onOverallCategoryChanged,
                onDailyCategoryChanged = onDailyCategoryChanged,
            )
        }
        item { SectionHeader(title = "Brands Analytics") }
        item { BrandRow(snap) }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    SurfaceCard(modifier = modifier, contentPadding = 16.dp, content = content)
}

@Composable
private fun NetWorthCard(snap: DashboardSnapshot) {
    MetricCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Net Worth Over Time",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            formatCompactMoney(snap.netWorth),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (snap.netWorthSeries.isNotEmpty()) {
            AreaLineChart(
                values = snap.netWorthSeries.map { it.amountMinor / 100.0 },
                lineColor = MaterialTheme.colorScheme.primary,
                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun TotalsRow(snap: DashboardSnapshot) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SmallTotalCard("Total Cash", snap.totalCash, Modifier.weight(1f))
        SmallTotalCard("Total Savings", snap.totalSavings, Modifier.weight(1f))
        SmallTotalCard("Total Investment", snap.totalInvestment, Modifier.weight(1f))
    }
}

@Composable
private fun SmallTotalCard(title: String, money: Money, modifier: Modifier = Modifier) {
    MetricCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            formatCompactMoney(money),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun IncomeExpenseRow(snap: DashboardSnapshot) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        KpiCard(
            title = "Total Income",
            money = snap.incomeMonth,
            trendPct = snap.incomeTrendPct,
            trendPositiveIsGood = true,
            modifier = Modifier.weight(1f),
        )
        KpiCard(
            title = "Total Expenses",
            money = snap.expenseMonth,
            trendPct = snap.expenseTrendPct,
            trendPositiveIsGood = false,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun KpiCard(
    title: String,
    money: Money,
    trendPct: Double?,
    trendPositiveIsGood: Boolean,
    modifier: Modifier = Modifier,
) {
    MetricCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            formatCompactMoney(money),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        TrendIndicator(pct = trendPct, positiveIsGood = trendPositiveIsGood)
    }
}

@Composable
private fun TrendIndicator(pct: Double?, positiveIsGood: Boolean) {
    if (pct == null || abs(pct) < 0.01) {
        Text("No Change", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    val isUp = pct > 0
    val good = if (positiveIsGood) isUp else !isUp
    val color = if (good) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val icon = when {
        isUp -> Icons.Filled.TrendingUp
        pct < 0 -> Icons.Filled.TrendingDown
        else -> Icons.AutoMirrored.Filled.TrendingFlat
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Text(
            text = " %.2f%% %s".format(abs(pct), if (isUp) "Increase" else "Decrease"),
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
    }
}

@Composable
private fun OverTimeRow(snap: DashboardSnapshot) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OverTimeCard(
            title = "Income Over Time",
            money = snap.incomeMonth,
            series = snap.incomeDaily,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
        OverTimeCard(
            title = "Spending Over Time",
            money = snap.expenseMonth,
            series = snap.expenseDaily,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun OverTimeCard(
    title: String,
    money: Money,
    series: List<DayPoint>,
    color: Color,
    modifier: Modifier = Modifier,
) {
    MetricCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            formatCompactMoney(money),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (series.isNotEmpty()) {
            BarSparkline(
                values = series.map { it.amountMinor / 100.0 },
                barColor = color,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun CategoryDonutsRow(snap: DashboardSnapshot) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        DonutBreakdownCard(
            title = "Income Sources",
            shares = snap.incomeByCategory,
            modifier = Modifier.weight(1f),
        )
        DonutBreakdownCard(
            title = "Spending by Category",
            shares = snap.expenseByCategory,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun DonutBreakdownCard(
    title: String,
    shares: List<CategoryShare>,
    modifier: Modifier = Modifier,
) {
    MetricCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (shares.isEmpty()) {
            Text("—", style = MaterialTheme.typography.titleMedium)
            return@MetricCard
        }
        DonutChart(
            slices = shares.map { DonutSlice(it.pct, CategoryStyle.color(it.color)) },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp),
        )
        shares.take(4).forEach { share ->
            LegendRow(color = CategoryStyle.color(share.color), label = share.name, pct = share.pct)
        }
    }
}

@Composable
private fun LegendRow(color: Color, label: String, pct: Double) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 1.dp)) {
        Box(Modifier.size(8.dp).background(color, CircleShape))
        Text(
            text = " %s • %.0f%%".format(label, pct * 100),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun CategoryTrendsRow(
    snap: DashboardSnapshot,
    overallCategoryId: CategoryId?,
    dailyCategoryId: CategoryId?,
    onOverallCategoryChanged: (CategoryId) -> Unit,
    onDailyCategoryChanged: (CategoryId) -> Unit,
) {
    val overallPoints = overallCategoryId?.let { snap.overallTrendByCategory[it] }.orEmpty()
    val dailyPoints = dailyCategoryId?.let { snap.dailyTrendByCategory[it] }.orEmpty()
    val overallTotal = overallPoints.sumOf { it.amountMinor }
    val dailyTotal = dailyPoints.sumOf { it.amountMinor }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryTrendCard(
            title = "Overall Trend by Category",
            totalLabel = formatCompactUnits(overallTotal),
            values = overallPoints.map { it.amountMinor / 100.0 },
            color = MaterialTheme.colorScheme.primary,
            options = snap.categoryOptions,
            selectedId = overallCategoryId,
            onSelected = onOverallCategoryChanged,
            modifier = Modifier.weight(1f),
        )
        CategoryTrendCard(
            title = "Daily Trend by Category",
            totalLabel = formatCompactUnits(dailyTotal),
            values = dailyPoints.map { it.amountMinor / 100.0 },
            color = MaterialTheme.colorScheme.error,
            options = snap.categoryOptions,
            selectedId = dailyCategoryId,
            onSelected = onDailyCategoryChanged,
            modifier = Modifier.weight(1f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryTrendCard(
    title: String,
    totalLabel: String,
    values: List<Double>,
    color: Color,
    options: List<CategoryOption>,
    selectedId: CategoryId?,
    onSelected: (CategoryId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selected = options.firstOrNull { it.id == selectedId }
    var expanded by remember { mutableStateOf(false) }
    MetricCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            OutlinedTextField(
                value = selected?.name ?: "—",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        leadingIcon = {
                            Box(Modifier.size(10.dp).background(
                                CategoryStyle.color(option.color), CircleShape))
                        },
                        onClick = {
                            onSelected(option.id)
                            expanded = false
                        },
                    )
                }
            }
        }
        Text(
            totalLabel,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp),
        )
        if (values.isNotEmpty()) {
            AreaLineChart(
                values = values,
                lineColor = color,
                fillColor = color.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun BrandRow(snap: DashboardSnapshot) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        BrandDonutCard(
            title = "Spending by Brand",
            shares = snap.expenseByBrand,
            modifier = Modifier.weight(1f),
        )
        TrendCard(
            title = "Top Brand — ${snap.topBrandName ?: "—"}",
            totalLabel = totalOf(snap.topBrandTrend),
            values = snap.topBrandTrend.map { it.amountMinor / 100.0 },
            color = HisabakTheme.colors.investment,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun BrandDonutCard(
    title: String,
    shares: List<BrandShare>,
    modifier: Modifier = Modifier,
) {
    MetricCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (shares.isEmpty()) {
            Text("—", style = MaterialTheme.typography.titleMedium)
            return@MetricCard
        }
        DonutChart(
            slices = shares.map { DonutSlice(it.pct, CategoryStyle.color(it.color)) },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp),
        )
        shares.take(4).forEach { share ->
            LegendRow(color = CategoryStyle.color(share.color), label = share.name, pct = share.pct)
        }
    }
}

@Composable
private fun TrendCard(
    title: String,
    totalLabel: String,
    values: List<Double>,
    color: Color,
    modifier: Modifier = Modifier,
) {
    MetricCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(totalLabel, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        if (values.isNotEmpty()) {
            AreaLineChart(
                values = values,
                lineColor = color,
                fillColor = color.copy(alpha = 0.15f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            )
        }
    }
}

private fun totalOf(points: List<MonthPoint>): String {
    val total = points.sumOf { it.amountMinor }
    return formatCompactUnits(total)
}

private fun formatCompactMoney(money: Money): String =
    "${money.currency.code} ${formatCompactUnits(money.amountMinor)}"

private fun formatCompactUnits(amountMinor: Long): String {
    val major = amountMinor.toDouble() / 100.0
    val abs = kotlin.math.abs(major)
    return when {
        abs >= 1_000_000 -> "%.3fM".format(major / 1_000_000.0)
        abs >= 1_000 -> "%.3fk".format(major / 1_000.0)
        else -> "%.2f".format(major)
    }
}

