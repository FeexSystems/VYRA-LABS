package com.example.vyra.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.vyra.ui.viewmodels.ContentOptimizerViewModel
import com.example.vyra.ui.viewmodels.FanDnaViewModel
import com.example.vyra.webview.ReactComponentWrapper

@Composable
fun FanDnaScreen(
    viewModel: FanDnaViewModel,
    optimizerViewModel: ContentOptimizerViewModel? = null
) {
    val fans by viewModel.allFans.collectAsState()
    val filter by viewModel.selectedTierFilter.collectAsState()
    val query by viewModel.searchQuery.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Fan DNA, 1 = Optimizer

    var showAddDialog by remember { mutableStateOf(false) }

    var newName by remember { mutableStateOf("") }
    var newUsername by remember { mutableStateOf("") }
    var newTier by remember { mutableStateOf("VIP") }
    var newPlatform by remember { mutableStateOf("X") }
    var newSpend by remember { mutableStateOf("250") }
    var useReactCards by remember { mutableStateOf(false) }

    val filteredFans = fans.filter { fan ->
        (filter == "All" || fan.tier == filter) &&
        (query.isBlank() || fan.name.contains(query, ignoreCase = true) || fan.username.contains(query, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
    ) {
        // Tab Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CyberSurface)
                .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (activeTab == 0) ElectricMagenta else Color.Transparent)
                    .clickable { activeTab = 0 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FAN DNA PROFILES",
                    color = if (activeTab == 0) Color.White else TextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (activeTab == 1) NeonCyan else Color.Transparent)
                    .clickable { activeTab = 1 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "OPTIMIZER & HOOKS",
                    color = if (activeTab == 1) Color.Black else TextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }

        if (activeTab == 1 && optimizerViewModel != null) {
            OptimizerScreen(viewModel = optimizerViewModel)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                Column {
                    Text(
                        text = "FAN DNA ANALYTICS",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Behavioral profiling, engagement metrics & lifetime value",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.testTag("btn_add_fan"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricMagenta),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Fan", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fan_search_input"),
                placeholder = { Text("Search fan name or handle...", color = TextMuted, fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CyberSurface,
                    unfocusedContainerColor = CyberSurface,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Filter Chips & Hybrid Switcher
        item {
            val tiers = listOf("All", "VIP", "Premium", "Standard")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(tiers) { t ->
                        val isSelected = filter == t
                        Box(
                            modifier = Modifier
                                .testTag("fan_filter_$t")
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) ElectricMagenta else CyberSurface)
                                .border(1.dp, if (isSelected) ElectricMagenta else CyberBorder, RoundedCornerShape(20.dp))
                                .clickable { viewModel.setFilter(t) }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = t,
                                color = if (isSelected) Color.White else TextMuted,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .testTag("toggle_react_hybrid")
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (useReactCards) NeonCyan.copy(alpha = 0.25f) else CyberSurface)
                        .border(1.dp, if (useReactCards) NeonCyan else CyberBorder, RoundedCornerShape(20.dp))
                        .clickable { useReactCards = !useReactCards }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (useReactCards) "⚡ REACT HYBRID" else "NATIVE UI",
                        color = if (useReactCards) NeonCyan else TextMuted,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }

        items(filteredFans) { fan ->
            if (useReactCards) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, if (fan.tier == "VIP") ElectricMagenta else CyberBorder, RoundedCornerShape(12.dp))
                ) {
                    ReactComponentWrapper(
                        componentName = "FanProfileCard",
                        props = mapOf(
                            "id" to fan.id,
                            "name" to fan.name,
                            "username" to fan.username,
                            "tier" to fan.tier,
                            "platform" to fan.platform,
                            "totalSpend" to fan.totalSpend.toString()
                        ),
                        modifier = Modifier.fillMaxSize(),
                        onEvent = { eventName, _ ->
                            if (eventName == "upgrade_tier") {
                                val nextTier = when (fan.tier) {
                                    "Standard" -> "Premium"
                                    "Premium" -> "VIP"
                                    else -> "Standard"
                                }
                                viewModel.updateTier(fan.id, nextTier)
                            }
                        }
                    )
                }
            } else {
                CyberpunkCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fan_card_${fan.id}"),
                    borderColor = when (fan.tier) {
                        "VIP" -> ElectricMagenta
                        "Premium" -> NeonCyan
                        else -> CyberBorder
                    }
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                when (fan.tier) {
                                    "VIP" -> ElectricMagenta.copy(alpha = 0.2f)
                                    "Premium" -> NeonCyan.copy(alpha = 0.2f)
                                    else -> QuantumViolet.copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = when (fan.tier) {
                                "VIP" -> ElectricMagenta
                                "Premium" -> NeonCyan
                                else -> QuantumViolet
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = fan.name,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TierBadge(
                                    tier = fan.tier,
                                    onClick = {
                                        val nextTier = when (fan.tier) {
                                            "Standard" -> "Premium"
                                            "Premium" -> "VIP"
                                            else -> "Standard"
                                        }
                                        viewModel.updateTier(fan.id, nextTier)
                                    }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { viewModel.deleteFan(fan.id) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Fan",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "${fan.username} • ${fan.primaryPlatform}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "LTV: $${fan.lifetimeSpend.toInt()}",
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Engagement: ${fan.engagementScore}%",
                                color = NeonCyan,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
}
}

    // Add Fan Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = CyberSurface,
            title = {
                Text("Register Fan DNA Profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Fan Name", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_fan_name")
                    )

                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("Handle (@username)", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_fan_username")
                    )

                    OutlinedTextField(
                        value = newSpend,
                        onValueChange = { newSpend = it },
                        label = { Text("Lifetime Spend ($)", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("VIP", "Premium", "Standard").forEach { t ->
                            OutlinedButton(
                                onClick = { newTier = t },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (newTier == t) ElectricMagenta.copy(alpha = 0.3f) else Color.Transparent
                                )
                            ) {
                                Text(t, fontSize = 10.sp, color = if (newTier == t) ElectricMagenta else TextMuted)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addNewFan(
                            name = newName,
                            username = newUsername,
                            tier = newTier,
                            platform = newPlatform,
                            spend = newSpend.toDoubleOrNull() ?: 100.0
                        )
                        showAddDialog = false
                        newName = ""
                        newUsername = ""
                    },
                    modifier = Modifier.testTag("btn_save_fan"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricMagenta)
                ) {
                    Text("Save Profile", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}

@Composable
private fun TierBadge(tier: String, onClick: () -> Unit = {}) {
    val color = when (tier) {
        "VIP" -> ElectricMagenta
        "Premium" -> NeonCyan
        else -> QuantumViolet
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(10.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = tier,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
