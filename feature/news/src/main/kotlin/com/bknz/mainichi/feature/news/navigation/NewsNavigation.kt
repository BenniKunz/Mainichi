package com.bknz.mainichi.feature.news.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bknz.mainichi.feature.news.NewsEffect
import com.bknz.mainichi.feature.news.NewsScreen
import com.bknz.mainichi.navigation.MainichiDestinationNavigation

object SettingsDestination : MainichiDestinationNavigation {
    override val destination = "news"
    override val route = "news"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.newsGraph(
    onNavigate: (String) -> Unit
) {
    composable(route = SettingsDestination.route) {

        NewsScreen(
            viewModel = hiltViewModel(),
            onNavigate = { effect ->
                when (effect) {
                    NewsEffect.Navigation.NavigateToMenu -> onNavigate("appMenu")
                    NewsEffect.Navigation.NavigateToSettings -> onNavigate("settings")
                }
            })
    }
}