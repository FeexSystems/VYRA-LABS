package com.example.vyra.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vyra.data.models.ContentPost
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomefeedScreen(
    viewModel: HomeFeedViewModel,
    monetizationViewModel: MonetizationViewModel? = null,
    onNavigateToChat: () -> Unit = {},
    onNavigateToShow: () -> Unit = {}
) {
    val posts by viewModel.posts.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    var showCreateModal by remember { mutableStateOf(false) }

    val filteredPosts = remember(posts, selectedFilter) {
        if (selectedFilter == "ALL") posts
        else posts.filter { it.mediaType.equals(selectedFilter, ignoreCase = true) }
    }

    val filters = listOf("ALL", "MUSIC", "VIDEOS", "IMAGES", "TEXT")

    Scaffold(
        containerColor = CyberBg,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateModal = true },
                containerColor = ElectricMagenta,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("create_post_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Post")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("CAST ON VYRA", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CyberBg)
        ) {
            // Header
            GoVyraHeader(title = "HOMEFEED", subtitle = "Public Stream • Viral Creator & Fan Casts")

            // Filters Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = selectedFilter.equals(filter, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) NeonCyan else CyberSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonCyan else CyberBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { viewModel.setFilter(filter) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Masonry Staggered Grid Feed
            if (filteredPosts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No public casts yet in this category. Be the first to Cast on VYRA!",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalItemSpacing = 10.dp
                ) {
                    items(filteredPosts, key = { it.id }) { post ->
                        PostMasonryCardItem(
                            post = post,
                            onToggleCast = { viewModel.toggleCast(post.id) },
                            onToggleSighted = { viewModel.toggleSighted(post.id) },
                            onRevyralize = { viewModel.revyralize(post.id) }
                        )
                    }
                }
            }
        }
    }

    if (showCreateModal) {
        CreatePostBottomSheet(
            onDismiss = { showCreateModal = false },
            onCreate = { title, content, mediaUrl, mediaType, tags ->
                viewModel.createPost(title, content, mediaUrl, mediaType, tags)
                showCreateModal = false
            }
        )
    }
}

/**
 * Data model for a single floating neon particle in the Casted heart burst animation
 */
private data class HeartParticle(
    val id: Long,
    val startOffsetX: Float,
    val targetOffsetX: Float,
    val targetOffsetY: Float,
    val color: Color,
    val sizeDp: Float
)

@Composable
fun PostMasonryCardItem(
    post: ContentPost,
    onToggleCast: () -> Unit,
    onToggleSighted: () -> Unit,
    onRevyralize: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Bouncy scale animation for Casted button
    val castScale by animateFloatAsState(
        targetValue = if (post.isCasted) 1.35f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "castScale"
    )

    val sightedScale by animateFloatAsState(
        targetValue = if (post.isSighted) 1.25f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "sightedScale"
    )

    // Burst Heart Particles for "Casted" user engagement
    val particles = remember { mutableStateListOf<HeartParticle>() }
    var triggerBurstCounter by remember { mutableIntStateOf(0) }

    // Big central heart animation for double-tap on media
    val bigHeartScale = remember { Animatable(0f) }
    val bigHeartAlpha = remember { Animatable(0f) }

    fun triggerCastedHeartBurst() {
        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
        onToggleCast()
        triggerBurstCounter++

        val colors = listOf(ElectricMagenta, NeonCyan, QuantumViolet, NeonGreen, Color(0xFFFF4081))
        val newParticles = (0..6).map { i ->
            HeartParticle(
                id = System.nanoTime() + i,
                startOffsetX = (Random.nextFloat() - 0.5f) * 16f,
                targetOffsetX = (Random.nextFloat() - 0.5f) * 90f,
                targetOffsetY = -Random.nextFloat() * 110f - 40f,
                color = colors[Random.nextInt(colors.size)],
                sizeDp = Random.nextFloat() * 10f + 12f
            )
        }
        particles.clear()
        particles.addAll(newParticles)

        scope.launch {
            delay(1000)
            particles.clear()
        }
    }

    fun triggerDoubleTapMediaBurst() {
        if (!post.isCasted) {
            triggerCastedHeartBurst()
        }
        scope.launch {
            bigHeartScale.snapTo(0.2f)
            bigHeartAlpha.snapTo(1f)
            launch {
                bigHeartScale.animateTo(
                    targetValue = 1.4f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                )
            }
            delay(400)
            bigHeartAlpha.animateTo(0f, animationSpec = tween(300, easing = FastOutSlowInEasing))
            bigHeartScale.snapTo(0f)
        }
    }

    CyberpunkCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}"),
        borderColor = if (post.isCasted) ElectricMagenta else if (post.isSighted) NeonCyan else CyberBorder
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Author Avatar & Role Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (post.authorRole == UserRole.CREATOR) ElectricMagenta.copy(alpha = 0.3f)
                                else NeonGreen.copy(alpha = 0.3f)
                            )
                            .border(
                                1.dp,
                                if (post.authorRole == UserRole.CREATOR) ElectricMagenta else NeonGreen,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = post.authorName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = post.timestamp,
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (post.authorRole == UserRole.CREATOR) ElectricMagenta.copy(alpha = 0.2f)
                            else NeonGreen.copy(alpha = 0.2f)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (post.authorRole == UserRole.CREATOR) "CREATOR" else "FAN",
                        color = if (post.authorRole == UserRole.CREATOR) ElectricMagenta else NeonGreen,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Post Title & Content
            if (post.title.isNotBlank()) {
                Text(
                    text = post.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (post.content.isNotBlank()) {
                Text(
                    text = post.content,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Custom Media Rendering (Image, Video, Audio) with double tap Cast support
            if (post.mediaUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                val mediaHeight = remember(post.id) {
                    when (post.mediaType.lowercase()) {
                        "video" -> 160.dp
                        "audio", "music" -> 110.dp
                        else -> if (post.id.hashCode() % 2 == 0) 180.dp else 140.dp
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(mediaHeight)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberBg)
                        .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                        .pointerInput(post.id) {
                            detectTapGestures(
                                onDoubleTap = { triggerDoubleTapMediaBurst() },
                                onTap = { onToggleSighted() }
                            )
                        }
                ) {
                    AsyncImage(
                        model = post.mediaUrl,
                        contentDescription = post.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Audio visualizer overlay
                    if (post.mediaType.equals("audio", ignoreCase = true) || post.mediaType.equals("music", ignoreCase = true)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, CyberBg.copy(alpha = 0.85f))
                                    )
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = "Audio Track",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "AUDIO CAST • 320 KBPS",
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    } else if (post.mediaType.equals("video", ignoreCase = true)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.35f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ElectricMagenta.copy(alpha = 0.85f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Video",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    // Double-tap central exploding heart
                    if (bigHeartAlpha.value > 0.01f) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Liked",
                                tint = ElectricMagenta,
                                modifier = Modifier
                                    .size(64.dp)
                                    .scale(bigHeartScale.value)
                                    .alpha(bigHeartAlpha.value)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Interactive Action Mechanics (Casted, Sighted, Revyral, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Casted (Heart) Button with Custom Heart Particle Burst
                Box(contentAlignment = Alignment.CenterStart) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { triggerCastedHeartBurst() }
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                            .testTag("btn_cast_${post.id}")
                    ) {
                        Icon(
                            imageVector = if (post.isCasted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Cast",
                            tint = if (post.isCasted) ElectricMagenta else TextMuted,
                            modifier = Modifier
                                .size(17.dp)
                                .scale(castScale)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${post.castCount}",
                            color = if (post.isCasted) ElectricMagenta else TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Floating Particle Canvas overlay
                    particles.forEach { particle ->
                        FloatingHeartParticle(particle = particle)
                    }
                }

                // Sighted (View) Button with Bouncy Eye Animation
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            onToggleSighted()
                        }
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                        .testTag("btn_sighted_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSighted) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Sighted",
                        tint = if (post.isSighted) NeonCyan else TextMuted,
                        modifier = Modifier
                            .size(17.dp)
                            .scale(sightedScale)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.sightedCount}",
                        color = if (post.isSighted) NeonCyan else TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Revyralize (Repost) Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            onRevyralize()
                        }
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                        .testTag("btn_revyral_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "Revyralize",
                        tint = NeonGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${post.revyralCount}",
                        color = NeonGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Animated floating cyberpunk heart particle for the Casted interaction
 */
@Composable
private fun FloatingHeartParticle(particle: HeartParticle) {
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(particle.id) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    val progress = animProgress.value
    if (progress < 1f) {
        val currentX = particle.startOffsetX + (particle.targetOffsetX * progress)
        val currentY = particle.targetOffsetY * progress
        val alpha = (1f - progress).coerceIn(0f, 1f)
        val scale = (0.5f + progress * 0.8f).coerceIn(0f, 1.3f)

        Box(
            modifier = Modifier
                .offset { IntOffset(currentX.toInt(), currentY.toInt()) }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    alpha = alpha
                )
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = particle.color,
                modifier = Modifier.size(particle.sizeDp.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostBottomSheet(
    onDismiss: () -> Unit,
    onCreate: (title: String, content: String, mediaUrl: String, mediaType: String, tags: List<String>) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var mediaUrl by remember { mutableStateOf("") }
    var selectedMediaType by remember { mutableStateOf("image") }
    var tagInput by remember { mutableStateOf("") }

    val mediaTypes = listOf("image", "video", "audio", "text")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = CyberBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "CAST ON VYRA",
                color = ElectricMagenta,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Share your latest music, pictures, video hooks, or thoughts with the global VYRA network.",
                color = TextMuted,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title / Headline") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Content Input
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("What's happening on VYRA?") },
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Media Type Selector
            Text("SELECT MEDIA TYPE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                mediaTypes.forEach { type ->
                    val isSelected = selectedMediaType == type
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) ElectricMagenta else CyberSurface)
                            .clickable { selectedMediaType = type }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = type.uppercase(),
                            color = if (isSelected) Color.White else TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Media URL Input
            OutlinedTextField(
                value = mediaUrl,
                onValueChange = { mediaUrl = it },
                label = { Text("Media Image / Video / Audio URL (Optional)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tag Input
            OutlinedTextField(
                value = tagInput,
                onValueChange = { tagInput = it },
                label = { Text("Tags (comma separated e.g. AfroBeats, Cyberpunk)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    if (content.isNotBlank() || title.isNotBlank()) {
                        val tags = tagInput.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        onCreate(title, content, mediaUrl, selectedMediaType, tags)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricMagenta, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("PUBLISH CAST", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
