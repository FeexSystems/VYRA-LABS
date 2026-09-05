package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import com.example.vyra.utils.CacheManager
import com.example.vyra.webview.StateSyncManager

data class SubscriptionTier(
    val id: String,
    val name: String,
    val priceMonthly: Double,
    val subscribersCount: Int,
    val benefits: List<String>,
    val badgeColorHex: Long
)

class MonetizationViewModel(
    private val stateSyncManager: StateSyncManager? = null,
    private val cacheManager: CacheManager? = null
) : ViewModel() {

    private val _vipPrice = MutableStateFlow(19.99f)
    val vipPrice: StateFlow<Float> = _vipPrice.asStateFlow()

    private val _premiumPrice = MutableStateFlow(9.99f)
    val premiumPrice: StateFlow<Float> = _premiumPrice.asStateFlow()

    private val _projectedRevenue = MutableStateFlow(14250.0)
    val projectedRevenue: StateFlow<Double> = _projectedRevenue.asStateFlow()

    private val _tierChartData = MutableStateFlow<List<Pair<String, Double>>>(emptyList())
    val tierChartData: StateFlow<List<Pair<String, Double>>> = _tierChartData.asStateFlow()

    private val _monthlyTrajectoryChartData = MutableStateFlow<List<Pair<String, Double>>>(emptyList())
    val monthlyTrajectoryChartData: StateFlow<List<Pair<String, Double>>> = _monthlyTrajectoryChartData.asStateFlow()

    private val _tiers = MutableStateFlow<List<SubscriptionTier>>(
        listOf(
            SubscriptionTier(
                id = "tier_vip",
                name = "VIP Cyber Elite",
                priceMonthly = 19.99,
                subscribersCount = 185,
                benefits = listOf("Direct AI Voice Chat access", "Exclusive monthly asset drops", "Priority fan message queue"),
                badgeColorHex = 0xFFFF007A
            ),
            SubscriptionTier(
                id = "tier_premium",
                name = "Premium Supporter",
                priceMonthly = 9.99,
                subscribersCount = 420,
                benefits = listOf("Early access content", "Custom Discord role", "Subscriber-only posts"),
                badgeColorHex = 0xFF00F5FF
            ),
            SubscriptionTier(
                id = "tier_standard",
                name = "Standard Fan",
                priceMonthly = 4.99,
                subscribersCount = 815,
                benefits = listOf("Access to public community feed", "Support badge"),
                badgeColorHex = 0xFF8B00FF
            )
        )
    )
    val tiers: StateFlow<List<SubscriptionTier>> = _tiers.asStateFlow()

    init {
        recalculateProjection()
    }

    fun updateVipPrice(newPrice: Float) {
        _vipPrice.value = newPrice
        recalculateProjection()
    }

    fun updatePremiumPrice(newPrice: Float) {
        _premiumPrice.value = newPrice
        recalculateProjection()
    }

    private fun recalculateProjection() {
        val vipSubs = 185
        val premSubs = 420
        val stdSubs = 815
        val vipRev = vipSubs * _vipPrice.value.toDouble()
        val premRev = premSubs * _premiumPrice.value.toDouble()
        val stdRev = stdSubs * 4.99

        val total = vipRev + premRev + stdRev
        _projectedRevenue.value = total

        _tierChartData.value = listOf(
            "VIP Elite" to vipRev,
            "Premium" to premRev,
            "Standard" to stdRev
        )

        _monthlyTrajectoryChartData.value = listOf(
            "Month 1" to total,
            "Month 2" to total * 1.08,
            "Month 3" to total * 1.18,
            "Month 4" to total * 1.30,
            "Month 5" to total * 1.45,
            "Month 6" to total * 1.62
        )

        stateSyncManager?.updateNativeState(
            "monetization_metrics",
            mapOf(
                "vipPrice" to _vipPrice.value,
                "premiumPrice" to _premiumPrice.value,
                "projectedMonthlyRevenue" to total
            )
        )
        cacheManager?.put("monetization_projected_rev", total.toString())
    }
}
