package com.hisabak.feature.brand.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hisabak.feature.category.domain.CategoryId
import com.hisabak.feature.category.presentation.CategoryStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandEditScreen(
    state: BrandEditUiState,
    onNameChange: (String) -> Unit,
    onCategoryChange: (CategoryId?) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Add brand" else "Edit brand") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = state.nameInput,
                onValueChange = onNameChange,
                label = { Text("Name") },
                isError = state.nameError != null,
                supportingText = state.nameError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Category", style = MaterialTheme.typography.labelLarge)
                CategoryPicker(
                    options = state.categoryOptions,
                    selected = state.selectedCategoryId,
                    onSelect = onCategoryChange,
                )
            }

            state.generalError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = onSave, enabled = state.canSave) {
                    Text(if (state.isSaving) "Saving…" else "Save")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryPicker(
    options: List<BrandEditUiState.CategoryOption>,
    selected: CategoryId?,
    onSelect: (CategoryId?) -> Unit,
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = selected == null,
            onClick = { onSelect(null) },
            label = { Text("None") },
        )
        options.forEach { option ->
            FilterChip(
                selected = selected == option.id,
                onClick = { onSelect(option.id) },
                label = { Text(option.name) },
                leadingIcon = {
                    Box(
                        Modifier
                            .size(10.dp)
                            .background(CategoryStyle.color(option.color), CircleShape),
                    )
                },
            )
        }
    }
}
