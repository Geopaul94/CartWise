package com.geo.cartwise.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = GreenOnPrimary,
    primaryContainer = GreenPrimaryContainer,
    secondary = GreenSecondary,
    background = GreenBackground,
    onBackground = GreenOnBackground,
    surface = GreenSurface,
    onSurface = GreenOnBackground
)

private val DarkColors = darkColorScheme(
    primary = GreenSecondary,
    onPrimary = GreenPrimaryDark,
    primaryContainer = GreenPrimaryDark,
    secondary = GreenSecondary,
    background = GreenBackgroundDark,
    onBackground = GreenOnBackgroundDark,
    surface = GreenSurfaceDark,
    onSurface = GreenOnBackgroundDark
)

@Composable
fun CartWiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = CartWiseTypography,
        content = content
    )
}
