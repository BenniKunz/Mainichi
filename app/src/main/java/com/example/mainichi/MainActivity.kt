package com.example.mainichi

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.example.mainichi.ui.newsScreen.NewsScreen
import androidx.navigation.compose.rememberNavController
import com.example.mainichi.navigationDrawer.*
import com.example.mainichi.ui.appMenu.AppMenu
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationScreen
import com.example.mainichi.ui.newsScreen.NewsEffect
import com.example.mainichi.ui.createNotificationScreen.ShowNotificationScreen
import com.example.mainichi.feature.settings.settingsScreen.SettingsContract.UiState.*
import com.example.mainichi.feature.settings.settingsScreen.themeDialog.ThemeDialogContract.UiState.*
import com.example.mainichi.core.designsystem.MainichiTheme
import com.example.mainichi.feature.notification.navigation.notificationGraph
import com.example.mainichi.feature.crypto.navigation.CryptoDestination
import com.example.mainichi.feature.crypto.navigation.cryptoGraph
import com.example.mainichi.feature.settings.navigation.settingsGraph
import com.example.mainichi.feature.settings.settingsScreen.StartUpViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val startUpViewModel: StartUpViewModel = hiltViewModel()

            val state = startUpViewModel.settingsState.collectAsState().value

            MainichiTheme(
                darkTheme = when (state.theme) {
                    Theme.DarkMode -> true
                    Theme.LightMode -> false
                    Theme.SystemSetting -> isSystemInDarkTheme()
                }
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = when (state.launchScreen) {
                            LaunchScreen.Crypto -> {
                                CryptoDestination.route
                            }
                            LaunchScreen.News -> {
                                "news"
                            }
                            else -> {
                                "crypto"
                            }
                        }
                    ) {

//                        composable(route = "splash") {
//                            com.example.mainichi.ui.splashScreen.SplashScreen(
//                                navController = navController,
//                                launchScreen = state.launchScreen
//                            )
//                        }

                        dialog(route = "appMenu") {
                            AppMenu(
                                onItemClick = { screenType ->

                                    when (screenType) {
                                        ScreenType.Crypto -> navController.navigate(route = "crypto")
                                        ScreenType.News -> navController.navigate(route = "news")
                                    }
                                },
                                onNavigateUpRequested = { navController.navigateUp() }
                            )
                        }

                        composable(route = "news") {
                            NewsScreen(
                                viewModel = hiltViewModel(),
                                onNavigate = { effect ->
                                    when (effect) {
                                        is NewsEffect.Navigation.NavigateToSettings -> {
                                            navController.navigate(route = "settings")
                                        }
                                        is NewsEffect.Navigation.NavigateToMenu -> {
                                            navController.navigate(route = "appMenu")
                                        }
                                    }
                                },
                            )
                        }

                        cryptoGraph(
                            onNavigate = { route ->
                                navController.navigate(route)
                            }
                        )

                        notificationGraph(
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )

                        settingsGraph(
                            onNavigate = { route ->
                                navController.navigate(route)
                            },
                            onBackClick = {
                                navController.navigateUp()
                            }
                        )

                        composable(
                            route = "showNotifications"
                        ) {

                            ShowNotificationScreen(
                                viewModel = hiltViewModel(),
                                onNavigate = {
                                    navController.navigate(route = "createNotification")

                                },
                                onNavigateUp = {
                                    navController.navigateUp()
                                })
                        }
3
                        composable(
                            route = "createNotification"
                        ) {

                            CreateNotificationScreen(
                                viewModel = hiltViewModel(),
                                onDismissDialog = {
                                    navController.navigateUp()

                                })
                        }

//                        composable(
//                            route = "settings"
//                        ) {
//                            SettingsScreen(
//                                viewModel = hiltViewModel(),
//                                onNavigate = { navController.navigate("showNotifications") },
//                                onNavigateUp = { navController.navigateUp() }
//                            )
//                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainichiTheme {

    }
}