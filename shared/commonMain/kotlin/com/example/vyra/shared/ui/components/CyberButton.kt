package com.example.vyra.shared.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Cyberpunk-themed button component for multi-platform use
 */
@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF00F5FF),
    backgroundColor: Color = Color.Transparent,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = accentColor,
            disabledBackgroundColor = backgroundColor.copy(alpha = 0.3f),
            disabledContentColor = accentColor.copy(alpha = 0.3f)
        ),
        border = BorderStroke(2.dp, accentColor),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = androidx.compose.material.MaterialTheme.typography.button
        )
    }
}

@Composable
fun CyberButtonFilled(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF00F5FF),
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = accentColor,
            contentColor = Color.Black,
            disabledBackgroundColor = accentColor.copy(alpha = 0.3f),
            disabledContentColor = Color.Black.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = androidx.compose.material.MaterialTheme.typography.button
        )
    }
}
