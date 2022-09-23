package com.example.mainichi.feature.crypto.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.mainichi.feature.crypto.overview.CryptoEffect
import com.example.mainichi.feature.crypto.overview.CryptoScreen
import com.example.mainichi.navigation.MainichiDestinationNavigation

object CryptoDestination : MainichiDestinationNavigation {
    override val destination = "crypto"
    override val route = "crypto"
}

fun NavGraphBuilder.cryptoGraph(
    onNavigate : (String) -> Unit
) {
    composable(route = CryptoDestination.route) {
        CryptoScreen(
            viewModel = hiltViewModel(),
            onNavigate = {
                    effect ->
                when (effect) {
                    is CryptoEffect.Navigation.NavigateToCoinScreen -> {
                        onNavigate("coin/${effect.coin}")
//                        navController.navigate(route = "coin/${effect.coin}")
                    }
                    is CryptoEffect.Navigation.NavigateToSettingsScreen -> {
                        onNavigate("settings")
                    }
                    is CryptoEffect.Navigation.NavigateToMenuScreen -> {
//                        navController.navigate(route = "appMenu")
                    }
                }
            }
        )
    }
}
