package com.hisabak.feature.category.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hisabak.feature.category.domain.CategoryType
import com.hisabak.feature.category.presentation.CategoryStyle
import com.hisabak.ui.components.BadgeTone
import com.hisabak.ui.components.HisabakButton
import com.hisabak.ui.components.ButtonVariant
import com.hisabak.ui.components.SegmentOption
import com.hisabak.ui.components.SegmentedControl
import com.hisabak.ui.components.SurfaceCard
import com.hisabak.ui.components.iconForKey
import com.hisabak.ui.components.tintPairForColor
import com.hisabak.ui.theme.Sizing
import com.hisabak.ui.theme.Spacing

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
            .padding(Spacing.pageMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.s5),
    ) {
            // Name field
            OutlinedTextField(
                value = state.nameInput,
                onValueChange = onNameChange,
                label = { Text("Name") },
                isError = state.nameError != null,
                supportingText = state.nameError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            // Type picker
            FormSection(label = "Type") {
                SegmentedControl(
                    options = listOf(
                        SegmentOption(CategoryType.INCOME, "Income", BadgeTone.Income),
                        SegmentOption(CategoryType.EXPENSES, "Expenses", BadgeTone.Expense),
                        SegmentOption(CategoryType.SAVINGS, "Savings", BadgeTone.Savings),
                        SegmentOption(CategoryType.INVESTMENT, "Invest", BadgeTone.Investment),
                    ),
                    selected = state.type,
                    onSelect = onTypeChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Color picker
            FormSection(label = "Color") {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    items(CategoryStyle.palette) { key ->
                        ColorSwatch(
                            colorKey = key,
                            selected = key == state.color,
                            onSelect = { onColorChange(key) },
                        )
                    }
                }
            }

            // Icon picker
            FormSection(label = "Icon") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(184.dp),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s3),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s3),
                    userScrollEnabled = false,
                ) {
                    items(CategoryStyle.icons) { key ->
                        IconChip(
                            iconKey = key,
                            colorKey = state.color,
                            selected = key == state.icon,
                            onSelect = { onIconChange(key) },
                        )
                    }
                }
            }

            // Live preview
            FormSection(label = "Preview") {
                LivePreviewTile(
                    name = state.nameInput.ifBlank { "Category name" },
                    iconKey = state.icon,
                    colorKey = state.color,
                )
            }

            // General error
            state.generalError?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            // Actions
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s3)) {
                HisabakButton(
                    text = if (state.isSaving) "Saving…" else "Save",
                    onClick = onSave,
                    enabled = state.canSave,
                    variant = ButtonVariant.Primary,
                    fullWidth = true,
                )
                HisabakButton(
                    text = "Cancel",
                    onClick = onCancel,
                    variant = ButtonVariant.Ghost,
                    fullWidth = true,
                )
            }

            Spacer(Modifier.height(Spacing.s3))
        }
}

@Composable
private fun FormSection(
    label: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        content()
    }
}

@Composable
private fun ColorSwatch(
    colorKey: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val swatchColor = CategoryStyle.color(colorKey)
    val swatchShape = MaterialTheme.shapes.small
    Box(
        modifier = Modifier
            .size(Sizing.avatar)
            .clip(swatchShape)
            .background(swatchColor, swatchShape)
            .then(
                if (selected)
                    Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, swatchShape)
                else
                    Modifier
            )
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun IconChip(
    iconKey: String,
    colorKey: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val (bg, fg) = tintPairForColor(colorKey)
    val shape = MaterialTheme.shapes.medium
    val borderColor = if (selected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(shape)
            .background(bg, shape)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onSelect),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = iconForKey(iconKey),
            contentDescription = null,
            tint = fg,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
private fun LivePreviewTile(
    name: String,
    iconKey: String,
    colorKey: String,
) {
    val (bg, fg) = tintPairForColor(colorKey)
    SurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.s4),
        ) {
            IconTile(
                icon = iconForKey(iconKey),
                size = Sizing.tileSize,
                iconSize = 22.dp,
                background = bg,
                foreground = fg,
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
        }
    }
}
