package com.hisabak.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// The design calls for a fixed green palette, so we don't opt into Android 12
// dynamic color — it would override the brand's surface/primary tokens.

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    inversePrimary = InversePrimary,

    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,

    background = Background,
    onBackground = OnBackground,

    surface = SurfaceColor,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceTint = Primary,

    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,

    error = ErrorColor,
    onError = OnErrorColor,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,

    outline = Outline,
    outlineVariant = OutlineVariant,

    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
)

// Dark palette inverts core tokens; kept minimal for now (the design spec is
// light-first). When a full dark spec lands we can refine these values.
private val DarkColors = darkColorScheme(
    primary = PrimaryFixedDim,
    onPrimary = OnPrimaryContainer,
    primaryContainer = OnPrimaryContainer,
    onPrimaryContainer = PrimaryFixed,
    inversePrimary = Primary,

    secondary = SecondaryContainer,
    onSecondary = OnSecondaryContainer,
    secondaryContainer = OnSecondaryContainer,
    onSecondaryContainer = SecondaryContainer,

    tertiary = TertiaryFixed,
    onTertiary = OnTertiaryContainer,
    tertiaryContainer = OnTertiaryContainer,
    onTertiaryContainer = TertiaryFixed,

    background = InverseSurface,
    onBackground = InverseOnSurface,
    surface = InverseSurface,
    onSurface = InverseOnSurface,
    surfaceVariant = OnSurfaceVariant,
    onSurfaceVariant = SurfaceVariant,
    surfaceTint = PrimaryFixedDim,

    error = ErrorColor,
    onError = OnErrorColor,
    errorContainer = OnErrorContainer,
    onErrorContainer = ErrorContainer,

    outline = OutlineVariant,
    outlineVariant = Outline,

    inverseSurface = SurfaceColor,
    inverseOnSurface = OnSurface,
)

@Composable
fun HisabakTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = AppShapes,
        content = content,
    )
}
