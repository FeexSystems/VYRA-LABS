package com.example.vyra.ai.models

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * TrueFoundry AI Gateway Integration
 * Supports Claude Haiku 4.5, AWS Claude, xAI Grok, and OpenAI / Vertex TTS
 */
class TrueFoundryModel(
    private val apiKey: String = System.getenv("TRUEFOUNDRY_API_KEY") ?: "",
    private val baseUrl: String = "https://gateway.truefoundry.ai/api/llm"
) {
    private val client = HttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        const val MODEL_CLAUDE_HAIKU = "anthropic/claude-haiku-4-5-20251001"
        const val MODEL_AWS_CLAUDE_HAIKU = "aws-claude-platform/claude-haiku-4-5-20251001"
        const val MODEL_GROK_LATEST = "xai/grok-build-latest"
        const val MODEL_TTS_OPENAI = "openai/gpt-4o-mini-tts-2025-12-15"
        const val MODEL_TTS_GEMINI = "google-vertex/gemini-2.5-flash-tts"
    }

    @Serializable
    data class ChatMessage(
        val role: String,
        val content: String
    )

    @Serializable
    data class ChatCompletionRequest(
        val model: String,
        val messages: List<ChatMessage>,
        val temperature: Double = 0.7,
        val max_tokens: Int = 1000
    )

    @Serializable
    data class ChatCompletionResponse(
        val choices: List<Choice> = emptyList()
    ) {
        @Serializable
        data class Choice(
            val message: MessagePayload
        ) {
            @Serializable
            data class MessagePayload(
                val role: String = "assistant",
                val content: String = ""
            )
        }
    }

    /**
     * Generate chat completion via TrueFoundry Gateway
     */
    suspend fun generateCompletion(
        prompt: String,
        systemPrompt: String = "You are an AI assistant for the VYRA Cyberpunk Creator Platform.",
        model: String = MODEL_CLAUDE_HAIKU
    ): Result<String> {
        return try {
            val messages = listOf(
                ChatMessage(role = "system", content = systemPrompt),
                ChatMessage(role = "user", content = prompt)
            )
            val request = ChatCompletionRequest(
                model = model,
                messages = messages
            )

            val endpoint = "$baseUrl/chat/completions"
            val responseText: String = client.post(endpoint) {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $apiKey")
                    append("X-TFY-METADATA", "{}")
                    append("X-TFY-LOGGING-CONFIG", "{\"enabled\": true}")
                }
                setBody(json.encodeToString(request))
            }.body()

            val response = json.decodeFromString<ChatCompletionResponse>(responseText)
            val content = response.choices.firstOrNull()?.message?.content
                ?: "TrueFoundry gateway response received."
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Analyze virality potential of a Vyra Show broadcast
     */
    suspend fun analyzeShowVirality(
        title: String,
        content: String,
        tags: List<String>
    ): Result<String> {
        val prompt = """
            Analyze the virality potential of this VYRA Show broadcast:
            Title: $title
            Content: $content
            Tags: ${tags.joinToString(", ")}
            
            Provide:
            1. Estimated Virality Score (0-100%)
            2. Virality Velocity Rating
            3. 2 key recommendations to accelerate Revyralize reposts and shares across Afro-Cyberpunk audiences.
        """.trimIndent()

        val systemPrompt = "You are HoloKai AI, the Cyberpunk engagement and virality modeling engine of VYRA."
        return generateCompletion(prompt = prompt, systemPrompt = systemPrompt, model = MODEL_GROK_LATEST)
    }
}
