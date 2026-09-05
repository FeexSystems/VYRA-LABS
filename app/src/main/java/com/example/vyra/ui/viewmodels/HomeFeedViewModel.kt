package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.models.AfricanCurrency
import com.example.vyra.data.models.ContentPost
import com.example.vyra.data.models.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

class HomeFeedViewModel(private val repository: VyraRepository) : ViewModel() {

    private val _posts = MutableStateFlow<List<ContentPost>>(emptyList())
    val posts: StateFlow<List<ContentPost>> = _posts.asStateFlow()

    private val _selectedFilter = MutableStateFlow("ALL") // "ALL", "MUSIC", "VIDEOS", "IMAGES", "TEXT"
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allPosts.collectLatest { list ->
                _posts.value = list
            }
        }
    }

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun toggleCast(postId: String) {
        viewModelScope.launch {
            repository.toggleCastPost(postId)
        }
    }

    fun toggleSighted(postId: String) {
        viewModelScope.launch {
            repository.toggleSightedPost(postId)
        }
    }

    fun revyralize(postId: String) {
        viewModelScope.launch {
            repository.incrementRevyralPost(postId)
        }
    }

    fun sharePost(postId: String) {
        viewModelScope.launch {
            repository.incrementSharePost(postId)
        }
    }

    fun createPost(
        title: String,
        content: String,
        mediaUrl: String,
        mediaType: String,
        tags: List<String>,
        authorName: String = "Amina Vyra",
        authorHandle: String = "@AminaVyra",
        authorRole: UserRole = UserRole.CREATOR
    ) {
        val newPost = ContentPost(
            id = "post_${UUID.randomUUID().toString().take(8)}",
            title = title,
            content = content,
            authorName = authorName,
            authorHandle = authorHandle,
            authorRole = authorRole,
            mediaUrl = mediaUrl.ifBlank { "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800" },
            mediaType = mediaType,
            castCount = 1,
            isCasted = true,
            revyralCount = 0,
            commentCount = 0,
            timestamp = "Just now",
            tags = tags.ifEmpty { listOf("VYRA", "Afrofuturism") }
        )
        repository.addPost(newPost)
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            kotlinx.coroutines.delay(1000)
            _isRefreshing.value = false
        }
    }
}
