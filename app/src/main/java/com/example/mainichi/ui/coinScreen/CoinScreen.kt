package com.example.mainichi.ui.coinScreen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.helper.api.crypto.APIAsset
import com.example.mainichi.helper.fakeAPIAsset
import com.example.mainichi.ui.cryptoScreen.*
import com.example.mainichi.ui.theme.MainichiTheme
import kotlinx.coroutines.flow.collect

@Composable
fun CoinScreen(
    viewModel: CoinScreenViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {

    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = navController) {

        viewModel.effect.collect {
            when (it) {
                is CoinEffect.NavigateBack -> TODO()
            }
        }
    }

    CoinScreen(
        state = state,
        navController = navController,
        paddingValues = paddingValues,
        onEventFired = { event -> viewModel.setEvent(event) })
}

@Composable
fun CoinScreen(
    state: CoinUiState,
    navController: NavController,
    paddingValues: PaddingValues,
    onEventFired: (CoinEvent) -> Unit
) {
    when (state) {
        is CoinUiState.LoadingState -> LoadingStateProgressIndicator(
            color = MaterialTheme.colors.onBackground,
            size = 50
        )
        is CoinUiState.ContentState -> CoinContent(
            coin = state.coin
        )
    }
}

@Composable
fun CoinContent(coin: APIAsset) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Box(modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colors.primaryVariant)

                ) {
                    
                ImageLoader(data = coin.image)
            }

//            val gradientGreenRed = Brush.horizontalGradient(0f to MaterialTheme.colors.primary, 1000f to MaterialTheme.colors.onBackground)

            Text(
                text = coin.name,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(start = 16.dp)
//                    .background(gradientGreenRed)
                            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCoinContent() {

    MainichiTheme() {

        CoinContent(coin = fakeAPIAsset)

    }
}

