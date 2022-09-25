package com.example.mainichi.feature.notification.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mainichi.feature.notification.createNotificationScreen.CreateNotificationScreen
import com.example.mainichi.feature.notification.showNotificationScreen.ShowNotificationScreen
import com.example.mainichi.navigation.MainichiDestinationNavigation

object ShowNotificationDestination : MainichiDestinationNavigation {
    override val destination = "showNotification"
    override val route = "notification"
}

object CreateNotificationDestination : MainichiDestinationNavigation {
    override val destination = "createNotification"
    override val route = "createNotification"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.notificationGraph(
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {

    navigation(
        startDestination = ShowNotificationDestination.destination,
        route = ShowNotificationDestination.route
    ) {
        composable(
            route = ShowNotificationDestination.destination
        ) {
            ShowNotificationScreen(
                viewModel = hiltViewModel(),
                onNavigateUp = { onBackClick() },
                onNavigate = { onNavigate("createNotification") })
        }

        composable(
            route = CreateNotificationDestination.route
        ) {
            CreateNotificationScreen(
                viewModel = hiltViewModel(),
                onDismissDialog = {
                    onBackClick()
                })
        }
    }
}