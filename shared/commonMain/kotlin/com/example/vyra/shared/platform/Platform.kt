package com.example.vyra.shared.platform

/**
 * Platform information and utilities
 */
expect class Platform {
    val name: String
    val model: String
    val version: String
    
    fun getDeviceInfo(): DeviceInfo
}

data class DeviceInfo(
    val platform: String,
    val name: String,
    val model: String,
    val version: String,
    val bundleId: String
)
