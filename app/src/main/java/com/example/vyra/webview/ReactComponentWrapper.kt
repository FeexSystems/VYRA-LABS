package com.example.vyra.webview

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Native wrapper for React components
 * Allows embedding React components within native Compose UI
 */
@Composable
fun ReactComponentWrapper(
    componentName: String,
    props: Map<String, Any>,
    modifier: Modifier = Modifier,
    onEvent: (String, Any) -> Unit = { _, _ -> }
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    val json = Json { ignoreUnknownKeys = true }
    
    val propsJson = json.encodeToString(
        ReactComponentProps(
            component = componentName,
            props = props,
            theme = "cyberpunk"
        )
    )
    
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = VyraWebViewClient()
                
                // Add JavaScript interface for communication
                addJavascriptInterface(
                    ReactBridge(onEvent),
                    "ReactBridge"
                )
                
                loadDataWithBaseURL(
                    null,
                    getReactComponentHtml(componentName, propsJson),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        modifier = modifier,
        update = { view ->
            webView = view
            // Update props
            view.evaluateJavascript(
                """
                if (window.updateReactProps) {
                    window.updateReactProps($propsJson);
                }
                """.trimIndent(),
                null
            )
        }
    )
}

@Serializable
data class ReactComponentProps(
    val component: String,
    val props: Map<String, Any>,
    val theme: String
)

class ReactBridge(
    private val onEvent: (String, Any) -> Unit
) {
    @android.webkit.JavascriptInterface
    fun onReactEvent(eventName: String, eventData: String) {
        onEvent(eventName, eventData)
    }
}

private fun getReactComponentHtml(componentName: String, propsJson: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
            <script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
            <script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    background-color: #0A0A12;
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                }
                #root {
                    width: 100%;
                    height: 100%;
                }
            </style>
        </head>
        <body>
            <div id="root"></div>
            <script type="text/babel">
                const initialProps = $propsJson;
                
                // Cyberpunk theme
                const theme = {
                    colors: {
                        primary: '#00F5FF',
                        secondary: '#FF007A',
                        accent: '#8B00FF',
                        background: '#0A0A12',
                        surface: '#12121E',
                        border: '#2A2A48',
                        text: '#FFFFFF',
                        textSecondary: '#A0A0B0'
                    }
                };
                
                // Component registry
                const components = {
                    'AnalyticsChart': AnalyticsChart,
                    'FanProfileCard': FanProfileCard,
                    'ContentOptimizer': ContentOptimizer,
                    'VoiceInteractionPanel': VoiceInteractionPanel
                };
                
                // Sample components
                function AnalyticsChart({ data }) {
                    return (
                        <div style={{ padding: '16px', color: theme.colors.text }}>
                            <h3 style={{ color: theme.colors.primary }}>Analytics</h3>
                            <div style={{ 
                                background: theme.colors.surface, 
                                padding: '20px',
                                borderRadius: '8px',
                                border: `1px solid ${theme.colors.border}`
                            }}>
                                <p>Chart visualization: {data?.length || 0} data points</p>
                            </div>
                        </div>
                    );
                }
                
                function FanProfileCard({ profile }) {
                    return (
                        <div style={{ padding: '16px', color: theme.colors.text }}>
                            <h3 style={{ color: theme.colors.secondary }}>Fan Profile</h3>
                            <div style={{ 
                                background: theme.colors.surface, 
                                padding: '20px',
                                borderRadius: '8px',
                                border: `1px solid ${theme.colors.border}`
                            }}>
                                <p>Name: {profile?.name || 'Unknown'}</p>
                                <p>Tier: {profile?.tier || 'N/A'}</p>
                            </div>
                        </div>
                    );
                }
                
                function ContentOptimizer({ content }) {
                    return (
                        <div style={{ padding: '16px', color: theme.colors.text }}>
                            <h3 style={{ color: theme.colors.accent }}>Content Optimizer</h3>
                            <div style={{ 
                                background: theme.colors.surface, 
                                padding: '20px',
                                borderRadius: '8px',
                                border: `1px solid ${theme.colors.border}`
                            }}>
                                <p>Optimizing: {content?.title || 'Untitled'}</p>
                            </div>
                        </div>
                    );
                }
                
                function VoiceInteractionPanel({ agent }) {
                    return (
                        <div style={{ padding: '16px', color: theme.colors.text }}>
                            <h3 style={{ color: theme.colors.primary }}>Voice Interaction</h3>
                            <div style={{ 
                                background: theme.colors.surface, 
                                padding: '20px',
                                borderRadius: '8px',
                                border: `1px solid ${theme.colors.border}`
                            }}>
                                <p>Agent: {agent?.name || 'Unknown'}</p>
                            </div>
                        </div>
                    );
                }
                
                // Render component
                const ComponentToRender = components[initialProps.component] || (() => <div>Component not found</div>);
                
                const root = ReactDOM.createRoot(document.getElementById('root'));
                root.render(<ComponentToRender {...initialProps.props} theme={theme} />);
                
                // Update function
                window.updateReactProps = (newProps) => {
                    root.render(<ComponentToRender {...newProps.props} theme={theme} />);
                };
            </script>
        </body>
        </html>
    """.trimIndent()
}
