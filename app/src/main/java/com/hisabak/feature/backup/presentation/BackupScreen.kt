package com.hisabak.feature.backup.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.hisabak.R
import com.hisabak.core.domain.backup.AutoBackupPeriod
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
    onSetEnabled: (Boolean) -> Unit,
    onSetEncryptionEnabled: (Boolean) -> Unit,
    onSetPassphrase: (String) -> Unit,
    onSetPeriod: (AutoBackupPeriod) -> Unit,
    onConnectAccount: () -> Unit,
    onBackupNow: () -> Unit,
    onDismissMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPassphraseSheet by rememberSaveable { mutableStateOf(false) }
    var showPeriodSheet by rememberSaveable { mutableStateOf(false) }

    val canBackupNow = !state.busy && state.account != null &&
        (!state.encryptionEnabled || state.passphraseSet)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.pageMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.sectionGap),
    ) {
        state.message?.let { MessageBanner(it, onDismissMessage) }

        // ---- Google Drive ----
        Section(title = stringResource(R.string.backup_drive_title)) {
            SettingRow(
                title = stringResource(R.string.backup_enable),
                subtitle = stringResource(R.string.backup_enable_hint),
            ) {
                Switch(
                    checked = state.enabled,
                    onCheckedChange = { enabled ->
                        onSetEnabled(enabled)
                        if (enabled && state.account == null) onConnectAccount()
                    },
                )
            }

            if (state.enabled) {
                SettingRow(
                    title = stringResource(R.string.backup_account),
                    subtitle = state.account?.email ?: stringResource(R.string.backup_account_not_connected),
                    onClick = onConnectAccount,
                )
                SettingRow(
                    title = stringResource(R.string.backup_auto_title),
                    subtitle = stringResource(state.period.labelRes()),
                    onClick = { showPeriodSheet = true },
                )
                HisabakButton(
                    text = stringResource(if (state.busy) R.string.backup_running else R.string.backup_now),
                    onClick = onBackupNow,
                    variant = ButtonVariant.Primary,
                    enabled = canBackupNow,
                    fullWidth = true,
                )
            }
        }

        // ---- Security ----
        if (state.enabled) {
            Section(title = stringResource(R.string.backup_security_title)) {
                SettingRow(
                    title = stringResource(R.string.backup_encrypt),
                    subtitle = stringResource(R.string.backup_encrypt_hint),
                ) {
                    Switch(
                        checked = state.encryptionEnabled,
                        onCheckedChange = { enabled ->
                            onSetEncryptionEnabled(enabled)
                            if (enabled && !state.passphraseSet) showPassphraseSheet = true
                        },
                    )
                }
                if (state.encryptionEnabled) {
                    SettingRow(
                        title = stringResource(R.string.backup_passphrase),
                        subtitle = stringResource(
                            if (state.passphraseSet) R.string.backup_passphrase_set
                            else R.string.backup_passphrase_not_set,
                        ),
                        onClick = { showPassphraseSheet = true },
                    )
                }
            }
        }
    }

    if (showPassphraseSheet) {
        PassphraseSheet(
            onDismiss = { showPassphraseSheet = false },
            onSave = {
                onSetPassphrase(it)
                showPassphraseSheet = false
            },
        )
    }

    if (showPeriodSheet) {
        PeriodSheet(
            selected = state.period,
            onSelect = {
                onSetPeriod(it)
                showPeriodSheet = false
            },
            onDismiss = { showPeriodSheet = false },
        )
    }
}

@Composable
private fun MessageBanner(message: BackupMessage, onDismiss: () -> Unit) {
    val isError = message is BackupMessage.Failed
    SurfaceCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceContainerLowest,
        onClick = onDismiss,
    ) {
        Text(
            text = message.text(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isError) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun BackupMessage.text(): String = when (this) {
    BackupMessage.BackedUp -> stringResource(R.string.backup_done)
    is BackupMessage.Failed -> stringResource(error.messageRes())
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sectionTitleGap)) {
        SectionHeader(title = title)
        SurfaceCard(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s4)) { content() }
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        trailing?.invoke()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PassphraseSheet(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var passphrase by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    val tooShort = passphrase.isNotEmpty() && passphrase.length < MIN_PASSPHRASE_LENGTH
    val mismatch = confirm.isNotEmpty() && confirm != passphrase
    val canSave = passphrase.length >= MIN_PASSPHRASE_LENGTH && passphrase == confirm

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.pageMargin)
                .padding(bottom = Spacing.sectionGap),
            verticalArrangement = Arrangement.spacedBy(Spacing.s4),
        ) {
            Text(
                stringResource(R.string.backup_passphrase_sheet_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                stringResource(R.string.backup_passphrase_warning),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            PassphraseField(
                value = passphrase,
                onChange = { passphrase = it },
                label = stringResource(R.string.backup_passphrase),
                error = if (tooShort) stringResource(R.string.backup_passphrase_too_short) else null,
            )
            PassphraseField(
                value = confirm,
                onChange = { confirm = it },
                label = stringResource(R.string.backup_passphrase_confirm),
                error = if (mismatch) stringResource(R.string.backup_passphrase_mismatch) else null,
            )
            HisabakButton(
                text = stringResource(R.string.backup_passphrase_save),
                onClick = { onSave(passphrase) },
                enabled = canSave,
                fullWidth = true,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodSheet(
    selected: AutoBackupPeriod,
    onSelect: (AutoBackupPeriod) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.sectionGap),
        ) {
            Text(
                stringResource(R.string.backup_auto_sheet_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = Spacing.pageMargin, vertical = Spacing.s3),
            )
            AutoBackupPeriod.entries.forEach { period ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = period == selected, onClick = { onSelect(period) })
                        .padding(horizontal = Spacing.pageMargin, vertical = Spacing.s4),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
                ) {
                    RadioButton(selected = period == selected, onClick = { onSelect(period) })
                    Text(
                        stringResource(period.labelRes()),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
private fun PassphraseField(value: String, onChange: (String) -> Unit, label: String, error: String?) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
    )
}

private fun AutoBackupPeriod.labelRes(): Int = when (this) {
    AutoBackupPeriod.NEVER -> R.string.backup_auto_never
    AutoBackupPeriod.DAILY -> R.string.backup_auto_daily
    AutoBackupPeriod.WEEKLY -> R.string.backup_auto_weekly
    AutoBackupPeriod.MONTHLY -> R.string.backup_auto_monthly
}

internal fun BackupError.messageRes(): Int = when (this) {
    BackupError.WrongPassphrase -> R.string.backup_err_wrong_passphrase
    BackupError.Corrupt -> R.string.backup_err_corrupt
    BackupError.Empty -> R.string.backup_err_empty
    BackupError.AuthRequired -> R.string.backup_err_auth
    BackupError.Network -> R.string.backup_err_network
    BackupError.PassphraseRequired -> R.string.backup_err_no_passphrase
    is BackupError.UnsupportedVersion -> R.string.backup_err_unsupported
}
