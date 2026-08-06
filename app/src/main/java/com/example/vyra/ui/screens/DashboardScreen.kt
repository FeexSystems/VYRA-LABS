package com.example.vyra.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vyra.theme.CyberBg
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
    onNavigateToMonetization: () -> Unit
) {
    val metrics by viewModel.metrics.collectAsState()
    val activities by viewModel.recentActivities.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan,
                accentGlow = QuantumViolet
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CREATOR COMMAND CENTER",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Welcome back, Creator",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
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
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
                            label = "ACTIVE FANS",
                            value = "${metrics.totalFans}",
                            icon = Icons.Default.People,
                            tint = NeonCyan
                        )
                        MetricBlock(
                            label = "VIRALITY INDEX",
                            value = "${metrics.viralityScore}%",
                            icon = Icons.Default.TrendingUp,
                            tint = ElectricMagenta
                        )
                    }
                }
            }
        }

        // Quick Agent Shortcuts
        item {
            Text(
                text = "AI AGENTS SUITE",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToAgents,
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumViolet),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_chat_agents")
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat Agents", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onNavigateToOptimizer,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_optimize_content")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Optimize", fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Recent Agent Insights
        item {
            Text(
                text = "RECENT AGENT LOGS",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 8.dp)
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
                            .size(38.dp)
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
                            modifier = Modifier.size(20.dp)
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
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "${activity.agentName}: ${activity.detail}",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
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
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
    }
}
