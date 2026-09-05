package com.example.vyra.shared.platform

import platform.Foundation.NSBundle

/**
 * iOS-specific platform implementation
 */
actual class Platform {
    actual val name: String = UIDevice.currentDevice.systemName
    actual val model: String = UIDevice.currentDevice.model
    actual val version: String = UIDevice.currentDevice.systemVersion
    
    actual fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            platform = "iOS",
            name = name,
            model = model,
            version = version,
            bundleId = NSBundle.mainBundle.bundleIdentifier ?: "unknown"
        )
    }
}
