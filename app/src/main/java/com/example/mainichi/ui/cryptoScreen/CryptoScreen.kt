package bknz.example.mainichi.ui.cryptoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.ui.cryptoScreen.CryptoUiState.*
import com.example.mainichi.ui.uiElements.Asset

@Composable
fun CryptoScreen(
    viewModel: CryptoScreenViewModel,
    onNavigate: (CryptoEffect) -> Unit,
    paddingValues: PaddingValues
) {

    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onNavigate) {

        viewModel.effect.collect {
            when (it) {
                is CryptoEffect.NavigateToCoinScreen -> onNavigate(it)
            }
        }
    }

    CryptoScreen(
        state = state,
        paddingValues = paddingValues,
        onViewModelEvent = { event -> viewModel.setEvent(event) })
}

@Composable
fun CryptoScreen(
    state: CryptoUiState,
    paddingValues: PaddingValues,
    onViewModelEvent: (CryptoEvent) -> Unit
) {
    when (state) {
        is LoadingState -> LoadingStateProgressIndicator(
            color = MaterialTheme.colors.onBackground,
            size = 50
        )
        is ContentState -> CryptoContent(
            assets = state.assets,
            onViewModelEvent = onViewModelEvent
        )
    }
}

@Composable
fun CryptoContent(
    assets: List<Asset>,
    onViewModelEvent: (CryptoEvent) -> Unit,
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End) {

            IconButton(onClick = { onViewModelEvent(CryptoEvent.UpdateRequested) }) {

                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Update page")
            }
        }

        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                CryptoNews(
                    assets = assets
                )
            }

            item {

                CryptoRow(
                    topic = "Your favorites",
                    assets = assets,
                    onViewModelEvent = onViewModelEvent
                )
            }

            item {
                AllAssets(
                    assets = assets,
                    onViewModelEvent = onViewModelEvent
                )
            }
        }
    }
}

@Composable
fun AllAssets(
    assets: List<Asset>,
    onViewModelEvent: (CryptoEvent) -> Unit
) {

    assets.forEachIndexed { index, coin ->

        Card(
            modifier = Modifier.clickable {
                onViewModelEvent(CryptoEvent.CoinClicked(coin.name))
            },
            elevation = 10.dp
        ) {
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.background)
                    .height(100.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly

            ) {

                Column(
                    modifier = Modifier
                        .weight(0.1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (index + 1).toString(),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primaryVariant
                    )

                    Text(
                        text = coin.symbol.uppercase(),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .size(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val url = coin.image

                    ImageLoader(
                        data = when (url) {
                            null -> ""
                            else -> {
                                url
                            }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = coin.name,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onBackground
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = String.format("%.2f", coin.currentPrice) + " $",
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onBackground
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    IconButton(onClick = {
                        onViewModelEvent(
                            CryptoEvent.FavoriteClicked(
                                coin.name,
                                setFavorite = !coin.isFavorite
                            )
                        )
                    }) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favorite",
                            tint = when {
                                coin.isFavorite -> {
                                    MaterialTheme.colors.primary
                                }
                                else -> {
                                    MaterialTheme.colors.onBackground
                                }
                            }
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun CryptoNews(assets: List<Asset>) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        assets.forEach { coin ->

            item {
                Box(
                    modifier = Modifier
                        .size(280.dp, 200.dp)
                        .padding(10.dp)
                        .background(MaterialTheme.colors.primaryVariant)
                ) {
                    Text(coin.name)
                }
            }
        }
    }
}

@Composable
fun CryptoRow(
    topic: String,
    assets: List<Asset>,
    onViewModelEvent: (CryptoEvent) -> Unit
) {

    Row(
        modifier = Modifier
            .padding(start = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = topic,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onBackground
        )

        IconButton(onClick = { }) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "Favorite",
                tint = MaterialTheme.colors.primary
            )
        }
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        assets.forEach { coin ->

            if (coin.isFavorite) {
                item {

                    CryptoItem(
                        coin = coin,
                        url = coin.image,
                        onViewModelEvent = onViewModelEvent
                    )
                }
            }
        }
    }
}

@Composable
fun CryptoItem(
    coin: Asset,
    url: String?,
    onViewModelEvent: (CryptoEvent) -> Unit
) {

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .size(100.dp, 100.dp)
            .background(MaterialTheme.colors.onBackground)
            .clickable {
                onViewModelEvent(
                    CryptoEvent.CoinClicked(
                        coin = coin.name
                    )
                )
            }
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(50.dp)
                .padding(top = 5.dp)
        ) {

            ImageLoader(
                data = when (url) {
                    null -> ""
                    else -> {
                        url
                    }
                },
                noPictureColor = MaterialTheme.colors.background
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {

            Column(
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = String.format("%.2f", coin.currentPrice) + " $",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
        }
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun PreviewCryptoRow() {
//    MainichiTheme() {
//        CryptoItem(coin = fakeAPIAsset, url = "", onViewModelEvent = {})
//    }
//}
//
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun PreviewCryptoMain() {
//    MainichiTheme(
//    ) {
//
//        AllAssets(
//            assets = listOf(fakeAPIAsset),
//            onViewModelEvent = {}
//        )
//    }
//}