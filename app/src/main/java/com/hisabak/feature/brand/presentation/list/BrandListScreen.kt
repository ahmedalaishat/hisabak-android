package com.hisabak.feature.brand.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.presentation.CategoryStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandListScreen(
    state: BrandListUiState,
    onSearchChange: (String) -> Unit,
    onCategoryFilterChange: (CategoryId?) -> Unit,
    onDelete: (BrandId) -> Unit,
    onAdd: () -> Unit,
    onEdit: (BrandId) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Brands") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Filled.Add, contentDescription = "Add brand")
            }
        },
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = state.search,
                onValueChange = onSearchChange,
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            CategoryFilterChips(
                options = state.availableCategories,
                selected = state.categoryFilter,
                onSelect = onCategoryFilterChange,
            )

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.rows.isEmpty() -> EmptyState(state)
                else -> LazyColumn(Modifier.fillMaxSize()) {
                    items(state.rows, key = { it.id.value }) { row ->
                        BrandRowItem(
                            row = row,
                            onEdit = { onEdit(row.id) },
                            onDelete = { onDelete(row.id) },
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterChips(
    options: List<BrandListUiState.CategoryOption>,
    selected: CategoryId?,
    onSelect: (CategoryId?) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
    ) {
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelect(null) },
                label = { Text("All") },
            )
        }
        items(options, key = { it.id.value }) { option ->
            FilterChip(
                selected = selected == option.id,
                onClick = { onSelect(option.id) },
                label = { Text(option.name) },
                leadingIcon = {
                    Box(
                        Modifier
                            .size(10.dp)
                            .background(CategoryStyle.color(option.color), CircleShape),
                    )
                },
            )
        }
    }
}

@Composable
private fun EmptyState(state: BrandListUiState) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            when {
                state.search.isNotBlank() -> "No brands match \"${state.search}\"."
                state.categoryFilter != null -> "No brands in this category."
                else -> "No brands yet. Tap + to add one."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun BrandRowItem(
    row: BrandRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(14.dp)
                .background(CategoryStyle.color(row.categoryColor), CircleShape),
        )
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Text(row.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                row.categoryName ?: "Uncategorized",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
        }
    }
}
