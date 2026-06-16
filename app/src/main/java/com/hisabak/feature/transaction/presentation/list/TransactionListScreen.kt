package com.hisabak.feature.transaction.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ReceiptLong
import com.hisabak.ui.components.SkeletonCard
import com.hisabak.ui.components.SkeletonRowList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hisabak.core.common.Money
import com.hisabak.core.common.SummaryPeriod
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.category.presentation.CategoryStyle
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.ui.components.AmountText
import com.hisabak.ui.components.AmountTone
import com.hisabak.ui.components.CircleIconTile
import com.hisabak.ui.components.PeriodChipRow
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.ExpensesStatCard
import com.hisabak.ui.components.IncomeStatCard
import com.hisabak.ui.components.ListRow
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.iconForKey
import com.hisabak.ui.components.tintPairForColor
import com.hisabak.ui.theme.PillShape
import com.hisabak.ui.theme.Sizing
import com.hisabak.ui.theme.Spacing
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@Composable
fun TransactionListScreen(
    state: TransactionListUiState,
    onSearchChange: (String) -> Unit,
    onPeriodChange: (SummaryPeriod) -> Unit,
    onBrandFilterChange: (BrandId?) -> Unit,
    onCategoryFilterChange: (CategoryId?) -> Unit,
    onDateRangeChange: (DateRangeFilter) -> Unit,
    onClearFilters: () -> Unit,
    onDelete: (TransactionId) -> Unit,
    onAdd: () -> Unit,
    onEdit: (TransactionId) -> Unit,
) {
    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.pageMargin, vertical = Spacing.s3),
            verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap)) {
                SkeletonCard(Modifier.weight(1f))
                SkeletonCard(Modifier.weight(1f))
            }
            SkeletonRowList(count = 6)
        }
        return
    }

    // The period scopes the summary cards; brand / category / date-range scope the list.
    var openFilter by remember { mutableStateOf<FilterTarget?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Spacing.pageMargin,
            end = Spacing.pageMargin,
            top = Spacing.s3,
            bottom = Spacing.s10 + Spacing.s7, // clear the Add FAB
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s3)) {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                PeriodChipRow(selected = state.period, onSelect = onPeriodChange)
            }
        }

        item {
            Row(
                Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
            ) {
                IncomeStatCard(
                    value = formatAmountMajor(state.summaryIncome),
                    currencySymbol = true,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
                ExpensesStatCard(
                    value = formatAmountMajor(state.summaryExpenses),
                    currencySymbol = true,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }
        }

        item {
            SearchField(
                value = state.search,
                onValueChange = onSearchChange,
                placeholder = "Search transactions...",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilterPill(
                    label = state.selectedCategoryName ?: "Category",
                    active = state.categoryFilter != null,
                    onClick = { openFilter = FilterTarget.CATEGORY },
                )
                FilterPill(
                    label = state.selectedBrandName ?: "Brand",
                    active = state.brandFilter != null,
                    onClick = { openFilter = FilterTarget.BRAND },
                )
                FilterPill(
                    label = if (state.dateRange == DateRangeFilter.ALL) "Date" else state.dateRange.label,
                    active = state.dateRange != DateRangeFilter.ALL,
                    onClick = { openFilter = FilterTarget.DATE },
                )
                if (state.hasActiveFilters) {
                    Text(
                        text = "Clear",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable(onClick = onClearFilters)
                            .padding(horizontal = Spacing.s2, vertical = Spacing.s2),
                    )
                }
            }
        }

        if (state.rows.isEmpty()) {
            val filtered = state.hasActiveFilters || state.search.isNotBlank()
            item {
                if (filtered) {
                    EmptyStatePanel(
                        title = "No matching transactions",
                        subtitle = "Try a different period, brand or category",
                        icon = Icons.Filled.ReceiptLong,
                        actionLabel = if (state.hasActiveFilters) "Clear filters" else "Add transaction",
                        onAction = if (state.hasActiveFilters) onClearFilters else onAdd,
                    )
                } else {
                    EmptyStatePanel(
                        title = "No transactions yet",
                        subtitle = "Add your first or import from SMS",
                        icon = Icons.Filled.ReceiptLong,
                        actionLabel = "Add transaction",
                        onAction = onAdd,
                    )
                }
            }
        } else {
            items(state.rows, key = { it.id.value }) { row ->
                TransactionRowItem(
                    row = row,
                    onEdit = { onEdit(row.id) },
                    onDelete = { onDelete(row.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }

    when (openFilter) {
        FilterTarget.CATEGORY -> FilterSelectSheet(
            title = "Filter by category",
            entries = state.categoryOptions.map { FilterEntry(it.id.value, it.name, it.color) },
            selectedId = state.categoryFilter?.value,
            onSelect = { id -> onCategoryFilterChange(id?.let(::CategoryId)); openFilter = null },
            onDismiss = { openFilter = null },
        )
        FilterTarget.BRAND -> FilterSelectSheet(
            title = "Filter by brand",
            entries = state.brandOptions.map { FilterEntry(it.id.value, it.name, null) },
            selectedId = state.brandFilter?.value,
            onSelect = { id -> onBrandFilterChange(id?.let(::BrandId)); openFilter = null },
            onDismiss = { openFilter = null },
        )
        FilterTarget.DATE -> FilterSelectSheet(
            title = "Filter by date",
            entries = DateRangeFilter.entries
                .filter { it != DateRangeFilter.ALL }
                .map { FilterEntry(it.name, it.label, null) },
            selectedId = state.dateRange.takeIf { it != DateRangeFilter.ALL }?.name,
            onSelect = { id ->
                onDateRangeChange(id?.let { DateRangeFilter.valueOf(it) } ?: DateRangeFilter.ALL)
                openFilter = null
            },
            onDismiss = { openFilter = null },
            allLabel = DateRangeFilter.ALL.label,
        )
        null -> Unit
    }
}

private enum class FilterTarget { CATEGORY, BRAND, DATE }

private data class FilterEntry(val id: String, val label: String, val color: String?)

@Composable
private fun FilterPill(label: String, active: Boolean, onClick: () -> Unit) {
    val bg = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh
    val fg = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .clip(PillShape)
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.s4, vertical = Spacing.s2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s1),
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = fg, maxLines = 1)
        Icon(
            Icons.Filled.ExpandMore,
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(Sizing.iconSm),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSelectSheet(
    title: String,
    entries: List<FilterEntry>,
    selectedId: String?,
    onSelect: (String?) -> Unit,
    onDismiss: () -> Unit,
    allLabel: String = "All",
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = Spacing.s7),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = Spacing.pageMargin, vertical = Spacing.s4),
            )
            FilterSheetRow(label = allLabel, colorKey = null, selected = selectedId == null) { onSelect(null) }
            entries.forEach { entry ->
                FilterSheetRow(
                    label = entry.label,
                    colorKey = entry.color,
                    selected = selectedId == entry.id,
                ) { onSelect(entry.id) }
            }
        }
    }
}

@Composable
private fun FilterSheetRow(
    label: String,
    colorKey: String?,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.pageMargin, vertical = Spacing.s4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s3),
    ) {
        if (colorKey != null) {
            Box(Modifier.size(10.dp).background(CategoryStyle.color(colorKey), CircleShape))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        if (selected) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(Sizing.icon),
            )
        }
    }
}

// ---- internals -----------------------------------------------------------

@Composable
private fun TransactionRowItem(
    row: TransactionRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = when (row.categoryType) {
        CategoryType.INCOME -> AmountTone.Income
        CategoryType.EXPENSES -> AmountTone.Expense
        else -> if (row.amount.amountMinor >= 0) AmountTone.Income else AmountTone.Expense
    }
    val (bg, fg) = tintPairForColor(row.categoryColor)
    val amountValue = row.amount.amountMinor.toMajorDouble()
    val dateLabel = formatRelative(row.occurredAt)

    ListRow(
        modifier = modifier,
        title = row.brandName,
        subtitle = row.note?.takeIf { it.isNotBlank() },
        leading = {
            CircleIconTile(
                icon = iconForKey(row.categoryIcon),
                background = bg,
                foreground = fg,
            )
        },
        trailing = {
            Column(horizontalAlignment = Alignment.End) {
                AmountText(
                    value = abs(amountValue),
                    currency = row.amount.currency.code,
                    showSign = true,
                    tone = tone,
                    size = 14.sp,
                )
                Text(
                    text = dateLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        onClick = onEdit,
    )
}

private fun Long.toMajor(): String {
    val major = this / 100
    val minor = abs(this % 100)
    return "$major.${minor.toString().padStart(2, '0')}"
}

private fun Long.toMajorDouble(): Double = this / 100.0

internal fun formatMoneyMajor(amountMinor: Long, currency: String): String {
    val prefix = if (currency.isBlank()) "" else "$currency "
    return prefix + amountMinor.toMajor()
}

/** Grouped, 2-decimal amount with no currency code (the glyph is shown separately). */
private fun formatAmountMajor(amountMinor: Long): String = "%,.2f".format(amountMinor / 100.0)

internal fun formatSignedAmount(money: Money, positive: Boolean): String {
    val sign = if (positive) "+" else "-"
    return "$sign${money.currency.code} ${abs(money.amountMinor).toMajor()}"
}

internal fun formatMoney(money: Money): String {
    val major = money.amountMinor / 100
    val minor = abs(money.amountMinor % 100)
    val sign = if (money.amountMinor < 0) "-" else ""
    return "$sign${money.currency.code} $major.${minor.toString().padStart(2, '0')}"
}

internal fun formatDate(instant: Instant): String =
    DateTimeFormatter.ofPattern("MMM d, yyyy")
        .withZone(ZoneId.systemDefault())
        .format(instant)

private fun formatRelative(instant: Instant): String {
    val now = Instant.now()
    val diff = Duration.between(instant, now)
    return when {
        diff.isNegative -> formatDate(instant)
        diff.toHours() < 1 -> "${diff.toMinutes().coerceAtLeast(1)}m ago"
        diff.toHours() < 24 -> "${diff.toHours()}h ago"
        diff.toDays() == 1L -> "Yesterday"
        diff.toDays() < 7 -> "${diff.toDays()}d ago"
        else -> formatDate(instant)
    }
}
