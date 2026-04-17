package com.hisabak.feature.sms.presentation.inbox

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hisabak.core.presentation.LaunchedViewEffectHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SmsInboxRoute(
    modifier: Modifier = Modifier,
    viewModel: SmsInboxViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedViewEffectHandler(
        effectFlow = viewModel.effect,
        onConsumeEffect = { viewModel.onIntent(SmsInboxIntent.ConsumeEffect) },
        onEffect = { effect ->
            when (effect) {
                is SmsInboxEffect.ParseFailed ->
                    snackbarHostState.showSnackbar("Could not parse: ${effect.reason}")
                is SmsInboxEffect.TransactionCreated ->
                    snackbarHostState.showSnackbar("Transaction created: ${formatMoney(effect.amount)}")
            }
        },
    )

    SmsInboxScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onSearchChange = { viewModel.onIntent(SmsInboxIntent.SearchChanged(it)) },
        onDraftChange = { viewModel.onIntent(SmsInboxIntent.DraftChanged(it)) },
        onIngest = { viewModel.onIntent(SmsInboxIntent.IngestDraft) },
        onDelete = { viewModel.onIntent(SmsInboxIntent.Delete(it)) },
        modifier = modifier,
    )
}
