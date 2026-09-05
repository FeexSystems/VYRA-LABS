package com.example.vyra.shared.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.CardDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Cyberpunk-themed card component for multi-platform use
 */
@Composable
fun CyberCard(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF00F5FF),
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFF12121E),
        contentColor = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF2A2A48)),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                color = accentColor
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body2,
                    color = Color(0xFFA0A0B0)
                )
            }
            content()
        }
    }
}

object RoundedCornerShape {
    operator fun invoke(dp: Int): androidx.compose.foundation.shape.RoundedCornerShape {
        return androidx.compose.foundation.shape.RoundedCornerShape(dp.dp)
    }
}
