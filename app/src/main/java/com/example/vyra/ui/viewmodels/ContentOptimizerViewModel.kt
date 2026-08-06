package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.db.ContentPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        val virality = (85..99).random()
        val hashtags = when (platform) {
            "TikTok" -> "#FYP #Cyberpunk #Creator #Viral #VYRA"
            "Instagram" -> "#CreatorEconomy #DigitalArt #Tech #Innovation"
            "YouTube" -> "#Shorts #TechReview #AI #Future"
            else -> "#VyraAI #CreatorLife #Growth #Tech"
        }

        val optimizedText = "⚡ [OPTIMIZED FOR $platform]: $text\n\n🔥 Call to Action: Follow for daily cyber tech insights & exclusive drops! 💎"

        val newPost = ContentPost(
            title = text.take(30) + "...",
            originalText = text,
            optimizedText = optimizedText,
            platform = platform,
            viralityScore = virality,
            hashtags = hashtags,
            status = "Draft"
        )

        _generatedOptimization.value = newPost

        viewModelScope.launch {
            repository.addPost(newPost)
        }
    }

    fun updateStatus(postId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updatePostStatus(postId, newStatus)
        }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch {
            repository.deletePost(postId)
        }
    }
}
