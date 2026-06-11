package com.hisabak.feature.brand.presentation.list

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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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
import com.hisabak.ui.components.CreateActionButton
import com.hisabak.ui.components.DarkPromoBanner
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.FilterChipRow
import com.hisabak.ui.components.IconTile
import com.hisabak.ui.components.ListRow
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.components.iconForKey
import com.hisabak.ui.components.tintPairForColor
import com.hisabak.ui.theme.HisabakTheme

@Composable
fun BrandListScreen(
    state: BrandListUiState,
    onSearchChange: (String) -> Unit,
    onCategoryFilterChange: (CategoryId?) -> Unit,
    onDelete: (BrandId) -> Unit,
    onAdd: () -> Unit,
    onEdit: (BrandId) -> Unit,
) {
    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val mostUsed = state.rows.firstOrNull()
    val filterOptions: List<Pair<String, CategoryId?>> = buildList {
        add("All" to null)
        state.availableCategories.forEach { add(it.name to it.id) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { BrandHeader(onCreate = onAdd) }
        item { SearchField(value = state.search, onValueChange = onSearchChange, placeholder = "Search brands", modifier = Modifier.fillMaxWidth()) }
        item { InsightsRow(totalBrands = state.rows.size, categoryCount = state.availableCategories.size) }
        if (mostUsed != null) item { MostUsedBrandCard(name = mostUsed.name, categoryName = mostUsed.categoryName, colorKey = mostUsed.categoryColor) }
        if (state.availableCategories.isNotEmpty()) {
            item {
                FilterChipRow(
                    options = filterOptions,
                    selected = state.categoryFilter,
                    onSelect = onCategoryFilterChange,
                    contentPadding = PaddingValues(horizontal = 0.dp),
                )
            }
        }
        item {
            SectionHeader(
                title = "Recent Brands",
                actionLabel = if (state.rows.size > 4) "View All" else null,
                onAction = if (state.rows.size > 4) ({ /* full-list view TBD */ }) else null,
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
                    subtitle = if (state.search.isBlank()) "Tap Create Brand to add one." else "Nothing matches \"${state.search}\".",
                )
            }
        } else {
            items(state.rows.take(6), key = { it.id.value }) { row ->
                BrandRowItem(row = row, onEdit = { onEdit(row.id) }, onDelete = { onDelete(row.id) })
            }
        }
        item { Spacer(Modifier.height(4.dp)) }
        item {
            DarkPromoBanner(
                title = "Spending Insights",
                body = "Discover hidden patterns in your monthly brand loyalty.",
                ctaLabel = "Unlock Premium",
                onCtaClick = { /* future: open premium upsell */ },
            )
        }
    }
}

@Composable
private fun BrandHeader(onCreate: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
            Text(
                "Brands",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                "Manage your spending partners",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        CreateActionButton(text = "Create Brand", onClick = onCreate)
    }
}

@Composable
private fun InsightsRow(totalBrands: Int, categoryCount: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        InsightCard(
            icon = Icons.Filled.TrendingUp,
            label = "Total brands",
            value = totalBrands.toString(),
            accentBg = HisabakTheme.colors.incomeSoft,
            accentFg = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
        )
        InsightCard(
            icon = Icons.Filled.TrendingDown,
            label = "Categories",
            value = categoryCount.toString(),
            accentBg = HisabakTheme.colors.expenseSoft,
            accentFg = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun InsightCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    accentBg: androidx.compose.ui.graphics.Color,
    accentFg: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    SurfaceCard(modifier = modifier) {
        IconTile(
            icon = icon,
            size = 28.dp,
            iconSize = 16.dp,
            background = accentBg,
            foreground = accentFg,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            color = accentFg,
        )
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MostUsedBrandCard(name: String, categoryName: String?, colorKey: String?) {
    val (bg, fg) = tintPairForColor(colorKey)
    SurfaceCard(contentPadding = 20.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                Text(
                    "MOST USED",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(bottom = 6.dp),
                )
                Text(
                    name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    categoryName ?: "Uncategorized",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            IconTile(
                icon = Icons.Filled.Stars,
                size = 56.dp,
                iconSize = 28.dp,
                background = bg,
                foreground = fg,
                shape = androidx.compose.foundation.shape.CircleShape,
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
                icon = iconForKey(null),
                background = bg,
                foreground = fg,
            )
        },
        trailing = {
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.DeleteOutline,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        onClick = onEdit,
    )
}
