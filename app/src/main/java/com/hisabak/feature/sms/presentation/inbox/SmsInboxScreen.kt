package com.hisabak.feature.sms.presentation.inbox

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hisabak.core.common.Money
import com.hisabak.feature.sms.domain.SmsMessageId
import com.hisabak.ui.components.AmountText
import com.hisabak.ui.components.compactAmount
import com.hisabak.ui.components.AmountTone
import com.hisabak.ui.components.Badge
import com.hisabak.ui.components.SkeletonRowList
import com.hisabak.ui.components.BadgeTone
import com.hisabak.ui.components.EmptyStatePanel
import com.hisabak.ui.components.HisabakButton
import com.hisabak.ui.components.ButtonVariant
import com.hisabak.ui.components.PrimaryPillButton
import com.hisabak.ui.components.SearchField
import com.hisabak.ui.components.SectionHeader
import com.hisabak.ui.components.SmsStatus
import com.hisabak.ui.components.StatusChip
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.theme.Sizing
import com.hisabak.ui.theme.Spacing
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
            contentPadding = PaddingValues(horizontal = Spacing.pageMargin, vertical = Spacing.s5),
            verticalArrangement = Arrangement.spacedBy(Spacing.cardGap),
        ) {
            item { AutoImportBanner(granted = state.autoImportGranted, onEnable = onEnableAutoImport) }
            item { PasteParseCard(draft = state.draftBody, isProcessing = state.isProcessing, onDraftChange = onDraftChange, onIngest = onIngest) }
            item {
                SearchField(
                    value = state.search,
                    onValueChange = onSearchChange,
                    placeholder = "Search messages",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item { SectionHeader(title = "Recent SMS") }
            if (state.isLoading) {
                item { SkeletonRowList(count = 5) }
            } else if (state.rows.isEmpty()) {
                item {
                    EmptyStatePanel(
                        icon = Icons.Filled.Inbox,
                        title = "No SMS messages",
                        subtitle = if (state.search.isBlank())
                            "Enable auto-import to capture bank messages automatically."
                        else
                            "Nothing matches \"${state.search}\".",
                    )
                }
            } else {
                items(state.rows, key = { it.id.value }) { row ->
                    SmsRowCard(
                        row = row,
                        onImport = { onIngest() },
                        onDelete = { onDelete(row.id) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Spacing.pageMargin),
        )
    }
}

@Composable
private fun AutoImportBanner(granted: Boolean, onEnable: () -> Unit) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        if (granted) {
            // Compact: the "Active" badge is small and sits inline with the copy.
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.cardGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.s2)) {
                    Text(
                        "Auto-import active",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "New bank SMS are parsed automatically.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Badge(label = "Active", tone = BadgeTone.Income)
            }
        } else {
            // Stack vertically so the title isn't squeezed by the badge + Enable button.
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.s4),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.s2)) {
                    Text(
                        "Auto-import is disabled",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "Turn it on to log transactions from SMS.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Badge(label = "Auto-import off", tone = BadgeTone.Warning, dot = true)
                    Spacer(Modifier.weight(1f))
                    HisabakButton(
                        text = "Enable",
                        onClick = onEnable,
                        variant = ButtonVariant.Primary,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasteParseCard(
    draft: String,
    isProcessing: Boolean,
    onDraftChange: (String) -> Unit,
    onIngest: () -> Unit,
) {
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Paste an SMS",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(Spacing.sectionTitleGap))
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            placeholder = {
                Text(
                    "Paste a bank message…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            minLines = 2,
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(Spacing.cardGap))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isProcessing) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(10.dp))
            }
            HisabakButton(
                text = if (isProcessing) "Parsing…" else "Parse & import",
                onClick = onIngest,
                variant = ButtonVariant.Secondary,
                enabled = !isProcessing,
            )
        }
    }
}

@Composable
private fun SmsRowCard(
    row: SmsInboxRow,
    onImport: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val status = when {
        row.isLinked -> SmsStatus.Linked
        row.parsedAmount != null -> SmsStatus.Parsed
        else -> SmsStatus.Unparsed
    }
    val isParsed = status == SmsStatus.Parsed

    SurfaceCard(modifier = modifier.fillMaxWidth()) {
        // Top row: date/time left, StatusChip right
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                formatDate(row.receivedAt),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            StatusChip(status = status)
        }
        Spacer(Modifier.height(Spacing.sectionTitleGap))
        // SMS body capped at 2 lines
        Text(
            row.body,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
        )
        // Parsed detail row
        if (isParsed && row.parsedBrand != null && row.parsedAmount != null) {
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s3),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        row.parsedBrand,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    AmountText(
                        value = row.parsedAmount.amountMinor / 100.0,
                        tone = AmountTone.Expense,
                        showSign = false,
                        size = 18.sp,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PrimaryPillButton(
                        text = "Import",
                        onClick = onImport,
                        leadingIcon = Icons.Filled.Download,
                    )
                    IconButton(onClick = onDelete, modifier = Modifier.size(Sizing.controlHeightSm)) {
                        Icon(
                            Icons.Filled.DeleteOutline,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        } else {
            Spacer(Modifier.height(Spacing.s3))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onDelete, modifier = Modifier.size(Sizing.controlHeightSm)) {
                    Icon(
                        Icons.Filled.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

internal fun formatMoney(money: Money): String {
    val sign = if (money.amountMinor < 0) "-" else ""
    return "$sign${money.currency.code} ${compactAmount(abs(money.amountMinor) / 100.0)}"
}

internal fun formatDate(instant: java.time.Instant): String =
    DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
        .format(instant)
