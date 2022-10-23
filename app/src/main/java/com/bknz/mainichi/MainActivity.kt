package com.bknz.mainichi

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bknz.mainichi.core.designsystem.MainichiTheme
import com.bknz.mainichi.core.model.Theme
import com.bknz.mainichi.feature.coin.navigation.coinGraph
import com.bknz.mainichi.feature.crypto.navigation.cryptoGraph
import com.bknz.mainichi.feature.navigation.loginGraph
import com.bknz.mainichi.feature.navigation.menuGraph
import com.bknz.mainichi.feature.news.navigation.newsGraph
import com.bknz.mainichi.feature.notification.navigation.notificationGraph
import com.bknz.mainichi.feature.settings.navigation.settingsGraph
import com.bknz.mainichi.feature.settings.settingsScreen.StartUpViewModel

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

                    Scaffold(
                        modifier = Modifier,
                        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
                        bottomBar = {
                            NavigationBar() {
                                NavigationBarItem(
                                    selected = false,
                                    onClick = {  },
                                    icon = { Icon(Icons.Default.Create, contentDescription = null) }
                                )
                            }
                        }
                    ) { paddingValues ->

                        val navController = rememberNavController()

                        NavHost(
                            navController = navController,
                            startDestination = "login"

                        ) {

//                        composable(route = "splash") {
//                            com.example.mainichi.ui.splashScreen.SplashScreen(
//                                navController = navController,
//                                launchScreen = state.launchScreen
//                            )
//                        }

                            loginGraph(
                                onNavigate = { route ->
                                    navController.navigate(route = route)
                                }
                            )

                            newsGraph(
                                onNavigate = { route ->
                                    navController.navigate(route = route)
                                }
                            )

                            cryptoGraph(
                                onNavigate = { route ->
                                    navController.navigate(route = route)
                                }
                            )

                            coinGraph(
                                onBackClick = {
                                    navController.navigateUp()
                                }
                            )

                            menuGraph(
                                onNavigate = { route ->
                                    navController.navigate(route = route)
                                },
                                onBackClick = {
                                    navController.navigateUp()
                                }
                            )

                            notificationGraph(
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                onNavigate = { route ->
                                    navController.navigate(route)
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