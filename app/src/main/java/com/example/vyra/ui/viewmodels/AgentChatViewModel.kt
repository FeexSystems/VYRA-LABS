package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.models.AiAgent
import com.example.vyra.data.models.AiAgents
import com.example.vyra.data.models.ChatMessage
import com.example.vyra.data.models.VoiceInteraction
import com.example.vyra.ai.models.ModelSelector
import com.example.vyra.utils.CacheManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class AgentChatViewModel(
    private val repository: VyraRepository,
    private val modelSelector: ModelSelector? = null,
    private val cacheManager: CacheManager? = null
) : ViewModel() {

    private val _selectedAgent = MutableStateFlow<AiAgent>(AiAgents.bushfeexer)
    val selectedAgent: StateFlow<AiAgent> = _selectedAgent.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isVoiceActive = MutableStateFlow(false)
    val isVoiceActive: StateFlow<Boolean> = _isVoiceActive.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _currentProviderName = MutableStateFlow("OpenAI GPT-4")
    val currentProviderName: StateFlow<String> = _currentProviderName.asStateFlow()

    val voiceInteractions: StateFlow<List<VoiceInteraction>> = repository.allVoiceInteractions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        observeMessagesForAgent(AiAgents.bushfeexer.id)
    }

    fun selectAgent(agent: AiAgent) {
        _selectedAgent.value = agent
        observeMessagesForAgent(agent.id)
    }

    fun toggleVoiceMode() {
        _isVoiceActive.value = !_isVoiceActive.value
    }

    private fun observeMessagesForAgent(agentId: String) {
        viewModelScope.launch {
            repository.getMessagesForAgent(agentId).collectLatest { messageList ->
                _messages.value = messageList
            }
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val currentAgent = _selectedAgent.value
        _isGenerating.value = true

        viewModelScope.launch {
            repository.sendMessage(recipientId = currentAgent.id, content = userText, isAgent = false)

            val cacheKey = "agent_resp_${currentAgent.id}_${userText.trim().lowercase().hashCode()}"
            val cachedResponse = cacheManager?.get(cacheKey)

            val response = if (cachedResponse != null) {
                cachedResponse
            } else {
                val generated = fetchAiModelResponse(currentAgent, userText)
                cacheManager?.put(cacheKey, generated)
                generated
            }

            repository.sendMessage(recipientId = currentAgent.id, content = response, isAgent = true)

            if (_isVoiceActive.value || currentAgent.id == "holokai") {
                val voiceLog = VoiceInteraction(
                    id = "v_${UUID.randomUUID().toString().take(6)}",
                    agentId = currentAgent.id,
                    agentName = currentAgent.name,
                    transcript = userText,
                    agentResponse = response,
                    timestamp = "Just now",
                    durationSeconds = (10..25).random(),
                    sentimentScore = 0.96
                )
                repository.saveVoiceInteraction(voiceLog)
            }

            _isGenerating.value = false
        }
    }

    private suspend fun fetchAiModelResponse(agent: AiAgent, input: String): String {
        if (modelSelector != null) {
            val taskType = when (agent.id) {
                "bushfeexer" -> ModelSelector.TaskType.ContentOptimization
                "holokai" -> ModelSelector.TaskType.Conversation
                "lord_odin" -> ModelSelector.TaskType.BusinessIntelligence
                else -> ModelSelector.TaskType.General
            }
            val preferredProvider = modelSelector.selectModelForTask(taskType)
            _currentProviderName.value = when (preferredProvider) {
                ModelSelector.ModelProvider.OpenAI -> "OpenAI GPT-4"
                ModelSelector.ModelProvider.Anthropic -> "Anthropic Claude"
                ModelSelector.ModelProvider.Google -> "Google Gemini"
            }

            val result = modelSelector.generateWithFallback(
                prompt = input,
                agentId = agent.id,
                preferredProvider = preferredProvider
            )
            if (result.isSuccess) {
                val text = result.getOrNull()
                if (!text.isNullOrBlank()) {
                    return text
                }
            }
        }
        return generateAgentFallbackResponse(agent, input)
    }

    fun clearVoiceHistory() {
        viewModelScope.launch {
            repository.clearVoiceInteractions()
        }
    }

    fun deleteVoiceInteraction(id: String) {
        viewModelScope.launch {
            repository.deleteVoiceInteraction(id)
        }
    }

    fun saveAgentPersonalitySettings(
        agentId: String,
        name: String,
        systemPrompt: String,
        voiceId: String,
        voiceName: String,
        speed: Float,
        pitch: Float
    ) {
        viewModelScope.launch {
            repository.saveAgentPersonality(agentId, systemPrompt, voiceName)
        }
    }

    private fun generateAgentFallbackResponse(agent: AiAgent, input: String): String {
        return when (agent.id) {
            "bushfeexer" -> {
                "⚡ [BUSHFEEXER OPTIMIZER]\nAnalyzed concept: \"$input\"\n\n" +
                "🔥 Recommended Hook Line:\n\"Stop making this content mistake in 2026. Here's how top 1% creators scale in 30 seconds:\"\n\n" +
                "📊 Predicted Engagement Lift: +38%\n" +
                "🏷️ Hashtags: #VyraCreator #GrowthHacks #ContentStrategy #AI"
            }
            "holokai" -> {
                "🔮 [HOLOKAI CYBER ENGINE]\nProcessing prompt: \"$input\"\n\n" +
                "✨ Custom Fan Reply Variant:\n\"Hey legend! Thanks for fueling the ecosystem with that support. Exclusive VIP drop unlocked for you in your inbox! 💎⚡\"\n\n" +
                "🎭 Sentiment Alignment: 99.2% Superfan Retention"
            }
            "lord_odin" -> {
                "🏛️ [LORD ODIN MONETIZATION]\nEvaluating strategy for: \"$input\"\n\n" +
                "💡 Strategic Action Plan:\n1. Launch a $14.99/mo 'Cyber Catalyst' tier with exclusive weekly Q&A.\n2. Add a $50 Fan Tip Jar reward with custom AI voice shoutouts.\n3. Estimated Monthly Incremental MRR: +$2,850.00"
            }
            else -> "Agent response generated for: $input"
        }
    }
}
