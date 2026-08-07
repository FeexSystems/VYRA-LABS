package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.models.ContentPost
import com.example.vyra.data.models.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

class ContentOptimizerViewModel(private val repository: VyraRepository) : ViewModel() {

    private val _posts = MutableStateFlow<List<ContentPost>>(emptyList())
    val posts: StateFlow<List<ContentPost>> = _posts.asStateFlow()

    private val _inputDraft = MutableStateFlow("")
    val inputDraft: StateFlow<String> = _inputDraft.asStateFlow()

    private val _selectedPlatform = MutableStateFlow("X") // "X", "TikTok", "Instagram", "YouTube"
    val selectedPlatform: StateFlow<String> = _selectedPlatform.asStateFlow()

    private val _generatedOptimization = MutableStateFlow<ContentPost?>(null)
    val generatedOptimization: StateFlow<ContentPost?> = _generatedOptimization.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allPosts.collectLatest { list ->
                _posts.value = list
            }
        }
    }

    fun updateDraft(text: String) {
        _inputDraft.value = text
    }

    fun selectPlatform(platform: String) {
        _selectedPlatform.value = platform
    }

    fun optimizeContent() {
        val text = _inputDraft.value
        if (text.isBlank()) return

        val platform = _selectedPlatform.value
        val hashtags = when (platform) {
            "TikTok" -> listOf("FYP", "Cyberpunk", "Creator", "Viral", "VYRA")
            "Instagram" -> listOf("CreatorEconomy", "DigitalArt", "Tech", "Innovation")
            "YouTube" -> listOf("Shorts", "TechReview", "AI", "Future")
            else -> listOf("VyraAI", "CreatorLife", "Growth", "Tech")
        }

        val newPost = ContentPost(
            id = "opt_${UUID.randomUUID().toString().take(6)}",
            title = text.take(30) + "...",
            content = "⚡ [OPTIMIZED FOR $platform]: $text\n\n🔥 Call to Action: Follow for daily cyber tech insights & exclusive drops! 💎",
            authorName = "Amina Vyra",
            authorHandle = "@AminaVyra",
            authorRole = UserRole.CREATOR,
            mediaType = "text",
            castCount = 14,
            isCasted = false,
            revyralCount = 2,
            commentCount = 1,
            timestamp = "Just now",
            tags = hashtags
        )

        _generatedOptimization.value = newPost

        viewModelScope.launch {
            repository.addPost(newPost)
        }
    }

    fun updateStatus(postId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updatePostStatus(postId, newStatus)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deletePost(postId)
        }
    }
}
