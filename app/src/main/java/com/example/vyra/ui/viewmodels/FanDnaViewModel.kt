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
}
