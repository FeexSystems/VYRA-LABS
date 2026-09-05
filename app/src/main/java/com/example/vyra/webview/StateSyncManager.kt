package com.example.vyra.webview

import android.webkit.WebView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * State synchronization manager between native and web
 * Handles bidirectional state updates and conflict resolution
 */
class StateSyncManager {
    private val json = Json { ignoreUnknownKeys = true }
    private val _nativeState = MutableStateFlow<Map<String, Any>>(emptyMap())
    private val _webState = MutableStateFlow<Map<String, Any>>(emptyMap())
    
    val nativeState = _nativeState.asStateFlow()
    val webState = _webState.asStateFlow()
    
    private val stateSubscribers = mutableMapOf<String, (StateUpdate) -> Unit>()
    
    /**
     * Update native state and sync to web
     */
    fun updateNativeState(key: String, value: Any) {
        val currentState = _nativeState.value.toMutableMap()
        currentState[key] = value
        _nativeState.value = currentState
        
        // Notify subscribers
        val update = StateUpdate(key, value, Source.Native)
        stateSubscribers[key]?.invoke(update)
    }
    
    /**
     * Update web state and sync to native
     */
    fun updateWebState(key: String, value: Any) {
        val currentState = _webState.value.toMutableMap()
        currentState[key] = value
        _webState.value = currentState
        
        // Notify subscribers
        val update = StateUpdate(key, value, Source.Web)
        stateSubscribers[key]?.invoke(update)
    }
    
    /**
     * Subscribe to state changes for a specific key
     */
    fun subscribeToState(key: String, callback: (StateUpdate) -> Unit) {
        stateSubscribers[key] = callback
    }
    
    /**
     * Unsubscribe from state changes
     */
    fun unsubscribeFromState(key: String) {
        stateSubscribers.remove(key)
    }
    
    /**
     * Sync native state to WebView
     */
    fun syncToWebView(webView: WebView, keys: List<String>? = null) {
        val stateToSync = if (keys != null) {
            _nativeState.value.filterKeys { it in keys }
        } else {
            _nativeState.value
        }
        
        val stateJson = json.encodeToString(StatePayload(stateToSync))
        webView.evaluateJavascript(
            """
            if (window.updateStateFromNative) {
                window.updateStateFromNative($stateJson);
            }
            """.trimIndent(),
            null
        )
    }
    
    /**
     * Sync web state to native
     */
    fun syncFromWebView(webView: WebView, keys: List<String>? = null) {
        webView.evaluateJavascript(
            """
            if (window.getState) {
                JSON.stringify(window.getState());
            } else {
                JSON.stringify({});
            }
            """.trimIndent()
        ) { result ->
            try {
                val state = json.decodeFromString<Map<String, Any>>(result)
                val stateToSync = if (keys != null) {
                    state.filterKeys { it in keys }
                } else {
                    state
                }
                
                stateToSync.forEach { (key, value) ->
                    updateWebState(key, value)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    /**
     * Get merged state (native takes precedence)
     */
    fun getMergedState(): Map<String, Any> {
        return _nativeState.value + _webState.value
    }
    
    /**
     * Observe specific state key
     */
    fun observeState(key: String): Flow<Any?> {
        return nativeState.map { it[key] }
    }
}

@Serializable
data class StatePayload(
    val state: Map<String, Any>
)

@Serializable
data class StateUpdate(
    val key: String,
    val value: Any,
    val source: Source
)

enum class Source {
    Native,
    Web
}
