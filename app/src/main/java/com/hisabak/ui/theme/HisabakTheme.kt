package com.hisabak.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

/*
 * HisabakTheme — wraps MaterialTheme with the Hisabak color scheme, typography, and
 * shapes, and provides the finance-specific colors via LocalHisabakColors.
 *
 * Usage:
 *   setContent { HisabakTheme { AppNavHost() } }
 *
 * Read finance colors anywhere under it:
 *   val c = HisabakTheme.colors
 *   Text(money, color = c.income, style = HisabakType.amount)
 *
 * Standard Material colors still come from MaterialTheme.colorScheme.primary etc.
 */

val LocalHisabakColors = staticCompositionLocalOf { HisabakLightSemantic }

@Composable
fun HisabakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) HisabakDarkColorScheme else HisabakLightColorScheme
    val semantic = if (darkTheme) HisabakDarkSemantic else HisabakLightSemantic

    CompositionLocalProvider(LocalHisabakColors provides semantic) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = HisabakTypography,
            shapes = HisabakShapes,
            content = content,
        )
    }
}

/** Accessor object — mirrors the MaterialTheme pattern: `HisabakTheme.colors.income`. */
object HisabakTheme {
    val colors: HisabakSemanticColors
        @Composable @ReadOnlyComposable get() = LocalHisabakColors.current
}
