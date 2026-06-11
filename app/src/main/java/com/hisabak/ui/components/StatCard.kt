package com.hisabak.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hisabak.ui.theme.HisabakTheme

/**
 * Small KPI card used in 2-up grids: tinted icon tile + label + big value,
 * with an optional thin progress bar beneath.
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    progress: Float? = null,
    accent: StatAccent = StatAccent.Positive,
) {
    val c = HisabakTheme.colors
    val bg = when (accent) {
        StatAccent.Positive -> c.incomeSoft
        StatAccent.Negative -> c.expenseSoft
    }
    val fg = when (accent) {
        StatAccent.Positive -> MaterialTheme.colorScheme.primary
        StatAccent.Negative -> MaterialTheme.colorScheme.error
    }
    SurfaceCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconTile(
                icon = icon,
                size = 28.dp,
                iconSize = 16.dp,
                background = bg,
                foreground = fg,
            )
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (progress != null) {
            Spacer(Modifier.height(8.dp))
            ProgressBar(progress = progress, color = fg)
        }
    }
}

enum class StatAccent { Positive, Negative }

@Composable
fun ProgressBar(
    progress: Float,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
) {
    val clamped = progress.coerceIn(0f, 1f)
    Box(
        modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(999.dp)),
    ) {
        Box(
            Modifier
                .fillMaxWidth(clamped)
                .height(4.dp)
                .background(color, RoundedCornerShape(999.dp)),
        )
    }
}

// Convenience wrappers that match the two most common call sites in the design.
@Composable
fun IncomeStatCard(value: String, progress: Float? = null, modifier: Modifier = Modifier) =
    StatCard(
        label = "Income",
        value = value,
        icon = Icons.Filled.TrendingUp,
        accent = StatAccent.Positive,
        progress = progress,
        modifier = modifier,
    )

@Composable
fun ExpensesStatCard(value: String, progress: Float? = null, modifier: Modifier = Modifier) =
    StatCard(
        label = "Expenses",
        value = value,
        icon = Icons.Filled.TrendingDown,
        accent = StatAccent.Negative,
        progress = progress,
        modifier = modifier,
    )
