package com.example.vyra.ai.models

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Model selector with fallback strategy for AI model selection
 */
class ModelSelector(
    private val openAI: OpenAIModel,
    private val claude: ClaudeModel,
    private val gemini: GeminiModel
) {
    
    enum class ModelProvider {
        OpenAI,
        Anthropic,
        Google
    }
    
    enum class Priority {
        High,   // OpenAI GPT-4
        Medium, // Anthropic Claude
        Low     // Google Gemini
    }
    
    private val modelPriority = mapOf(
        ModelProvider.OpenAI to Priority.High,
        ModelProvider.Anthropic to Priority.Medium,
        ModelProvider.Google to Priority.Low
    )
    
    /**
     * Generate completion with automatic fallback
     */
    suspend fun generateWithFallback(
        prompt: String,
        agentId: String,
        preferredProvider: ModelProvider = ModelProvider.OpenAI
    ): Result<String> {
        val providers = getProvidersInPriorityOrder(preferredProvider)
        
        for (provider in providers) {
            try {
                val result = when (provider) {
                    ModelProvider.OpenAI -> {
                        val systemPrompt = openAI.createSystemPrompt(agentId)
                        val messages = listOf(
                            openAI.createSystemMessage(systemPrompt),
                            openAI.createUserMessage(prompt)
                        )
                        openAI.generateCompletion(messages)
                    }
                    ModelProvider.Anthropic -> {
                        val systemPrompt = openAI.createSystemPrompt(agentId)
                        claude.generateCompletionWithSystem(systemPrompt, prompt)
                    }
                    ModelProvider.Google -> {
                        gemini.generateCompletion(prompt)
                    }
                }
                
                if (result.isSuccess) {
                    return result
                }
            } catch (e: Exception) {
                // Try next provider
                continue
            }
        }
        
        return Result.failure(Exception("All AI models failed"))
    }
    
    /**
     * Stream completion with fallback
     */
    fun streamWithFallback(
        prompt: String,
        agentId: String,
        preferredProvider: ModelProvider = ModelProvider.OpenAI
    ): Flow<String> = flow {
        val providers = getProvidersInPriorityOrder(preferredProvider)
        
        for (provider in providers) {
            try {
                when (provider) {
                    ModelProvider.OpenAI -> {
                        val systemPrompt = openAI.createSystemPrompt(agentId)
                        val messages = listOf(
                            openAI.createSystemMessage(systemPrompt),
                            openAI.createUserMessage(prompt)
                        )
                        openAI.streamCompletion(messages).collect { emit(it) }
                        return@flow
                    }
                    ModelProvider.Anthropic -> {
                        // Claude streaming not implemented in this version
                        val result = claude.generateCompletion(prompt)
                        result.fold(
                            { emit(it) },
                            { continue }
                        )
                    }
                    ModelProvider.Google -> {
                        gemini.streamCompletion(prompt).collect { emit(it) }
                        return@flow
                    }
                }
            } catch (e: Exception) {
                continue
            }
        }
        
        emit("Error: All AI models failed")
    }
    
    /**
     * Get providers in priority order
     */
    private fun getProvidersInPriorityOrder(preferred: ModelProvider): List<ModelProvider> {
        return ModelProvider.entries.sortedByDescending { 
            if (it == preferred) Priority.High else modelPriority[it] ?: Priority.Low
        }
    }
    
    /**
     * Select best model based on task type
     */
    fun selectModelForTask(taskType: TaskType): ModelProvider {
        return when (taskType) {
            TaskType.ContentOptimization -> ModelProvider.OpenAI
            TaskType.Conversation -> ModelProvider.Anthropic
            TaskType.BusinessIntelligence -> ModelProvider.OpenAI
            TaskType.VoiceInteraction -> ModelProvider.Google
            TaskType.Analytics -> ModelProvider.OpenAI
            TaskType.General -> ModelProvider.OpenAI
        }
    }
    
    enum class TaskType {
        ContentOptimization,
        Conversation,
        BusinessIntelligence,
        VoiceInteraction,
        Analytics,
        General
    }
}
