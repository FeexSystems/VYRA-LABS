package com.example.vyra.ui.screens

import androidx.compose.runtime.Composable
import com.example.vyra.ui.viewmodels.HomeFeedViewModel
import com.example.vyra.ui.viewmodels.MonetizationViewModel
import com.example.vyra.ui.viewmodels.ProfileViewModel
import com.example.vyra.ui.viewmodels.SettingsViewModel

@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    settingsViewModel: SettingsViewModel? = null,
    monetizationViewModel: MonetizationViewModel? = null,
    homeFeedViewModel: HomeFeedViewModel? = null
) {
    ProfileScreen(
        viewModel = viewModel,
        settingsViewModel = settingsViewModel,
        monetizationViewModel = monetizationViewModel,
        homeFeedViewModel = homeFeedViewModel
    )
}
