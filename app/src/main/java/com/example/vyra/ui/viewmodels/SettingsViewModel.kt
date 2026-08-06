package com.example.vyra.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.QuantumViolet
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class VoicePersonality(
    val id: String,
    val name: String,
    val title: String,
    val description: String,
    val elevenLabsVoiceId: String,
    val samplePhrase: String,
    val traits: String,
    val accentColor: Color
)

object VoicePersonalities {
    val Amina = VoicePersonality(
        id = "amina",
        name = "Amina",
        title = "Warm & Engaging",
        description = "Smooth, conversational tone with vibrant, resonant cadence. Perfect for fan community interactions and warm creator responses.",
        elevenLabsVoiceId = "eleven_amina_v2_warm",
        samplePhrase = "Hey there! Welcome to my creator space. I'm Amina, powered by ElevenLabs voice AI. Let's build something amazing together!",
        traits = "Warm • Resonant • Natural",
        accentColor = NeonCyan
    )

    val Vyra = VoicePersonality(
        id = "vyra",
        name = "Vyra",
        title = "Cybernetic Core AI",
        description = "Sleek, futuristic voice with crisp audio pulse synthesis. Tailored for high-tech cyberpunk creator content and real-time streaming.",
        elevenLabsVoiceId = "eleven_vyra_cyber_core",
        samplePhrase = "Initiating ElevenLabs voice protocol. I am Vyra, your cybernetic creator assistant. High-frequency neural synthesis online.",
        traits = "Cybernetic • Confident • Crisp",
        accentColor = ElectricMagenta
    )

    val Kenji = VoicePersonality(
        id = "kenji",
        name = "Kenji",
        title = "Strategic Deep Bass",
        description = "Calm, rich, authoritative voice inspired by cyberpunk strategists. Grounded tones crafted for monetization and business insights.",
        elevenLabsVoiceId = "eleven_kenji_deep_bass",
        samplePhrase = "Greetings, creator. Kenji operational. Analyzing monetizing streams and high-LTV fan engagement paths. Let's review the strategy.",
        traits = "Authoritative • Deep • Strategic",
        accentColor = QuantumViolet
    )

    val all = listOf(Amina, Vyra, Kenji)
}

class SettingsViewModel(private val repository: VyraRepository) : ViewModel() {

    private val _selectedPersonality = MutableStateFlow<VoicePersonality>(VoicePersonalities.Vyra)
    val selectedPersonality: StateFlow<VoicePersonality> = _selectedPersonality.asStateFlow()

    private val _playingPersonalityId = MutableStateFlow<String?>(null)
    val playingPersonalityId: StateFlow<String?> = _playingPersonalityId.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    private val _playbackStatus = MutableStateFlow<String>("")
    val playbackStatus: StateFlow<String> = _playbackStatus.asStateFlow()

    private val _elevenLabsKey = MutableStateFlow("••••••••••••••••3a9b")
    val elevenLabsKey: StateFlow<String> = _elevenLabsKey.asStateFlow()

    private val _voiceModeEnabled = MutableStateFlow(true)
    val voiceModeEnabled: StateFlow<Boolean> = _voiceModeEnabled.asStateFlow()

    private val _creatorName = MutableStateFlow("Kaiser Prime")
    val creatorName: StateFlow<String> = _creatorName.asStateFlow()

    private val _handle = MutableStateFlow("@kaiser_prime")
    val handle: StateFlow<String> = _handle.asStateFlow()

    private val _bio = MutableStateFlow("Cyberpunk digital artist & creator powered by VYRA AI.")
    val bio: StateFlow<String> = _bio.asStateFlow()

    private val _glowEffects = MutableStateFlow(true)
    val glowEffects: StateFlow<Boolean> = _glowEffects.asStateFlow()

    private val _privacyModeEnabled = MutableStateFlow(false)
    val privacyModeEnabled: StateFlow<Boolean> = _privacyModeEnabled.asStateFlow()

    private val _cloudSyncEnabled = MutableStateFlow(true)
    val cloudSyncEnabled: StateFlow<Boolean> = _cloudSyncEnabled.asStateFlow()

    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    private val _selectedBillingCurrency = MutableStateFlow("NGN")
    val selectedBillingCurrency: StateFlow<String> = _selectedBillingCurrency.asStateFlow()

    private var playbackJob: Job? = null

    init {
        viewModelScope.launch {
            repository.billingCurrency.collectLatest { billingPref ->
                if (billingPref != null && billingPref.currencyCode.isNotBlank()) {
                    _selectedBillingCurrency.value = billingPref.currencyCode
                }
            }
        }

        viewModelScope.launch {
            repository.userPreferences.collectLatest { prefs ->
                prefs.forEach { p ->
                    when (p.key) {
                        "creator_name" -> _creatorName.value = p.value
                        "creator_handle" -> _handle.value = p.value
                        "creator_bio" -> _bio.value = p.value
                        "voice_mode_enabled" -> _voiceModeEnabled.value = p.value.toBoolean()
                        "glow_effects_enabled" -> _glowEffects.value = p.value.toBoolean()
                        "onboarding_completed" -> _onboardingCompleted.value = p.value.toBoolean()
                        "selected_billing_currency" -> _selectedBillingCurrency.value = p.value
                        "privacy_mode_enabled" -> {
                            val enabled = p.value.toBoolean()
                            _privacyModeEnabled.value = enabled
                            if (enabled) _cloudSyncEnabled.value = false
                        }
                        "cloud_sync_enabled" -> _cloudSyncEnabled.value = p.value.toBoolean()
                        "selected_personality_id" -> {
                            VoicePersonalities.all.find { it.id == p.value }?.let {
                                _selectedPersonality.value = it
                            }
                        }
                    }
                }
            }
        }
    }

    fun setBillingCurrency(currencyCode: String) {
        _selectedBillingCurrency.value = currencyCode
        viewModelScope.launch {
            repository.saveBillingCurrencyPreference(currencyCode)
        }
    }

    fun selectPersonality(personality: VoicePersonality) {
        _selectedPersonality.value = personality
        savePreferenceToRoom("selected_personality_id", personality.id)
    }

    fun updateCreatorName(name: String) {
        _creatorName.value = name
        savePreferenceToRoom("creator_name", name)
    }

    fun updateHandle(handle: String) {
        _handle.value = handle
        savePreferenceToRoom("creator_handle", handle)
    }

    fun updateBio(bio: String) {
        _bio.value = bio
        savePreferenceToRoom("creator_bio", bio)
    }

    fun updateElevenLabsKey(key: String) {
        _elevenLabsKey.value = key
        savePreferenceToRoom("elevenlabs_key", key)
    }

    fun toggleVoiceMode(enabled: Boolean) {
        _voiceModeEnabled.value = enabled
        savePreferenceToRoom("voice_mode_enabled", enabled.toString())
    }

    fun toggleGlowEffects(enabled: Boolean) {
        _glowEffects.value = enabled
        savePreferenceToRoom("glow_effects_enabled", enabled.toString())
    }

    fun togglePrivacyMode(enabled: Boolean) {
        _privacyModeEnabled.value = enabled
        _cloudSyncEnabled.value = !enabled
        savePreferenceToRoom("privacy_mode_enabled", enabled.toString())
        savePreferenceToRoom("cloud_sync_enabled", (!enabled).toString())

        if (enabled) {
            viewModelScope.launch {
                repository.clearVoiceInteractions()
            }
        }
    }

    fun clearLocalInteractionHistory() {
        viewModelScope.launch {
            repository.clearVoiceInteractions()
        }
    }

    fun setOnboardingCompleted(completed: Boolean) {
        _onboardingCompleted.value = completed
        savePreferenceToRoom("onboarding_completed", completed.toString())
    }

    fun resetOnboarding() {
        _onboardingCompleted.value = false
        savePreferenceToRoom("onboarding_completed", "false")
    }

    private fun savePreferenceToRoom(key: String, value: String) {
        viewModelScope.launch {
            repository.savePreference(key, value)
        }
    }

    fun playSample(personality: VoicePersonality) {
        if (_playingPersonalityId.value == personality.id) {
            stopSample()
            return
        }

        playbackJob?.cancel()
        _playingPersonalityId.value = personality.id
        _playbackProgress.value = 0f
        _playbackStatus.value = "Connecting to ElevenLabs API..."

        playbackJob = viewModelScope.launch {
            try {
                // Phase 1: Synthesizing voice via ElevenLabs API
                _playbackStatus.value = "ElevenLabs API: Synthesizing '${personality.name}' audio..."
                delay(600)

                _playbackStatus.value = "ElevenLabs Stream: Streaming 24kHz PCM Audio (${personality.elevenLabsVoiceId})..."
                delay(400)

                // Phase 2: Playing back audio stream sample (~3.5 sec audio playback simulation)
                val totalSteps = 35
                for (step in 1..totalSteps) {
                    _playbackProgress.value = step.toFloat() / totalSteps
                    val secondsLeft = String.format("%.1f", (totalSteps - step) * 0.1f)
                    _playbackStatus.value = "Playing audio sample for ${personality.name} (${secondsLeft}s)"
                    delay(100)
                }

                _playbackStatus.value = "Playback finished for ${personality.name}."
                delay(800)
            } finally {
                _playingPersonalityId.value = null
                _playbackProgress.value = 0f
                _playbackStatus.value = ""
            }
        }
    }

    fun stopSample() {
        playbackJob?.cancel()
        playbackJob = null
        _playingPersonalityId.value = null
        _playbackProgress.value = 0f
        _playbackStatus.value = ""
    }
}
