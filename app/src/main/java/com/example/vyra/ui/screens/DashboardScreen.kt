package com.example.vyra.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vyra.theme.CyberBg
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.CyberSurfaceVariant
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextSecondary
import com.example.vyra.ui.components.CyberpunkCard
import com.example.vyra.ui.viewmodels.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToAgents: () -> Unit,
    onNavigateToOptimizer: () -> Unit,
    onNavigateToMonetization: () -> Unit,
    onNavigateToSettings: () -> Unit = {}
) {
    val metrics by viewModel.metrics.collectAsState()
    val activities by viewModel.recentActivities.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val gridAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "grid_pulse"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Landing Hero Banner
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan,
                accentGlow = QuantumViolet
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Cyberpunk Mesh Canvas Overlay
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        val width = size.width
                        val height = size.height

                        // Draw Grid Lines
                        val cols = 8
                        for (i in 0..cols) {
                            val x = width * (i.toFloat() / cols)
                            drawLine(
                                color = NeonCyan.copy(alpha = 0.15f * gridAlpha),
                                start = androidx.compose.ui.geometry.Offset(x, 0f),
                                end = androidx.compose.ui.geometry.Offset(x, height),
                                strokeWidth = 1f
                            )
                        }

                        // Curve Glow Path
                        val path = Path().apply {
                            moveTo(0f, height * 0.7f)
                            cubicTo(
                                width * 0.25f, height * 0.3f,
                                width * 0.6f, height * 0.85f,
                                width, height * 0.2f
                            )
                        }
                        drawPath(
                            path = path,
                            brush = Brush.horizontalGradient(
                                listOf(NeonCyan, QuantumViolet, ElectricMagenta)
                            ),
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

                    Column(modifier = Modifier.padding(2.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(NeonCyan.copy(alpha = 0.2f))
                                        .border(1.dp, NeonCyan, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "AI CREATOR PLATFORM",
                                        color = NeonCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = NeonGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(NeonGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+${metrics.revenueGrowthPercent}% MoM",
                                    color = NeonGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "VYRA AI COMMAND CENTER",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Encrypted messaging, ElevenLabs Voice AI, Fan DNA analytics & automated monetization",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Key Metric Blocks Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MetricBlock(
                                label = "ESTIMATED MRR",
                                value = "$${metrics.monthlyRevenue.toInt()}",
                                icon = Icons.Default.MonetizationOn,
                                tint = NeonGreen
                            )
                            MetricBlock(
                                label = "TOTAL FANS",
                                value = "${metrics.totalFans}",
                                icon = Icons.Default.People,
                                tint = NeonCyan
                            )
                            MetricBlock(
                                label = "VIRALITY SCORE",
                                value = "${metrics.viralityScore}%",
                                icon = Icons.Default.TrendingUp,
                                tint = ElectricMagenta
                            )
                        }
                    }
                }
            }
        }

        // Live Telemetry Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CyberSurface)
                    .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NeonGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ELEVENLABS VOICE: ONLINE (18ms)",
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = ElectricMagenta,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AGENTS: 3 ACTIVE",
                            color = ElectricMagenta,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Quick Navigation Launch Tiles
        item {
            Text(
                text = "QUICK LAUNCH SUITE",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickLaunchCard(
                    title = "Chat Agents",
                    subtitle = "Bushfeexer • HoloKai",
                    icon = Icons.Default.Psychology,
                    color = QuantumViolet,
                    testTag = "btn_chat_agents",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToAgents
                )

                QuickLaunchCard(
                    title = "Optimizer",
                    subtitle = "Viral Content AI",
                    icon = Icons.Default.AutoAwesome,
                    color = NeonCyan,
                    testTag = "btn_optimize_content",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToOptimizer
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickLaunchCard(
                    title = "Monetize",
                    subtitle = "Subscriptions & LTV",
                    icon = Icons.Default.MonetizationOn,
                    color = NeonGreen,
                    testTag = "btn_monetization_quick",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMonetization
                )

                QuickLaunchCard(
                    title = "Voice Setup",
                    subtitle = "ElevenLabs Samples",
                    icon = Icons.Default.RecordVoiceOver,
                    color = ElectricMagenta,
                    testTag = "btn_settings_quick",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToSettings
                )
            }
        }

        // Revenue Growth & Virality Visualizer Chart
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = QuantumViolet
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = QuantumViolet,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PLATFORM REVENUE & VIRALITY TRAJECTORY",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = "Last 30 Days",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        val points = listOf(0.2f, 0.35f, 0.28f, 0.55f, 0.48f, 0.78f, 0.95f)
                        val w = size.width
                        val h = size.height
                        val stepX = w / (points.size - 1)

                        val path = Path().apply {
                            moveTo(0f, h * (1f - points[0]))
                            for (i in 1 until points.size) {
                                val prevX = (i - 1) * stepX
                                val prevY = h * (1f - points[i - 1])
                                val curX = i * stepX
                                val curY = h * (1f - points[i])
                                cubicTo(
                                    prevX + stepX / 2f, prevY,
                                    curX - stepX / 2f, curY,
                                    curX, curY
                                )
                            }
                        }

                        // Gradient Fill Under Curve
                        val fillPath = Path().apply {
                            addPath(path)
                            lineTo(w, h)
                            lineTo(0f, h)
                            close()
                        }

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                listOf(QuantumViolet.copy(alpha = 0.35f), Color.Transparent)
                            )
                        )

                        // Line Stroke
                        drawPath(
                            path = path,
                            brush = Brush.horizontalGradient(
                                listOf(NeonCyan, QuantumViolet, ElectricMagenta)
                            ),
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // Points
                        for (i in points.indices) {
                            val px = i * stepX
                            val py = h * (1f - points[i])
                            drawCircle(
                                color = NeonGreen,
                                radius = 3.dp.toPx(),
                                center = androidx.compose.ui.geometry.Offset(px, py)
                            )
                        }
                    }
                }
            }
        }

        // Recent Agent Logs Section
        item {
            Text(
                text = "REAL-TIME AGENT LOGS",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        items(activities) { activity ->
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when (activity.agentName) {
                                    "Bushfeexer" -> NeonCyan.copy(alpha = 0.2f)
                                    "HoloKai" -> ElectricMagenta.copy(alpha = 0.2f)
                                    else -> QuantumViolet.copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = when (activity.agentName) {
                                "Bushfeexer" -> NeonCyan
                                "HoloKai" -> ElectricMagenta
                                else -> QuantumViolet
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = activity.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = activity.timeAgo,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = "${activity.agentName}: ${activity.detail}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickLaunchCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                onClick()
            }
            .testTag(testTag)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun MetricBlock(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Black)
    }
}
