package com.example.mainichi

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.example.mainichi.ui.newsScreen.NewsScreen
import com.example.mainichi.ui.cryptoScreen.CryptoScreen
import com.example.mainichi.ui.theme.MainichiTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mainichi.helper.worker.TestWorker
import com.example.mainichi.navigationDrawer.*
import com.example.mainichi.ui.appMenu.AppMenu
import com.example.mainichi.ui.coinScreen.CoinScreen
import com.example.mainichi.ui.cryptoScreen.CryptoEffect
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val request = PeriodicWorkRequestBuilder<TestWorker>(
            repeatInterval = 5,
            repeatIntervalTimeUnit = TimeUnit.SECONDS
        ).build()

        val workManager = WorkManager.getInstance(applicationContext).enqueue(request)

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

            Log.d("Shared Preferences: ", sharedPref.getBoolean("isDarkMode", false).toString())

            MainichiTheme(isDarkMode) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val scaffoldState = rememberScaffoldState()
                    var isDetailScreen by remember { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            MainichiAppBar(
                                isDetailScreen = isDetailScreen,
                                onToggleDrawer = {
                                    navController.navigate(route = "AppMenu")
                                },
                                onNavigateUp = {
                                    isDetailScreen = false
                                    navController.navigateUp() })
                        },

                        ) { paddingValues ->

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                        }
//                        Box(modifier = Modifier
//                            .size(200.dp, 150.dp)
//                            .clickable {
//                                scope.launch {
//                                    val result = scaffoldState.snackbarHostState.showSnackbar(
//                                        message = "Hello snackbar",
//                                        actionLabel = "Action"
//                                    )
//
//                                    when (result) {
//                                        SnackbarResult.Dismissed -> Log.d("Test", "Test")
//                                        SnackbarResult.ActionPerformed -> Log.d("Test", "Test")
//                                    }
//                                }
//                            }
//                            .graphicsLayer {
//                                shadowElevation = 8.dp.toPx()
//                                shape = CustomShape(24.dp.toPx())
//                                clip = true
//                            }
//                            .background(Color.Red)) {
//
//                        }

                        NavHost(navController = navController, startDestination = "Splash") {

                            composable(route = "Splash") {
                                com.example.mainichi.ui.splashScreen.SplashScreen(navController = navController)
                            }

                            dialog(route = "AppMenu") {
                                AppMenu(
                                    onItemClick = { screenType ->

                                        when (screenType) {
                                            ScreenType.Crypto -> navController.navigate(route = "Crypto")
                                            ScreenType.News -> navController.navigate(route = "News")
                                        }
                                    },
                                    onNavigateUpRequested = { navController.navigateUp() },
                                    onChangeTheme = { isDarkMode = !isDarkMode },
                                    isDarkMode = isDarkMode
                                )
                            }

                            composable(route = "News") {
                                NewsScreen(
                                    viewModel = hiltViewModel(),
                                    onNavigate = {},
                                    paddingValues = paddingValues
                                )
                            }

                            composable(route = "Crypto") {
                                CryptoScreen(
                                    viewModel = hiltViewModel(),
                                    onNavigate = { effect ->
                                        when (effect) {
                                            is CryptoEffect.NavigateToCoinScreen -> {
                                                isDetailScreen = true
                                                navController.navigate(route = "coin/${effect.coin}")
                                            }
                                        }
                                    },
                                    paddingValues = paddingValues
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
                                    viewModel = hiltViewModel(),
                                    navController = navController,
                                    paddingValues = paddingValues
                                )
                            }
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