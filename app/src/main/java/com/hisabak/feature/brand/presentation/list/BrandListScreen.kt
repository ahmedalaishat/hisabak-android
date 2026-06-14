package com.hisabak.feature.brand.presentation.list

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.ui.components.CircleIconTile
import com.hisabak.ui.components.ColoredFilterChip
import com.hisabak.ui.components.CreateActionButton
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.IconTile
import com.hisabak.ui.components.ListRow
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.components.iconForKey
import com.hisabak.ui.components.tintPairForColor
import com.hisabak.ui.theme.Sizing
import com.hisabak.ui.theme.Spacing

@Composable
fun BrandListScreen(
    state: BrandListUiState,
    onSearchChange: (String) -> Unit,
    onCategoryFilterChange: (CategoryId?) -> Unit,
    onDelete: (BrandId) -> Unit,
    onAdd: () -> Unit,
    onEdit: (BrandId) -> Unit,
    showHeader: Boolean = true,
) {
    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val filterOptions: List<Pair<String, CategoryId?>> = buildList {
        add("All" to null)
        state.availableCategories.forEach { add(it.name to it.id) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = Spacing.pageMargin,
            end = Spacing.pageMargin,
            top = Spacing.s5,
            bottom = Spacing.s10 + Spacing.s7, // clear the Manage FAB
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
    ) {
        if (showHeader) item { HeaderRow(onCreate = onAdd) }

        item {
            SearchField(
                value = state.search,
                onValueChange = onSearchChange,
                placeholder = "Search brands",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (state.availableCategories.isNotEmpty()) {
            item {
                CategoryFilterRow(
                    allOptions = filterOptions,
                    colorByCategory = state.availableCategories.associate { it.id to it.color },
                    selected = state.categoryFilter,
                    onSelect = onCategoryFilterChange,
                )
            }
        }

        // Most-used card hidden for now (see MostUsedCard / BrandRow.transactionCount).

        item {
            SectionHeader(
                title = "All brands",
                actionLabel = if (state.rows.size > 6) "See all" else null,
                onAction = if (state.rows.size > 6) ({}) else null,
            )
        }

        if (state.rows.isEmpty()) {
            item {
                EmptyStatePanel(
                    title = when {
                        state.search.isNotBlank() -> "No matches"
                        state.categoryFilter != null -> "No brands in this category"
                        else -> "No brands yet"
                    },
                    subtitle = if (state.search.isBlank())
                        "Tap New brand to add one."
                    else
                        "Nothing matches \"${state.search}\".",
                    icon = Icons.Filled.Storefront,
                )
            }
        } else {
            items(state.rows, key = { it.id.value }) { row ->
                BrandRowItem(
                    row = row,
                    onEdit = { onEdit(row.id) },
                    onDelete = { onDelete(row.id) },
                )
            }
        }

        item { Spacer(Modifier.height(Spacing.s3)) }
    }
}

@Composable
private fun HeaderRow(onCreate: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Brands",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        CreateActionButton(text = "New brand", onClick = onCreate)
    }
}


@Composable
private fun MostUsedCard(row: BrandRow) {
    val (tileBg, tileFg) = tintPairForColor(row.categoryColor)
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
                icon = iconForKey(row.categoryIcon),
                size = Sizing.controlHeight,
                iconSize = Sizing.icon,
                background = tileBg,
                foreground = tileFg,
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
                    text = row.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            if (row.categoryName != null) {
                ColoredFilterChip(
                    label = row.categoryName,
                    colorKey = row.categoryColor,
                    selected = false,
                    onClick = {},
                )
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    allOptions: List<Pair<String, CategoryId?>>,
    colorByCategory: Map<CategoryId, String>,
    selected: CategoryId?,
    onSelect: (CategoryId?) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Spacing.s3),
        contentPadding = PaddingValues(vertical = Spacing.s1),
    ) {
        items(allOptions) { (label, value) ->
            ColoredFilterChip(
                label = label,
                colorKey = value?.let { colorByCategory[it] },
                selected = selected == value,
                onClick = { onSelect(value) },
            )
        }
    }
}

@Composable
private fun BrandRowItem(
    row: BrandRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val (bg, fg) = tintPairForColor(row.categoryColor)
    ListRow(
        title = row.name,
        subtitle = row.categoryName,
        leading = {
            CircleIconTile(
                icon = iconForKey(row.categoryIcon),
                background = bg,
                foreground = fg,
            )
        },
        trailing = {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.DeleteOutline,
                    contentDescription = "Delete ${row.name}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        onClick = onEdit,
    )
}
