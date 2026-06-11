package com.hisabak.feature.sms.presentation.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hisabak.core.common.Money
import com.hisabak.feature.sms.domain.SmsMessageId
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.IconTile
import com.hisabak.ui.components.PrimaryPillButton
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.theme.HisabakTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsInboxScreen(
    state: SmsInboxUiState,
    snackbarHostState: SnackbarHostState,
    onSearchChange: (String) -> Unit,
    onDraftChange: (String) -> Unit,
    onIngest: () -> Unit,
    onDelete: (SmsMessageId) -> Unit,
    onEnableAutoImport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    "SMS Inbox",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            item { AutoImportBanner(granted = state.autoImportGranted, onEnable = onEnableAutoImport) }
            item { IngestCard(draft = state.draftBody, isProcessing = state.isProcessing, onDraftChange = onDraftChange, onIngest = onIngest) }
            item {
                SearchField(
                    value = state.search,
                    onValueChange = onSearchChange,
                    placeholder = "Search SMS body",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item { SectionHeader(title = "Recent SMS") }
            if (state.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(vertical = 40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (state.rows.isEmpty()) {
                item {
                    EmptyStatePanel(
                        title = if (state.search.isBlank()) "No SMS yet" else "No matches",
                        subtitle = if (state.search.isBlank())
                            "Paste a bank SMS above to parse it into a transaction."
                        else
                            "Nothing matches \"${state.search}\".",
                        icon = Icons.Filled.Sms,
                    )
                }
            } else {
                items(state.rows, key = { it.id.value }) { row ->
                    SmsRowItem(row = row, onDelete = { onDelete(row.id) })
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
        )
    }
}

@Composable
private fun AutoImportBanner(granted: Boolean, onEnable: () -> Unit) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconTile(
                icon = Icons.Filled.Check,
                background = if (granted) HisabakTheme.colors.incomeSoft else MaterialTheme.colorScheme.surfaceContainerHigh,
                foreground = if (granted) HisabakTheme.colors.income else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Column(Modifier.weight(1f)) {
                Text(
                    if (granted) "Auto-import is on" else "Auto-import incoming SMS",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    if (granted)
                        "New bank SMS will be parsed automatically in the background."
                    else
                        "Grant SMS access to turn every matching bank SMS into a transaction.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (!granted) {
                PrimaryPillButton(text = "Enable", onClick = onEnable)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IngestCard(
    draft: String,
    isProcessing: Boolean,
    onDraftChange: (String) -> Unit,
    onIngest: () -> Unit,
) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Paste a bank SMS",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            placeholder = {
                Text(
                    "e.g. Purchase of AED 45.50 with Card 1234 at Starbucks, …",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            minLines = 2,
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PrimaryPillButton(
                text = if (isProcessing) "Parsing…" else "Parse & create",
                onClick = onIngest,
            )
            if (isProcessing) CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
        }
    }
}

@Composable
private fun SmsRowItem(row: SmsInboxRow, onDelete: () -> Unit) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(
                    row.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 4,
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    StatusChip(linked = row.isLinked)
                    row.parsedBrand?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    row.parsedAmount?.let {
                        Text(
                            formatMoney(it),
                            style = MaterialTheme.typography.labelMedium,
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
                Icon(
                    Icons.Filled.DeleteOutline,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun StatusChip(linked: Boolean) {
    val (label, bg, fg) = if (linked) {
        Triple("Linked", HisabakTheme.colors.incomeSoft, HisabakTheme.colors.income)
    } else {
        Triple("Unparsed", MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Text(
        label,
        style = MaterialTheme.typography.labelSmall,
        color = fg,
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

internal fun formatMoney(money: Money): String {
    val major = money.amountMinor / 100
    val minor = abs(money.amountMinor % 100)
    val sign = if (money.amountMinor < 0) "-" else ""
    return "$sign${money.currency.code} $major.${minor.toString().padStart(2, '0')}"
}

internal fun formatDate(instant: Instant): String =
    DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
        .format(instant)
