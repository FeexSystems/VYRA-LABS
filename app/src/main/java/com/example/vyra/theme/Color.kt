package com.example.vyra.theme

import androidx.compose.ui.graphics.Color

val CyberBg = Color(0xFF0A0A12)
val CyberSurface = Color(0xFF12121E)
val CyberSurfaceVariant = Color(0xFF1A1A2E)
val CyberBorder = Color(0xFF2A2A48)

val NeonCyan = Color(0xFF00F5FF)
val ElectricMagenta = Color(0xFFFF007A)
val QuantumViolet = Color(0xFF8B00FF)
val NeonGreen = Color(0xFF00FF87)
val CyberAmber = Color(0xFFFFB800)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA0A0C0)
val TextMuted = Color(0xFF606080)

fun parseHexColor(hex: String): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = if (cleanHex.length == 6) {
            android.graphics.Color.parseColor("#$cleanHex")
        } else {
            android.graphics.Color.parseColor(hex)
        }
        Color(colorInt)
    } catch (e: Exception) {
        NeonCyan
    }
}
