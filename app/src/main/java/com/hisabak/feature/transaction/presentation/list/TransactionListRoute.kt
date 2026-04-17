package com.hisabak.feature.transaction.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hisabak.feature.transaction.domain.TransactionId
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TransactionListRoute(
    onAdd: () -> Unit,
    onEdit: (TransactionId) -> Unit,
    viewModel: TransactionListViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val search by viewModel.searchQuery.collectAsStateWithLifecycle()

    TransactionListScreen(
        state = state,
        search = search,
        onSearchChange = viewModel::onSearchChange,
        onDelete = viewModel::onDelete,
        onAdd = onAdd,
        onEdit = onEdit,
    )
}
