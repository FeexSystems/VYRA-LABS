package com.example.vyra.shared.platform

import android.os.Build

/**
 * Android-specific platform implementation
 */
actual class Platform {
    actual val name: String = "Android"
    actual val model: String = "${Build.MANUFACTURER} ${Build.MODEL}"
    actual val version: String = "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    
    actual fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            platform = "Android",
            name = name,
            model = model,
            version = version,
            bundleId = "" // Android doesn't use bundleId
        )
    }
}
