package com.example.vyra.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan

@Composable
fun CyberpunkCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberBorder,
    accentGlow: Color? = null,
    cornerRadius: Dp = 12.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val borderBrush = if (accentGlow != null) {
        Brush.horizontalGradient(listOf(accentGlow, NeonCyan))
    } else {
        Brush.linearGradient(listOf(borderColor, CyberBorder))
    }

    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(CyberSurface)
            .border(
                width = 1.dp,
                brush = borderBrush,
                shape = RoundedCornerShape(cornerRadius)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onClick()
                    }
                } else {
                    Modifier
                }
            )
            .padding(16.dp),
        content = content
    )
}
