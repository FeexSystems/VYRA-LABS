package com.example.vyra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vyra.data.VyraRepository
import com.example.vyra.data.db.VyraDatabase
import com.example.vyra.navigation.VyraNavGraph
import com.example.vyra.theme.VYRATheme
import com.example.vyra.ui.components.CyberpunkBottomNav
import com.example.vyra.ui.components.CyberpunkHeader
import com.example.vyra.ui.components.NavItem
import com.example.vyra.ui.viewmodels.AgentChatViewModel
import com.example.vyra.ui.viewmodels.ContentOptimizerViewModel
import com.example.vyra.ui.viewmodels.DashboardViewModel
import com.example.vyra.ui.viewmodels.FanDnaViewModel
import com.example.vyra.ui.viewmodels.MonetizationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = VyraDatabase.getDatabase(this)
        val repository = VyraRepository(database.vyraDao())

        val dashboardViewModel = DashboardViewModel(repository)
        val agentChatViewModel = AgentChatViewModel(repository)
        val fanDnaViewModel = FanDnaViewModel(repository)
        val optimizerViewModel = ContentOptimizerViewModel(repository)
        val monetizationViewModel = MonetizationViewModel()

        setContent {
            VYRATheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: NavItem.Dashboard.route

                val isVoiceActive = agentChatViewModel.isVoiceActive.value

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CyberpunkHeader(
                            isVoiceActive = isVoiceActive,
                            onVoiceToggle = { agentChatViewModel.toggleVoiceMode() }
                        )
                    },
                    bottomBar = {
                        CyberpunkBottomNav(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    VyraNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        dashboardViewModel = dashboardViewModel,
                        agentChatViewModel = agentChatViewModel,
                        fanDnaViewModel = fanDnaViewModel,
                        optimizerViewModel = optimizerViewModel,
                        monetizationViewModel = monetizationViewModel
                    )
                }
            }
        }
    }
}
