package com.example.vyra.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.vyra.data.models.AfricanCurrencies
import com.example.vyra.data.models.ContentPost
import com.example.vyra.theme.CyberBg
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextSecondary
import com.example.vyra.ui.components.AfricanPaymentGatewaySheet
import com.example.vyra.ui.components.GoVyraHeader
import com.example.vyra.ui.viewmodels.HomeFeedViewModel
import kotlinx.coroutines.delay

@Composable
fun VyraShowScreen(
    viewModel: HomeFeedViewModel
) {
    val posts by viewModel.posts.collectAsState()
    var selectedPostIndex by remember { mutableStateOf(0) }
    var showTipSheet by remember { mutableStateOf(false) }
    var showShareModal by remember { mutableStateOf(false) }
    var showRevyralToast by remember { mutableStateOf(false) }
    var selectedViralityFilter by remember { mutableStateOf("🔥 ALL VIRAL") }

    val context = LocalContext.current

    // Filter posts for high-virality hub
    val filteredPosts = remember(posts, selectedViralityFilter) {
        when (selectedViralityFilter) {
            "⚡ TOP REVYRALIZED" -> posts.sortedByDescending { it.revyralCount }
            "💎 TOP CASTED" -> posts.sortedByDescending { it.castCount }
            "🌍 AFRO-CYBER" -> posts.filter { it.tags.any { t -> t.contains("AFRO", ignoreCase = true) || t.contains("AMAPIANO", ignoreCase = true) } }
            "🤖 AI BROADCASTS" -> posts.filter { it.tags.any { t -> t.contains("AI", ignoreCase = true) } }
            else -> posts.sortedByDescending { it.viralityScore }
        }
    }

    val currentPost = if (filteredPosts.isNotEmpty()) {
        filteredPosts[selectedPostIndex.coerceIn(0, filteredPosts.size - 1)]
    } else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBg)
    ) {
        if (currentPost != null) {
            // Full Screen Show Player
            VyraShowPlayerView(
                post = currentPost,
                onToggleCast = { viewModel.toggleCast(currentPost.id) },
                onRevyralize = {
                    viewModel.revyralize(currentPost.id)
                    showRevyralToast = true
                },
                onShare = {
                    viewModel.sharePost(currentPost.id)
                    // Trigger native Android share sheet
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "⚡ Watch \"${currentPost.title}\" by ${currentPost.authorName} on VYRA SHOW! https://vyra.io/show/${currentPost.id} #VyraShow #AfroCyberpunk #Revyralize"
                        )
                        putExtra(Intent.EXTRA_TITLE, currentPost.title)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Vyra Show via")
                    context.startActivity(shareIntent)
                },
                onOpenShareModal = { showShareModal = true },
                onOpenTip = { showTipSheet = true },
                onNextPost = {
                    if (filteredPosts.isNotEmpty()) {
                        selectedPostIndex = (selectedPostIndex + 1) % filteredPosts.size
                    }
                },
                onPrevPost = {
                    if (filteredPosts.isNotEmpty()) {
                        selectedPostIndex = if (selectedPostIndex > 0) selectedPostIndex - 1 else filteredPosts.size - 1
                    }
                }
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Loading Virality Hub",
                        tint = ElectricMagenta,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "SYNCHRONIZING VIRALITY HUB...",
                        color = NeonCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Overlay Header & Virality Filter Chips
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)
                    )
                )
        ) {
            GoVyraHeader(
                title = "VYRA SHOW",
                subtitle = "High-Virality Broadcast Hub"
            )

            // Virality Filter Scrollable Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "🔥 ALL VIRAL",
                    "⚡ TOP REVYRALIZED",
                    "💎 TOP CASTED",
                    "🌍 AFRO-CYBER",
                    "🤖 AI BROADCASTS"
                ).forEach { filter ->
                    val isSelected = selectedViralityFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) ElectricMagenta.copy(alpha = 0.25f) else CyberSurface.copy(alpha = 0.7f)
                            )
                            .border(
                                1.dp,
                                if (isSelected) ElectricMagenta else CyberBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                selectedViralityFilter = filter
                                selectedPostIndex = 0
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Animated Revyralized Toast Feedback
        AnimatedVisibility(
            visible = showRevyralToast,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
        ) {
            LaunchedEffect(showRevyralToast) {
                delay(2000)
                showRevyralToast = false
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonGreen)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Revyralized",
                        tint = NeonGreen,
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            text = "REVYRALIZED TO NEURAL GRID!",
                            color = NeonGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "+15% Reach Multiplier Activated",
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Tipping Sheet Integration
        if (showTipSheet && currentPost != null) {
            AfricanPaymentGatewaySheet(
                title = "Tip Show Creator: ${currentPost.authorName}",
                amountUsd = 10.0,
                selectedCurrency = AfricanCurrencies.NGN,
                onCurrencyChanged = {},
                gateways = emptyList(),
                onDismiss = { showTipSheet = false },
                onCheckoutSuccess = { showTipSheet = false }
            )
        }

        // In-App Cyberpunk Share Modal
        if (showShareModal && currentPost != null) {
            CyberShareModal(
                post = currentPost,
                onDismiss = { showShareModal = false },
                onShareCompleted = {
                    viewModel.sharePost(currentPost.id)
                    showShareModal = false
                }
            )
        }
    }
}

@Composable
fun VyraShowPlayerView(
    post: ContentPost,
    onToggleCast: () -> Unit,
    onRevyralize: () -> Unit,
    onShare: () -> Unit,
    onOpenShareModal: () -> Unit,
    onOpenTip: () -> Unit,
    onNextPost: () -> Unit,
    onPrevPost: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Media Backdrop
        AsyncImage(
            model = post.mediaUrl.ifBlank { "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800" },
            contentDescription = post.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Cyberpunk Gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.65f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.92f)
                        )
                    )
                )
        )

        // Top-Right Live Virality Gauge
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 116.dp, end = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.75f)),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Virality Index",
                    tint = NeonGreen,
                    modifier = Modifier.size(16.dp)
                )
                Column {
                    Text(
                        text = "${post.viralityScore}% VIRAL",
                        color = NeonGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "+${post.viralityVelocity}k vel/h",
                        color = NeonCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Center Audio Visualizer if Media is Audio
        if (post.mediaType.equals("AUDIO", ignoreCase = true)) {
            AnimatedAudioWaveVisualizer(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 60.dp)
            )
        }

        // Right-Side Secondary Action Column (Cast, Comments, Tip)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp, bottom = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cast (Like) Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(CyberSurface.copy(alpha = 0.85f))
                        .border(1.dp, if (post.isCasted) ElectricMagenta else CyberBorder, CircleShape)
                        .clickable { onToggleCast() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (post.isCasted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Cast",
                        tint = if (post.isCasted) ElectricMagenta else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${post.castCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Comments Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(CyberSurface.copy(alpha = 0.85f))
                        .border(1.dp, CyberBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${post.commentCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // African Currency Tipping Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(ElectricMagenta, QuantumViolet))
                        )
                        .clickable { onOpenTip() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("₦/$", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text("TIP", color = ElectricMagenta, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        // Bottom Broadcast Details & PROMINENT Action Buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Creator Attribution Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(NeonCyan.copy(alpha = 0.25f))
                        .border(1.5.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Creator",
                            tint = NeonCyan,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Text(
                        text = post.authorHandle,
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // AI Virality Tag Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(QuantumViolet.copy(alpha = 0.35f))
                        .border(1.dp, QuantumViolet, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Virality",
                            tint = NeonCyan,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "HOLOKAI AI",
                            color = NeonCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Show Title & Caption
            Text(
                text = post.title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = post.content,
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            // =========================================================================
            // PROMINENT 'REVYRALIZE' AND 'SHARE' ACTION BUTTONS
            // =========================================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prominent Revyralize (Repost) Action Button
                Button(
                    onClick = onRevyralize,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(NeonGreen.copy(alpha = 0.2f), Color.Black.copy(alpha = 0.8f))
                                )
                            )
                            .border(1.8.dp, NeonGreen, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Revyralize",
                                tint = NeonGreen,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "REVYRALIZE",
                                    color = NeonGreen,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${post.revyralCount} REVYRALS",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Prominent Share Action Button
                Button(
                    onClick = onShare,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(ElectricMagenta, QuantumViolet)
                                )
                            )
                            .border(1.2.dp, ElectricMagenta, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "SHARE SHOW",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${post.shareCount} SHARES",
                                    color = NeonCyan,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Quick In-App Share Sheet Trigger
                IconButton(
                    onClick = onOpenShareModal,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CyberSurface)
                        .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Link",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Pager Row (Previous / Next Show)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPrevPost,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurface.copy(alpha = 0.85f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("‹ PREV SHOW", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "SWIPE OR TAP TO BROWSE",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = onNextPost,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("NEXT SHOW ›", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
fun CyberShareModal(
    post: ContentPost,
    onDismiss: () -> Unit,
    onShareCompleted: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    val showUrl = "https://vyra.io/show/${post.id}"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CyberSurface),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricMagenta)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = ElectricMagenta,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "SHARE VYRA SHOW",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Text(
                        text = "✕",
                        color = TextMuted,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(4.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = post.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Copy Link Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberBg)
                        .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = showUrl,
                            color = NeonCyan,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (copied) NeonGreen else ElectricMagenta)
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(showUrl))
                                    copied = true
                                    onShareCompleted()
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (copied) "COPIED!" else "COPY",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "TARGET NEURAL CHANNELS",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("WhatsApp", "X / Twitter", "TikTok", "Instagram").forEach { channel ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberBg)
                                .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                                .clickable { onShareCompleted() }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = channel,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedAudioWaveVisualizer(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(600, easing = LinearEasing), RepeatMode.Reverse),
        label = "w1"
    )
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing), RepeatMode.Reverse),
        label = "w2"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(wave1, wave2, wave1 * 0.7f, wave2 * 1.2f, wave1 * 0.5f, wave2 * 0.9f).forEach { scale ->
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height((50 * scale.coerceIn(0.2f, 1.0f)).dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(NeonCyan, ElectricMagenta)
                        )
                    )
            )
        }
    }
}
