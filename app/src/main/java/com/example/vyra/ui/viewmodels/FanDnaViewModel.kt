package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.db.FanProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FanDnaViewModel(private val repository: VyraRepository) : ViewModel() {

    private val _allFans = MutableStateFlow<List<FanProfile>>(emptyList())
    val allFans: StateFlow<List<FanProfile>> = _allFans.asStateFlow()

    private val _selectedTierFilter = MutableStateFlow("All") // "All", "VIP", "Premium", "Standard"
    val selectedTierFilter: StateFlow<String> = _selectedTierFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allFanProfiles.collectLatest { fans ->
                _allFans.value = fans
            }
        }
    }

    fun setFilter(tier: String) {
        _selectedTierFilter.value = tier
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addNewFan(name: String, username: String, tier: String, platform: String, spend: Double) {
        viewModelScope.launch {
            val fan = FanProfile(
                id = "fan_${System.currentTimeMillis()}",
                username = if (username.startsWith("@")) username else "@$username",
                name = name.ifBlank { "Anonymous Fan" },
                tier = tier,
                lifetimeSpend = spend,
                engagementScore = (70..99).random(),
                sentiment = if (spend > 500) "Superfan" else "Positive",
                primaryPlatform = platform,
                lastActive = "Just now"
            )
            repository.addFanProfile(fan)
        }
    }

    fun updateTier(fanId: String, newTier: String) {
        viewModelScope.launch {
            repository.updateFanTier(fanId, newTier)
        }
    }

    fun deleteFan(fanId: String) {
        viewModelScope.launch {
            repository.deleteFanProfile(fanId)
        }
    }
}
