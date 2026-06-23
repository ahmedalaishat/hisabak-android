package com.hisabak.feature.backup.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BackupRoute(
    modifier: Modifier = Modifier,
    viewModel: BackupViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BackupScreen(
        state = state,
        onSetEnabled = viewModel::setEnabled,
        onSetEncryptionEnabled = viewModel::setEncryptionEnabled,
        onSetPassphrase = viewModel::setPassphrase,
        onSetPeriod = viewModel::setAutoBackupPeriod,
        modifier = modifier,
    )
}
