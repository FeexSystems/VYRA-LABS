package com.example.vyra.webview

import android.content.Context
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * Configuration for WebView to enable hybrid native/web functionality
 */
object WebViewConfig {

    fun configureWebView(webView: WebView, context: Context) {
        val settings = webView.settings

        // Enable JavaScript
        settings.javaScriptEnabled = true

        // Enable DOM storage
        settings.domStorageEnabled = true

        // Enable database storage
        settings.databaseEnabled = true

        // Enable file access
        settings.allowFileAccess = true
        settings.allowContentAccess = true

        // Enable zoom controls
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = false

        // Enable responsive design
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true

        // Enable mixed content mode for development
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_MODE_ALLOW

        // Enable caching
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        // Enable smooth scrolling
        settings.setSmoothScrollingEnabled(true)

        // Set default text encoding
        settings.defaultTextEncodingName = "utf-8"

        // Enable geolocation if needed
        settings.setGeolocationEnabled(true)
    }

    fun getWebViewUrl(isDevelopment: Boolean): String {
        return if (isDevelopment) {
            "http://10.0.2.2:3000" // Android emulator localhost
        } else {
            "https://vyra-platform.com" // Production URL
        }
    }
}
