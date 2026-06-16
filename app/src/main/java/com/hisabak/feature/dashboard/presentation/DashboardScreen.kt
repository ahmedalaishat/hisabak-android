package com.hisabak.feature.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hisabak.core.common.Money
import com.hisabak.core.common.SummaryPeriod
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
import com.hisabak.feature.dashboard.presentation.components.GroupedBarChart
import com.hisabak.ui.components.MoneyText
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.theme.HisabakTheme
import com.hisabak.ui.theme.HisabakType
import com.hisabak.ui.theme.Sizing
import com.hisabak.ui.theme.Spacing
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onPeriodChange: (SummaryPeriod) -> Unit,
    onOverallCategoryChanged: (CategoryId) -> Unit,
    onDailyCategoryChanged: (CategoryId) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snap = state.snapshot
    if (snap == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val c = HisabakTheme.colors

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = Spacing.pageMargin, end = Spacing.pageMargin, top = Spacing.s5, bottom = Spacing.s8),
        verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
    ) {
        // ── Period selector (drives net worth, income & expenses) ───────────
        item {
            PeriodSelectorRow(selected = state.period, onSelect = onPeriodChange)
        }

        // ── Net worth hero ──────────────────────────────────────────────────
        item {
            OverTimeCard(
                label = "Net worth",
                money = snap.netWorth,
                trendPct = snap.netWorthTrendPct,
                trendPositiveIsGood = true,
                series = snap.netWorthSeries,
                period = state.period,
                lineColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // ── Cash / Savings / Investment pills ───────────────────────────────
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TotalPill(
                    label = "Cash",
                    money = snap.totalCash,
                    icon = { Icon(Icons.Filled.AccountBalanceWallet, null, modifier = Modifier.size(16.dp)) },
                    bgColor = MaterialTheme.colorScheme.surfaceVariant,
                    fgColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                TotalPill(
                    label = "Savings",
                    money = snap.totalSavings,
                    icon = { Icon(Icons.Filled.Savings, null, modifier = Modifier.size(16.dp)) },
                    bgColor = c.savingsSoft,
                    fgColor = c.savings,
                    modifier = Modifier.weight(1f),
                )
                TotalPill(
                    label = "Invest",
                    money = snap.totalInvestment,
                    icon = { Icon(Icons.Filled.TrendingUp, null, modifier = Modifier.size(16.dp)) },
                    bgColor = c.investmentSoft,
                    fgColor = c.investment,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // ── Income / Expenses (net for the selected period) ─────────────────
        item {
            KpiCard(
                label = "Income",
                money = snap.income,
                trendPct = snap.incomeTrendPct,
                trendPositiveIsGood = true,
                amountColor = c.income,
                sparklineValues = snap.incomeDaily.map { it.amountMinor / 100.0 },
                sparklineColor = c.income,
                sparklineLabels = dateLabels(snap.incomeDaily.map { it.day }, state.period),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            KpiCard(
                label = "Expenses",
                money = snap.expense,
                trendPct = snap.expenseTrendPct,
                trendPositiveIsGood = false,
                amountColor = c.expense,
                sparklineValues = snap.expenseDaily.map { it.amountMinor / 100.0 },
                sparklineColor = c.expense,
                sparklineLabels = dateLabels(snap.expenseDaily.map { it.day }, state.period),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // ── Income over time ────────────────────────────────────────────────
        item {
            OverTimeCard(
                label = "Income over time",
                money = snap.incomeTotal,
                trendPct = snap.incomeSeriesTrendPct,
                trendPositiveIsGood = true,
                series = snap.incomeSeries,
                period = state.period,
                lineColor = c.income,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // ── Expense over time ───────────────────────────────────────────────
        item {
            OverTimeCard(
                label = "Expense over time",
                money = snap.expenseTotal,
                trendPct = snap.expenseSeriesTrendPct,
                trendPositiveIsGood = false,
                series = snap.expenseSeries,
                period = state.period,
                lineColor = c.expense,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // ── Income & spending grouped bars ──────────────────────────────────
        item { SectionHeader(title = "Income & spending") }
        item {
            val bars = monthlyPairs(snap.incomeDaily, snap.expenseDaily)
            if (bars.income.isNotEmpty()) {
                DashCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(bottom = Spacing.s4),
                    ) {
                        LegendDot(color = c.income, label = "Income")
                        LegendDot(color = c.expense, label = "Expenses")
                    }
                    GroupedBarChart(
                        incomeValues = bars.income,
                        expenseValues = bars.expense,
                        incomeColor = c.income,
                        expenseColor = c.expense,
                        modifier = Modifier.fillMaxWidth(),
                        xLabels = bars.labels,
                    )
                }
            }
        }

        // ── Expenses by category ────────────────────────────────────────────
        item { SectionHeader(title = "Expenses by category") }
        item {
            CategoryDonutCard(
                shares = snap.expenseByCategory,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // ── Top brands ──────────────────────────────────────────────────────
        item { SectionHeader(title = "Top brands") }
        item {
            BrandDonutCard(
                shares = snap.expenseByBrand,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // ── Income sources ──────────────────────────────────────────────────
        if (snap.incomeByCategory.isNotEmpty()) {
            item { SectionHeader(title = "Income sources") }
            item {
                CategoryDonutCard(
                    shares = snap.incomeByCategory,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // ── Category trends ─────────────────────────────────────────────────
        item { SectionHeader(title = "Category trends") }
        item {
            CategoryTrendsRow(
                snap = snap,
                overallCategoryId = state.overallTrendCategoryId,
                dailyCategoryId = state.dailyTrendCategoryId,
                onOverallCategoryChanged = onOverallCategoryChanged,
                onDailyCategoryChanged = onDailyCategoryChanged,
            )
        }
    }
}

// ── Shared period selector ─────────────────────────────────────────────────────

@Composable
private fun PeriodSelectorRow(
    selected: SummaryPeriod,
    onSelect: (SummaryPeriod) -> Unit,
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.s3)) {
        items(SummaryPeriod.entries.size) { i ->
            val option = SummaryPeriod.entries[i]
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(option.label, style = MaterialTheme.typography.labelMedium) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected == option,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    }
}

// ── Hero / over-time chart card ────────────────────────────────────────────────

@Composable
private fun OverTimeCard(
    label: String,
    money: Money,
    trendPct: Double?,
    trendPositiveIsGood: Boolean,
    series: List<MonthPoint>,
    period: SummaryPeriod,
    lineColor: Color,
    modifier: Modifier = Modifier,
) {
    DashCard(modifier = modifier) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(Spacing.s2))
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MoneyText(
                amountMinor = money.amountMinor,
                style = HisabakType.amountHero,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (trendPct != null) {
                TrendBadge(pct = trendPct, positiveIsGood = trendPositiveIsGood)
            }
        }
        if (series.isNotEmpty()) {
            AreaLineChart(
                values = series.map { it.amountMinor / 100.0 },
                lineColor = lineColor,
                fillColor = lineColor.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.cardGap, bottom = Spacing.s2),
                heightDp = 96.dp,
                xLabels = chartLabels(series, period),
            )
        }
    }
}

/** Per-point x-axis labels: day-of-month for month windows, month (with year when
 *  the window spans years) otherwise. */
private fun chartLabels(series: List<MonthPoint>, period: SummaryPeriod): List<String> =
    dateLabels(series.map { it.monthStart }, period)

private fun dateLabels(dates: List<LocalDate>, period: SummaryPeriod): List<String> {
    val daily = period == SummaryPeriod.CURRENT_MONTH || period == SummaryPeriod.LAST_MONTH
    val multiYear = dates.mapTo(HashSet()) { it.year }.size > 1
    val formatter = when {
        daily -> DateTimeFormatter.ofPattern("d MMM")
        multiYear -> DateTimeFormatter.ofPattern("MMM ''yy")
        else -> DateTimeFormatter.ofPattern("MMM")
    }
    return dates.map { it.format(formatter) }
}

// ── Stat pills ────────────────────────────────────────────────────────────────

@Composable
private fun TotalPill(
    label: String,
    money: Money,
    icon: @Composable () -> Unit,
    bgColor: Color,
    fgColor: Color,
    modifier: Modifier = Modifier,
) {
    SurfaceCard(modifier = modifier, contentPadding = Spacing.cardGap) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(bgColor, CircleShape)
                .padding(horizontal = Spacing.s3, vertical = Spacing.s2),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(16.dp)) {
                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.material3.LocalContentColor provides fgColor,
                ) { icon() }
            }
            Text(label, style = MaterialTheme.typography.labelSmall, color = fgColor)
        }
        Spacer(Modifier.height(6.dp))
        MoneyText(
            amountMinor = money.amountMinor,
            style = HisabakType.amount,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// ── KPI card (income / expense) ───────────────────────────────────────────────

@Composable
private fun KpiCard(
    label: String,
    money: Money,
    trendPct: Double?,
    trendPositiveIsGood: Boolean,
    amountColor: Color,
    sparklineValues: List<Double>,
    sparklineColor: Color,
    sparklineLabels: List<String> = emptyList(),
    modifier: Modifier = Modifier,
) {
    DashCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (trendPct != null) {
                TrendBadge(pct = trendPct, positiveIsGood = trendPositiveIsGood)
            }
        }
        Spacer(Modifier.height(6.dp))
        MoneyText(
            amountMinor = money.amountMinor,
            style = HisabakType.amountLarge,
            color = amountColor,
        )
        if (sparklineValues.isNotEmpty()) {
            BarSparkline(
                values = sparklineValues,
                barColor = sparklineColor,
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.s3),
                heightDp = 64.dp,
                xLabels = sparklineLabels,
            )
        }
    }
}

// ── Trend badge ───────────────────────────────────────────────────────────────

@Composable
private fun TrendBadge(pct: Double, positiveIsGood: Boolean) {
    if (abs(pct) < 0.01) return
    val c = HisabakTheme.colors
    val isUp = pct > 0
    val good = if (positiveIsGood) isUp else !isUp
    val color = if (good) c.income else c.expense
    val icon = when {
        isUp -> Icons.Filled.ArrowUpward
        else -> Icons.Filled.ArrowDownward
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s1),
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(13.dp))
        Text(
            "%.0f%%".format(abs(pct)),
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}

// ── Donut + legend cards ──────────────────────────────────────────────────────

@Composable
private fun CategoryDonutCard(
    shares: List<CategoryShare>,
    modifier: Modifier = Modifier,
) {
    DashCard(modifier = modifier) {
        if (shares.isEmpty()) {
            Text("No data yet", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            return@DashCard
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DonutChart(
                slices = shares.take(5).map { DonutSlice(it.pct, CategoryStyle.color(it.color)) },
                size = 112.dp,
                modifier = Modifier.size(112.dp),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.s3),
                modifier = Modifier.weight(1f),
            ) {
                shares.take(5).forEach { share ->
                    DonutLegendRow(
                        color = CategoryStyle.color(share.color),
                        label = share.name,
                        amount = share.amount,
                        pct = share.pct,
                    )
                }
            }
        }
    }
}

@Composable
private fun BrandDonutCard(
    shares: List<BrandShare>,
    modifier: Modifier = Modifier,
) {
    DashCard(modifier = modifier) {
        if (shares.isEmpty()) {
            Text("No data yet", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            return@DashCard
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DonutChart(
                slices = shares.take(5).map { DonutSlice(it.pct, CategoryStyle.color(it.color)) },
                size = 112.dp,
                modifier = Modifier.size(112.dp),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.s3),
                modifier = Modifier.weight(1f),
            ) {
                shares.take(5).forEach { share ->
                    DonutLegendRow(
                        color = CategoryStyle.color(share.color),
                        label = share.name,
                        amount = share.amount,
                        pct = share.pct,
                    )
                }
            }
        }
    }
}

@Composable
private fun DonutLegendRow(color: Color, label: String, amount: Money, pct: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(Modifier.size(Spacing.s3).background(color, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        MoneyText(
            amountMinor = amount.amountMinor,
            style = HisabakType.amount,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.width(Spacing.s2))
        Text(
            "%.0f%%".format(pct * 100),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.End,
        )
    }
}

// ── Category trends (existing, color-fixed) ───────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryTrendsRow(
    snap: DashboardSnapshot,
    overallCategoryId: CategoryId?,
    dailyCategoryId: CategoryId?,
    onOverallCategoryChanged: (CategoryId) -> Unit,
    onDailyCategoryChanged: (CategoryId) -> Unit,
) {
    val c = HisabakTheme.colors
    val overallPoints = overallCategoryId?.let { snap.overallTrendByCategory[it] }.orEmpty()
    val dailyPoints = dailyCategoryId?.let { snap.dailyTrendByCategory[it] }.orEmpty()
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap)) {
        CategoryTrendCard(
            title = "Overall",
            totalLabel = formatCompactUnits(overallPoints.sumOf { it.amountMinor }),
            values = overallPoints.map { it.amountMinor / 100.0 },
            color = c.income,
            options = snap.categoryOptions,
            selectedId = overallCategoryId,
            onSelected = onOverallCategoryChanged,
            modifier = Modifier.weight(1f),
        )
        CategoryTrendCard(
            title = "Daily",
            totalLabel = formatCompactUnits(dailyPoints.sumOf { it.amountMinor }),
            values = dailyPoints.map { it.amountMinor / 100.0 },
            color = c.expense,
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
    DashCard(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selected?.name ?: "—",
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        leadingIcon = {
                            Box(
                                Modifier.size(10.dp).background(CategoryStyle.color(option.color), CircleShape)
                            )
                        },
                        onClick = { onSelected(option.id); expanded = false },
                    )
                }
            }
        }
        Text(
            totalLabel,
            style = HisabakType.amountLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = Spacing.s2),
        )
        if (values.isNotEmpty()) {
            AreaLineChart(
                values = values,
                lineColor = color,
                fillColor = color.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.s2),
                heightDp = 80.dp,
            )
        }
    }
}

// ── Shared card shell ─────────────────────────────────────────────────────────

@Composable
private fun DashCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    SurfaceCard(modifier = modifier, contentPadding = Spacing.cardPadding, content = content)
}

// ── Grouped bar legend dot ────────────────────────────────────────────────────

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(9.dp).background(color, androidx.compose.foundation.shape.RoundedCornerShape(2.dp)))
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ── Data helpers ──────────────────────────────────────────────────────────────

private data class MonthlyBars(
    val income: List<Double>,
    val expense: List<Double>,
    val labels: List<String>,
)

/** Aggregate daily points into parallel monthly income/expense series across the period. */
private fun monthlyPairs(
    income: List<DayPoint>,
    expense: List<DayPoint>,
): MonthlyBars {
    val incomeByMonth = income
        .groupBy { it.day.withDayOfMonth(1) }
        .mapValues { (_, v) -> v.sumOf { it.amountMinor } / 100.0 }
    val expenseByMonth = expense
        .groupBy { it.day.withDayOfMonth(1) }
        .mapValues { (_, v) -> v.sumOf { it.amountMinor } / 100.0 }
    val months = (incomeByMonth.keys + expenseByMonth.keys)
        .toSortedSet()
        .toList()
    if (months.isEmpty()) return MonthlyBars(emptyList(), emptyList(), emptyList())
    val multiYear = months.mapTo(HashSet()) { it.year }.size > 1
    val formatter = DateTimeFormatter.ofPattern(if (multiYear) "MMM ''yy" else "MMM")
    return MonthlyBars(
        income = months.map { incomeByMonth[it] ?: 0.0 },
        expense = months.map { expenseByMonth[it] ?: 0.0 },
        labels = months.map { it.format(formatter) },
    )
}

private fun formatCompactUnits(amountMinor: Long): String {
    val major = amountMinor / 100.0
    val abs = abs(major)
    return when {
        abs >= 1_000_000 -> "%.2fM".format(major / 1_000_000.0)
        abs >= 1_000 -> "%.1fk".format(major / 1_000.0)
        else -> "%.0f".format(major)
    }
}
