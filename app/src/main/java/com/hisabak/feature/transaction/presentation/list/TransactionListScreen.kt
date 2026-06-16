package com.hisabak.feature.transaction.presentation.list

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
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.hisabak.core.common.Money
import com.hisabak.core.common.SummaryPeriod
import com.hisabak.feature.category.domain.CategoryType
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
import com.hisabak.ui.theme.Spacing
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

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
    var period by rememberSaveable { mutableStateOf(SummaryPeriod.CURRENT_MONTH) }
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
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s3)) {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                PeriodChipRow(selected = period, onSelect = { period = it })
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
        // Only typed income/expense count here; savings, investment and uncategorized
        // transactions are excluded, matching the dashboard's income/expense figures.
        when (row.categoryType) {
            CategoryType.INCOME -> income += abs(amount)
            CategoryType.EXPENSES -> expenses += abs(amount)
            else -> Unit
        }
    }
    return Totals(
        income = income,
        expenses = expenses,
        currencyCode = rows.first().amount.currency.code,
    )
}

/** Keeps only the rows whose [TransactionRow.occurredAt] falls in [period]'s range. */
private fun filterByPeriod(rows: List<TransactionRow>, period: SummaryPeriod): List<TransactionRow> {
    val zone = ZoneId.systemDefault()
    val (start, end) = period.instantRange(LocalDate.now(zone), zone) ?: return rows
    return rows.filter { !it.occurredAt.isBefore(start) && it.occurredAt.isBefore(end) }
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
