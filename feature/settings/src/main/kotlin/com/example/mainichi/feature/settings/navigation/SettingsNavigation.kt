package com.example.mainichi.feature.settings.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.mainichi.feature.settings.settingsScreen.SettingsScreen
import com.example.mainichi.navigation.MainichiDestinationNavigation

object SettingsDestination : MainichiDestinationNavigation {
    override val destination = "settings"
    override val route = "settings"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.settingsGraph(
    onNavigate: (String) -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = SettingsDestination.route) {

        SettingsScreen(
            viewModel = hiltViewModel(),
            onNavigate = {
                onNavigate("showNotification")
            },
            onBackClick = {
                onBackClick()
            }
        )
    }
}