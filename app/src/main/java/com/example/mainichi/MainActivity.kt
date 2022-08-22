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
import androidx.work.*
import com.example.mainichi.worker.PriceChangeWorker
import com.example.mainichi.navigationDrawer.*
import com.example.mainichi.ui.appMenu.AppMenu
import com.example.mainichi.ui.coinScreen.CoinScreen
import com.example.mainichi.ui.cryptoScreen.CryptoEffect
import com.example.mainichi.ui.settingsScreen.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val priceChangeWorker = initializeDelayedPeriodicWorker<PriceChangeWorker>(
            workerStartingDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
            workerStartingHour = 12L,
            workerStartingMinute = 52L,
            tag = "Price_Change",
            repeatInterval = 6,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "Price_Change",
            ExistingPeriodicWorkPolicy.REPLACE,
            priceChangeWorker
        )

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
                                    navController.navigateUp()
                                },
                                onNavigateToSettings = { navController.navigate(route = "Settings") {
                                    launchSingleTop = true
                                } })
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
                                    paddingValues = paddingValues
                                )
                            }

                            composable(
                                route = "Settings"
                            ) {
                                SettingsScreen(
                                    viewModel = hiltViewModel(),
                                    onNavigateUpRequested = {navController.navigateUp()}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private inline fun <reified T : CoroutineWorker> initializeDelayedPeriodicWorker(
    workerStartingHour: Long,
    workerStartingMinute: Long,
    workerStartingDay: Int,
    tag: String,
    repeatInterval: Long,
    repeatIntervalTimeUnit: TimeUnit
): PeriodicWorkRequest {
    val workerDelay: Duration = calculateDurationUntilStart(
        workerStartingHour = workerStartingHour,
        workerStartingMinute = workerStartingMinute,
        workerStartingDay = workerStartingDay
    )

    Log.d("Notification Test", workerDelay.toMillis().toString())

    return PeriodicWorkRequestBuilder<T>(
        repeatInterval = repeatInterval,
        repeatIntervalTimeUnit = repeatIntervalTimeUnit
    )
        .setInitialDelay(workerDelay.toMillis(), TimeUnit.MILLISECONDS)
        .setBackoffCriteria(
            BackoffPolicy.LINEAR,
            PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS
        )
        .addTag(tag)
        .build()
}

@RequiresApi(Build.VERSION_CODES.O)
private fun calculateDurationUntilStart(
    workerStartingHour: Long,
    workerStartingMinute: Long,
    workerStartingDay: Int = Calendar.TUESDAY
): Duration {
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    val currentDateTime = LocalDateTime.now()

    var dueDay = workerStartingDay

    val dayDifference = dueDay - currentDay

    var delayInDays = 0L

    when {
        dayDifference < 0 -> {
            delayInDays += Calendar.SATURDAY + dayDifference
        }
        dayDifference == 0 &&
                workerStartingHour < currentDateTime.hour -> {
            delayInDays += Calendar.SATURDAY
        }
        dayDifference == 0 &&
                workerStartingHour.toInt() == currentDateTime.hour &&
                workerStartingMinute <= currentDateTime.minute -> {
            delayInDays += Calendar.SATURDAY
        }
        else -> {
            delayInDays += dayDifference
        }
    }

    val firstExecutionDate: LocalDateTime = currentDateTime
        .plusDays(delayInDays)
        .plusHours(workerStartingHour - currentDateTime.hour)
        .plusMinutes(workerStartingMinute - currentDateTime.minute)
        .plusSeconds(0L - currentDateTime.second)

    return Duration.between(currentDateTime, firstExecutionDate)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainichiTheme {

    }
}