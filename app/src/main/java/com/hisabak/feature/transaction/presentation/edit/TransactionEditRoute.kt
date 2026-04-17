package com.hisabak.feature.transaction.presentation.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hisabak.feature.transaction.domain.TransactionId
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TransactionEditRoute(
    transactionId: TransactionId?,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    viewModel: TransactionEditViewModel = koinViewModel(
        key = transactionId?.value ?: "new",
        parameters = { parametersOf(transactionId) },
    ),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) onDone()
    }

    TransactionEditScreen(
        state = state,
        onAmountChange = viewModel::onAmountChange,
        onBrandChange = viewModel::onBrandChange,
        onNoteChange = viewModel::onNoteChange,
        onSave = viewModel::save,
        onCancel = onCancel,
    )
}
