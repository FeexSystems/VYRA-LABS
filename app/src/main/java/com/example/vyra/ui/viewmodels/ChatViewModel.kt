package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.models.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ChatContact(
    val id: String,
    val name: String,
    val handle: String,
    val role: String,
    val isOnline: Boolean,
    val lastMessage: String,
    val lastMessageTime: String,
    val avatarUrl: String = "",
    val isAgent: Boolean = false
)

class ChatViewModel(private val repository: VyraRepository) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _selectedContactId = MutableStateFlow<String>("bushfeexer")
    val selectedContactId: StateFlow<String> = _selectedContactId.asStateFlow()

    private val _contacts = MutableStateFlow<List<ChatContact>>(
        listOf(
            ChatContact("bushfeexer", "Bushfeexer AI", "@bushfeexer", "AI Optimization Agent", true, "Here are the top viral tags for Lagos...", "10:33 AM", isAgent = true),
            ChatContact("holokai", "HoloKai AI", "@holokai", "Cyberpunk Persona Agent", true, "HoloKai is ready to handle VIP voice greetings...", "09:15 AM", isAgent = true),
            ChatContact("lord_odin", "Lord Odin AI", "@lord_odin", "Business Intelligence Agent", true, "Paystack & Flutterwave rates are optimal...", "08:00 AM", isAgent = true),
            ChatContact("fan_kofi", "Kofi Sax", "@kofisax", "Cyber VIP Fan", true, "Hey Amina! Loved the new stems you posted!", "11:05 AM", isAgent = false),
            ChatContact("fan_zuri", "Zuri Cyber", "@zuricyber", "Gold Tier Fan", false, "Can't wait for the live concert stream!", "Yesterday", isAgent = false)
        )
    )
    val contacts: StateFlow<List<ChatContact>> = _contacts.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allMessages.collectLatest { list ->
                _messages.value = list
            }
        }
    }

    fun selectContact(contactId: String) {
        _selectedContactId.value = contactId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        val currentContact = _contacts.value.find { it.id == _selectedContactId.value }
        val isAgent = currentContact?.isAgent ?: false
        repository.sendMessage(_selectedContactId.value, content, isAgent = isAgent)

        // Update contact last message
        _contacts.value = _contacts.value.map { contact ->
            if (contact.id == _selectedContactId.value) {
                contact.copy(lastMessage = content, lastMessageTime = "Just now")
            } else contact
        }
    }
}
