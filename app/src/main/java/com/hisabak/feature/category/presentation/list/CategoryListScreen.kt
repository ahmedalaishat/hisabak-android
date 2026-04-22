package com.hisabak.feature.category.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.ui.components.CircleIconTile
import com.hisabak.ui.components.CreateActionButton
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.FilterChipRow
import com.hisabak.ui.components.IconTile
import com.hisabak.ui.components.ProgressBar
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.StatAccent
import com.hisabak.ui.components.StatCard
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.components.iconForKey
import com.hisabak.ui.components.tintPairForColor
import com.hisabak.ui.theme.TintEmerald
import com.hisabak.ui.theme.TintOrange
import com.hisabak.ui.theme.TintOrangeOn

@Composable
fun CategoryListScreen(
    state: CategoryListUiState,
    onSearchChange: (String) -> Unit,
    onTypeFilterChange: (CategoryType?) -> Unit,
    onDelete: (CategoryId) -> Unit,
    onAdd: () -> Unit,
    onEdit: (CategoryId) -> Unit,
) {
    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
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
    val mostUsed = state.rows.firstOrNull { it.type == CategoryType.EXPENSES }
        ?: state.rows.firstOrNull()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Header row spans both columns.
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Categories",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                CreateActionButton(text = "Create Category", onClick = onAdd, showIcon = false)
            }
        }
        // Search + Most Used bento card spans both columns.
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
            SearchField(
                value = state.search,
                onValueChange = onSearchChange,
                placeholder = "Search categories",
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (mostUsed != null) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                MostUsedCard(
                    name = mostUsed.name,
                    count = state.rows.size,
                )
            }
        }
        // Mini stats row (2 cards, one per column).
        item {
            StatCard(
                label = "Income types",
                value = incomeCount.toString(),
                icon = Icons.Filled.ShoppingBag,
                progress = if (state.rows.isEmpty()) 0f else incomeCount.toFloat() / state.rows.size,
                accent = StatAccent.Positive,
            )
        }
        item {
            StatCard(
                label = "Spending types",
                value = expenseCount.toString(),
                icon = Icons.Filled.ShoppingBag,
                progress = if (state.rows.isEmpty()) 0f else expenseCount.toFloat() / state.rows.size,
                accent = StatAccent.Negative,
            )
        }
        // Filter chips span both columns.
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
            FilterChipRow(
                options = typeOptions,
                selected = state.typeFilter,
                onSelect = onTypeFilterChange,
                contentPadding = PaddingValues(0.dp),
            )
        }
        // Empty state spans both columns.
        if (state.rows.isEmpty()) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                EmptyStatePanel(
                    title = when {
                        state.search.isNotBlank() -> "No matches"
                        state.typeFilter != null -> "No categories of this type"
                        else -> "No categories yet"
                    },
                    subtitle = if (state.search.isBlank())
                        "Tap Create Category to get started."
                    else
                        "Nothing matches \"${state.search}\".",
                )
            }
        } else {
            // Category tiles.
            items(state.rows, key = { it.id.value }) { row ->
                CategoryTile(
                    row = row,
                    onEdit = { onEdit(row.id) },
                    onDelete = { onDelete(row.id) },
                )
            }
            // "Add New" dashed placeholder tile.
            item {
                AddNewTile(onClick = onAdd)
            }
        }
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun MostUsedCard(name: String, count: Int) {
    SurfaceCard(contentPadding = 20.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                Text(
                    "MOST USED",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            IconTile(
                icon = Icons.Filled.ShoppingBag,
                size = 44.dp,
                iconSize = 22.dp,
                background = TintOrange,
                foreground = TintOrangeOn,
                shape = androidx.compose.foundation.shape.CircleShape,
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "$count categories total",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(
                    Icons.Filled.DeleteOutline,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            row.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
        Text(
            row.type.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AddNewTile(onClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CircleIconTile(
                icon = Icons.Filled.AddCircleOutline,
                size = 32.dp,
                iconSize = 20.dp,
                background = TintEmerald,
                foreground = MaterialTheme.colorScheme.primary,
            )
            Text(
                "Add New",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
