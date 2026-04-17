package com.hisabak.feature.transaction.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditScreen(
    state: TransactionEditUiState,
    onAmountChange: (String) -> Unit,
    onBrandChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Add transaction" else "Edit transaction") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = state.amountInput,
                onValueChange = onAmountChange,
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.amountError != null,
                supportingText = state.amountError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.brandInput,
                onValueChange = onBrandChange,
                label = { Text("Brand") },
                placeholder = { Text("Type or pick existing") },
                isError = state.brandError != null,
                supportingText = state.brandError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            if (state.brandSuggestions.isNotEmpty()) {
                val filtered = remember(state.brandInput, state.brandSuggestions) {
                    val q = state.brandInput.trim()
                    if (q.isEmpty()) state.brandSuggestions.take(8)
                    else state.brandSuggestions
                        .filter { it.contains(q, ignoreCase = true) }
                        .take(8)
                }
                if (filtered.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filtered) { name ->
                            AssistChip(
                                onClick = { onBrandChange(name) },
                                label = { Text(name) },
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = state.noteInput,
                onValueChange = onNoteChange,
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth(),
            )

            state.generalError?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(
                    onClick = onSave,
                    enabled = state.canSave,
                ) {
                    Text(if (state.isSaving) "Saving…" else "Save")
                }
            }
        }
    }
}
