package com.example.vyra.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vyra.data.models.AfricanCurrencies
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
import com.example.vyra.ui.viewmodels.ProfileViewModel
import com.example.vyra.ui.viewmodels.SettingsViewModel
import com.example.vyra.ui.viewmodels.VoicePersonalities
import com.example.vyra.ui.viewmodels.VoicePersonality

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    profileViewModel: ProfileViewModel
) {
    val selectedPersonality by viewModel.selectedPersonality.collectAsState()
    val playingPersonalityId by viewModel.playingPersonalityId.collectAsState()
    val playbackProgress by viewModel.playbackProgress.collectAsState()
    val playbackStatus by viewModel.playbackStatus.collectAsState()

    val creatorName by viewModel.creatorName.collectAsState()
    val handle by viewModel.handle.collectAsState()
    val bio by viewModel.bio.collectAsState()

    val elevenLabsKey by viewModel.elevenLabsKey.collectAsState()
    val voiceModeEnabled by viewModel.voiceModeEnabled.collectAsState()
    val glowEffects by viewModel.glowEffects.collectAsState()

    val privacyModeEnabled by viewModel.privacyModeEnabled.collectAsState()
    val cloudSyncEnabled by viewModel.cloudSyncEnabled.collectAsState()

    val selectedCurrency by profileViewModel.selectedCurrency.collectAsState()

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
                text = "Primary billing currency, Voice Agent personality selection, ElevenLabs synthesis & profile options",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // 1. PRIMARY BILLING & AFRICAN CURRENCY SETTINGS SECTION
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen,
                accentGlow = NeonGreen.copy(alpha = 0.2f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "PRIMARY BILLING & AFRICAN CURRENCY",
                                color = NeonGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonGreen.copy(alpha = 0.2f))
                                .border(1.dp, NeonGreen, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ACTIVE: ${selectedCurrency.code} (${selectedCurrency.symbol})",
                                color = NeonGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Toggle your primary billing currency across African markets (NGN, KES, ZAR, GHS, EGP, TZS, UGX, RWF, etc.):",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Interactive Currency Dropdown Selector
                    var dropdownExpanded by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dropdown_currency_selector")
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberSurface)
                            .border(1.5.dp, NeonGreen, RoundedCornerShape(10.dp))
                            .clickable { dropdownExpanded = true }
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = selectedCurrency.flag, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "${selectedCurrency.name} (${selectedCurrency.code})",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Symbol: ${selectedCurrency.symbol} • Exchange Rate: 1 USD = ${selectedCurrency.rateToUsd} ${selectedCurrency.code}",
                                        color = NeonGreen,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Expand Currency Dropdown",
                                tint = NeonGreen
                            )
                        }

                        androidx.compose.material3.DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier
                                .background(CyberSurface)
                                .border(1.dp, NeonGreen, RoundedCornerShape(8.dp))
                        ) {
                            AfricanCurrencies.all.forEach { currency ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = currency.flag, fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "${currency.code} - ${currency.name} (${currency.symbol})",
                                                color = if (currency.code == selectedCurrency.code) NeonGreen else Color.White,
                                                fontWeight = if (currency.code == selectedCurrency.code) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 13.sp
                                            )
                                        }
                                    },
                                    onClick = {
                                        profileViewModel.setCurrency(currency.code)
                                        dropdownExpanded = false
                                    },
                                    modifier = Modifier.testTag("dropdown_item_currency_${currency.code}")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AfricanCurrencies.all.forEach { currency ->
                            val isSelected = currency.code == selectedCurrency.code
                            Box(
                                modifier = Modifier
                                    .testTag("settings_currency_${currency.code}")
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) NeonGreen.copy(alpha = 0.25f)
                                        else CyberSurface
                                    )
                                    .border(
                                        1.5.dp,
                                        if (isSelected) NeonGreen else CyberBorder,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { profileViewModel.setCurrency(currency.code) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = currency.flag, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "${currency.code} (${currency.symbol})",
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        Text(
                                            text = currency.name,
                                            color = TextMuted,
                                            fontSize = 9.sp
                                        )
                                    }
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = NeonGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberBg)
                            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "Exchange Rate Baseline (Simulated Real-time FX):",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "1 USD = ${selectedCurrency.rateToUsd} ${selectedCurrency.code} • Sample $100 payout = ${selectedCurrency.formatAmount(100.0)}",
                                color = NeonGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Voice Agent Personality Selector Section (ElevenLabs Integration)
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PREFERRED VOICE AGENT PERSONALITY",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Select ElevenLabs voice model for AI agent audio synthesis & speech streaming",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        items(VoicePersonalities.all) { personality ->
            val isSelected = selectedPersonality.id == personality.id
            val isPlaying = playingPersonalityId == personality.id

            CyberpunkCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("voice_agent_${personality.id}")
                    .clickable { viewModel.selectPersonality(personality) },
                borderColor = if (isSelected) personality.accentColor else CyberBorder,
                accentGlow = if (isSelected) personality.accentColor else Color.Transparent
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) personality.accentColor else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = personality.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(personality.accentColor.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = personality.title,
                                            color = personality.accentColor,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = "ElevenLabs ID: ${personality.elevenLabsVoiceId}",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(personality.accentColor.copy(alpha = 0.2f))
                                    .border(1.dp, personality.accentColor, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ACTIVE VOICE",
                                    color = personality.accentColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = personality.description,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Traits pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyberSurface)
                            .border(1.dp, CyberBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = personality.traits,
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sample Phrase Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberBg)
                            .border(1.dp, CyberBorder.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = personality.accentColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Sample Phrase Quote",
                                    color = personality.accentColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${personality.samplePhrase}\"",
                                color = Color.LightGray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Play Sample Button Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.playSample(personality) },
                            modifier = Modifier
                                .testTag(if (isSelected) "btn_play_sample" else "btn_play_sample_${personality.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPlaying) NeonGreen else personality.accentColor
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isPlaying) "STOP SAMPLE" else "PLAY SAMPLE",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        if (isPlaying) {
                            AudioSpectrumBars(accentColor = personality.accentColor)
                        }
                    }

                    // Playing Status & Progress Indicator
                    if (isPlaying) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column {
                            LinearProgressIndicator(
                                progress = { playbackProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = personality.accentColor,
                                trackColor = CyberBorder
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = playbackStatus,
                                color = NeonGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
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
                        onValueChange = { viewModel.updateCreatorName(it) },
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
                        onValueChange = { viewModel.updateHandle(it) },
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
                        onValueChange = { viewModel.updateBio(it) },
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
                        text = "ELEVENLABS INTEGRATIONS & API KEYS",
                        color = QuantumViolet,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = elevenLabsKey,
                        onValueChange = { viewModel.updateElevenLabsKey(it) },
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
                            onCheckedChange = { viewModel.toggleVoiceMode(it) },
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
                            onCheckedChange = { viewModel.toggleGlowEffects(it) },
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

        // Privacy Mode & Data Control Section
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (privacyModeEnabled) NeonGreen else CyberBorder,
                accentGlow = if (privacyModeEnabled) NeonGreen else Color.Transparent
            ) {
                Column {
                    Text(
                        text = "PRIVACY MODE & LOCAL DATA CONTROL",
                        color = if (privacyModeEnabled) NeonGreen else NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Privacy Mode automatically clears local interaction history stored in the Room database and disables cloud sync for new sessions.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Privacy Mode",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (privacyModeEnabled) "Active • Local History Cleared • Cloud Sync Disabled" else "Disabled • Normal Cloud Backup Active",
                                color = if (privacyModeEnabled) NeonGreen else TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Switch(
                            checked = privacyModeEnabled,
                            onCheckedChange = { viewModel.togglePrivacyMode(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonGreen,
                                checkedTrackColor = NeonGreen.copy(alpha = 0.3f),
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = CyberBorder
                            ),
                            modifier = Modifier.testTag("switch_privacy_mode")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Manual Clear History Action
                    OutlinedButton(
                        onClick = { viewModel.clearLocalInteractionHistory() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_clear_voice_history"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricMagenta.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "CLEAR ROOM LOCAL VOICE HISTORY NOW",
                            color = ElectricMagenta,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Onboarding & System Tour Section
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan,
                accentGlow = NeonCyan.copy(alpha = 0.2f)
            ) {
                Column {
                    Text(
                        text = "SYSTEM TOUR & ONBOARDING",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Re-launch the lightweight onboarding overlay to review AI agent personalities (Bushfeexer, HoloKai, Lord Odin, ElevenLabs Voice) and navigation controls.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.resetOnboarding() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_replay_onboarding"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "REPLAY ONBOARDING TOUR NOW",
                            color = Color.Black,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioSpectrumBars(accentColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_spectrum")
    val heights = listOf(
        infiniteTransition.animateFloat(
            initialValue = 6f,
            targetValue = 22f,
            animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "bar1"
        ),
        infiniteTransition.animateFloat(
            initialValue = 18f,
            targetValue = 8f,
            animationSpec = infiniteRepeatable(tween(280, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "bar2"
        ),
        infiniteTransition.animateFloat(
            initialValue = 10f,
            targetValue = 26f,
            animationSpec = infiniteRepeatable(tween(420, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "bar3"
        ),
        infiniteTransition.animateFloat(
            initialValue = 22f,
            targetValue = 12f,
            animationSpec = infiniteRepeatable(tween(310, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "bar4"
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        heights.forEach { animState ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(animState.value.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(accentColor)
            )
        }
    }
}
