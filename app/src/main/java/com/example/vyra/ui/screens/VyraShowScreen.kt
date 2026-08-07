package com.example.vyra.ui.screens

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun VyraShowScreen(
    viewModel: HomeFeedViewModel
) {
    val posts by viewModel.posts.collectAsState()
    var selectedPostIndex by remember { mutableStateOf(0) }
    var showTipSheet by remember { mutableStateOf(false) }

    val currentPost = if (posts.isNotEmpty()) posts[selectedPostIndex % posts.size] else null

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
                onRevyralize = { viewModel.revyralize(currentPost.id) },
                onOpenTip = { showTipSheet = true },
                onNextPost = {
                    if (posts.isNotEmpty()) {
                        selectedPostIndex = (selectedPostIndex + 1) % posts.size
                    }
                },
                onPrevPost = {
                    if (posts.isNotEmpty()) {
                        selectedPostIndex = if (selectedPostIndex > 0) selectedPostIndex - 1 else posts.size - 1
                    }
                }
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No Vyra Shows currently broadcasting.", color = TextMuted)
            }
        }

        // Overlay Header
        Column(modifier = Modifier.fillMaxWidth()) {
            GoVyraHeader(
                title = "VYRA SHOW",
                subtitle = "Cyberpunk Video & Stream Broadcasts"
            )
        }

        if (showTipSheet && currentPost != null) {
            AfricanPaymentGatewaySheet(
                title = "Tip Creator: ${currentPost.authorName}",
                amountUsd = 10.0,
                selectedCurrency = AfricanCurrencies.NGN,
                onCurrencyChanged = {},
                gateways = emptyList(),
                onDismiss = { showTipSheet = false },
                onCheckoutSuccess = { showTipSheet = false }
            )
        }
    }
}

@Composable
fun VyraShowPlayerView(
    post: ContentPost,
    onToggleCast: () -> Unit,
    onRevyralize: () -> Unit,
    onOpenTip: () -> Unit,
    onNextPost: () -> Unit,
    onPrevPost: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Media Image/Video Backdrop
        AsyncImage(
            model = post.mediaUrl.ifBlank { "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800" },
            contentDescription = post.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark Cyberpunk Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Audio Wave Bars Animation (if audio or music show)
        AnimatedAudioWaveVisualizer(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 80.dp)
        )

        // Right-Side Interactive Action Column
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cast Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(CyberSurface.copy(alpha = 0.8f))
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${post.castCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Revyralize Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(CyberSurface.copy(alpha = 0.8f))
                        .border(1.dp, NeonGreen, CircleShape)
                        .clickable { onRevyralize() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Revyralize",
                        tint = NeonGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${post.revyralCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Comments Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(CyberSurface.copy(alpha = 0.8f))
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${post.commentCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Tip Button (African Gateway support)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(ElectricMagenta)
                        .clickable { onOpenTip() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("₦/$", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("TIP", color = ElectricMagenta, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        // Bottom Creator Info Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(NeonCyan.copy(alpha = 0.3f))
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

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
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
                        text = post.authorHandle,
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = post.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = post.content,
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Pager Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPrevPost,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurface),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("‹ PREV SHOW", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onNextPost,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("NEXT SHOW ›", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
        listOf(wave1, wave2, wave1 * 0.7f, wave2 * 1.2f, wave1 * 0.5f).forEach { scale ->
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height((40 * scale.coerceIn(0.2f, 1.0f)).dp)
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
