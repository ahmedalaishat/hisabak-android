package com.hisabak.feature.category.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.category.presentation.CategoryStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditScreen(
    state: CategoryEditUiState,
    onNameChange: (String) -> Unit,
    onTypeChange: (CategoryType) -> Unit,
    onColorChange: (String) -> Unit,
    onIconChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Add category" else "Edit category") },
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

            LabeledSection("Type") {
                TypePicker(selected = state.type, onSelect = onTypeChange)
            }

            LabeledSection("Color") {
                ColorSwatchGrid(selected = state.color, onSelect = onColorChange)
            }

            LabeledSection("Icon") {
                IconPicker(selected = state.icon, onSelect = onIconChange)
            }

            state.generalError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Button(onClick = onSave, enabled = state.canSave) {
                    Text(if (state.isSaving) "Saving…" else "Save")
                }
            }
        }
    }
}

@Composable
private fun LabeledSection(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        content()
    }
}

@Composable
private fun TypePicker(selected: CategoryType, onSelect: (CategoryType) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CategoryType.entries.forEach { type ->
            FilterChip(
                selected = type == selected,
                onClick = { onSelect(type) },
                label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
            )
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun ColorSwatchGrid(selected: String, onSelect: (String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryStyle.palette.forEach { key ->
            val color = CategoryStyle.color(key)
            val isSelected = key == selected
            val borderColor = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color, CircleShape)
                    .border(width = 2.dp, color = borderColor, shape = CircleShape)
                    .clickable { onSelect(key) },
                contentAlignment = Alignment.Center,
            ) {
                if (isSelected) {
                    Text("✓", color = Color.White, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun IconPicker(selected: String, onSelect: (String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CategoryStyle.icons.forEach { key ->
            val isSelected = key == selected
            AssistChip(
                onClick = { onSelect(key) },
                label = { Text(key) },
                colors = if (isSelected)
                    AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                else AssistChipDefaults.assistChipColors(),
            )
        }
    }
}
