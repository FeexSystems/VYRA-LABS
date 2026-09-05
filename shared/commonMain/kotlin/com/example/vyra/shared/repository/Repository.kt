package com.example.vyra.shared.repository

import com.example.vyra.shared.models.AiAgent
import com.example.vyra.shared.models.ChatMessage
import com.example.vyra.shared.models.VoiceInteraction
import kotlinx.coroutines.flow.Flow

/**
 * Shared repository interface for multi-platform data access
 */
interface Repository {
    // Chat Messages
    fun getAllChatMessages(): Flow<List<ChatMessage>>
    fun getChatMessagesByAgent(agentId: String): Flow<List<ChatMessage>>
    suspend fun insertChatMessage(message: ChatMessage): Result<Unit>
    suspend fun updateChatMessage(message: ChatMessage): Result<Unit>
    suspend fun deleteChatMessage(message: ChatMessage): Result<Unit>
    
    // Voice Interactions
    fun getAllVoiceInteractions(): Flow<List<VoiceInteraction>>
    fun getVoiceInteractionsByAgent(agentId: String): Flow<List<VoiceInteraction>>
    suspend fun insertVoiceInteraction(interaction: VoiceInteraction): Result<Unit>
    suspend fun updateVoiceInteraction(interaction: VoiceInteraction): Result<Unit>
    suspend fun deleteVoiceInteraction(interaction: VoiceInteraction): Result<Unit>
    
    // AI Agents
    suspend fun getAllAgents(): Result<List<AiAgent>>
    suspend fun getAgentById(agentId: String): Result<AiAgent?>
}

/**
 * Base repository implementation with common logic
 */
abstract class BaseRepository : Repository {
    protected suspend fun <T> safeCall(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Platform-specific repository implementation
 */
expect class PlatformRepository : BaseRepository
