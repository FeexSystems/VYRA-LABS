package com.example.vyra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vyra.theme.CyberBg
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextSecondary
import com.example.vyra.ui.components.CyberpunkCard

@Composable
fun SettingsScreen() {
    var creatorName by remember { mutableStateOf("Kaiser Prime") }
    var handle by remember { mutableStateOf("@kaiser_prime") }
    var bio by remember { mutableStateOf("Cyberpunk digital artist & creator powered by VYRA AI.") }

    var glowEffects by remember { mutableStateOf(true) }
    var voiceModeEnabled by remember { mutableStateOf(true) }
    var elevenLabsKey by remember { mutableStateOf("••••••••••••••••3a9b") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "CREATOR SETTINGS & PREFERENCES",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "Profile customizer, API key credentials & cyberpunk theme toggles",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // Creator Profile Section
        item {
            CyberpunkCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "CREATOR PROFILE",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = creatorName,
                        onValueChange = { creatorName = it },
                        label = { Text("Display Name", color = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_creator_name"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CyberSurface,
                            unfocusedContainerColor = CyberSurface,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = handle,
                        onValueChange = { handle = it },
                        label = { Text("Handle", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CyberSurface,
                            unfocusedContainerColor = CyberSurface,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Bio / Tagline", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CyberSurface,
                            unfocusedContainerColor = CyberSurface,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // API Credentials Section
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = QuantumViolet
            ) {
                Column {
                    Text(
                        text = "INTEGRATIONS & API KEYS",
                        color = QuantumViolet,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = elevenLabsKey,
                        onValueChange = { elevenLabsKey = it },
                        label = { Text("ElevenLabs API Key", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CyberSurface,
                            unfocusedContainerColor = CyberSurface,
                            focusedBorderColor = QuantumViolet,
                            unfocusedBorderColor = CyberBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ElevenLabs Real-time Voice Mode",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Switch(
                            checked = voiceModeEnabled,
                            onCheckedChange = { voiceModeEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonGreen,
                                checkedTrackColor = NeonGreen.copy(alpha = 0.3f),
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = CyberBorder
                            ),
                            modifier = Modifier.testTag("switch_voice_mode")
                        )
                    }
                }
            }
        }

        // Visual Preferences
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricMagenta
            ) {
                Column {
                    Text(
                        text = "CYBERPUNK THEME PREFERENCES",
                        color = ElectricMagenta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Neon Glow & Pulse Animations",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Switch(
                            checked = glowEffects,
                            onCheckedChange = { glowEffects = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ElectricMagenta,
                                checkedTrackColor = ElectricMagenta.copy(alpha = 0.3f),
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = CyberBorder
                            ),
                            modifier = Modifier.testTag("switch_glow_effects")
                        )
                    }
                }
            }
        }
    }
}
