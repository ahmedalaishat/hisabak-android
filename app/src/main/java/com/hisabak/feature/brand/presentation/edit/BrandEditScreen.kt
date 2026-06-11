package com.hisabak.feature.brand.presentation.edit

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.ui.components.ButtonVariant
import com.hisabak.ui.components.ColoredFilterChip
import com.hisabak.ui.components.HisabakButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandEditScreen(
    state: BrandEditUiState,
    onNameChange: (String) -> Unit,
    onCategoryChange: (CategoryId?) -> Unit,
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
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
            NameField(
                value = state.nameInput,
                error = state.nameError,
                onValueChange = onNameChange,
            )

            CategorySection(
                options = state.categoryOptions,
                selected = state.selectedCategoryId,
                onSelect = onCategoryChange,
            )

            if (state.generalError != null) {
                Text(
                    text = state.generalError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(Modifier.height(4.dp))

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
}

@Composable
private fun NameField(
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Brand name") },
            isError = error != null,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
    }
}

@Composable
private fun CategorySection(
    options: List<BrandEditUiState.CategoryOption>,
    selected: CategoryId?,
    onSelect: (CategoryId?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Category",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
        ) {
            item {
                ColoredFilterChip(
                    label = "None",
                    colorKey = null,
                    selected = selected == null,
                    onClick = { onSelect(null) },
                )
            }
            items(options) { option ->
                ColoredFilterChip(
                    label = option.name,
                    colorKey = option.color,
                    selected = selected == option.id,
                    onClick = { onSelect(option.id) },
                )
            }
        }
    }
}
