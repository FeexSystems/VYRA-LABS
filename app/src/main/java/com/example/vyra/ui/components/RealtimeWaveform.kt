package com.example.vyra.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.NeonGreen
import com.example.vyra.theme.QuantumViolet
import com.example.vyra.theme.TextMuted
import com.example.vyra.theme.TextSecondary
import kotlin.math.sin

@Composable
fun RealtimeWaveform(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    inputVolume: Float = 0.75f,
    outputVolume: Float = 0.88f,
    sentiment: String = "Engaging"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform_anim")

    val phaseShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val inputPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "input_pulse"
    )

    val outputPulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(380, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "output_pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CyberSurface)
            .border(
                1.dp,
                if (isActive) NeonGreen.copy(alpha = 0.7f) else CyberBorder,
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
            .testTag("realtime_waveform_container")
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
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isActive) NeonGreen else TextMuted)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isActive) "REALTIME AUDIO SPECTRUM WAVEFORM" else "VOICE INACTIVE",
                        color = if (isActive) NeonGreen else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }

                if (isActive) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (sentiment.lowercase()) {
                                        "positive", "high virality" -> NeonGreen.copy(alpha = 0.2f)
                                        "urgent", "critical" -> ElectricMagenta.copy(alpha = 0.2f)
                                        else -> NeonCyan.copy(alpha = 0.2f)
                                    }
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "AI SENTIMENT: ${sentiment.uppercase()}",
                                color = when (sentiment.lowercase()) {
                                    "positive", "high virality" -> NeonGreen
                                    "urgent", "critical" -> ElectricMagenta
                                    else -> NeonCyan
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "48kHz / 192kbps",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Canvas Waveform Render
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberBg)
            ) {
                val width = size.width
                val height = size.height
                val midY = height / 2

                if (!isActive) {
                    // Flat line when inactive
                    drawLine(
                        color = CyberBorder,
                        start = Offset(0f, midY),
                        end = Offset(width, midY),
                        strokeWidth = 2.dp.toPx()
                    )
                } else {
                    // Render Output Waveform (Agent Voice) - Magenta/Violet
                    val outputPath = Path()
                    outputPath.moveTo(0f, midY)
                    var x = 0f
                    val step = 4.dp.toPx()
                    while (x <= width) {
                        val angle = (x / width) * 4 * Math.PI + phaseShift
                        val amplitude = (midY * 0.7f) * outputVolume * outputPulse * sin(angle).toFloat()
                        outputPath.lineTo(x, midY + amplitude)
                        x += step
                    }
                    drawPath(
                        path = outputPath,
                        color = ElectricMagenta,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // Render Input Waveform (Mic/User Voice) - Neon Cyan/Green
                    val inputPath = Path()
                    inputPath.moveTo(0f, midY)
                    x = 0f
                    while (x <= width) {
                        val angle = (x / width) * 6 * Math.PI - phaseShift * 1.5f
                        val amplitude = (midY * 0.5f) * inputVolume * inputPulse * sin(angle).toFloat()
                        inputPath.lineTo(x, midY + amplitude)
                        x += step
                    }
                    drawPath(
                        path = inputPath,
                        color = NeonCyan,
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Draw spectrum bar overlay at bottom
                    val barCount = 20
                    val barWidth = width / barCount
                    for (i in 0 until barCount) {
                        val barHeightFactor = (sin(i + phaseShift * 2) + 1) / 2
                        val bHeight = (height * 0.4f * barHeightFactor * outputPulse).coerceAtLeast(4f)
                        val barX = i * barWidth + barWidth * 0.2f
                        drawRect(
                            brush = Brush.verticalGradient(
                                listOf(NeonGreen.copy(alpha = 0.8f), QuantumViolet.copy(alpha = 0.3f))
                            ),
                            topLeft = Offset(barX, height - bHeight),
                            size = androidx.compose.ui.geometry.Size(barWidth * 0.6f, bHeight)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Volume Levels Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Mic Input",
                        tint = NeonCyan,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mic Input: ${if (isActive) "${(inputVolume * inputPulse * 100).toInt()}% (-12dB)" else "Muted"}",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Voice Output",
                        tint = ElectricMagenta,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Voice Synth Output: ${if (isActive) "${(outputVolume * outputPulse * 100).toInt()}% (+4dB)" else "Silent"}",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
