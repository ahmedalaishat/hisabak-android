package com.hisabak.feature.transaction.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.ui.components.BadgeTone
import com.hisabak.ui.components.ButtonVariant
import com.hisabak.ui.components.ColoredFilterChip
import com.hisabak.ui.components.DirhamGlyph
import com.hisabak.ui.components.HisabakButton
import com.hisabak.ui.components.SegmentOption
import com.hisabak.ui.components.SegmentedControl
import com.hisabak.ui.theme.HisabakTheme
import com.hisabak.ui.theme.HisabakType
import com.hisabak.ui.theme.Spacing
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditScreen(
    state: TransactionEditUiState,
    onAmountChange: (String) -> Unit,
    onBrandSelected: (BrandId) -> Unit,
    onNoteChange: (String) -> Unit,
    onTypeSelected: (CategoryType) -> Unit,
    onDateClick: () -> Unit,
    onDateSelected: (Instant) -> Unit,
    onDateDismiss: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    if (state.isLoading) {
        Box(
            Modifier.fillMaxWidth().padding(Spacing.s8),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.pageMargin)
            .padding(bottom = Spacing.s5)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.s5),
    ) {
        Text(
            text = if (state.isNew) "New transaction" else "Edit transaction",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        AmountHeroField(
            amountInput = state.amountInput,
            error = state.amountError,
            type = state.selectedType,
            onAmountChange = onAmountChange,
        )

        SegmentedControl(
            options = listOf(
                SegmentOption(CategoryType.EXPENSES,   "Expense",  BadgeTone.Expense),
                SegmentOption(CategoryType.INCOME,     "Income",   BadgeTone.Income),
                SegmentOption(CategoryType.SAVINGS,    "Savings",  BadgeTone.Savings),
                SegmentOption(CategoryType.INVESTMENT, "Invest",   BadgeTone.Investment),
            ),
            selected = state.selectedType,
            onSelect = onTypeSelected,
            modifier = Modifier.fillMaxWidth(),
        )

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sectionTitleGap)) {
            Text(
                text = "Brand",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (state.brandOptions.isEmpty()) {
                Text(
                    text = "No brands for this type. Add one from the Manage tab first.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s3),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    items(state.brandOptions, key = { it.id.value }) { option ->
                        ColoredFilterChip(
                            label = option.name,
                            colorKey = option.categoryColor,
                            selected = option.id == state.selectedBrandId,
                            onClick = { onBrandSelected(option.id) },
                        )
                    }
                }
            }
            state.brandError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.sectionTitleGap)) {
            Text(
                text = "Date",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            HisabakButton(
                text = formatDate(state.occurredAt),
                onClick = onDateClick,
                variant = ButtonVariant.Secondary,
                leadingIcon = Icons.Filled.CalendarToday,
                fullWidth = true,
            )
        }

        OutlinedTextField(
            value = state.noteInput,
            onValueChange = onNoteChange,
            label = { Text("Note (optional)") },
            modifier = Modifier.fillMaxWidth(),
        )

        state.generalError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        HisabakButton(
            text = if (state.isSaving) "Saving…" else "Save",
            onClick = onSave,
            variant = ButtonVariant.Primary,
            enabled = state.canSave,
            fullWidth = true,
        )
        HisabakButton(
            text = "Cancel",
            onClick = onCancel,
            variant = ButtonVariant.Ghost,
            fullWidth = true,
        )
    }

    if (state.showDatePicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.occurredAt.toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = onDateDismiss,
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { onDateSelected(Instant.ofEpochMilli(it)) }
                        ?: onDateDismiss()
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = onDateDismiss) { Text("Cancel") } },
        ) {
            DatePicker(state = pickerState)
        }
    }
}

/** Large editable amount — the dirham glyph + an inline borderless field, tinted by type. */
@Composable
private fun AmountHeroField(
    amountInput: String,
    error: String?,
    type: CategoryType,
    onAmountChange: (String) -> Unit,
) {
    val c = HisabakTheme.colors
    val color = when (type) {
        CategoryType.INCOME     -> c.income
        CategoryType.EXPENSES   -> c.expense
        CategoryType.SAVINGS    -> c.savings
        CategoryType.INVESTMENT -> c.investment
    }
    val heroStyle = HisabakType.amount.copy(
        fontSize = 44.sp,
        fontWeight = FontWeight.Bold,
        color = color,
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.s3),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Center the glyph + field as one tight cluster. The field wraps to its
        // content width (IntrinsicSize.Min) so it doesn't eat the row and push
        // the glyph to the edge.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DirhamGlyph(size = 44.sp * 0.82f, tint = color)
            Spacer(Modifier.width(Spacing.s2))
            Box(contentAlignment = Alignment.CenterStart) {
                if (amountInput.isEmpty()) {
                    Text("0.00", style = heroStyle.copy(color = c.textTertiary))
                }
                BasicTextField(
                    value = amountInput,
                    onValueChange = { onAmountChange(sanitizeAmount(it)) },
                    textStyle = heroStyle,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    cursorBrush = SolidColor(color),
                    modifier = Modifier.width(IntrinsicSize.Min),
                )
            }
        }
        if (error != null) {
            Spacer(Modifier.height(Spacing.s2))
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

/** Keeps digits and a single decimal point; drops anything else the keyboard sends. */
private fun sanitizeAmount(input: String): String {
    val sb = StringBuilder()
    var hasDot = false
    for (ch in input) {
        when {
            ch.isDigit() -> sb.append(ch)
            ch == '.' && !hasDot -> { sb.append(ch); hasDot = true }
        }
    }
    return sb.toString()
}

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.getDefault())

private fun formatDate(instant: Instant): String =
    dateFormatter.format(instant.atZone(ZoneId.systemDefault()).toLocalDate())
