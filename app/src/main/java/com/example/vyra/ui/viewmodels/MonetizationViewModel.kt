package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SubscriptionTier(
    val id: String,
    val name: String,
    val priceMonthly: Double,
    val subscribersCount: Int,
    val benefits: List<String>,
    val badgeColorHex: Long
)

class MonetizationViewModel : ViewModel() {

    private val _vipPrice = MutableStateFlow(19.99f)
    val vipPrice: StateFlow<Float> = _vipPrice.asStateFlow()

    private val _premiumPrice = MutableStateFlow(9.99f)
    val premiumPrice: StateFlow<Float> = _premiumPrice.asStateFlow()

    private val _projectedRevenue = MutableStateFlow(14250.0)
    val projectedRevenue: StateFlow<Double> = _projectedRevenue.asStateFlow()

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
        _projectedRevenue.value = (vipSubs * _vipPrice.value) + (premSubs * _premiumPrice.value) + (stdSubs * 4.99)
    }
}
