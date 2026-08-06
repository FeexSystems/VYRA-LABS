package com.example.vyra.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vyra.theme.CyberBorder
import com.example.vyra.theme.CyberSurface
import com.example.vyra.theme.ElectricMagenta
import com.example.vyra.theme.NeonCyan
import com.example.vyra.theme.TextMuted

sealed class NavItem(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : NavItem("dashboard", "Overview", Icons.Default.Dashboard)
    object Profile : NavItem("profile", "Profile", Icons.Default.AccountCircle)
    object Agents : NavItem("agents", "AI Agents", Icons.Default.Psychology)
    object FanDna : NavItem("fan_dna", "Fan DNA", Icons.Default.People)
    object Optimizer : NavItem("optimizer", "Optimizer", Icons.Default.AutoAwesome)
    object Monetization : NavItem("monetization", "Revenue", Icons.Default.MonetizationOn)
    object Settings : NavItem("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun CyberpunkBottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    val items = listOf(
        NavItem.Dashboard,
        NavItem.Profile,
        NavItem.Agents,
        NavItem.FanDna,
        NavItem.Optimizer,
        NavItem.Monetization,
        NavItem.Settings
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(NeonCyan, ElectricMagenta)),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        containerColor = CyberSurface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    onNavigate(item.route)
                },
                modifier = Modifier.testTag("nav_${item.route}"),
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) NeonCyan else TextMuted
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) NeonCyan else TextMuted
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    unselectedIconColor = TextMuted,
                    indicatorColor = ElectricMagenta.copy(alpha = 0.2f)
                )
            )
        }
    }
}
