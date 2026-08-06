package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.db.AgentPersonality
import com.example.vyra.data.db.ChatMessage
import com.example.vyra.data.db.VoiceInteraction
import com.example.vyra.data.models.AiAgent
import com.example.vyra.data.models.AiAgents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AgentChatViewModel(private val repository: VyraRepository) : ViewModel() {

    private val _selectedAgent = MutableStateFlow<AiAgent>(AiAgents.Bushfeexer)
    val selectedAgent: StateFlow<AiAgent> = _selectedAgent.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isVoiceActive = MutableStateFlow(false)
    val isVoiceActive: StateFlow<Boolean> = _isVoiceActive.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    // Room Database Cached Voice History & Agent Personalities
    val voiceInteractions: StateFlow<List<VoiceInteraction>> = repository.allVoiceInteractions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val agentPersonalities: StateFlow<List<AgentPersonality>> = repository.agentPersonalities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        observeMessagesForAgent(AiAgents.Bushfeexer.id)
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
            // Generate tailored cyberpunk AI response based on agent
            val response = generateAgentResponse(currentAgent, userText)
            repository.sendMessage(
                agentId = currentAgent.id,
                userPrompt = userText,
                agentResponse = response
            )

            // Save Voice Interaction to Room DB if voice mode is active or for voice agent
            if (_isVoiceActive.value || currentAgent.id == "voice_agent") {
                val voiceLog = VoiceInteraction(
                    agentId = currentAgent.id,
                    agentName = currentAgent.name,
                    transcript = userText,
                    agentResponse = response,
                    durationSeconds = (10..25).random(),
                    sentimentScore = 0.96f
                )
                repository.saveVoiceInteraction(voiceLog)
            }

            _isGenerating.value = false
        }
    }

    fun clearVoiceHistory() {
        viewModelScope.launch {
            repository.clearVoiceInteractions()
        }
    }

    fun deleteVoiceInteraction(id: Long) {
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
            repository.saveAgentPersonality(
                AgentPersonality(
                    agentId = agentId,
                    name = name,
                    systemPrompt = systemPrompt,
                    voiceId = voiceId,
                    voiceName = voiceName,
                    speed = speed,
                    pitch = pitch
                )
            )
        }
    }

    private fun generateAgentResponse(agent: AiAgent, input: String): String {
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
            "voice_agent" -> {
                "🎙️ [ELEVENLABS VOICE AI]\nSynthesizing voice audio stream for prompt: \"$input\"\n\n" +
                "🔊 Audio Waveform Rendered. Pitch: Cyber Accent 2.0. Voice synthesis complete! Streaming in real-time."
            }
            else -> "Agent response generated for: $input"
        }
    }
}
