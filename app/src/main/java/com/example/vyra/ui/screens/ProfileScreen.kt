package com.example.vyra.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Security
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vyra.data.models.AfricanCurrencies
import com.example.vyra.data.models.AfricanCurrency
import com.example.vyra.data.models.PaymentGateway
import com.example.vyra.data.models.PaymentTransaction
import com.example.vyra.data.models.PlatformLink
import com.example.vyra.data.models.UserRole
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
import com.example.vyra.ui.components.CyberpunkCard
import com.example.vyra.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    settingsViewModel: com.example.vyra.ui.viewmodels.SettingsViewModel? = null,
    monetizationViewModel: com.example.vyra.ui.viewmodels.MonetizationViewModel? = null,
    homeFeedViewModel: com.example.vyra.ui.viewmodels.HomeFeedViewModel? = null
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showLinkPlatformDialog by remember { mutableStateOf<PlatformLink?>(null) }
    var showConfigureGatewayDialog by remember { mutableStateOf<PaymentGateway?>(null) }
    var showTransactionDialog by remember { mutableStateOf(false) }

    var isRevenueExpanded by remember { mutableStateOf(false) }
    var isSettingsExpanded by remember { mutableStateOf(false) }

    val activeRole = userProfile.activeRole
    val creator = userProfile.creatorDetails
    val fan = userProfile.fanDetails

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // MAIN PROFILE HEADER CARD (.profile-header)
        item {
            CyberpunkCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile-header")
                    .testTag("profile_header"),
                borderColor = if (activeRole == UserRole.CREATOR) NeonCyan else NeonGreen,
                accentGlow = if (activeRole == UserRole.CREATOR) ElectricMagenta.copy(alpha = 0.25f) else QuantumViolet.copy(alpha = 0.25f)
            ) {
                Column {
                    // 1. BRAND LOGO HEADER ROW ("Go VYRA")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Brush.radialGradient(listOf(NeonCyan, ElectricMagenta)))
                                    .border(1.5.dp, NeonCyan, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Go VYRA Brand Logo",
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Go VYRA",
                                    color = NeonCyan,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.5.sp
                                )
                                Text(
                                    text = "AI CREATOR NETWORK & PERSONA HUB",
                                    color = TextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CyberSurface)
                                .border(1.dp, CyberBorder, CircleShape)
                                .testTag("btn_edit_profile")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Divider(color = CyberBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. AVATAR & PERSONA IDENTITY
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar Box
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                if (activeRole == UserRole.CREATOR) NeonCyan.copy(alpha = 0.4f) else NeonGreen.copy(alpha = 0.4f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                                    .border(
                                        2.dp,
                                        if (activeRole == UserRole.CREATOR) NeonCyan else NeonGreen,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (activeRole == UserRole.CREATOR) Icons.Default.Psychology else Icons.Default.Person,
                                    contentDescription = "Avatar",
                                    tint = if (activeRole == UserRole.CREATOR) NeonCyan else NeonGreen,
                                    modifier = Modifier.size(34.dp)
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

                                // Persona Indicator Pill
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (activeRole == UserRole.CREATOR) ElectricMagenta else NeonGreen)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
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
                                            text = if (activeRole == UserRole.CREATOR) "CREATOR PERSONA • VERIFIED" else "FAN PERSONA • ${fan.tier}",
                                            color = if (activeRole == UserRole.CREATOR) ElectricMagenta else NeonGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bio / Tagline Text
                    Text(
                        text = if (activeRole == UserRole.CREATOR) creator.bio else "Fan Member of Go VYRA Cyber Network. Supporting Afrofuturist creators and AI agent synthesis.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Share Profile & Revyralize Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { /* Share Profile */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.IosShare, contentDescription = "Share", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("SHARE PROFILE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { /* Revyralize Profile */ },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricMagenta, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Repeat, contentDescription = "Revyralize", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("REVYRALIZE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. HORIZONTAL SEGMENTED CONTROL PERSONA SWITCHER WITH NEON GLOWING DOT INDICATOR
                    Text(
                        text = "PERSONA MODE SWITCHER",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_role_switcher")
                            .testTag("segmented_role_switcher"),
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
                            // CREATOR SEGMENT
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
                                    .testTag("tab_creator_mode"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Neon Glowing Dot Indicator for Active Creator Mode
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (isCreatorMode) NeonCyan else TextMuted.copy(alpha = 0.3f))
                                            .border(
                                                if (isCreatorMode) 2.dp else 0.dp,
                                                if (isCreatorMode) ElectricMagenta else Color.Transparent,
                                                CircleShape
                                            )
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

                            // FAN SEGMENT
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
                                    .testTag("tab_fan_mode"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Neon Glowing Dot Indicator for Active Fan Mode
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (isFanMode) NeonGreen else TextMuted.copy(alpha = 0.3f))
                                            .border(
                                                if (isFanMode) 2.dp else 0.dp,
                                                if (isFanMode) QuantumViolet else Color.Transparent,
                                                CircleShape
                                            )
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

                    // 4. INTERACTIVE ROUNDED NEON CHIPS FOR SOCIAL PLATFORM HANDLES (BENEATH SWITCHER)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SOCIAL PLATFORM HANDLES",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "TAP TO EDIT",
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
                                    .testTag("chip_social_${platform.id}")
                                    .testTag("social_chip_${platform.id}")
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isConnected) chipAccent.copy(alpha = 0.15f) else CyberSurface)
                                    .border(
                                        1.dp,
                                        if (isConnected) chipAccent else CyberBorder,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { showLinkPlatformDialog = platform }
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

                    // Key Stats Grid
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
                            ProfileStatItem(
                                label = "POSTS",
                                value = "${creator.postsCount}",
                                color = NeonGreen
                            )
                            ProfileStatItem(
                                label = "FOLLOWERS",
                                value = "${creator.followersCount}",
                                color = NeonCyan
                            )
                            ProfileStatItem(
                                label = "FOLLOWING",
                                value = "184",
                                color = QuantumViolet
                            )
                            ProfileStatItem(
                                label = "LIKES",
                                value = "${creator.likesCount}",
                                color = ElectricMagenta
                            )
                        } else {
                            ProfileStatItem(
                                label = "FAN DNA SCORE",
                                value = "${fan.fanDnaScore}",
                                color = NeonGreen
                            )
                            ProfileStatItem(
                                label = "TOKEN BAL",
                                value = selectedCurrency.formatAmount(fan.tokenBalanceUsd),
                                color = NeonCyan
                            )
                            ProfileStatItem(
                                label = "TOTAL TIPPED",
                                value = selectedCurrency.formatAmount(fan.totalTippedUsd),
                                color = CyberAmber
                            )
                        }
                    }
                }
            }
        }

        // 3. AFRICAN CURRENCY SELECTION MODULE
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberAmber,
                accentGlow = CyberAmber.copy(alpha = 0.15f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = selectedCurrency.flag, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "AFRICAN CURRENCY PREFERENCE",
                                    color = CyberAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "${selectedCurrency.name} (${selectedCurrency.code} ${selectedCurrency.symbol})",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Button(
                            onClick = { showCurrencyDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberAmber,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_change_currency")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("CHANGE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "All subscription pricing, creator payouts, and fan tipping metrics across VYRA AI are formatted dynamically in ${selectedCurrency.name} at real-time exchange rates (1 USD = ${selectedCurrency.formatAmount(1.0)}).",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // 4. POSTS & MEDIA ACTIVITY GRID (TILE/GRID FORMAT FRAMES)
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "POSTS & MEDIA ACTIVITY TILES",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Posts, video frames, and music casts published on VYRA",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

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
                                    .height(110.dp)
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
                                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                            )
                                        )
                                        .padding(6.dp),
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    Text(title, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                                    .height(110.dp)
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
                                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                            )
                                        )
                                        .padding(6.dp),
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    Text(title, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. COLLAPSIBLE REVENUE SCREEN SECTION
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricMagenta
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isRevenueExpanded = !isRevenueExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = ElectricMagenta)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("REVENUE & MONETIZATION METRICS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Subscribers, African gateways & MRR", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                        Text(if (isRevenueExpanded) "▲ CLOSE" else "▼ OPEN", color = ElectricMagenta, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    AnimatedVisibility(visible = isRevenueExpanded) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            if (monetizationViewModel != null) {
                                MonetizationScreen(viewModel = monetizationViewModel, profileViewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }

        // 6. COLLAPSIBLE SETTINGS SCREEN MENU SECTION
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = QuantumViolet
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isSettingsExpanded = !isSettingsExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = QuantumViolet)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("SETTINGS & SYSTEM CONFIGURATION", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Billing & currency, voice agents & account security", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                        Text(if (isSettingsExpanded) "▲ CLOSE" else "▼ OPEN", color = QuantumViolet, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    AnimatedVisibility(visible = isSettingsExpanded) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            if (settingsViewModel != null) {
                                SettingsScreen(viewModel = settingsViewModel, profileViewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }

        // 4. AFRICAN PAYMENT SYSTEMS INTEGRATION (FLUTTERWAVE, PAYSTACK, OPAY, M-PESA, CHIPPER)
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricMagenta,
                accentGlow = ElectricMagenta.copy(alpha = 0.2f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "AFRICAN PAYMENT SYSTEMS INTEGRATION",
                                color = ElectricMagenta,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Flutterwave • Paystack • OPay • M-Pesa • Chipper Cash",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = { showTransactionDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElectricMagenta,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_test_transaction")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (activeRole == UserRole.CREATOR) "PAYOUT TEST" else "TIP CREATOR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gateway Cards List
                    userProfile.gateways.forEach { gateway ->
                        GatewayIntegrationRow(
                            gateway = gateway,
                            isPrimary = if (activeRole == UserRole.CREATOR)
                                creator.primaryPayoutGatewayId == gateway.id
                            else fan.preferredPaymentGatewayId == gateway.id,
                            onSetPrimary = {
                                if (activeRole == UserRole.CREATOR)
                                    viewModel.setPrimaryPayoutGateway(gateway.id)
                                else viewModel.setPreferredFanGateway(gateway.id)
                            },
                            onConfigure = { showConfigureGatewayDialog = gateway }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // 5. EXTERNAL PLATFORMS LINKING (YOUTUBE, TIKTOK, SPOTIFY, BOOMPLAY, AUDIOMACK, X, ETC)
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan,
                accentGlow = NeonCyan.copy(alpha = 0.2f)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "EXTERNAL PLATFORM INTEGRATIONS",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Link YouTube, TikTok, Spotify, Boomplay, Audiomack & X",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${userProfile.platforms.count { it.isConnected }}/${userProfile.platforms.size} LINKED",
                                color = NeonCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Platform Rows
                    userProfile.platforms.forEach { platform ->
                        PlatformIntegrationRow(
                            platform = platform,
                            onToggleLink = { viewModel.togglePlatformConnection(platform.id) },
                            onEdit = { showLinkPlatformDialog = platform }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // 6. TRANSACTION & PAYMENT HISTORY
        item {
            CyberpunkCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = QuantumViolet,
                accentGlow = QuantumViolet.copy(alpha = 0.15f)
            ) {
                Column {
                    Text(
                        text = "AFRICAN PAYMENT TRANSACTION LOGS",
                        color = QuantumViolet,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (userProfile.transactions.isEmpty()) {
                        Text(
                            text = "No recent transactions.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    } else {
                        userProfile.transactions.take(5).forEach { tx ->
                            TransactionRow(
                                tx = tx,
                                currency = selectedCurrency
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // --- DIALOGS ---

    // 1. Edit Profile Dialog
    if (showEditProfileDialog) {
        EditProfileModal(
            role = activeRole,
            creator = creator,
            fan = fan,
            onDismiss = { showEditProfileDialog = false },
            onSaveCreator = { name, handle, bio, category, location ->
                viewModel.updateCreatorProfile(name, handle, bio, category, location)
                showEditProfileDialog = false
            },
            onSaveFan = { name, handle, tag ->
                viewModel.updateFanProfile(name, handle, tag)
                showEditProfileDialog = false
            }
        )
    }

    // 2. Select African Currency Dialog
    if (showCurrencyDialog) {
        SelectAfricanCurrencyModal(
            currentCurrency = selectedCurrency,
            onDismiss = { showCurrencyDialog = false },
            onSelectCurrency = { currency ->
                viewModel.setCurrency(currency.code)
                showCurrencyDialog = false
            }
        )
    }

    // 3. Link External Platform Dialog
    showLinkPlatformDialog?.let { platform ->
        LinkPlatformModal(
            platform = platform,
            onDismiss = { showLinkPlatformDialog = null },
            onConfirmLink = { handle ->
                viewModel.togglePlatformConnection(platform.id, handle)
                showLinkPlatformDialog = null
            }
        )
    }

    // 4. Configure Payment Gateway Dialog
    showConfigureGatewayDialog?.let { gateway ->
        ConfigureGatewayModal(
            gateway = gateway,
            onDismiss = { showConfigureGatewayDialog = null },
            onSave = { accountId, accountName ->
                viewModel.updateGatewayDetails(gateway.id, accountId, accountName)
                showConfigureGatewayDialog = null
            }
        )
    }

    // 5. Test Payment Transaction Dialog (Flutterwave / Paystack / OPay / M-Pesa)
    if (showTransactionDialog) {
        TestTransactionModal(
            role = activeRole,
            gateways = userProfile.gateways,
            currency = selectedCurrency,
            onDismiss = { showTransactionDialog = false },
            onExecute = { type, title, amountUsd, gatewayName ->
                viewModel.executeSimulatedTransaction(type, title, amountUsd, gatewayName)
                showTransactionDialog = false
            }
        )
    }
}

// --- SUB-COMPONENTS ---

@Composable
private fun ProfileStatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GatewayIntegrationRow(
    gateway: PaymentGateway,
    isPrimary: Boolean,
    onSetPrimary: () -> Unit,
    onConfigure: () -> Unit
) {
    val gateColor = com.example.vyra.theme.parseHexColor(gateway.badgeColorHex)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .border(
                1.dp,
                if (isPrimary) gateColor else CyberBorder,
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(gateColor.copy(alpha = 0.2f))
                            .border(1.dp, gateColor, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = gateway.name.take(2).uppercase(),
                            color = gateColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = gateway.name,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (isPrimary) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(gateColor.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PRIMARY",
                                        color = gateColor,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text(
                            text = gateway.supportedRegions,
                            color = TextSecondary,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onConfigure,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Gateway",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    if (!isPrimary) {
                        OutlinedButton(
                            onClick = onSetPrimary,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, gateColor),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("SET DEFAULT", color = gateColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Account details pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberBg)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Account: ${gateway.accountIdentifier} (${gateway.accountName})",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${gateway.feePercentage}% Fee",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun PlatformIntegrationRow(
    platform: PlatformLink,
    onToggleLink: () -> Unit,
    onEdit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CyberSurface)
            .border(
                1.dp,
                if (platform.isConnected) NeonCyan.copy(alpha = 0.5f) else CyberBorder,
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (platform.isConnected) NeonCyan.copy(alpha = 0.2f) else CyberBorder.copy(alpha = 0.3f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (platform.isConnected) Icons.Default.Link else Icons.Default.LinkOff,
                        contentDescription = platform.name,
                        tint = if (platform.isConnected) NeonCyan else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = platform.name,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "• ${platform.followersCount}",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }

                    Text(
                        text = "${platform.handle} • ${platform.lastSynced}",
                        color = if (platform.isConnected) NeonGreen else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Handle",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Button(
                    onClick = onToggleLink,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (platform.isConnected) ElectricMagenta.copy(alpha = 0.2f) else NeonCyan,
                        contentColor = if (platform.isConnected) ElectricMagenta else Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = if (platform.isConnected) "UNLINK" else "LINK",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(
    tx: PaymentTransaction,
    currency: AfricanCurrency
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberBg)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = tx.title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Via ${tx.gatewayName} • ${tx.referenceCode} • ${tx.timestamp}",
                color = TextMuted,
                fontSize = 10.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = currency.formatAmount(tx.amountUsd),
                color = if (tx.type == "PAYOUT") NeonGreen else ElectricMagenta,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(NeonGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(text = tx.status, color = NeonGreen, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- MODALS ---

@Composable
private fun SelectAfricanCurrencyModal(
    currentCurrency: AfricanCurrency,
    onDismiss: () -> Unit,
    onSelectCurrency: (AfricanCurrency) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, CyberAmber, RoundedCornerShape(16.dp)),
            color = CyberBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SELECT AFRICAN CURRENCY",
                        color = CyberAmber,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(AfricanCurrencies.list) { currency ->
                        val isSelected = currency.code == currentCurrency.code
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) CyberAmber.copy(alpha = 0.2f) else CyberSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) CyberAmber else CyberBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onSelectCurrency(currency) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = currency.flag, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "${currency.name} (${currency.code})",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Symbol: ${currency.symbol} • Rate: $1 USD = ${currency.formatAmount(1.0)}",
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = CyberAmber
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditProfileModal(
    role: UserRole,
    creator: com.example.vyra.data.models.CreatorProfileDetails,
    fan: com.example.vyra.data.models.FanProfileDetails,
    onDismiss: () -> Unit,
    onSaveCreator: (name: String, handle: String, bio: String, category: String, location: String) -> Unit,
    onSaveFan: (name: String, handle: String, tag: String) -> Unit
) {
    var name by remember { mutableStateOf(if (role == UserRole.CREATOR) creator.name else fan.name) }
    var handle by remember { mutableStateOf(if (role == UserRole.CREATOR) creator.handle else fan.handle) }
    var bio by remember { mutableStateOf(creator.bio) }
    var category by remember { mutableStateOf(creator.category) }
    var location by remember { mutableStateOf(creator.location) }
    var fanTag by remember { mutableStateOf(fan.fanTag) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp)),
            color = CyberBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "EDIT ${role.name} PROFILE",
                    color = NeonCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedLabelColor = NeonCyan,
                        unfocusedLabelColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = handle,
                    onValueChange = { handle = it },
                    label = { Text("Handle / Username") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedLabelColor = NeonCyan,
                        unfocusedLabelColor = TextMuted
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (role == UserRole.CREATOR) {
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Creator Category") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Bio") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = fanTag,
                        onValueChange = { fanTag = it },
                        label = { Text("Fan Tag") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CANCEL", color = TextMuted, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (role == UserRole.CREATOR) {
                                onSaveCreator(name, handle, bio, category, location)
                            } else {
                                onSaveFan(name, handle, fanTag)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SAVE CHANGES", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun LinkPlatformModal(
    platform: PlatformLink,
    onDismiss: () -> Unit,
    onConfirmLink: (handle: String) -> Unit
) {
    var handle by remember { mutableStateOf(platform.handle) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp)),
            color = CyberBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "LINK ${platform.name.uppercase()}",
                    color = NeonCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter your ${platform.name} handle, channel ID, or artist URL to sync audience stats and verification badges.",
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = handle,
                    onValueChange = { handle = it },
                    label = { Text("${platform.name} Handle / Link") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                        Text("CANCEL", color = TextMuted, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirmLink(handle) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CONNECT PLATFORM", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfigureGatewayModal(
    gateway: PaymentGateway,
    onDismiss: () -> Unit,
    onSave: (accountId: String, accountName: String) -> Unit
) {
    var accountId by remember { mutableStateOf(gateway.accountIdentifier) }
    var accountName by remember { mutableStateOf(gateway.accountName) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, com.example.vyra.theme.parseHexColor(gateway.badgeColorHex), RoundedCornerShape(16.dp)),
            color = CyberBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "CONFIGURE ${gateway.name.uppercase()}",
                    color = com.example.vyra.theme.parseHexColor(gateway.badgeColorHex),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Configure your payout account or wallet identifier for ${gateway.name}.",
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = accountId,
                    onValueChange = { accountId = it },
                    label = { Text("Account Number / Merchant ID / Phone") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = com.example.vyra.theme.parseHexColor(gateway.badgeColorHex),
                        unfocusedBorderColor = CyberBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = accountName,
                    onValueChange = { accountName = it },
                    label = { Text("Account Holder Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = com.example.vyra.theme.parseHexColor(gateway.badgeColorHex),
                        unfocusedBorderColor = CyberBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                        Text("CANCEL", color = TextMuted, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(accountId, accountName) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = com.example.vyra.theme.parseHexColor(gateway.badgeColorHex),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SAVE DETAILS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun TestTransactionModal(
    role: UserRole,
    gateways: List<PaymentGateway>,
    currency: AfricanCurrency,
    onDismiss: () -> Unit,
    onExecute: (type: String, title: String, amountUsd: Double, gatewayName: String) -> Unit
) {
    var selectedGateway by remember { mutableStateOf(gateways.firstOrNull()?.name ?: "Paystack") }
    var amountText by remember { mutableStateOf("25") }
    var isProcessing by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.5.dp, ElectricMagenta, RoundedCornerShape(16.dp)),
            color = CyberBg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (role == UserRole.CREATOR) "SIMULATE AFRICAN PAYOUT" else "SEND FAN TIP IN ${currency.code}",
                    color = ElectricMagenta,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Select an African payment system (Flutterwave, Paystack, OPay, M-Pesa) and enter amount:",
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Select Gateway
                Text(
                    text = "PAYMENT SYSTEM:",
                    color = NeonCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                gateways.forEach { gw ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedGateway == gw.name) ElectricMagenta.copy(alpha = 0.2f) else CyberSurface)
                            .clickable { selectedGateway = gw.name }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedGateway == gw.name,
                            onClick = { selectedGateway = gw.name },
                            colors = RadioButtonDefaults.colors(selectedColor = ElectricMagenta)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(text = gw.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = gw.brandTagline, color = TextMuted, fontSize = 9.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount in USD ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricMagenta,
                        unfocusedBorderColor = CyberBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                val usdVal = amountText.toDoubleOrNull() ?: 0.0
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Equivalent in ${currency.name}: ${currency.formatAmount(usdVal)}",
                    color = NeonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isProcessing) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = ElectricMagenta, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("PROCESSING VIA $selectedGateway...", color = ElectricMagenta, fontSize = 12.sp)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                            Text("CANCEL", color = TextMuted, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                isProcessing = true
                                val title = if (role == UserRole.CREATOR)
                                    "Creator Payout via $selectedGateway"
                                else "Fan Super Tip via $selectedGateway"
                                val type = if (role == UserRole.CREATOR) "PAYOUT" else "FAN_TIP"
                                onExecute(type, title, usdVal, selectedGateway)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ElectricMagenta,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("PROCESS TRANSACTION", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
