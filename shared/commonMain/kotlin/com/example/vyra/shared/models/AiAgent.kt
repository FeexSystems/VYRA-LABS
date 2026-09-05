package com.example.vyra.shared.models

import kotlinx.serialization.Serializable

@Serializable
data class AiAgent(
    val id: String,
    val name: String,
    val description: String,
    val systemPrompt: String,
    val voiceId: String,
    val voiceName: String,
    val speed: Float,
    val pitch: Float,
    val icon: String,
    val color: String
)

@Serializable
data class ChatMessage(
    val id: String,
    val agentId: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long,
    val isRead: Boolean
)

@Serializable
data class VoiceInteraction(
    val id: String,
    val agentId: String,
    val agentName: String,
    val transcript: String,
    val agentResponse: String,
    val timestamp: String,
    val durationSeconds: Int,
    val sentimentScore: Double
)

@Serializable
data class FanProfile(
    val id: String,
    val name: String,
    val handle: String,
    val tier: String,
    val engagementScore: Double,
    val lifetimeValue: Double,
    val joinDate: Long,
    val lastInteraction: Long,
    val isVip: Boolean,
    val totalSpend: Double
)

@Serializable
data class ContentPost(
    val id: String,
    val title: String,
    val content: String,
    val platform: String,
    val viralityScore: Double,
    val hashtags: List<String>,
    val createdAt: Long,
    val publishedAt: Long?,
    val isOptimized: Boolean,
    val imageUrl: String?
)
