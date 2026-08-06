package com.example.vyra.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.vyra.theme.CyberAmber
import com.example.vyra.theme.CyberBg
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextPrimary
import com.example.vyra.theme.TextSecondary

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingOverlay(
    onDismiss: (dontShowAgain: Boolean) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var dontShowAgain by remember { mutableStateOf(true) }

    val totalSteps = 4

    Dialog(
        onDismissRequest = { onDismiss(dontShowAgain) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.88f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(NeonCyan, ElectricMagenta, QuantumViolet, NeonGreen)
                        ),
                        RoundedCornerShape(20.dp)
                    )
                    .testTag("onboarding_overlay_card"),
                color = CyberBg,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Header Bar: Step indicator & Dismiss button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Go VYRA // SYSTEM TOUR [STEP ${currentStep + 1}/$totalSteps]",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        IconButton(
                            onClick = { onDismiss(dontShowAgain) },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("btn_close_onboarding")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Onboarding",
                                tint = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Step Indicator Dots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 0 until totalSteps) {
                            val isActive = i == currentStep
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .height(4.dp)
                                    .width(if (isActive) 28.dp else 12.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (isActive) NeonCyan else CyberBorder)
                                    .clickable { currentStep = i }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Animated Step Content Container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .height(380.dp)
                    ) {
                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    slideInHorizontally { width -> width } + fadeIn() with
                                            slideOutHorizontally { width -> -width } + fadeOut()
                                } else {
                                    slideInHorizontally { width -> -width } + fadeIn() with
                                            slideOutHorizontally { width -> width } + fadeOut()
                                }
                            },
                            label = "onboarding_step_anim"
                        ) { step ->
                            val scrollState = rememberScrollState()
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scrollState)
                            ) {
                                when (step) {
                                    0 -> OnboardingStepWelcome()
                                    1 -> OnboardingStepAgentPersonalities()
                                    2 -> OnboardingStepNavigationControls()
                                    3 -> OnboardingStepLaunchReady(
                                        dontShowAgain = dontShowAgain,
                                        onDontShowAgainChange = { dontShowAgain = it }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Footer Navigation Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentStep > 0) {
                            OutlinedButton(
                                onClick = { currentStep-- },
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_onboarding_prev")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChevronLeft,
                                    contentDescription = "Previous",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("PREV", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            // Skip Tour button
                            OutlinedButton(
                                onClick = { onDismiss(dontShowAgain) },
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_onboarding_skip")
                            ) {
                                Text("SKIP TOUR", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        if (currentStep < totalSteps - 1) {
                            Button(
                                onClick = { currentStep++ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonCyan,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_onboarding_next")
                            ) {
                                Text("NEXT", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Next",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else {
                            Button(
                                onClick = { onDismiss(dontShowAgain) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonGreen,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_onboarding_launch")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Launch",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Go VYRA", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingStepWelcome() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(NeonCyan.copy(alpha = 0.4f), Color.Transparent)
                    )
                )
                .border(2.dp, NeonCyan, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "Go VYRA Core",
                tint = NeonCyan,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "WELCOME TO Go VYRA",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Production Creator Platform & Cyberpunk AI Network",
            color = NeonCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CyberSurface)
                .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = "PLATFORM CAPABILITIES",
                    color = NeonGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FeatureBullet(
                    icon = Icons.Default.RecordVoiceOver,
                    tint = NeonCyan,
                    title = "Realtime ElevenLabs Voice AI",
                    description = "Interactive voice synthesis with real-time spectrum waveform audio analysis."
                )
                Spacer(modifier = Modifier.height(8.dp))
                FeatureBullet(
                    icon = Icons.Default.SmartToy,
                    tint = ElectricMagenta,
                    title = "Autonomous AI Agent Trio",
                    description = "Bushfeexer, HoloKai, and Lord Odin driving content, chat & monetization."
                )
                Spacer(modifier = Modifier.height(8.dp))
                FeatureBullet(
                    icon = Icons.Default.People,
                    tint = QuantumViolet,
                    title = "Encrypted Fan DNA & Scoring",
                    description = "Deep community analytics, engagement rings, and direct creator messaging."
                )
            }
        }
    }
}

@Composable
private fun OnboardingStepAgentPersonalities() {
    Column {
        Text(
            text = "VOICE AGENT PERSONALITIES",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Text(
            text = "Meet your specialized AI squad powered by ElevenLabs voice synthesis:",
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Bushfeexer Card
        AgentPersonalityRow(
            name = "Bushfeexer",
            tagline = "Content Optimization & Virality Agent",
            traits = "Virality Engine • Hashtag Clusters • Draft Refiner",
            color = NeonGreen,
            icon = Icons.Default.AutoAwesome
        )

        Spacer(modifier = Modifier.height(8.dp))

        // HoloKai Card
        AgentPersonalityRow(
            name = "HoloKai",
            tagline = "Cyberpunk Personality & Fan Conversation",
            traits = "High-Affinity Reply • Scarcity Framing • Neural Chat",
            color = NeonCyan,
            icon = Icons.Default.SmartToy
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lord Odin Card
        AgentPersonalityRow(
            name = "Lord Odin",
            tagline = "Monetization & Creator Strategy",
            traits = "VIP Tier Pricing • MRR Projections • Strategic Bass",
            color = ElectricMagenta,
            icon = Icons.Default.MonetizationOn
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ElevenLabs Audio Core
        AgentPersonalityRow(
            name = "ElevenLabs Realtime Synth",
            tagline = "Custom Voice Synthesis (Amina, Vyra, Kenji)",
            traits = "48kHz / 192kbps • Low-latency PCM • Spectrum Waveform",
            color = QuantumViolet,
            icon = Icons.Default.GraphicEq
        )
    }
}

@Composable
private fun OnboardingStepNavigationControls() {
    Column {
        Text(
            text = "NAVIGATION & CONTROL MODULES",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Text(
            text = "Use the bottom navigation bar and top header controls to switch modules:",
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        NavControlRow(
            label = "CORE",
            subtitle = "Dashboard & Live Feed",
            desc = "Track virality score, Fan LTV, MRR, and system event feed.",
            icon = Icons.Default.Dashboard,
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(8.dp))

        NavControlRow(
            label = "AGENTS",
            subtitle = "Multi-Agent Chat & Voice Orb",
            desc = "Transmit prompts, trigger ElevenLabs voice synth, and view waveform.",
            icon = Icons.Default.SmartToy,
            color = NeonGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        NavControlRow(
            label = "DNA",
            subtitle = "Fan Scoring & Tier Ranks",
            desc = "Inspect VIP/Pro/Standard fan profiles and engagement rings.",
            icon = Icons.Default.People,
            color = ElectricMagenta
        )

        Spacer(modifier = Modifier.height(8.dp))

        NavControlRow(
            label = "OPT / MRR",
            subtitle = "Content Optimizer & Monetization",
            desc = "Refine posts with Bushfeexer or forecast MRR with Lord Odin.",
            icon = Icons.Default.AutoAwesome,
            color = CyberAmber
        )

        Spacer(modifier = Modifier.height(8.dp))

        NavControlRow(
            label = "HEADER VOICE TOGGLE",
            subtitle = "Top-Right Mic Button",
            desc = "Toggle instant voice listening mode on any screen.",
            icon = Icons.Default.Mic,
            color = QuantumViolet
        )
    }
}

@Composable
private fun OnboardingStepLaunchReady(
    dontShowAgain: Boolean,
    onDontShowAgainChange: (Boolean) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(NeonGreen.copy(alpha = 0.2f))
                .border(2.dp, NeonGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = "System Ready",
                tint = NeonGreen,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "SYSTEM INITIALIZED",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Text(
            text = "You are ready to manage your creator hub with AI agents.",
            color = TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CyberSurface)
                .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Tip",
                        tint = CyberAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PRO TIP",
                        color = CyberAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "You can re-open this onboarding tour at any time from the Settings screen under 'SYSTEM TOUR & ONBOARDING'.",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Don't show again toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CyberBg)
                .clickable { onDontShowAgainChange(!dontShowAgain) }
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = dontShowAgain,
                onCheckedChange = { onDontShowAgainChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = NeonGreen,
                    checkmarkColor = Color.Black,
                    uncheckedColor = TextMuted
                ),
                modifier = Modifier.testTag("chk_dont_show_again")
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Do not show this onboarding tour automatically on startup",
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun FeatureBullet(
    icon: ImageVector,
    tint: Color,
    title: String,
    description: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = description, color = TextSecondary, fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun AgentPersonalityRow(
    name: String,
    tagline: String,
    traits: String,
    color: Color,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CyberSurface)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f))
                    .border(1.dp, color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = name, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = name, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = tagline, color = TextSecondary, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = traits, color = TextMuted, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun NavControlRow(
    label: String,
    subtitle: String,
    desc: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CyberSurface)
            .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "• $subtitle", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
            Text(text = desc, color = TextMuted, fontSize = 10.sp)
        }
    }
}
