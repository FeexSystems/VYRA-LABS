package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardMetrics(
    val monthlyRevenue: Double = 12480.00,
    val revenueGrowthPercent: Double = 24.5,
    val totalFans: Int = 1420,
    val vipFans: Int = 185,
    val engagementRate: Double = 8.7,
    val viralityScore: Int = 92,
    val activeVoiceSessions: Int = 38
)

data class RecentActivity(
    val id: String,
    val title: String,
    val agentName: String,
    val timeAgo: String,
    val detail: String
)

class DashboardViewModel(private val repository: VyraRepository) : ViewModel() {

    private val _metrics = MutableStateFlow(DashboardMetrics())
    val metrics: StateFlow<DashboardMetrics> = _metrics.asStateFlow()

    private val _recentActivities = MutableStateFlow<List<RecentActivity>>(emptyList())
    val recentActivities: StateFlow<List<RecentActivity>> = _recentActivities.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
            loadActivities()
        }
    }

    private fun loadActivities() {
        _recentActivities.value = listOf(
            RecentActivity(
                id = "act_1",
                title = "Content Hook Optimized",
                agentName = "Bushfeexer",
                timeAgo = "10m ago",
                detail = "Increased predicted virality score to 94% on X post."
            ),
            RecentActivity(
                id = "act_2",
                title = "VIP Fan Reply Automated",
                agentName = "HoloKai",
                timeAgo = "25m ago",
                detail = "Sent personalized welcome message to @cyber_kaiser."
            ),
            RecentActivity(
                id = "act_3",
                title = "Paywall Strategy Calculated",
                agentName = "Lord Odin",
                timeAgo = "1h ago",
                detail = "Recommended $19.99/mo optimal VIP subscriber pricing."
            ),
            RecentActivity(
                id = "act_4",
                title = "ElevenLabs Voice Stream Active",
                agentName = "Voice Agent",
                timeAgo = "2h ago",
                detail = "Generated 42 audio clips for fan voice mode interactions."
            )
        )
    }
}
