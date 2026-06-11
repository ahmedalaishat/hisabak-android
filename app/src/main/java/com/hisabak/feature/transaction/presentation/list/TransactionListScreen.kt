package com.hisabak.feature.transaction.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hisabak.core.common.Money
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.ui.components.AmountText
import com.hisabak.ui.components.AmountTone
import com.hisabak.ui.components.ButtonVariant
import com.hisabak.ui.components.CircleIconTile
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.ExpensesStatCard
import com.hisabak.ui.components.FilterPill
import com.hisabak.ui.components.HisabakButton
import com.hisabak.ui.components.IncomeStatCard
import com.hisabak.ui.components.ListRow
import com.hisabak.ui.components.ProgressBar
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.components.iconForKey
import com.hisabak.ui.components.tintPairForColor
import com.hisabak.ui.theme.HisabakTheme
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

private enum class Period { TODAY, WEEK, MONTH, ALL }

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

    val totals = remember(state.rows) { computeTotals(state.rows) }
    var period by rememberSaveable { mutableStateOf(Period.MONTH) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { BalanceHeroCard(totals = totals, onAdd = onAdd) }

        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IncomeStatCard(
                    value = formatMoneyMajor(totals.income, totals.currencyCode),
                    modifier = Modifier.weight(1f),
                )
                ExpensesStatCard(
                    value = formatMoneyMajor(totals.expenses, totals.currencyCode),
                    modifier = Modifier.weight(1f),
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
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 0.dp),
            ) {
                item {
                    FilterPill(label = "Today", selected = period == Period.TODAY, onClick = { period = Period.TODAY })
                }
                item {
                    FilterPill(label = "Week", selected = period == Period.WEEK, onClick = { period = Period.WEEK })
                }
                item {
                    FilterPill(label = "Month", selected = period == Period.MONTH, onClick = { period = Period.MONTH })
                }
                item {
                    FilterPill(label = "All", selected = period == Period.ALL, onClick = { period = Period.ALL })
                }
            }
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

        item { Spacer(Modifier.height(8.dp)) }
    }
}

// ---- internals -----------------------------------------------------------

private data class Totals(
    val balance: Long,
    val income: Long,
    val expenses: Long,
    val currencyCode: String,
)

private fun computeTotals(rows: List<TransactionRow>): Totals {
    if (rows.isEmpty()) return Totals(0, 0, 0, "")
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
        balance = income - expenses,
        income = income,
        expenses = expenses,
        currencyCode = rows.first().amount.currency.code,
    )
}

@Composable
private fun BalanceHeroCard(totals: Totals, onAdd: () -> Unit) {
    val incomeRatio = if (totals.income == 0L) 0f else
        (totals.income.toFloat() / (totals.income + totals.expenses).toFloat().coerceAtLeast(1f))

    SurfaceCard(modifier = Modifier.fillMaxWidth(), contentPadding = 20.dp) {
        Text(
            text = "Total Balance",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        AmountText(
            value = totals.balance.toMajorDouble(),
            currency = totals.currencyCode.ifBlank { "SAR" },
            showSign = false,
            tone = AmountTone.Neutral,
            size = 36.sp,
        )
        Spacer(Modifier.height(14.dp))
        ProgressBar(
            progress = incomeRatio,
            color = HisabakTheme.colors.income,
        )
        Spacer(Modifier.height(6.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "${(incomeRatio * 100).toInt()}% income ratio",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "${totals.currencyCode} ${totals.income.toMajor()} in · ${totals.currencyCode} ${totals.expenses.toMajor()} out",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(16.dp))
        HisabakButton(
            text = "Add Transaction",
            onClick = onAdd,
            variant = ButtonVariant.Primary,
            leadingIcon = Icons.Filled.Add,
            fullWidth = true,
        )
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
                icon = iconForKey(null),
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
