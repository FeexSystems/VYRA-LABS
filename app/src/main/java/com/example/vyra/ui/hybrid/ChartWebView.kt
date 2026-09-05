package com.example.vyra.ui.hybrid

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * WebView-based chart component using Chart.js
 */
@Composable
fun ChartWebView(
    data: List<Pair<String, Double>>,
    chartType: ChartType = ChartType.Line,
    modifier: Modifier = Modifier,
    accentColor: String = "#00F5FF"
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    
    val chartConfig = generateChartConfig(data, chartType, accentColor)
    
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()
                loadDataWithBaseURL(
                    null,
                    getChartHtml(chartConfig),
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        modifier = modifier,
        update = { view ->
            webView = view
            view.loadDataWithBaseURL(
                null,
                getChartHtml(chartConfig),
                "text/html",
                "UTF-8",
                null
            )
        }
    )
}

enum class ChartType {
    Line,
    Bar,
    Pie,
    Doughnut
}

private fun generateChartConfig(
    data: List<Pair<String, Double>>,
    chartType: ChartType,
    accentColor: String
): String {
    val labels = data.joinToString(",") { "\"${it.first}\"" }
    val values = data.joinToString(",") { it.second.toString() }
    val type = when (chartType) {
        ChartType.Line -> "line"
        ChartType.Bar -> "bar"
        ChartType.Pie -> "pie"
        ChartType.Doughnut -> "doughnut"
    }
    
    return """
        {
            "type": "$type",
            "data": {
                "labels": [$labels],
                "datasets": [{
                    "label": "Data",
                    "data": [$values],
                    "borderColor": "$accentColor",
                    "backgroundColor": "$accentColor",
                    "fill": false
                }]
            },
            "options": {
                "responsive": true,
                "maintainAspectRatio": true,
                "plugins": {
                    "legend": {
                        "display": true,
                        "labels": {
                            "color": "#FFFFFF"
                        }
                    }
                },
                "scales": {
                    "x": {
                        "ticks": {
                            "color": "#A0A0B0"
                        },
                        "grid": {
                            "color": "#2A2A48"
                        }
                    },
                    "y": {
                        "ticks": {
                            "color": "#A0A0B0"
                        },
                        "grid": {
                            "color": "#2A2A48"
                        }
                    }
                }
            }
        }
    """.trimIndent()
}

private fun getChartHtml(chartConfig: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.js"></script>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    background-color: #0A0A12;
                }
                canvas {
                    max-width: 100%;
                    max-height: 100%;
                }
            </style>
        </head>
        <body>
            <canvas id="chart"></canvas>
            <script>
                const config = $chartConfig;
                const ctx = document.getElementById('chart').getContext('2d');
                new Chart(ctx, config);
            </script>
        </body>
        </html>
    """.trimIndent()
}
