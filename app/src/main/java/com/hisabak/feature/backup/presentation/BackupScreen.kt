package com.hisabak.feature.backup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.hisabak.R
import com.hisabak.core.domain.backup.BackupError
import com.hisabak.ui.components.ButtonVariant
import com.hisabak.ui.components.HisabakButton
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.theme.Spacing

private const val MIN_PASSPHRASE_LENGTH = 8

@Composable
fun BackupScreen(
    state: BackupUiState,
    importFileName: String?,
    onExport: (passphrase: String) -> Unit,
    onChooseImportFile: () -> Unit,
    onRestore: (passphrase: String) -> Unit,
    onDismissResult: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var exportPass by rememberSaveable { mutableStateOf("") }
    var exportConfirm by rememberSaveable { mutableStateOf("") }
    var importPass by rememberSaveable { mutableStateOf("") }
    var showConfirm by rememberSaveable { mutableStateOf(false) }

    val tooShort = exportPass.isNotEmpty() && exportPass.length < MIN_PASSPHRASE_LENGTH
    val mismatch = exportConfirm.isNotEmpty() && exportConfirm != exportPass
    val canExport = !state.busy && exportPass.length >= MIN_PASSPHRASE_LENGTH && exportPass == exportConfirm
    val canRestore = !state.busy && importFileName != null && importPass.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.pageMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.sectionGap),
    ) {
        state.result?.let { ResultBanner(it, onDismissResult) }

        // ---- Export ----
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sectionTitleGap)) {
            SectionHeader(title = stringResource(R.string.backup_export_title))
            SurfaceCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.s4)) {
                    Text(
                        stringResource(R.string.backup_export_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PassphraseField(
                        value = exportPass,
                        onChange = { exportPass = it },
                        label = stringResource(R.string.backup_passphrase),
                        error = if (tooShort) stringResource(R.string.backup_passphrase_too_short) else null,
                        enabled = !state.busy,
                    )
                    PassphraseField(
                        value = exportConfirm,
                        onChange = { exportConfirm = it },
                        label = stringResource(R.string.backup_passphrase_confirm),
                        error = if (mismatch) stringResource(R.string.backup_passphrase_mismatch) else null,
                        enabled = !state.busy,
                    )
                    Text(
                        stringResource(R.string.backup_passphrase_warning),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    HisabakButton(
                        text = stringResource(R.string.backup_export_action),
                        onClick = { onExport(exportPass) },
                        enabled = canExport,
                        fullWidth = true,
                    )
                }
            }
        }

        // ---- Restore ----
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sectionTitleGap)) {
            SectionHeader(title = stringResource(R.string.backup_restore_title))
            SurfaceCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.s4)) {
                    Text(
                        stringResource(R.string.backup_restore_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    HisabakButton(
                        text = stringResource(R.string.backup_choose_file),
                        onClick = onChooseImportFile,
                        variant = ButtonVariant.Secondary,
                        enabled = !state.busy,
                        fullWidth = true,
                    )
                    Text(
                        importFileName ?: stringResource(R.string.backup_no_file),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PassphraseField(
                        value = importPass,
                        onChange = { importPass = it },
                        label = stringResource(R.string.backup_passphrase),
                        error = null,
                        enabled = !state.busy && importFileName != null,
                    )
                    HisabakButton(
                        text = stringResource(R.string.backup_restore_action),
                        onClick = { showConfirm = true },
                        variant = ButtonVariant.Danger,
                        enabled = canRestore,
                        fullWidth = true,
                    )
                }
            }
        }

        if (state.busy) {
            CircularProgressIndicator(Modifier.padding(top = Spacing.s2))
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text(stringResource(R.string.backup_confirm_title)) },
            text = { Text(stringResource(R.string.backup_confirm_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    onRestore(importPass)
                }) { Text(stringResource(R.string.backup_restore_action)) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}

@Composable
private fun PassphraseField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    error: String?,
    enabled: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        enabled = enabled,
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ResultBanner(outcome: BackupOutcome, onDismiss: () -> Unit) {
    val isError = outcome is BackupOutcome.Failed || outcome is BackupOutcome.FileError
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (isError) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLowest
        },
        onClick = onDismiss,
    ) {
        Text(
            text = outcome.message(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isError) {
                MaterialTheme.colorScheme.onErrorContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            },
        )
    }
}

@Composable
private fun BackupOutcome.message(): String = when (this) {
    BackupOutcome.Exported -> stringResource(R.string.backup_result_exported)
    is BackupOutcome.Imported -> stringResource(R.string.backup_result_imported, records)
    BackupOutcome.FileError -> stringResource(R.string.backup_error_file)
    is BackupOutcome.Failed -> when (error) {
        BackupError.WrongPassphrase -> stringResource(R.string.backup_error_wrong_passphrase)
        BackupError.Corrupt -> stringResource(R.string.backup_error_corrupt)
        BackupError.Empty -> stringResource(R.string.backup_error_empty)
        is BackupError.UnsupportedVersion -> stringResource(R.string.backup_error_unsupported)
    }
}
