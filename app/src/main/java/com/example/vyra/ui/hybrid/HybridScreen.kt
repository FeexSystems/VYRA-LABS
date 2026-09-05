package com.example.vyra.ui.hybrid

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.NeonMagenta
import com.example.vyra.theme.NeonViolet

/**
 * Hybrid screen demonstrating native-web component integration
 */
@Composable
fun HybridScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Hybrid Components",
            style = MaterialTheme.typography.headlineMedium,
            color = NeonCyan
        )
        
        // Hybrid Cards
        HybridCard(
            title = "Native Integration",
            subtitle = "WebView + Compose",
            accentColor = NeonCyan
        ) {
            Text(
                text = "This card combines native Compose UI with web-based content rendering capabilities.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        HybridCard(
            title = "AI Collaboration",
            subtitle = "Multi-agent system",
            accentColor = NeonMagenta
        ) {
            Text(
                text = "Coordinated AI agents working together with sequential, parallel, and consensus patterns.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        HybridCard(
            title = "Cloud Sync",
            subtitle = "Offline-first architecture",
            accentColor = NeonViolet
        ) {
            Text(
                text = "Bidirectional synchronization between local Room database and cloud storage with conflict resolution.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        HybridCard(
            title = "Multi-Platform",
            subtitle = "Kotlin Multiplatform",
            accentColor = NeonGreen
        ) {
            Text(
                text = "Shared business logic and design tokens across Android, iOS, and web platforms.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        // Hybrid Chart
        HybridChart(
            data = listOf(
                "Jan" to 100.0,
                "Feb" to 150.0,
                "Mar" to 200.0,
                "Apr" to 180.0,
                "May" to 250.0
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Hybrid Form
        HybridForm(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Hybrid Form",
                style = MaterialTheme.typography.titleMedium,
                color = NeonCyan
            )
            
            var text by remember { mutableStateOf("") }
            
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter data") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color(0xFF2A2A48)
                )
            )
            
            Button(
                onClick = { /* Handle submit */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan
                )
            ) {
                Text("Submit", color = Color.Black)
            }
        }
    }
}
