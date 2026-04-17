package com.hisabak.feature.sms.presentation.inbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hisabak.core.common.Money
import com.hisabak.feature.sms.domain.SmsMessageId
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsInboxScreen(
    state: SmsInboxUiState,
    snackbarHostState: SnackbarHostState,
    onSearchChange: (String) -> Unit,
    onDraftChange: (String) -> Unit,
    onIngest: () -> Unit,
    onDelete: (SmsMessageId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("SMS Inbox") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            IngestCard(
                draft = state.draftBody,
                isProcessing = state.isProcessing,
                onDraftChange = onDraftChange,
                onIngest = onIngest,
            )

            OutlinedTextField(
                value = state.search,
                onValueChange = onSearchChange,
                placeholder = { Text("Search SMS body") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            )

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.rows.isEmpty() -> EmptyState(state.search)
                else -> LazyColumn(Modifier.fillMaxSize()) {
                    items(state.rows, key = { it.id.value }) { row ->
                        SmsRowItem(row = row, onDelete = { onDelete(row.id) })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun IngestCard(
    draft: String,
    isProcessing: Boolean,
    onDraftChange: (String) -> Unit,
    onIngest: () -> Unit,
) {
    ElevatedCard(Modifier.fillMaxWidth().padding(16.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Paste a bank SMS", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = draft,
                onValueChange = onDraftChange,
                placeholder = { Text("e.g. Purchase of AED 45.50 with Card 1234 at Starbucks, …") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = onIngest,
                    enabled = draft.isNotBlank() && !isProcessing,
                ) {
                    Text(if (isProcessing) "Parsing…" else "Parse & create transaction")
                }
                if (isProcessing) CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            }
        }
    }
}

@Composable
private fun EmptyState(search: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            if (search.isBlank()) "No SMS yet. Paste one above to test."
            else "No SMS match \"$search\".",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SmsRowItem(row: SmsInboxRow, onDelete: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(Modifier.weight(1f)) {
            Text(row.body, style = MaterialTheme.typography.bodyMedium, maxLines = 4)
            Row(
                modifier = Modifier.padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (row.isLinked) {
                    AssistChip(
                        onClick = {},
                        enabled = false,
                        label = { Text("Linked") },
                        leadingIcon = { Icon(Icons.Filled.Check, null, Modifier.size(14.dp)) },
                        colors = AssistChipDefaults.assistChipColors(
                            disabledContainerColor = Color(0xFFE0F2F1),
                            disabledLabelColor = Color(0xFF00695C),
                            disabledLeadingIconContentColor = Color(0xFF00695C),
                        ),
                    )
                } else {
                    AssistChip(
                        onClick = {},
                        enabled = false,
                        label = { Text("Unparsed") },
                    )
                }
                row.parsedBrand?.let {
                    Text(it, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                }
                row.parsedAmount?.let {
                    Text(
                        formatMoney(it),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Text(
                    formatDate(row.receivedAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
        }
    }
}

internal fun formatMoney(money: Money): String {
    val major = money.amountMinor / 100
    val minor = kotlin.math.abs(money.amountMinor % 100)
    val sign = if (money.amountMinor < 0) "-" else ""
    return "$sign${money.currency.code} $major.${minor.toString().padStart(2, '0')}"
}

internal fun formatDate(instant: Instant): String =
    DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
        .format(instant)
