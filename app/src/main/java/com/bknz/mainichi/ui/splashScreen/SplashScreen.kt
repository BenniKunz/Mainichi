package com.bknz.mainichi.ui.splashScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.bknz.mainichi.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    launchScreen: Int
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {

        LaunchedEffect(Unit) {

            delay(2000)
            navController.navigate(route = when (launchScreen) {
                0 -> {
                    "crypto"
                }
                1 -> {
                    "news"
                }
                else -> {
                    "crypto"
                }
            }
            )
        }

        Image(
            painterResource(id = R.drawable.splash),
            contentDescription = "Icon"
        )
    }
}