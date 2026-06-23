package com.hisabak.feature.restore.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import com.hisabak.R
import com.hisabak.feature.backup.presentation.messageRes
import com.hisabak.ui.components.ButtonVariant
import com.hisabak.ui.components.HisabakButton
import com.hisabak.ui.theme.Spacing

@Composable
fun RestoreScreen(
    state: RestoreUiState,
    onConnect: () -> Unit,
    onSubmitPassphrase: (String) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var passphrase by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.pageMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.s4),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.restore_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Spacing.sectionGap),
        )
        Text(
            stringResource(R.string.restore_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (state.busy) {
            CircularProgressIndicator(Modifier.padding(Spacing.s6))
        }

        state.message?.let {
            Text(
                text = when (it) {
                    RestoreMessage.NothingFound -> stringResource(R.string.restore_none_found)
                    is RestoreMessage.Failed -> stringResource(it.error.messageRes())
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }

        if (state.needsPassphrase) {
            OutlinedTextField(
                value = passphrase,
                onValueChange = { passphrase = it },
                label = { Text(stringResource(R.string.backup_passphrase)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth().padding(top = Spacing.s4),
            )
            HisabakButton(
                text = stringResource(R.string.restore_action),
                onClick = { onSubmitPassphrase(passphrase) },
                enabled = passphrase.isNotEmpty() && !state.busy,
                fullWidth = true,
            )
        } else {
            HisabakButton(
                text = stringResource(
                    if (state.account == null) R.string.restore_choose_account else R.string.restore_action,
                ),
                onClick = onConnect,
                enabled = !state.busy,
                fullWidth = true,
                modifier = Modifier.padding(top = Spacing.s4),
            )
        }

        HisabakButton(
            text = stringResource(R.string.restore_skip),
            onClick = onSkip,
            variant = ButtonVariant.Ghost,
            enabled = !state.busy,
            fullWidth = true,
        )
    }
}
