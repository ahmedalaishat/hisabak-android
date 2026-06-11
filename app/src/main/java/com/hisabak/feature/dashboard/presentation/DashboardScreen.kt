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
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.theme.HisabakTheme
import com.hisabak.ui.theme.HisabakType
import kotlin.math.abs

private val PERIODS = listOf("Week", "Month", "Year", "All")

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
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val c = HisabakTheme.colors
    var period by rememberSaveable { mutableStateOf("Month") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // ── Page title ──────────────────────────────────────────────────────
        item {
            Text(
                "Dashboard",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        // ── Net worth hero ──────────────────────────────────────────────────
        item {
            NetWorthCard(snap = snap, period = period, onPeriodChange = { period = it })
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

        // ── Income / Expenses ───────────────────────────────────────────────
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard(
                    label = "Income",
                    money = snap.incomeMonth,
                    trendPct = snap.incomeTrendPct,
                    trendPositiveIsGood = true,
                    amountColor = c.income,
                    sparklineValues = snap.incomeDaily.map { it.amountMinor / 100.0 },
                    sparklineColor = c.income,
                    modifier = Modifier.weight(1f),
                )
                KpiCard(
                    label = "Expenses",
                    money = snap.expenseMonth,
                    trendPct = snap.expenseTrendPct,
                    trendPositiveIsGood = false,
                    amountColor = c.expense,
                    sparklineValues = snap.expenseDaily.map { it.amountMinor / 100.0 },
                    sparklineColor = c.expense,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // ── Income & spending grouped bars ──────────────────────────────────
        item { SectionHeader(title = "Income & spending") }
        item {
            val (incomeMonthly, expenseMonthly) = monthlyPairs(snap.incomeDaily, snap.expenseDaily)
            if (incomeMonthly.isNotEmpty()) {
                DashCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(bottom = 10.dp),
                    ) {
                        LegendDot(color = c.income, label = "Income")
                        LegendDot(color = c.expense, label = "Expenses")
                    }
                    GroupedBarChart(
                        incomeValues = incomeMonthly,
                        expenseValues = expenseMonthly,
                        incomeColor = c.income,
                        expenseColor = c.expense,
                        modifier = Modifier.fillMaxWidth(),
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

// ── Hero card ────────────────────────────────────────────────────────────────

@Composable
private fun NetWorthCard(
    snap: DashboardSnapshot,
    period: String,
    onPeriodChange: (String) -> Unit,
) {
    val c = HisabakTheme.colors
    val trendPct = netWorthTrend(snap.netWorthSeries)

    DashCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Net worth",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                formatMoney(snap.netWorth),
                style = HisabakType.amountHero,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (trendPct != null) {
                TrendBadge(pct = trendPct, positiveIsGood = true)
            }
        }
        if (snap.netWorthSeries.isNotEmpty()) {
            AreaLineChart(
                values = snap.netWorthSeries.map { it.amountMinor / 100.0 },
                lineColor = MaterialTheme.colorScheme.primary,
                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp),
                heightDp = 96.dp,
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp),
        ) {
            items(PERIODS.size) { i ->
                val p = PERIODS[i]
                FilterChip(
                    selected = period == p,
                    onClick = { onPeriodChange(p) },
                    label = { Text(p, style = MaterialTheme.typography.labelMedium) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = period == p,
                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
        }
    }
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
    SurfaceCard(modifier = modifier, contentPadding = 12.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .background(bgColor, CircleShape)
                .padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(16.dp)) {
                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.material3.LocalContentColor provides fgColor,
                ) { icon() }
            }
            Text(label, style = MaterialTheme.typography.labelSmall, color = fgColor)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            formatMoney(money),
            style = HisabakType.amount,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
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
        Text(
            formatMoney(money),
            style = HisabakType.amountLarge,
            color = amountColor,
        )
        if (sparklineValues.isNotEmpty()) {
            BarSparkline(
                values = sparklineValues,
                barColor = sparklineColor,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                heightDp = 48.dp,
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
        horizontalArrangement = Arrangement.spacedBy(2.dp),
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
        Box(Modifier.size(8.dp).background(color, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            formatMoney(amount),
            style = HisabakType.amount,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.width(4.dp))
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
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
            modifier = Modifier.padding(top = 4.dp),
        )
        if (values.isNotEmpty()) {
            AreaLineChart(
                values = values,
                lineColor = color,
                fillColor = color.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
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
    SurfaceCard(modifier = modifier, contentPadding = 16.dp, content = content)
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

private fun netWorthTrend(series: List<MonthPoint>): Double? {
    if (series.size < 2) return null
    val prev = series[series.size - 2].amountMinor
    val curr = series.last().amountMinor
    return if (prev != 0L) (curr - prev).toDouble() / prev.toDouble() * 100 else null
}

/** Aggregate daily points into parallel monthly series (last 5 months). */
private fun monthlyPairs(
    income: List<DayPoint>,
    expense: List<DayPoint>,
): Pair<List<Double>, List<Double>> {
    val incomeByMonth = income
        .groupBy { it.day.withDayOfMonth(1) }
        .mapValues { (_, v) -> v.sumOf { it.amountMinor } / 100.0 }
    val expenseByMonth = expense
        .groupBy { it.day.withDayOfMonth(1) }
        .mapValues { (_, v) -> v.sumOf { it.amountMinor } / 100.0 }
    val months = (incomeByMonth.keys + expenseByMonth.keys)
        .toSortedSet()
        .toList()
        .takeLast(5)
    if (months.isEmpty()) return emptyList<Double>() to emptyList()
    return months.map { incomeByMonth[it] ?: 0.0 } to months.map { expenseByMonth[it] ?: 0.0 }
}

private fun formatMoney(money: Money): String {
    val major = money.amountMinor / 100.0
    val abs = abs(major)
    val formatted = when {
        abs >= 1_000_000 -> "%.2fM".format(major / 1_000_000.0)
        else -> "%,.0f".format(major)
    }
    return "${money.currency.code} $formatted"
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
