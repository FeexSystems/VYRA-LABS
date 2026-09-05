package com.example.vyra.webview

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.Uri
import java.io.File

/**
 * Custom WebViewClient for handling navigation and resource loading
 */
class VyraWebViewClient(
    private val context: Context,
    private val onNavigationRequest: (url: String) -> Boolean = { true }
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val url = request?.url?.toString() ?: return false

        // Handle special schemes
        return when {
            url.startsWith("vyra://") -> {
                // Handle custom protocol
                handleCustomProtocol(url)
                true
            }
            url.startsWith("http://") || url.startsWith("https://") -> {
                // Allow web navigation
                onNavigationRequest(url)
            }
            else -> {
                // Block other schemes
                true
            }
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        android.util.Log.d("VyraWebViewClient", "Page finished loading: $url")
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: android.webkit.WebResourceError
    ) {
        super.onReceivedError(view, request, error)
        android.util.Log.e(
            "VyraWebViewClient",
            "Web resource error: ${error.description} for ${request?.url}"
        )
    }

    private fun handleCustomProtocol(url: String) {
        val uri = Uri.parse(url)
        val action = uri.host

        when (action) {
            "navigate" -> {
                val targetRoute = uri.getQueryParameter("route")
                // Handle navigation to native screen
                android.util.Log.d("VyraWebViewClient", "Navigate to: $targetRoute")
            }
            "action" -> {
                val actionType = uri.getQueryParameter("type")
                // Handle native action
                android.util.Log.d("VyraWebViewClient", "Action: $actionType")
            }
            else -> {
                android.util.Log.w("VyraWebViewClient", "Unknown custom protocol: $action")
            }
        }
    }
}
