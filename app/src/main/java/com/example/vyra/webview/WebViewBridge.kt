package com.example.vyra.webview

import android.webkit.JavascriptInterface
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * JavaScript interface for bidirectional communication between native Android and web content
 */
class WebViewBridge(
    private val onMessageFromWeb: (WebViewMessage) -> Unit
) {

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
    }

    /**
     * Called from JavaScript to send messages to native Android
     */
    @JavascriptInterface
    fun postMessage(messageJson: String) {
        try {
            val message = json.decodeFromString<WebViewMessage>(messageJson)
            onMessageFromWeb(message)
        } catch (e: Exception) {
            android.util.Log.e("WebViewBridge", "Failed to parse message: $messageJson", e)
        }
    }

    /**
     * Send a message from native Android to JavaScript
     */
    fun sendMessageToWeb(message: WebViewMessage): String {
        return json.encodeToString(message)
    }
}

@Serializable
data class WebViewMessage(
    val type: String,
    val payload: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class NativeToWebMessage(
    val type: String,
    val payload: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
) : WebViewMessage(type, payload, timestamp)

@Serializable
data class WebToNativeMessage(
    val type: String,
    val payload: Map<String, Any> = emptyMap(),
    val timestamp: Long = System.currentTimeMillis()
) : WebViewMessage(type, payload, timestamp)
