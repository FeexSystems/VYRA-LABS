package com.example.vyra.shared.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Cyberpunk-themed text field component for multi-platform use
 */
@Composable
fun CyberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF00F5FF),
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = accentColor) },
        placeholder = { Text(placeholder, color = Color(0xFF606070)) },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = singleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.White,
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color(0xFF2A2A48),
            disabledBorderColor = Color(0xFF2A2A48).copy(alpha = 0.3f),
            cursorColor = accentColor,
            focusedLabelColor = accentColor,
            unfocusedLabelColor = Color(0xFFA0A0B0)
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun CyberTextFieldFilled(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF00F5FF),
    placeholder: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    androidx.compose.material.TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = accentColor) },
        placeholder = { Text(placeholder, color = Color(0xFF606070)) },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = singleLine,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            backgroundColor = Color(0xFF1A1A2E),
            focusedIndicatorColor = accentColor,
            unfocusedIndicatorColor = Color(0xFF2A2A48),
            disabledIndicatorColor = Color(0xFF2A2A48).copy(alpha = 0.3f),
            cursorColor = accentColor,
            focusedLabelColor = accentColor,
            unfocusedLabelColor = Color(0xFFA0A0B0)
        ),
        shape = RoundedCornerShape(8.dp)
    )
}
