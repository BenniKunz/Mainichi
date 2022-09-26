package com.bknz.mainichi.feature.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.bknz.mainichi.feature.appMenu.AppMenu
import com.bknz.mainichi.feature.appMenu.ScreenType
import com.bknz.mainichi.navigation.MainichiDestinationNavigation

object MenuDestination : MainichiDestinationNavigation {
    override val destination = "appMenu"
    override val route = "appMenu"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.menuGraph(
    onNavigate: (String) -> Unit,
    onBackClick: () -> Unit
) {
    dialog(route = MenuDestination.route) {

        AppMenu(
            onItemClick = { screenType ->

                when (screenType) {
                    ScreenType.Crypto -> onNavigate("crypto")
                    ScreenType.News -> onNavigate("news")
                }
            },
            onNavigateUpRequested = { onBackClick() }
        )
    }
}