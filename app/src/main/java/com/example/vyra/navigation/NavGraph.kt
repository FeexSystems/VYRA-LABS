package com.example.vyra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vyra.ui.components.NavItem
import com.example.vyra.ui.screens.AgentsScreen
import com.example.vyra.ui.screens.ChatScreen
import com.example.vyra.ui.screens.DashboardScreen
import com.example.vyra.ui.screens.FanDnaScreen
import com.example.vyra.ui.screens.HomefeedScreen
import com.example.vyra.ui.screens.MonetizationScreen
import com.example.vyra.ui.screens.OptimizerScreen
import com.example.vyra.ui.screens.ProfileScreen
import com.example.vyra.ui.screens.SettingsScreen
import com.example.vyra.ui.screens.UserProfileScreen
import com.example.vyra.ui.screens.VyraShowScreen
import com.example.vyra.ui.viewmodels.AgentChatViewModel
import com.example.vyra.ui.viewmodels.ChatViewModel
import com.example.vyra.ui.viewmodels.ContentOptimizerViewModel
import com.example.vyra.ui.viewmodels.DashboardViewModel
import com.example.vyra.ui.viewmodels.FanDnaViewModel
import com.example.vyra.ui.viewmodels.HomeFeedViewModel
import com.example.vyra.ui.viewmodels.MonetizationViewModel
import com.example.vyra.ui.viewmodels.ProfileViewModel
import com.example.vyra.ui.viewmodels.SettingsViewModel

@Composable
fun VyraNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel,
    profileViewModel: ProfileViewModel,
    agentChatViewModel: AgentChatViewModel,
    fanDnaViewModel: FanDnaViewModel,
    optimizerViewModel: ContentOptimizerViewModel,
    monetizationViewModel: MonetizationViewModel,
    settingsViewModel: SettingsViewModel,
    homeFeedViewModel: HomeFeedViewModel,
    chatViewModel: ChatViewModel
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

        composable(NavItem.Homefeed.route) {
            HomefeedScreen(
                viewModel = homeFeedViewModel,
                monetizationViewModel = monetizationViewModel
            )
        }

        composable(NavItem.VyraShow.route) {
            VyraShowScreen(viewModel = homeFeedViewModel)
        }

        composable(NavItem.Chat.route) {
            ChatScreen(viewModel = chatViewModel)
        }

        composable(NavItem.Profile.route) {
            UserProfileScreen(
                viewModel = profileViewModel,
                settingsViewModel = settingsViewModel,
                monetizationViewModel = monetizationViewModel,
                homeFeedViewModel = homeFeedViewModel
            )
        }

        composable(NavItem.Agents.route) {
            AgentsScreen(viewModel = agentChatViewModel)
        }

        composable(NavItem.FanDna.route) {
            FanDnaScreen(
                viewModel = fanDnaViewModel,
                optimizerViewModel = optimizerViewModel
            )
        }

        composable(NavItem.Optimizer.route) {
            OptimizerScreen(viewModel = optimizerViewModel)
        }

        composable(NavItem.Monetization.route) {
            MonetizationScreen(
                viewModel = monetizationViewModel,
                profileViewModel = profileViewModel
            )
        }

        composable(NavItem.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                profileViewModel = profileViewModel
            )
        }
    }
}
