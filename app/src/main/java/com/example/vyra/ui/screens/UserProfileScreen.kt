package com.example.vyra.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vyra.data.models.PlatformLink
import com.example.vyra.data.models.UserRole
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
import com.example.vyra.ui.components.GoVyraHeader
import com.example.vyra.ui.viewmodels.HomeFeedViewModel
import com.example.vyra.ui.viewmodels.MonetizationViewModel
import com.example.vyra.ui.viewmodels.ProfileViewModel
import com.example.vyra.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    settingsViewModel: SettingsViewModel? = null,
    monetizationViewModel: MonetizationViewModel? = null,
    homeFeedViewModel: HomeFeedViewModel? = null
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val activeRole = userProfile.activeRole
    val creator = userProfile.creatorDetails
    val fan = userProfile.fanDetails

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showEditPlatformDialog by remember { mutableStateOf<PlatformLink?>(null) }

    val tabs = listOf("MEDIA GRID", "REVENUE", "SETTINGS")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
    ) {
        GoVyraHeader(
            title = "USER PROFILE",
            subtitle = if (activeRole == UserRole.CREATOR) "Creator Mode • Broadcast & Revenue Hub" else "Fan Mode • VIP Collector & Tipping Hub"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Info Header Card
            item {
                CyberpunkCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("user_profile_header"),
                    borderColor = if (activeRole == UserRole.CREATOR) ElectricMagenta else NeonGreen
                ) {
                    Column {
                        // Avatar, Name, Handle, Verified Badge & Persona Indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (activeRole == UserRole.CREATOR) ElectricMagenta.copy(alpha = 0.25f)
                                            else NeonGreen.copy(alpha = 0.25f)
                                        )
                                        .border(
                                            2.dp,
                                            if (activeRole == UserRole.CREATOR) ElectricMagenta else NeonGreen,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Avatar",
                                        tint = if (activeRole == UserRole.CREATOR) NeonCyan else NeonGreen,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (activeRole == UserRole.CREATOR) creator.name else fan.name,
                                            color = Color.White,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = "Verified",
                                            tint = NeonCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    Text(
                                        text = if (activeRole == UserRole.CREATOR) creator.handle else fan.handle,
                                        color = NeonCyan,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                if (activeRole == UserRole.CREATOR) ElectricMagenta.copy(alpha = 0.2f)
                                                else NeonGreen.copy(alpha = 0.2f)
                                            )
                                            .border(
                                                1.dp,
                                                if (activeRole == UserRole.CREATOR) ElectricMagenta else NeonGreen,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (activeRole == UserRole.CREATOR) "CREATOR PERSONA • VERIFIED" else "FAN PERSONA • VIP TIER",
                                            color = if (activeRole == UserRole.CREATOR) ElectricMagenta else NeonGreen,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (activeRole == UserRole.CREATOR) creator.bio else "Fan Member of Go VYRA Cyber Network. Supporting Afrofuturist creators and AI agent synthesis.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Persona Toggle Segmented Switcher
                        Text(
                            text = "PERSONA TOGGLE (CREATOR VS FAN)",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("persona_toggle_segmented"),
                            colors = CardDefaults.cardColors(containerColor = CyberBg),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val isCreatorMode = activeRole == UserRole.CREATOR
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isCreatorMode)
                                                Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.35f), ElectricMagenta.copy(alpha = 0.35f)))
                                            else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                                        )
                                        .border(
                                            1.dp,
                                            if (isCreatorMode) NeonCyan else Color.Transparent,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { viewModel.switchRole(UserRole.CREATOR) }
                                        .padding(vertical = 10.dp)
                                        .testTag("toggle_creator_mode"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (isCreatorMode) NeonCyan else TextMuted.copy(alpha = 0.3f))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "CREATOR MODE",
                                            color = if (isCreatorMode) Color.White else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isCreatorMode) FontWeight.ExtraBold else FontWeight.Medium
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                val isFanMode = activeRole == UserRole.FAN
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isFanMode)
                                                Brush.horizontalGradient(listOf(QuantumViolet.copy(alpha = 0.35f), NeonGreen.copy(alpha = 0.35f)))
                                            else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                                        )
                                        .border(
                                            1.dp,
                                            if (isFanMode) NeonGreen else Color.Transparent,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { viewModel.switchRole(UserRole.FAN) }
                                        .padding(vertical = 10.dp)
                                        .testTag("toggle_fan_mode"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (isFanMode) NeonGreen else TextMuted.copy(alpha = 0.3f))
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "FAN MODE",
                                            color = if (isFanMode) Color.White else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isFanMode) FontWeight.ExtraBold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Social Media Handles Dynamic List
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SOCIAL MEDIA HANDLES",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "+ TAP HANDLE TO EDIT",
                                color = NeonCyan,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            userProfile.platforms.forEach { platform ->
                                val isConnected = platform.isConnected
                                val chipAccent = if (isConnected) NeonCyan else TextMuted

                                Box(
                                    modifier = Modifier
                                        .testTag("social_chip_${platform.id}")
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isConnected) chipAccent.copy(alpha = 0.15f) else CyberSurface)
                                        .border(
                                            1.dp,
                                            if (isConnected) chipAccent else CyberBorder,
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clickable { showEditPlatformDialog = platform }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Link,
                                            contentDescription = null,
                                            tint = chipAccent,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "${platform.name}: ${platform.handle}",
                                            color = if (isConnected) Color.White else TextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit handle",
                                            tint = chipAccent,
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Metric Counters (Followers, Following, Likes, Posts)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CyberBg)
                                .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            if (activeRole == UserRole.CREATOR) {
                                ProfileStatItem(label = "POSTS", value = "${creator.postsCount}", color = NeonGreen)
                                ProfileStatItem(label = "FOLLOWERS", value = "${creator.followersCount}", color = NeonCyan)
                                ProfileStatItem(label = "FOLLOWING", value = "184", color = QuantumViolet)
                                ProfileStatItem(label = "LIKES", value = "${creator.likesCount}", color = ElectricMagenta)
                            } else {
                                ProfileStatItem(label = "FAN DNA", value = "${fan.fanDnaScore}", color = NeonGreen)
                                ProfileStatItem(label = "BALANCE", value = selectedCurrency.formatAmount(fan.tokenBalanceUsd), color = NeonCyan)
                                ProfileStatItem(label = "TIPPED", value = selectedCurrency.formatAmount(fan.totalTippedUsd), color = ElectricMagenta)
                            }
                        }
                    }
                }
            }

            // Tab Navigation for Nested Screens (Media Grid, Revenue, Settings)
            item {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = CyberSurface,
                    contentColor = NeonCyan,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = NeonCyan,
                                height = 3.dp
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTabIndex = index },
                            modifier = Modifier.testTag("tab_profile_$index"),
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val icon = when (index) {
                                        0 -> Icons.Default.GridOn
                                        1 -> Icons.Default.MonetizationOn
                                        else -> Icons.Default.Settings
                                    }
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = title,
                                        tint = if (isSelected) NeonCyan else TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = title,
                                        color = if (isSelected) NeonCyan else TextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
                                    )
                                }
                            }
                        )
                    }
                }
            }

            // Tab Content
            item {
                when (selectedTabIndex) {
                    0 -> {
                        // Media Grid Tab
                        ProfileMediaGridSection()
                    }
                    1 -> {
                        // Revenue Tab
                        if (monetizationViewModel != null) {
                            MonetizationScreen(
                                viewModel = monetizationViewModel,
                                profileViewModel = viewModel
                            )
                        } else {
                            Text("Revenue Module Active", color = NeonCyan, fontSize = 12.sp)
                        }
                    }
                    2 -> {
                        // Settings Tab
                        if (settingsViewModel != null) {
                            SettingsScreen(
                                viewModel = settingsViewModel,
                                profileViewModel = viewModel
                            )
                        } else {
                            Text("Settings Module Active", color = QuantumViolet, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileMediaGridSection() {
    CyberpunkCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = NeonCyan
    ) {
        Column {
            Text(
                text = "PAST POSTS & MEDIA GRID",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Past audio, video, and visual casts published on VYRA network",
                color = TextMuted,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            val samplePostMedia = listOf(
                Pair("https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800", "Neon Horizons"),
                Pair("https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=800", "3D Stage Visuals"),
                Pair("https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800", "Fan Cover Sax"),
                Pair("https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800", "Lagos Cyber Fest")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                samplePostMedia.take(2).forEach { (url, title) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
                    ) {
                        coil.compose.AsyncImage(
                            model = url,
                            contentDescription = title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                    )
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                samplePostMedia.drop(2).take(2).forEach { (url, title) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, CyberBorder, RoundedCornerShape(10.dp))
                    ) {
                        coil.compose.AsyncImage(
                            model = url,
                            contentDescription = title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                                    )
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}



