package com.example.vyra.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextSecondary
import com.example.vyra.ui.components.CyberpunkCard
import com.example.vyra.ui.viewmodels.MonetizationViewModel

@Composable
fun MonetizationScreen(viewModel: MonetizationViewModel) {
    val projectedRevenue by viewModel.projectedRevenue.collectAsState()
    val vipPrice by viewModel.vipPrice.collectAsState()
    val premiumPrice by viewModel.premiumPrice.collectAsState()
    val tiers by viewModel.tiers.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "MONETIZATION & PAYWALLS",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Text(
                text = "Adaptive pricing modeling, subscription tier configuration & revenue analytics",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        // Projected Revenue Overview Card
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen,
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
                                text = "PROJECTED MONTHLY REVENUE",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$${String.format("%.2f", projectedRevenue)}",
                                color = NeonGreen,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(NeonGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = NeonGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }

        // Price Modeling Sliders
        item {
            CyberpunkCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "ADAPTIVE TIER PRICE SLIDERS",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "VIP Tier Price: $${String.format("%.2f", vipPrice)}/mo",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = vipPrice,
                        onValueChange = { viewModel.updateVipPrice(it) },
                        valueRange = 9.99f..49.99f,
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricMagenta,
                            activeTrackColor = ElectricMagenta,
                            inactiveTrackColor = CyberBorder
                        ),
                        modifier = Modifier.testTag("slider_vip_price")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Premium Tier Price: $${String.format("%.2f", premiumPrice)}/mo",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = premiumPrice,
                        onValueChange = { viewModel.updatePremiumPrice(it) },
                        valueRange = 4.99f..24.99f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = CyberBorder
                        ),
                        modifier = Modifier.testTag("slider_premium_price")
                    )
                }
            }
        }

        // Tiers List
        item {
            Text(
                text = "ACTIVE SUBSCRIPTION TIERS",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        items(tiers) { tier ->
            val tierColor = Color(tier.badgeColorHex)
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = tierColor
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = tier.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "${tier.subscribersCount} Active Subscribers",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "$${tier.priceMonthly}/mo",
                            color = tierColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    tier.benefits.forEach { b ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = tierColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = b, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
