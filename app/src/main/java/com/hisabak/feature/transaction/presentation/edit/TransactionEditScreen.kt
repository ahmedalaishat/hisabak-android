package com.hisabak.feature.transaction.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hisabak.feature.brand.domain.BrandId
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.ui.components.AmountText
import com.hisabak.ui.components.AmountTone
import com.hisabak.ui.components.BadgeTone
import com.hisabak.ui.components.ButtonVariant
import com.hisabak.ui.components.ColoredFilterChip
import com.hisabak.ui.components.HisabakButton
import com.hisabak.ui.components.SegmentOption
import com.hisabak.ui.components.SegmentedControl
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
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AmountHeroDisplay(state = state)

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

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Brand",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (state.brandOptions.isEmpty()) {
                Text(
                    text = "No brands yet. Add one from the Manage tab first.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

        Spacer(Modifier.weight(1f, fill = false))

        HisabakButton(
            text = "Cancel",
            onClick = onCancel,
            variant = ButtonVariant.Ghost,
            fullWidth = true,
        )
        HisabakButton(
            text = if (state.isSaving) "Saving…" else "Save",
            onClick = onSave,
            variant = ButtonVariant.Primary,
            enabled = state.canSave,
            fullWidth = true,
        )

        Spacer(Modifier.height(8.dp))
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

@Composable
private fun AmountHeroDisplay(state: TransactionEditUiState) {
    val amountValue = state.amountInput.toDoubleOrNull() ?: 0.0
    val tone = when (state.selectedType) {
        CategoryType.INCOME     -> AmountTone.Income
        CategoryType.EXPENSES   -> AmountTone.Expense
        CategoryType.SAVINGS    -> AmountTone.Savings
        CategoryType.INVESTMENT -> AmountTone.Investment
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("SAR", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(4.dp))
        AmountText(value = amountValue, currency = "", showSign = false, tone = tone, size = 44.sp)
        if (state.amountInput.isBlank()) {
            Text(
                text = "Enter amount above",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private val dateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.getDefault())

private fun formatDate(instant: Instant): String =
    dateFormatter.format(instant.atZone(ZoneId.systemDefault()).toLocalDate())
