package com.example.mainichi

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.example.mainichi.ui.newsScreen.NewsScreen
import com.example.mainichi.ui.cryptoScreen.CryptoScreen
import com.example.mainichi.ui.theme.MainichiTheme
import kotlinx.coroutines.launch
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mainichi.customShapes.CustomShape
import com.example.mainichi.customShapes.drawTicketPath
import com.example.mainichi.helper.worker.TestWorker
import com.example.mainichi.navigationDrawer.*
import com.example.mainichi.ui.appMenu.AppMenu
import com.example.mainichi.ui.coinScreen.CoinScreen
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
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
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            MainichiAppBar(
                                onToggleDrawer = {
                                    navController.navigate(route = "AppMenu")
                                })
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
                                            ScreenType.Home -> navController.navigate(route = "Home")
                                            ScreenType.Crypto -> navController.navigate(route = "Crypto")
                                            ScreenType.News -> navController.navigate(route = "News")
                                            ScreenType.Sports -> navController.navigate(route = "Sports")
                                        }
                                    },
                                    onChangeTheme = { isDarkMode = !isDarkMode },
                                    isDarkMode = isDarkMode
                                )
                            }

                            composable(route = "News") {
                                NewsScreen(
                                    viewModel = hiltViewModel(),
                                    navController = navController,
                                    paddingValues = paddingValues
                                )
                            }

                            composable(route = "Crypto") {
                                CryptoScreen(
                                    viewModel = hiltViewModel(),
                                    navController = navController,
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