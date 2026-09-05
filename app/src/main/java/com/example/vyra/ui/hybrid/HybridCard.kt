package com.example.vyra.ui.hybrid

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonMagenta
import com.example.vyra.theme.NeonViolet

/**
 * Hybrid card component combining native Compose with web-like styling
 */
@Composable
fun HybridCard(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonCyan,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF12121E),
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with accent
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = accentColor
                    )
                    subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFA0A0B0)
                        )
                    }
                }
                
                // Accent indicator
                Box(
                    modifier = Modifier
                        .size(4.dp, 40.dp)
                        .background(accentColor)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Content
            content()
        }
    }
}

/**
 * Hybrid chart component placeholder
 */
@Composable
fun HybridChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier,
    accentColor: Color = NeonViolet
) {
    HybridCard(
        title = "Analytics",
        subtitle = "Performance metrics",
        modifier = modifier,
        accentColor = accentColor
    ) {
        // TODO: Integrate WebView-based chart library
        Text(
            text = "Chart visualization will be rendered here",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF606070)
        )
    }
}

/**
 * Hybrid form component
 */
@Composable
fun HybridForm(
    modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF12121E),
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    }
}
