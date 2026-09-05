package com.example.vyra.webview

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject

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
    
    val propsObj = JSONObject()
    propsObj.put("component", componentName)
    propsObj.put("props", JSONObject(props))
    propsObj.put("theme", "cyberpunk")
    val propsJson = propsObj.toString()
    
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
                    const p = profile || {};
                    return (
                        <div style={{ padding: '14px', color: theme.colors.text }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                                <span style={{ fontSize: '11px', fontWeight: 'bold', color: theme.colors.primary, letterSpacing: '1px' }}>
                                    ⚡ REACT COMPONENT WRAPPER
                                </span>
                                <span style={{ fontSize: '10px', background: 'rgba(255, 0, 122, 0.2)', color: '#FF007A', border: '1px solid #FF007A', borderRadius: '4px', padding: '2px 6px', fontWeight: 'bold' }}>
                                    {p.tier || 'VIP'}
                                </span>
                            </div>
                            <div style={{ 
                                background: theme.colors.surface, 
                                padding: '16px',
                                borderRadius: '8px',
                                border: `1px solid ${theme.colors.border}`,
                                display: 'flex',
                                flexDirection: 'column',
                                gap: '8px'
                            }}>
                                <div style={{ fontSize: '16px', fontWeight: 'bold', color: '#FFF' }}>{p.name || 'Superfan'}</div>
                                <div style={{ fontSize: '12px', color: theme.colors.textSecondary }}>Handle: {p.username || '@fan'}</div>
                                <div style={{ display: 'flex', gap: '16px', marginTop: '4px' }}>
                                    <span style={{ fontSize: '11px', color: '#00FF87', fontWeight: 'bold' }}>Spent: ${p.totalSpend || '150.00'}</span>
                                    <span style={{ fontSize: '11px', color: '#00F5FF' }}>Platform: {p.platform || 'X'}</span>
                                </div>
                                <div style={{ display: 'flex', gap: '8px', marginTop: '8px' }}>
                                    <button 
                                        onClick={() => window.ReactBridge && window.ReactBridge.onReactEvent('upgrade_tier', JSON.stringify({ fanId: p.id }))}
                                        style={{
                                            background: theme.colors.secondary,
                                            color: '#FFF',
                                            border: 'none',
                                            padding: '6px 12px',
                                            borderRadius: '4px',
                                            fontWeight: 'bold',
                                            fontSize: '11px',
                                            cursor: 'pointer'
                                        }}>
                                        Cycle Tier
                                    </button>
                                    <button 
                                        onClick={() => window.ReactBridge && window.ReactBridge.onReactEvent('gift_perk', JSON.stringify({ fanId: p.id }))}
                                        style={{
                                            background: 'transparent',
                                            color: theme.colors.primary,
                                            border: `1px solid ${theme.colors.primary}`,
                                            padding: '6px 12px',
                                            borderRadius: '4px',
                                            fontWeight: 'bold',
                                            fontSize: '11px',
                                            cursor: 'pointer'
                                        }}>
                                        Gift VIP Drop
                                    </button>
                                </div>
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
