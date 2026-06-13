package com.hisabak.feature.category.presentation.list

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.ui.components.Badge
import com.hisabak.ui.components.BadgeTone
import com.hisabak.ui.components.CircleIconTile
import com.hisabak.ui.components.CreateActionButton
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.ExpensesStatCard
import com.hisabak.ui.components.FilterChipRow
import com.hisabak.ui.components.IconTile
import com.hisabak.ui.components.IncomeStatCard
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.components.iconForKey
import com.hisabak.ui.components.tintPairForColor
import com.hisabak.ui.theme.HisabakTheme
import com.hisabak.ui.theme.Sizing
import com.hisabak.ui.theme.Spacing

@Composable
fun CategoryListScreen(
    state: CategoryListUiState,
    onSearchChange: (String) -> Unit,
    onTypeFilterChange: (CategoryType?) -> Unit,
    onDelete: (CategoryId) -> Unit,
    onAdd: () -> Unit,
    onEdit: (CategoryId) -> Unit,
    showHeader: Boolean = true,
) {
    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val typeOptions: List<Pair<String, CategoryType?>> = listOf(
        "All" to null,
        "Expenses" to CategoryType.EXPENSES,
        "Income" to CategoryType.INCOME,
        "Savings" to CategoryType.SAVINGS,
        "Investment" to CategoryType.INVESTMENT,
    )

    val incomeCount = state.rows.count { it.type == CategoryType.INCOME }
    val expenseCount = state.rows.count { it.type == CategoryType.EXPENSES }
    val total = state.rows.size
    val mostUsed = state.rows.maxByOrNull { row ->
        state.rows.count { it.type == row.type }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Spacing.pageMargin,
            end = Spacing.pageMargin,
            top = Spacing.s5,
            bottom = Spacing.s10 + Spacing.s7, // clear the Manage FAB
        ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
        verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
    ) {
        if (showHeader) item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Categories",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                CreateActionButton(text = "New category", onClick = onAdd)
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            SearchField(
                value = state.search,
                onValueChange = onSearchChange,
                placeholder = "Search categories",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            FilterChipRow(
                options = typeOptions,
                selected = state.typeFilter,
                onSelect = onTypeFilterChange,
                contentPadding = PaddingValues(vertical = Spacing.s1),
            )
        }

        if (mostUsed != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                MostUsedCard(row = mostUsed)
            }
        }

        item {
            IncomeStatCard(
                value = incomeCount.toString(),
                progress = if (total == 0) 0f else incomeCount.toFloat() / total,
            )
        }

        item {
            ExpensesStatCard(
                value = expenseCount.toString(),
                progress = if (total == 0) 0f else expenseCount.toFloat() / total,
            )
        }

        if (state.rows.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EmptyStatePanel(
                    title = when {
                        state.search.isNotBlank() -> "No matches"
                        state.typeFilter != null -> "No categories of this type"
                        else -> "No categories yet"
                    },
                    subtitle = if (state.search.isBlank())
                        "Tap \"New category\" to get started."
                    else
                        "Nothing matches \"${state.search}\".",
                )
            }
        } else {
            items(state.rows, key = { it.id.value }) { row ->
                CategoryTile(
                    row = row,
                    onEdit = { onEdit(row.id) },
                    onDelete = { onDelete(row.id) },
                )
            }
            item {
                AddNewTile(onClick = onAdd)
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.height(Spacing.s3))
        }
    }
}

@Composable
private fun MostUsedCard(row: CategoryRow) {
    val (bg, fg) = tintPairForColor(row.color)
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.s4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconTile(
                icon = iconForKey(row.icon),
                size = Sizing.controlHeight,
                iconSize = Sizing.icon,
                background = bg,
                foreground = fg,
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp),
                    )
                    Text(
                        "MOST USED",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(Modifier.height(Spacing.s1))
                Text(
                    row.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Badge(
                label = row.type.displayName(),
                tone = row.type.badgeTone(),
            )
        }
    }
}

@Composable
private fun CategoryTile(
    row: CategoryRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val (bg, fg) = tintPairForColor(row.color)
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEdit,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            IconTile(
                icon = iconForKey(row.icon),
                background = bg,
                foreground = fg,
            )
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp),
            ) {
                Icon(
                    Icons.Filled.DeleteOutline,
                    contentDescription = "Delete ${row.name}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            row.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
        Spacer(Modifier.height(Spacing.s2))
        Badge(
            label = row.type.displayName(),
            tone = row.type.badgeTone(),
        )
    }
}

@Composable
private fun AddNewTile(onClick: () -> Unit) {
    val shape = MaterialTheme.shapes.medium
    val dashColor = MaterialTheme.colorScheme.outlineVariant
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val stroke = Stroke(
                width = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f),
            )
            drawRoundRect(
                color = dashColor,
                style = stroke,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
            )
        }
        Column(
            modifier = Modifier.padding(Spacing.s6),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.s3),
        ) {
            CircleIconTile(
                icon = Icons.Filled.Add,
                size = 40.dp,
                iconSize = Sizing.iconSm,
                background = HisabakTheme.colors.incomeSoft,
                foreground = MaterialTheme.colorScheme.primary,
            )
            Text(
                "Add new",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun CategoryType.displayName(): String = when (this) {
    CategoryType.INCOME -> "Income"
    CategoryType.EXPENSES -> "Expense"
    CategoryType.SAVINGS -> "Savings"
    CategoryType.INVESTMENT -> "Investment"
}

private fun CategoryType.badgeTone(): BadgeTone = when (this) {
    CategoryType.INCOME -> BadgeTone.Income
    CategoryType.EXPENSES -> BadgeTone.Expense
    CategoryType.SAVINGS -> BadgeTone.Savings
    CategoryType.INVESTMENT -> BadgeTone.Investment
}
