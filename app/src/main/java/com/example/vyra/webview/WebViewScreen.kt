package com.example.vyra.webview

import android.content.Context
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.vyra.webview.WebViewBridge
import com.example.vyra.webview.WebViewConfig
import com.example.vyra.webview.VyraWebViewClient

/**
 * Compose wrapper for WebView to enable hybrid native/web functionality
 */
@Composable
fun WebViewScreen(
    url: String,
    modifier: Modifier = Modifier,
    isDevelopment: Boolean = true,
    onMessageFromWeb: (WebViewMessage) -> Unit = {},
    onNavigationRequest: (url: String) -> Boolean = { true }
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val webView = remember { WebView(context) }
    
    // Configure WebView on first composition
    LaunchedEffect(webView) {
        WebViewConfig.configureWebView(webView, context)
        webView.webViewClient = VyraWebViewClient(context, onNavigationRequest)
        
        // Add JavaScript bridge
        val bridge = WebViewBridge(onMessageFromWeb)
        webView.addJavascriptInterface(bridge, "AndroidBridge")
        
        // Load URL
        webView.loadUrl(url)
    }
    
    // Update URL when it changes
    LaunchedEffect(url) {
        if (webView.url != url) {
            webView.loadUrl(url)
        }
    }
    
    AndroidView(
        factory = { webView },
        modifier = modifier.fillMaxSize(),
        update = { }
    )
}

/**
 * Send a message from native to web
 */
fun WebView.sendMessageToWeb(message: WebViewMessage) {
    val bridge = WebViewBridge {}
    val messageJson = bridge.sendMessageToWeb(message)
    evaluateJavascript(
        """
        if (window.WebViewBridge && window.WebViewBridge.onMessageFromNative) {
            window.WebViewBridge.onMessageFromNative($messageJson);
        }
        """.trimIndent(),
        null
    )
}
