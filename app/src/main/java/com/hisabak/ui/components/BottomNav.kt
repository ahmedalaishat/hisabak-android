package com.hisabak.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import com.hisabak.ui.theme.Motion
import com.hisabak.ui.theme.standardTween

data class BottomNavTab(
    val key: String,
    val label: String,
    val icon: ImageVector,
    val iconOutlined: ImageVector = icon,
)

@Composable
fun HisabakBottomNav(
    tabs: List<BottomNavTab>,
    selectedKey: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        tonalElevation = androidx.compose.ui.unit.Dp.Unspecified,
    ) {
        val haptic = LocalHapticFeedback.current
        val itemColors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        tabs.forEach { tab ->
            val selected = tab.key == selectedKey
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSelect(tab.key)
                },
                icon = {
                    Crossfade(
                        targetState = selected,
                        animationSpec = standardTween(Motion.Duration.Base),
                        label = "navIcon",
                    ) { isSelected ->
                        Icon(
                            imageVector = if (isSelected) tab.icon else tab.iconOutlined,
                            contentDescription = tab.label,
                        )
                    }
                },
                label = {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                alwaysShowLabel = true,
                colors = itemColors,
            )
        }
    }
}
