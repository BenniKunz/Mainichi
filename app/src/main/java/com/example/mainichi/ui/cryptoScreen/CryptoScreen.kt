package com.example.mainichi.ui.cryptoScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mainichi.R
import com.example.mainichi.helper.ImageLoader
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.helper.fakeAssets
import com.example.mainichi.ui.cryptoScreen.CryptoUiState.*
import com.example.mainichi.ui.entities.Asset
import com.example.mainichi.ui.theme.MainichiTheme

@Composable
fun CryptoScreen(
    viewModel: CryptoScreenViewModel,
    onNavigate: (CryptoEffect) -> Unit,
) {

    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onNavigate) {

        viewModel.effect.collect {
            when (it) {
                is CryptoEffect.Navigation -> onNavigate(it)
            }
        }
    }

    CryptoScreen(
        state = state,
        onViewModelEvent = { event -> viewModel.setEvent(event) })
}

@Composable
fun CryptoScreen(
    state: UiState,
    onViewModelEvent: (CryptoEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colors.primary
                    )
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary,
                navigationIcon = {

                    IconButton(onClick = {
                        onViewModelEvent(CryptoEvent.NavigateToMenu)
                    }) {

                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open menu",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onViewModelEvent(CryptoEvent.NavigateToSettingsScreen)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "settings",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )
        },

        ) {

        when (state.isLoading) {
            true -> LoadingStateProgressIndicator(
                color = MaterialTheme.colors.onBackground,
                size = 50
            )
            false -> CryptoContent(
                assets = state.assets,
                filteredAssets = state.filteredAssets,
                onViewModelEvent = onViewModelEvent
            )
        }
    }
}

@Composable
fun CryptoContent(
    assets: List<Asset>,
    filteredAssets: List<Asset>,
    onViewModelEvent: (CryptoEvent) -> Unit,
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            var text by rememberSaveable { mutableStateOf("") }

            SearchBar(
                text = text,
                onFilterChanged = onViewModelEvent,
                onTextChanged = { newText -> text = newText }
            )
        }

        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (filteredAssets.isEmpty()) {
                item {
                    CryptoNews(
                        assets = assets
                    )
                }

                item {

                    FavoritesRow(
                        topic = "Your favorites",
                        assets = assets,
                        onViewModelEvent = onViewModelEvent
                    )
                }
            }

            item {
                AllAssets(
                    assets = assets,
                    filteredAssets = filteredAssets,
                    onViewModelEvent = onViewModelEvent
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    text: String,
    onFilterChanged: (CryptoEvent.FilterChanged) -> Unit,
    onTextChanged: (String) -> Unit
) {

    TextField(
        value = text,
        onValueChange = { value ->
            onFilterChanged(CryptoEvent.FilterChanged(value))
            onTextChanged(value)
        },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = MaterialTheme.colors.secondary),
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onBackground,
            cursorColor = MaterialTheme.colors.primary,
            leadingIconColor = if (text.isBlank()) {
                MaterialTheme.colors.primaryVariant
            } else {
                MaterialTheme.colors.primary
            },
            backgroundColor = MaterialTheme.colors.background
        )
    )
}

@Composable
fun AllAssets(
    assets: List<Asset>,
    filteredAssets: List<Asset>,
    onViewModelEvent: (CryptoEvent) -> Unit
) {

    if (filteredAssets.isNotEmpty()) {
        filteredAssets.forEachIndexed { index, coin ->

            AssetRow(onViewModelEvent, coin, index)
        }
    } else {
        assets.forEachIndexed { index, coin ->

            AssetRow(onViewModelEvent, coin, index)
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun AssetRow(
    onViewModelEvent: (CryptoEvent) -> Unit,
    coin: Asset,
    index: Int
) {
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

                NewsCard(coin)
            }
        }
    }
}

@Composable
private fun NewsCard(coin: Asset) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .size(120.dp, 120.dp)
            .background(MaterialTheme.colors.primaryVariant),
        contentAlignment = Alignment.Center

    ) {
        Text(coin.name)
    }
}

@Composable
fun FavoritesRow(
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

        IconButton(onClick = { onViewModelEvent(CryptoEvent.UpdateRequested) }) {
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCryptoRow() {
    MainichiTheme() {
        CryptoItem(coin = fakeAssets.first(), url = "", onViewModelEvent = {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCryptoMain() {
    MainichiTheme(
    ) {

        AllAssets(
            assets = fakeAssets,
            filteredAssets = emptyList(),
            onViewModelEvent = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSearchBar() {
    MainichiTheme(
    ) {
        SearchBar(
            text = "Bitfgc",
            onFilterChanged = { /*TODO*/ },
            onTextChanged = {}
        )
    }
}