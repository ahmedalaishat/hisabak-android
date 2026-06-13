package com.hisabak.feature.transaction.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import com.hisabak.core.common.Money
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.ui.components.AmountText
import com.hisabak.ui.components.AmountTone
import com.hisabak.ui.components.CircleIconTile
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
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

private enum class Period(val label: String) {
    CURRENT_MONTH("This month"),
    LAST_MONTH("Last month"),
    CURRENT_YEAR("This year"),
    LAST_YEAR("Last year"),
    ALL("All time"),
}

@Composable
fun TransactionListScreen(
    state: TransactionListUiState,
    onSearchChange: (String) -> Unit,
    onDelete: (TransactionId) -> Unit,
    onAdd: () -> Unit,
    onEdit: (TransactionId) -> Unit,
) {
    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // The period filter scopes the income / expenses summary cards. Net worth lives
    // on the Dashboard; this screen is about activity, not a wealth snapshot.
    var period by rememberSaveable { mutableStateOf(Period.CURRENT_MONTH) }
    val periodTotals = remember(state.rows, period) {
        computeTotals(filterByPeriod(state.rows, period))
    }

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
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                PeriodFilterChip(period = period, onSelect = { period = it })
            }
        }

        item {
            Row(
                Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
            ) {
                IncomeStatCard(
                    value = formatAmountMajor(periodTotals.income),
                    currencySymbol = true,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
                ExpensesStatCard(
                    value = formatAmountMajor(periodTotals.expenses),
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

        if (state.rows.isEmpty()) {
            item {
                EmptyStatePanel(
                    title = "No transactions yet",
                    subtitle = "Add your first or import from SMS",
                    icon = Icons.Filled.ReceiptLong,
                    actionLabel = "Add transaction",
                    onAction = onAdd,
                )
            }
        } else {
            items(state.rows, key = { it.id.value }) { row ->
                TransactionRowItem(
                    row = row,
                    onEdit = { onEdit(row.id) },
                    onDelete = { onDelete(row.id) },
                )
            }
        }
    }
}

// ---- internals -----------------------------------------------------------

private data class Totals(
    val income: Long,
    val expenses: Long,
    val currencyCode: String,
)

private fun computeTotals(rows: List<TransactionRow>): Totals {
    if (rows.isEmpty()) return Totals(0, 0, "")
    var income = 0L
    var expenses = 0L
    rows.forEach { row ->
        val amount = row.amount.amountMinor
        when (row.categoryType) {
            CategoryType.INCOME -> income += abs(amount)
            CategoryType.EXPENSES -> expenses += abs(amount)
            else -> {
                if (amount >= 0) income += amount else expenses += abs(amount)
            }
        }
    }
    return Totals(
        income = income,
        expenses = expenses,
        currencyCode = rows.first().amount.currency.code,
    )
}

/** Keeps only the rows whose [TransactionRow.occurredAt] falls in [period]'s range. */
private fun filterByPeriod(rows: List<TransactionRow>, period: Period): List<TransactionRow> {
    val range = period.range() ?: return rows
    val (start, end) = range
    return rows.filter { !it.occurredAt.isBefore(start) && it.occurredAt.isBefore(end) }
}

/** [start, end) instants for the period, or null for [Period.ALL] (no bound). */
private fun Period.range(): Pair<Instant, Instant>? {
    val zone = ZoneId.systemDefault()
    val today = LocalDate.now(zone)
    val (startDate, endDate) = when (this) {
        Period.CURRENT_MONTH -> today.withDayOfMonth(1).let { it to it.plusMonths(1) }
        Period.LAST_MONTH -> today.withDayOfMonth(1).minusMonths(1).let { it to it.plusMonths(1) }
        Period.CURRENT_YEAR -> today.withDayOfYear(1).let { it to it.plusYears(1) }
        Period.LAST_YEAR -> today.withDayOfYear(1).minusYears(1).let { it to it.plusYears(1) }
        Period.ALL -> return null
    }
    return startDate.atStartOfDay(zone).toInstant() to endDate.atStartOfDay(zone).toInstant()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodFilterChip(period: Period, onSelect: (Period) -> Unit) {
    var showSheet by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clip(PillShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { showSheet = true }
            .padding(horizontal = Spacing.s4, vertical = Spacing.s2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s1),
    ) {
        Icon(
            Icons.Filled.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Sizing.iconSm),
        )
        Text(
            text = period.label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Icon(
            Icons.Filled.ExpandMore,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(Sizing.iconSm),
        )
    }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(Modifier.fillMaxWidth().padding(bottom = Spacing.s7)) {
                Text(
                    text = "Summary period",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = Spacing.pageMargin, vertical = Spacing.s4),
                )
                Period.entries.forEach { option ->
                    val selected = option == period
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(option)
                                showSheet = false
                            }
                            .padding(horizontal = Spacing.pageMargin, vertical = Spacing.s4),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (selected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface,
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
            }
        }
    }
}


@Composable
private fun TransactionRowItem(
    row: TransactionRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
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
