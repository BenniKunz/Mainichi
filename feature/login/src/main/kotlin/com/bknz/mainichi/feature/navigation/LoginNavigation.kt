package com.bknz.mainichi.feature.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bknz.mainichi.feature.login.LoginScreen
import com.bknz.mainichi.feature.signIn.SignInScreen
import com.bknz.mainichi.feature.signUp.SignUpScreen
import com.bknz.mainichi.navigation.MainichiDestinationNavigation

object LoginDestination : MainichiDestinationNavigation {
    override val destination = "loginScreen"
    override val route = "login"
}

object SignInDestination : MainichiDestinationNavigation {
    override val destination = "signIn"
    override val route = "signIn"
}

object SignUpDestination : MainichiDestinationNavigation {
    override val destination = "signUp"
    override val route = "signUp"
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.loginGraph(
    onNavigate: (String) -> Unit
) {

    navigation(
        startDestination = LoginDestination.destination,
        route = LoginDestination.route
    ) {
        composable(route = LoginDestination.destination) {

            LoginScreen(
                viewModel = hiltViewModel(),
                onNavigate = { route ->
                    onNavigate(route)
                })
        }

        composable(route = SignInDestination.route) {
            SignInScreen(viewModel = hiltViewModel(), onNavigate = { route ->
                onNavigate(route)
            })
        }

        composable(route = SignUpDestination.route) {
            SignUpScreen(
                viewModel = hiltViewModel(),
                onNavigate = { route ->
                    onNavigate(route)
                }
            )
        }
    }
}