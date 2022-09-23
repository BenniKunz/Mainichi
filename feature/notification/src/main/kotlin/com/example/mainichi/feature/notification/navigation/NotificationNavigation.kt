package com.example.mainichi.feature.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mainichi.navigation.MainichiDestinationNavigation

object NotificationDestination : MainichiDestinationNavigation {
    override val destination = "coin/{coinID}"
    override val route = "coin/{coinID}"
}

fun NavGraphBuilder.notificationGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = NotificationDestination.route,
        arguments = listOf(
            navArgument(name = "coinID") {
                type = NavType.StringType
            }
        )
    ) {

    }
}
