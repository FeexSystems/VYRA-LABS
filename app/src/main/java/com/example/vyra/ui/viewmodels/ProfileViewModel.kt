package com.example.vyra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.models.AfricanCurrencies
import com.example.vyra.data.models.AfricanCurrency
import com.example.vyra.data.models.AfricanPaymentGatewaysDefaults
import com.example.vyra.data.models.CreatorProfileDetails
import com.example.vyra.data.models.ExternalPlatformsDefaults
import com.example.vyra.data.models.FanProfileDetails
import com.example.vyra.data.models.PaymentGateway
import com.example.vyra.data.models.PaymentTransaction
import com.example.vyra.data.models.PlatformLink
import com.example.vyra.data.models.UserProfile
import com.example.vyra.data.models.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ProfileViewModel(private val repository: VyraRepository) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    val selectedCurrency: StateFlow<AfricanCurrency> = _userProfile.map { profile ->
        AfricanCurrencies.findByCode(profile.selectedCurrencyCode)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AfricanCurrencies.NGN
    )

    init {
        loadPreferencesFromRepository()
    }

    private fun loadPreferencesFromRepository() {
        viewModelScope.launch {
            repository.userPreferences.collect { prefs ->
                val prefMap = prefs.associate { it.key to it.value }
                
                val roleStr = prefMap["user_role"] ?: "CREATOR"
                val role = if (roleStr == "FAN") UserRole.FAN else UserRole.CREATOR
                val currencyCode = prefMap["currency_code"] ?: "NGN"

                val creatorName = prefMap["creator_name"] ?: "Amina Vyra"
                val creatorHandle = prefMap["creator_handle"] ?: "@AminaVyra"
                val creatorBio = prefMap["creator_bio"] ?: "Afrofuturist music producer, 3D visual artist & AI agent builder. Creating encrypted cyberpunk audio-visual experiences."
                val creatorCategory = prefMap["creator_category"] ?: "Afrofuturistic Music & AI Art"
                val primaryPayout = prefMap["primary_payout_gateway"] ?: "paystack"

                val fanName = prefMap["fan_name"] ?: "Amina Vyra (Fan Mode)"
                val fanTag = prefMap["fan_tag"] ?: "#VYRA_VIP_99"

                _userProfile.value = _userProfile.value.copy(
                    activeRole = role,
                    selectedCurrencyCode = currencyCode,
                    creatorDetails = _userProfile.value.creatorDetails.copy(
                        name = creatorName,
                        handle = creatorHandle,
                        bio = creatorBio,
                        category = creatorCategory,
                        primaryPayoutGatewayId = primaryPayout
                    ),
                    fanDetails = _userProfile.value.fanDetails.copy(
                        name = fanName,
                        fanTag = fanTag
                    )
            }
        }

        viewModelScope.launch {
            repository.billingCurrency.collect { billingPref ->
                if (billingPref != null && billingPref.currencyCode.isNotBlank()) {
                    _userProfile.value = _userProfile.value.copy(selectedCurrencyCode = billingPref.currencyCode)
                }
            }
        }
    }

    fun switchRole(role: UserRole) {
        _userProfile.value = _userProfile.value.copy(activeRole = role)
        savePref("user_role", role.name)
    }

    fun toggleRole() {
        val nextRole = if (_userProfile.value.activeRole == UserRole.CREATOR) UserRole.FAN else UserRole.CREATOR
        switchRole(nextRole)
    }

    fun setCurrency(currencyCode: String) {
        _userProfile.value = _userProfile.value.copy(selectedCurrencyCode = currencyCode)
        savePref("currency_code", currencyCode)
        val curr = AfricanCurrencies.findByCode(currencyCode)
        viewModelScope.launch {
            repository.saveBillingCurrencyPreference(curr.code, curr.name, curr.symbol)
        }
    }

    fun updateCreatorProfile(name: String, handle: String, bio: String, category: String, location: String) {
        _userProfile.value = _userProfile.value.copy(
            creatorDetails = _userProfile.value.creatorDetails.copy(
                name = name,
                handle = handle,
                bio = bio,
                category = category,
                location = location
            )
        )
        savePref("creator_name", name)
        savePref("creator_handle", handle)
        savePref("creator_bio", bio)
        savePref("creator_category", category)
    }

    fun updateFanProfile(name: String, handle: String, fanTag: String) {
        _userProfile.value = _userProfile.value.copy(
            fanDetails = _userProfile.value.fanDetails.copy(
                name = name,
                handle = handle,
                fanTag = fanTag
            )
        )
        savePref("fan_name", name)
        savePref("fan_tag", fanTag)
    }

    fun togglePlatformConnection(platformId: String, newHandle: String? = null) {
        val updatedPlatforms = _userProfile.value.platforms.map { platform ->
            if (platform.id == platformId) {
                val nextConnected = !platform.isConnected
                platform.copy(
                    isConnected = nextConnected,
                    handle = newHandle ?: platform.handle,
                    lastSynced = if (nextConnected) "Synced just now" else "Disconnected"
                )
            } else platform
        }
        _userProfile.value = _userProfile.value.copy(platforms = updatedPlatforms)
    }

    fun addSocialHandle(name: String, handle: String) {
        val newPlatform = ExternalPlatform(
            id = name.lowercase().replace(" ", "_"),
            name = name,
            handle = if (handle.startsWith("@")) handle else "@$handle",
            isConnected = true,
            followerCount = "New",
            lastSynced = "Synced just now"
        )
        val updated = _userProfile.value.platforms.toMutableList().apply { add(newPlatform) }
        _userProfile.value = _userProfile.value.copy(platforms = updated)
    }

    fun updateSocialHandle(id: String, newHandle: String) {
        val updated = _userProfile.value.platforms.map { p ->
            if (p.id == id) p.copy(handle = if (newHandle.startsWith("@")) newHandle else "@$newHandle", isConnected = true) else p
        }
        _userProfile.value = _userProfile.value.copy(platforms = updated)
    }

    fun removeSocialHandle(id: String) {
        val updated = _userProfile.value.platforms.filterNot { it.id == id }
        _userProfile.value = _userProfile.value.copy(platforms = updated)
    }

    fun updateGatewayDetails(gatewayId: String, accountIdentifier: String, accountName: String) {
        val updatedGateways = _userProfile.value.gateways.map { gw ->
            if (gw.id == gatewayId) {
                gw.copy(
                    isConnected = true,
                    accountIdentifier = accountIdentifier,
                    accountName = accountName
                )
            } else gw
        }
        _userProfile.value = _userProfile.value.copy(gateways = updatedGateways)
    }

    fun setPrimaryPayoutGateway(gatewayId: String) {
        _userProfile.value = _userProfile.value.copy(
            creatorDetails = _userProfile.value.creatorDetails.copy(
                primaryPayoutGatewayId = gatewayId
            )
        )
        savePref("primary_payout_gateway", gatewayId)
    }

    fun setPreferredFanGateway(gatewayId: String) {
        _userProfile.value = _userProfile.value.copy(
            fanDetails = _userProfile.value.fanDetails.copy(
                preferredPaymentGatewayId = gatewayId
            )
        )
        savePref("preferred_fan_gateway", gatewayId)
    }

    fun executeSimulatedTransaction(
        type: String, // "PAYOUT", "FAN_TIP", "SUBSCRIPTION", "TOKEN_PURCHASE"
        title: String,
        amountUsd: Double,
        gatewayName: String
    ) {
        val randomRef = "${gatewayName.take(3).uppercase()}-${(10000000..99999999).random()}"
        val newTx = PaymentTransaction(
            id = "tx_${UUID.randomUUID().toString().take(8)}",
            title = title,
            type = type,
            amountUsd = amountUsd,
            gatewayName = gatewayName,
            currencyCode = _userProfile.value.selectedCurrencyCode,
            timestamp = "Just now",
            referenceCode = randomRef,
            status = "SUCCESS"
        )

        val currentTxs = _userProfile.value.transactions.toMutableList()
        currentTxs.add(0, newTx)

        val updatedFan = if (type == "FAN_TIP" || type == "TOKEN_PURCHASE") {
            val currentTipped = _userProfile.value.fanDetails.totalTippedUsd
            val currentTokens = _userProfile.value.fanDetails.tokenBalanceUsd
            _userProfile.value.fanDetails.copy(
                totalTippedUsd = currentTipped + amountUsd,
                tokenBalanceUsd = if (type == "TOKEN_PURCHASE") currentTokens + amountUsd else currentTokens,
                fanDnaScore = (_userProfile.value.fanDetails.fanDnaScore + 15).coerceAtMost(1000)
            )
        } else _userProfile.value.fanDetails

        _userProfile.value = _userProfile.value.copy(
            transactions = currentTxs,
            fanDetails = updatedFan
        )
    }

    private fun savePref(key: String, value: String) {
        viewModelScope.launch {
            repository.savePreference(key, value)
        }
    }
}
