package com.example.vyra.ai.models

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Anthropic Claude API integration
 */
class ClaudeModel(
    private val apiKey: String
) {
    private val client = HttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private const val API_URL = "https://api.anthropic.com/v1/messages"
        private const val CLAUDE_3_OPUS = "claude-3-opus-20240229"
        private const val CLAUDE_3_SONNET = "claude-3-sonnet-20240229"
    }
    
    /**
     * Generate chat completion
     */
    suspend fun generateCompletion(
        message: String,
        model: String = CLAUDE_3_SONNET,
        maxTokens: Int = 1000
    ): Result<String> {
        return try {
            val request = ClaudeRequest(
                model = model,
                maxTokens = maxTokens,
                messages = listOf(ClaudeMessage(role = "user", content = message))
            )
            
            val response: ClaudeResponse = client.post(API_URL) {
                contentType(ContentType.Application.Json)
                headers {
                    append("x-api-key", apiKey)
                    append("anthropic-version", "2023-06-01")
                }
                setBody(json.encodeToString(request))
            }.body()
            
            Result.success(response.content.firstOrNull()?.text ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Generate completion with system prompt
     */
    suspend fun generateCompletionWithSystem(
        systemPrompt: String,
        message: String,
        model: String = CLAUDE_3_SONNET,
        maxTokens: Int = 1000
    ): Result<String> {
        return try {
            val request = ClaudeRequest(
                model = model,
                maxTokens = maxTokens,
                system = systemPrompt,
                messages = listOf(ClaudeMessage(role = "user", content = message))
            )
            
            val response: ClaudeResponse = client.post(API_URL) {
                contentType(ContentType.Application.Json)
                headers {
                    append("x-api-key", apiKey)
                    append("anthropic-version", "2023-06-01")
                }
                setBody(json.encodeToString(request))
            }.body()
            
            Result.success(response.content.firstOrNull()?.text ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

@Serializable
data class ClaudeRequest(
    val model: String,
    val maxTokens: Int,
    val system: String? = null,
    val messages: List<ClaudeMessage>
)

@Serializable
data class ClaudeMessage(
    val role: String,
    val content: String
)

@Serializable
data class ClaudeResponse(
    val id: String,
    val type: String,
    val role: String,
    val content: List<ClaudeContent>,
    val stopReason: String,
    val model: String
)

@Serializable
data class ClaudeContent(
    val type: String,
    val text: String
)
