package com.example.vyra.ai.models

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Google Gemini API integration
 */
class GeminiModel(
    private val apiKey: String
) {
    companion object {
        private const val GEMINI_PRO = "gemini-pro"
        private const val GEMINI_PRO_VISION = "gemini-pro-vision"
    }
    
    /**
     * Generate chat completion
     */
    suspend fun generateCompletion(
        prompt: String,
        modelName: String = GEMINI_PRO
    ): Result<String> {
        return try {
            val model = GenerativeModel(modelName, apiKey)
            val response = model.generateContent(prompt)
            Result.success(response.text ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Generate chat completion with history
     */
    suspend fun generateChatCompletion(
        messages: List<Pair<String, String>>,
        modelName: String = GEMINI_PRO
    ): Result<String> {
        return try {
            val model = GenerativeModel(modelName, apiKey)
            val chat = model.startChat()
            
            messages.forEach { (role, content) ->
                chat.sendMessage(content(role to content))
            }
            
            val response = chat.sendMessage(messages.last().second)
            Result.success(response.text ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Stream chat completion
     */
    fun streamCompletion(
        prompt: String,
        modelName: String = GEMINI_PRO
    ): Flow<String> = flow {
        try {
            val model = GenerativeModel(modelName, apiKey)
            model.generateContentStream(prompt).collect { chunk ->
                emit(chunk.text ?: "")
            }
        } catch (e: Exception) {
            emit("Error: ${e.message}")
        }
    }
}
