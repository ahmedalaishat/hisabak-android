package com.hisabak.feature.transaction.presentation.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hisabak.core.presentation.LaunchedViewEffectHandler
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

    LaunchedViewEffectHandler(
        effectFlow = viewModel.effect,
        onConsumeEffect = { viewModel.onIntent(TransactionEditIntent.ConsumeEffect) },
    ) { effect ->
        when (effect) {
            TransactionEditEffect.Saved -> onDone()
        }
    }

    TransactionEditScreen(
        state = state,
        onAmountChange = { viewModel.onIntent(TransactionEditIntent.AmountChanged(it)) },
        onBrandChange = { viewModel.onIntent(TransactionEditIntent.BrandChanged(it)) },
        onNoteChange = { viewModel.onIntent(TransactionEditIntent.NoteChanged(it)) },
        onSave = { viewModel.onIntent(TransactionEditIntent.Save) },
        onCancel = onCancel,
    )
}
