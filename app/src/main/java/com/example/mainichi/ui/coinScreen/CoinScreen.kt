package com.example.mainichi.ui.coinScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.theme.MainichiTheme
import com.example.mainichi.ui.entities.Asset

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
        is CoinUiState.ContentState ->
            CoinContent(
                coin = state.asset
            )
        CoinUiState.ErrorState -> TODO()
    }
}

@Composable
fun CoinContent(coin: Asset) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White.copy(alpha = 0.0f))

            ) {

                ImageLoader(data = coin.image)
            }

//            val gradientGreenRed = Brush.horizontalGradient(0f to MaterialTheme.colors.primary, 1000f to MaterialTheme.colors.onBackground)

            Text(
                text = "${coin.name}",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .padding(start = 16.dp, end = 40.dp)
//                    .background(gradientGreenRed)
            )

            Text(
                text = "$ ${coin.currentPrice}",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h4,
            )
        }

        Divider(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            color = MaterialTheme.colors.primaryVariant
        )

        var textState by remember { mutableStateOf("NOTHING") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colors.background),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CoinDataCard(
                    title = "Market Cap",
                    text = coin.marketCap.toString(),
                    onStateChange = { text -> textState = text })

                CoinDataCard(
                    title = "All Time High",
                    text = coin.ath.toString(),
                    onStateChange = { text -> textState = text })

                CoinDataCard(
                    title = "All Time Low",
                    text = coin.atl.toString(),
                    onStateChange = { text -> textState = text })

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CoinDataCard(
                    title = "24h high",
                    text = coin.high24h.toString(),
                    onStateChange = { text -> textState = text })

                CoinDataCard(
                    title = "24h low",
                    text = coin.low24h.toString(),
                    onStateChange = { text -> textState = text })

                CoinDataCard(
                    title = "Current Price",
                    text = coin.currentPrice.toString(),
                    onStateChange = { text -> textState = text })
            }
        }
        Text("Hello Angelo, here you go, information about: ${textState}")
    }
}

@Composable
fun CoinDataCard(title: String, text: String, onStateChange: (String) -> Unit) {

    var colorState by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                2.dp, if (colorState) {
                    MaterialTheme.colors.onPrimary
                } else {
                    MaterialTheme.colors.background
                }, RoundedCornerShape(16.dp)
            )
            .clickable {
                colorState = !colorState
                onStateChange(title)
            },
        elevation = 16.dp,
        backgroundColor = MaterialTheme.colors.background

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.caption,
                color = Color.White,
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCoinContent() {

    MainichiTheme() {

        CoinContent(
            coin = Asset(
                name = "Bitcoin",
                currentPrice = 20000.0,
                image = "",
                symbol = "BTC",
                isFavorite = true,
                high24h = 21000.5,
                low24h = 19000.0,
                marketCap = 3000000,
                ath = 60000.0,
                atl = 0.5
            )
        )

    }
}

