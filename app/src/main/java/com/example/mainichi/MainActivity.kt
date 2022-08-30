package com.example.mainichi

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.example.mainichi.ui.newsScreen.NewsScreen
import com.example.mainichi.ui.cryptoScreen.CryptoScreen
import com.example.mainichi.ui.theme.MainichiTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mainichi.navigationDrawer.*
import com.example.mainichi.ui.appMenu.AppMenu
import com.example.mainichi.ui.coinScreen.CoinScreen
import com.example.mainichi.ui.cryptoScreen.CryptoEffect
import com.example.mainichi.ui.newsScreen.NewsEffect
import com.example.mainichi.ui.createNotificationScreen.CreateNotificationScreen
import com.example.mainichi.ui.createNotificationScreen.ShowNotificationScreen
import com.example.mainichi.ui.settingsScreen.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val sharedPref = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            var isDarkMode: Boolean by remember {
                mutableStateOf(
                    sharedPref.getBoolean(
                        "isDarkMode",
                        false
                    )
                )
            }

            val editor = sharedPref.edit()
            editor.putBoolean("isDarkMode", isDarkMode)
            editor.apply()

            MainichiTheme(isDarkMode) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "splash") {

                        composable(route = "splash") {
                            com.example.mainichi.ui.splashScreen.SplashScreen(navController = navController)
                        }

                        dialog(route = "appMenu") {
                            AppMenu(
                                onItemClick = { screenType ->

                                    when (screenType) {
                                        ScreenType.Crypto -> navController.navigate(route = "crypto")
                                        ScreenType.News -> navController.navigate(route = "news")
                                    }
                                },
                                onNavigateUpRequested = { navController.navigateUp() },
                                onChangeTheme = { isDarkMode = !isDarkMode },
                                isDarkMode = isDarkMode
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

                        composable(route = "crypto") {
                            CryptoScreen(
                                viewModel = hiltViewModel(),
                                onNavigate = { effect ->
                                    when (effect) {
                                        is CryptoEffect.Navigation.NavigateToCoinScreen -> {
                                            navController.navigate(route = "coin/${effect.coin}")
                                        }
                                        is CryptoEffect.Navigation.NavigateToSettingsScreen -> {
                                            navController.navigate(route = "settings")
                                        }
                                        is CryptoEffect.Navigation.NavigateToMenuScreen -> {
                                            navController.navigate(route = "appMenu")
                                        }
                                    }
                                }
                            )
                        }

                        composable(
                            route = "coin/{coinID}",
                            arguments = listOf(
                                navArgument(name = "coinID") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            CoinScreen(
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                viewModel = hiltViewModel(),
                            )
                        }

                        composable(
                            route = "showNotifications"
                        ) {

                            ShowNotificationScreen(
                                viewModel = hiltViewModel(),
                                onNavigate = {
//                                    navController.navigate(route = "createNotification")

                                },
                                onNavigateUp = {
                                    navController.navigateUp()
                                })
                        }

//                        dialog(
//                            route = "createNotification"
//                        ) {
//                            CreateNotificationScreen(
//                                viewModel = hiltViewModel(),
//                                onNavigateUp = { navController.navigateUp() },
//                            )
//                        }

                        composable(
                            route = "settings"
                        ) {
                            SettingsScreen(
                                viewModel = hiltViewModel(),
                                onNavigate = { navController.navigate("showNotifications") },
                                onNavigateUp = { navController.navigateUp() }
                            )
                        }
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