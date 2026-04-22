package com.hisabak.feature.transaction.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hisabak.core.common.Money
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.transaction.domain.TransactionId
import com.hisabak.ui.components.CircleIconTile
import com.hisabak.ui.components.CreateActionButton
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.ExpensesStatCard
import com.hisabak.ui.components.GradientBanner
import com.hisabak.ui.components.IncomeStatCard
import com.hisabak.ui.components.ListRow
import com.hisabak.ui.components.ProgressBar
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.components.TrailingAmount
import com.hisabak.ui.components.iconForKey
import com.hisabak.ui.components.tintPairForColor
import java.time.Duration
import java.time.Instant
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

    val totals = remember(state.rows) { computeTotals(state.rows) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { BalanceCard(totals) }
        item { TotalsRow(totals) }
        item { SearchAndNew(search = state.search, onSearchChange = onSearchChange, onAdd = onAdd) }
        item {
            SectionHeader(
                title = "Recent Activities",
                actionLabel = if (state.rows.isNotEmpty()) "See all" else null,
                onAction = if (state.rows.isNotEmpty()) ({ /* full-list view TBD */ }) else null,
            )
        }
        if (state.rows.isEmpty()) {
            item {
                EmptyStatePanel(
                    title = if (state.search.isBlank()) "No transactions yet" else "No matches",
                    subtitle = if (state.search.isBlank())
                        "Tap New to log your first transaction."
                    else
                        "Nothing matches \"${state.search}\".",
                )
            }
        } else {
            items(state.rows.take(10), key = { it.id.value }) { row ->
                TransactionRowItem(
                    row = row,
                    onEdit = { onEdit(row.id) },
                    onDelete = { onDelete(row.id) },
                )
            }
        }
        item { Spacer(Modifier.height(4.dp)) }
        item {
            GradientBanner(
                title = "Smart Saving Tip",
                body = savingTip(totals),
            )
        }
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
private fun BalanceCard(totals: Totals) {
    SurfaceCard(contentPadding = 20.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            androidx.compose.foundation.layout.Column {
                Text(
                    "TOTAL BALANCE",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    formatMoneyMajor(totals.balance, totals.currencyCode),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Icon(
                Icons.Filled.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "Month to date",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            val pct = if (totals.income == 0L) 0 else
                ((totals.income - totals.expenses).coerceAtLeast(0L) * 100 / totals.income).toInt()
            Text(
                "$pct%",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(Modifier.height(6.dp))
        val ratio = if (totals.income == 0L) 0f else
            ((totals.income - totals.expenses).coerceAtLeast(0L).toFloat() / totals.income.toFloat())
        ProgressBar(progress = ratio)
        Spacer(Modifier.height(6.dp))
        Text(
            "You've logged ${totals.income.toMajor()} income and ${totals.expenses.toMajor()} spending this view.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun TotalsRow(totals: Totals) {
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

@Composable
private fun SearchAndNew(
    search: String,
    onSearchChange: (String) -> Unit,
    onAdd: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchField(
            value = search,
            onValueChange = onSearchChange,
            placeholder = "Search transactions...",
            modifier = Modifier.weight(1f),
        )
        CreateActionButton(text = "New", onClick = onAdd)
    }
}

@Composable
private fun TransactionRowItem(
    row: TransactionRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val positive = row.categoryType == CategoryType.INCOME
    val (bg, fg) = tintPairForColor(row.categoryColor)
    ListRow(
        title = row.brandName,
        subtitle = listOfNotNull(row.categoryName, formatRelative(row.occurredAt))
            .joinToString(" • "),
        leading = {
            CircleIconTile(
                icon = iconForKey(null), // categories track icons by name; key lookup could slot in here
                background = bg,
                foreground = fg,
            )
        },
        trailing = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TrailingAmount(
                    amount = formatSignedAmount(row.amount, positive),
                    positive = positive,
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        onClick = onEdit,
    )
}

private fun savingTip(totals: Totals): String = when {
    totals.income == 0L && totals.expenses == 0L ->
        "Log a few transactions to unlock personalized insights."
    totals.expenses > totals.income ->
        "Spending is outpacing income this period — review your top categories."
    else ->
        "You're saving ${(((totals.income - totals.expenses).coerceAtLeast(0L)).toMajor())} this period. Keep it up."
}

private fun Long.toMajor(): String {
    val major = this / 100
    val minor = abs(this % 100)
    return "$major.${minor.toString().padStart(2, '0')}"
}

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

