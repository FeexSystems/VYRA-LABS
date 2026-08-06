package com.example.vyra.theme

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CyberpunkThemeConfig(
    val isCyberpunkThemeEnabled: Boolean = true,
    val primaryNeonColor: Color = NeonCyan,
    val secondaryNeonColor: Color = ElectricMagenta,
    val tertiaryNeonColor: Color = QuantumViolet,
    val activeNeonColor: Color = NeonGreen,
    val glowEffectsEnabled: Boolean = true,
    val scanlinesEnabled: Boolean = true,
    val neonIntensity: Float = 1.0f
)

object ThemeManager {
    // Environment configurations with default cyberpunk fallback
    private val isEnvCyberpunkEnabled = System.getenv("CYBERPUNK_THEME_ENABLED")?.toBoolean() ?: true
    private val envNeonColors = System.getenv("NEON_COLORS") ?: "cyan,magenta,violet,green"

    private val _themeConfig = MutableStateFlow(
        CyberpunkThemeConfig(
            isCyberpunkThemeEnabled = isEnvCyberpunkEnabled,
            primaryNeonColor = if (envNeonColors.contains("cyan")) NeonCyan else Color.Cyan,
            secondaryNeonColor = if (envNeonColors.contains("magenta")) ElectricMagenta else Color.Magenta,
            tertiaryNeonColor = if (envNeonColors.contains("violet")) QuantumViolet else Color(0xFF8B00FF),
            activeNeonColor = if (envNeonColors.contains("green")) NeonGreen else Color.Green,
            glowEffectsEnabled = true
        )
    )
    val themeConfig: StateFlow<CyberpunkThemeConfig> = _themeConfig.asStateFlow()

    fun updateGlowEffects(enabled: Boolean) {
        _themeConfig.value = _themeConfig.value.copy(glowEffectsEnabled = enabled)
    }

    fun updateCyberpunkEnabled(enabled: Boolean) {
        _themeConfig.value = _themeConfig.value.copy(isCyberpunkThemeEnabled = enabled)
    }

    fun setPrimaryAccent(color: Color) {
        _themeConfig.value = _themeConfig.value.copy(primaryNeonColor = color)
    }

    fun getNeonPalette(): List<Color> = listOf(
        _themeConfig.value.primaryNeonColor,
        _themeConfig.value.secondaryNeonColor,
        _themeConfig.value.tertiaryNeonColor,
        _themeConfig.value.activeNeonColor,
        CyberAmber
    )
}
