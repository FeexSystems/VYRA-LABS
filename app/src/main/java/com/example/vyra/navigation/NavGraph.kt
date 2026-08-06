package com.example.vyra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vyra.ui.components.NavItem
import com.example.vyra.ui.screens.AgentsScreen
import com.example.vyra.ui.screens.DashboardScreen
import com.example.vyra.ui.screens.FanDnaScreen
import com.example.vyra.ui.screens.MonetizationScreen
import com.example.vyra.ui.screens.OptimizerScreen
import com.example.vyra.ui.screens.SettingsScreen
import com.example.vyra.ui.viewmodels.AgentChatViewModel
import com.example.vyra.ui.viewmodels.ContentOptimizerViewModel
import com.example.vyra.ui.viewmodels.DashboardViewModel
import com.example.vyra.ui.viewmodels.FanDnaViewModel
import com.example.vyra.ui.viewmodels.MonetizationViewModel
import com.example.vyra.ui.viewmodels.SettingsViewModel

@Composable
fun VyraNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel,
    agentChatViewModel: AgentChatViewModel,
    fanDnaViewModel: FanDnaViewModel,
    optimizerViewModel: ContentOptimizerViewModel,
    monetizationViewModel: MonetizationViewModel,
    settingsViewModel: SettingsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavItem.Dashboard.route,
        modifier = modifier
    ) {
        composable(NavItem.Dashboard.route) {
            DashboardScreen(
                viewModel = dashboardViewModel,
                onNavigateToAgents = { navController.navigate(NavItem.Agents.route) },
                onNavigateToOptimizer = { navController.navigate(NavItem.Optimizer.route) },
                onNavigateToMonetization = { navController.navigate(NavItem.Monetization.route) },
                onNavigateToSettings = { navController.navigate(NavItem.Settings.route) }
            )
        }

        composable(NavItem.Agents.route) {
            AgentsScreen(viewModel = agentChatViewModel)
        }

        composable(NavItem.FanDna.route) {
            FanDnaScreen(viewModel = fanDnaViewModel)
        }

        composable(NavItem.Optimizer.route) {
            OptimizerScreen(viewModel = optimizerViewModel)
        }

        composable(NavItem.Monetization.route) {
            MonetizationScreen(viewModel = monetizationViewModel)
        }

        composable(NavItem.Settings.route) {
            SettingsScreen(viewModel = settingsViewModel)
        }
    }
}
