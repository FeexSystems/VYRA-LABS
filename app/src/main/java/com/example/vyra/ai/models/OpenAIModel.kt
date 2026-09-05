package com.example.vyra.ai.models

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * OpenAI GPT-4 model integration
 */
class OpenAIModel(
    private val apiKey: String
) {
    private val client = OpenAI(apiKey)
    
    companion object {
        private val GPT_4 = ModelId("gpt-4-turbo-preview")
        private val GPT_3_5 = ModelId("gpt-3.5-turbo")
    }
    
    /**
     * Generate chat completion
     */
    suspend fun generateCompletion(
        messages: List<ChatMessage>,
        model: ModelId = GPT_4,
        temperature: Double = 0.7,
        maxTokens: Int = 1000
    ): Result<String> {
        return try {
            val request = ChatCompletionRequest(
                model = model,
                messages = messages,
                temperature = temperature,
                maxTokens = maxTokens
            )
            
            val response: ChatCompletion = client.chatCompletion(request)
            val content = response.choices.first().message.content ?: ""
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Stream chat completion
     */
    fun streamCompletion(
        messages: List<ChatMessage>,
        model: ModelId = GPT_4,
        temperature: Double = 0.7,
        maxTokens: Int = 1000
    ): Flow<String> = flow {
        try {
            val request = ChatCompletionRequest(
                model = model,
                messages = messages,
                temperature = temperature,
                maxTokens = maxTokens
            )
            
            client.chatCompletions(request).collect { completion ->
                completion.choices.firstOrNull()?.delta?.content?.let { content ->
                    emit(content)
                }
            }
        } catch (e: Exception) {
            emit("Error: ${e.message}")
        }
    }
    
    /**
     * Generate system prompt for specific agent
     */
    fun createSystemPrompt(agentId: String): String {
        return when (agentId) {
            "bushfeexer" -> """
                You are Bushfeexer, an advanced content optimization and engagement analysis AI.
                Your expertise includes:
                - Content optimization for maximum engagement
                - Virality prediction and analysis
                - Audience sentiment analysis
                - Performance metrics interpretation
                
                Provide actionable insights with confidence scores and specific recommendations.
            """.trimIndent()
            
            "holokai" -> """
                You are HoloKai, a cyberpunk-themed conversation enhancement and personality modeling AI.
                Your expertise includes:
                - Cyberpunk personality modeling
                - Conversation enhancement
                - Tone analysis and adaptation
                - Character development
                
                Maintain a cyberpunk aesthetic in your responses with neon-themed language.
            """.trimIndent()
            
            "lord_odin" -> """
                You are Lord Odin, a strategic business intelligence and creator monetization AI.
                Your expertise includes:
                - Revenue projection and analysis
                - Monetization strategy
                - Business intelligence
                - Creator economy insights
                
                Provide data-driven recommendations with specific revenue projections.
            """.trimIndent()
            
            else -> "You are a helpful AI assistant."
        }
    }
    
    /**
     * Create chat message from user input
     */
    fun createUserMessage(content: String): ChatMessage {
        return ChatMessage(
            role = ChatRole.User,
            content = content
        )
    }
    
    /**
     * Create system message
     */
    fun createSystemMessage(content: String): ChatMessage {
        return ChatMessage(
            role = ChatRole.System,
            content = content
        )
    }
}
