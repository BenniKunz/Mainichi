package com.bknz.mainichi.feature.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bknz.mainichi.feature.login.LoginScreen
import com.bknz.mainichi.navigation.MainichiDestinationNavigation

object LoginDestination : MainichiDestinationNavigation {
    override val destination = "login"
    override val route = "login"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.loginGraph(
    onNavigate: (String) -> Unit
) {
    composable(route = LoginDestination.route) {

        LoginScreen(viewModel = hiltViewModel())
    }
}