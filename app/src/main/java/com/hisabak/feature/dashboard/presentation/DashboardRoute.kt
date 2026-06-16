package com.hisabak.feature.dashboard.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardRoute(
    onShowUncategorized: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DashboardScreen(
        state = state,
        onPeriodChange = { viewModel.onIntent(DashboardIntent.PeriodChanged(it)) },
        onShowUncategorized = onShowUncategorized,
        modifier = modifier,
    )
}
