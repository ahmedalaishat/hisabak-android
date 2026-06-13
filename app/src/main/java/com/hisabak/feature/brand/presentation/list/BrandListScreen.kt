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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Layers
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
import com.hisabak.ui.components.Badge
import com.hisabak.ui.components.BadgeTone
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
import com.hisabak.ui.theme.HisabakTheme
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

    val mostUsed = state.rows.firstOrNull()
    val filterOptions: List<Pair<String, CategoryId?>> = buildList {
        add("All" to null)
        state.availableCategories.forEach { add(it.name to it.id) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = Spacing.pageMargin, vertical = Spacing.s5),
        verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
    ) {
        if (showHeader) item { HeaderRow(onCreate = onAdd) }

        item { InsightPills(brandCount = state.rows.size, categoryCount = state.availableCategories.size) }

        item {
            SearchField(
                value = state.search,
                onValueChange = onSearchChange,
                placeholder = "Search brands",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (mostUsed != null) {
            item { MostUsedCard(row = mostUsed) }
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
private fun InsightPills(brandCount: Int, categoryCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
    ) {
        SurfaceCard(modifier = Modifier.weight(1f), contentPadding = Spacing.s4) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                IconTile(
                    icon = Icons.Filled.Storefront,
                    size = Spacing.s8,
                    iconSize = 16.dp,
                    background = MaterialTheme.colorScheme.surfaceVariant,
                    foreground = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Column {
                    Text(
                        text = brandCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "brands",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        SurfaceCard(modifier = Modifier.weight(1f), contentPadding = Spacing.s4) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                IconTile(
                    icon = Icons.Filled.Layers,
                    size = Spacing.s8,
                    iconSize = 16.dp,
                    background = MaterialTheme.colorScheme.surfaceVariant,
                    foreground = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Column {
                    Text(
                        text = categoryCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "categories",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun MostUsedCard(row: BrandRow) {
    val (tileBg, tileFg) = tintPairForColor(row.categoryColor)
    val incomeSoft = HisabakTheme.colors.incomeSoft

    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = Spacing.cardPadding,
        backgroundColor = incomeSoft,
        borderColor = HisabakTheme.colors.income.copy(alpha = 0.18f),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s3),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = HisabakTheme.colors.income,
                    )
                    Badge(label = "Most used", tone = BadgeTone.Income)
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = row.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (row.categoryName != null) {
                    Spacer(Modifier.height(Spacing.s3))
                    ColoredFilterChip(
                        label = row.categoryName,
                        colorKey = row.categoryColor,
                        selected = false,
                        onClick = {},
                    )
                }
            }
            IconTile(
                icon = iconForKey(row.categoryIcon),
                size = 52.dp,
                iconSize = 26.dp,
                background = tileBg,
                foreground = tileFg,
                shape = androidx.compose.foundation.shape.CircleShape,
            )
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
        contentPadding = PaddingValues(horizontal = 0.dp),
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
