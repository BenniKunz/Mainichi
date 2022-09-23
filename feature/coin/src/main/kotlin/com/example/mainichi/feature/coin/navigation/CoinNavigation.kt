package com.example.mainichi.feature.coin.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mainichi.navigation.MainichiDestinationNavigation
import com.example.mainichi.feature.coin.coinScreen.CoinScreen

object CoinDestination : MainichiDestinationNavigation {
    override val destination = "coin/{coinID}"
    override val route = "coin/{coinID}"
}

fun NavGraphBuilder.coinGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = CoinDestination.route,
        arguments = listOf(
            navArgument(name = "coinID") {
                type = NavType.StringType
            }
        )
    ) {
        CoinScreen(
            onNavigateUp = {
                onBackClick()
            },
            viewModel = hiltViewModel(),
        )
    }
}
