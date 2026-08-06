package com.example.vyra.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = ElectricMagenta,
    tertiary = QuantumViolet,
    background = CyberBg,
    surface = CyberSurface,
    surfaceVariant = CyberSurfaceVariant,
    onPrimary = CyberBg,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = CyberBorder
)

@Composable
fun VYRATheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeConfig by ThemeManager.themeConfig.collectAsState()

    val dynamicColorScheme = darkColorScheme(
        primary = themeConfig.primaryNeonColor,
        secondary = themeConfig.secondaryNeonColor,
        tertiary = themeConfig.tertiaryNeonColor,
        background = CyberBg,
        surface = CyberSurface,
        surfaceVariant = CyberSurfaceVariant,
        onPrimary = CyberBg,
        onSecondary = TextPrimary,
        onTertiary = TextPrimary,
        onBackground = TextPrimary,
        onSurface = TextPrimary,
        onSurfaceVariant = TextSecondary,
        outline = CyberBorder
    )

    MaterialTheme(
        colorScheme = dynamicColorScheme,
        content = content
    )
}
