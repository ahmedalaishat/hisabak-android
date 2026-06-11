package com.hisabak.feature.brand.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hisabak.feature.brand.domain.BrandId
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BrandListRoute(
    onAdd: () -> Unit,
    onEdit: (BrandId) -> Unit,
    showHeader: Boolean = true,
    viewModel: BrandListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BrandListScreen(
        state = state,
        onSearchChange = { viewModel.onIntent(BrandListIntent.SearchChanged(it)) },
        onCategoryFilterChange = { viewModel.onIntent(BrandListIntent.CategoryFilterChanged(it)) },
        onDelete = { viewModel.onIntent(BrandListIntent.Delete(it)) },
        onAdd = onAdd,
        onEdit = onEdit,
        showHeader = showHeader,
    )
}
