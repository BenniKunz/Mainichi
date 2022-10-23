package com.bknz.mainichi.feature.crypto.overview

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.bknz.mainichi.core.model.Asset
import com.bknz.mainichi.feature.crypto.fakeAssets
import com.bknz.mainichi.feature.crypto.overview.CryptoUiState.*
import com.bknz.mainichi.core.designsystem.MainichiTheme
import com.bknz.mainichi.feature.crypto.R
import com.bknz.mainichi.ui.ImageLoader
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
internal fun CryptoScreen(
    viewModel: CryptoScreenViewModel,
    onNavigate: (CryptoEffect) -> Unit,
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onNavigate) {

        viewModel.effect.collect {
            when (it) {
                is CryptoEffect.Navigation -> onNavigate(it)
            }
        }
    }

    when (state) {
        is Content -> {
            CryptoScreen(
                state = state,
                assets = state.pager.collectAsLazyPagingItems(),
                onViewModelEvent = { event -> viewModel.setEvent(event) })

        }
        Loading -> CircularProgressIndicator()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun CryptoScreen(
    state: Content,
    assets: LazyPagingItems<Asset>,
    onViewModelEvent: (CryptoEvent) -> Unit
) {

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name) + " Hello ${state.userName}",
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { scope.launch { listState.scrollToItem(0) } },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.padding(16.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.MoveUp,
                    contentDescription = null
                )
            }
        }

    ) {

        CryptoContent(
            assets = assets,
            listState = listState,
            isRefreshing = false,
            onViewModelEvent = onViewModelEvent
        )
    }
}

@Composable
internal fun CryptoContent(
    assets: LazyPagingItems<Asset>,
    listState: LazyListState,
    isRefreshing: Boolean,
    onViewModelEvent: (CryptoEvent) -> Unit,
) {
    val favoriteAssets = assets.itemSnapshotList.items.filter { asset ->
        asset.isFavorite.collectAsState(
            initial = false
        ).value
    }

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

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
//                onViewModelEvent(CryptoEvent.UpdateRequested)
            },
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                item {

                    CryptoNews(
                        assets = favoriteAssets
                    )
                }

                item {

                    FavoritesRow(
                        topic = "Your favorites",
                        assets = favoriteAssets,
                        onViewModelEvent = onViewModelEvent
                    )
                }

                Log.d("Paging", "Size: ${assets.itemCount}")
                itemsIndexed(assets) { index, coin ->
                    if (coin != null) {
                        AssetRow(onViewModelEvent, coin, index)
                    }
                }

                item {
                    if (assets.loadState.append == LoadState.Loading) {
                        CircularProgressIndicator()
                    }
                }

                item { Spacer(modifier = Modifier.height(10.dp)) }
            }
        }
    }
}

@Composable
internal fun SearchBar(
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
private fun AssetRow(
    onViewModelEvent: (CryptoEvent) -> Unit,
    coin: Asset,
    index: Int
) {
    Card(
        modifier = Modifier.clickable {
            onViewModelEvent(CryptoEvent.CoinClicked(coin.id))
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

                val isFavorite = coin.isFavorite.collectAsState(initial = false).value
                IconButton(onClick = {
                    onViewModelEvent(
                        CryptoEvent.FavoriteClicked(
                            coin.name,
                            setFavorite = !isFavorite
                        )
                    )
                }) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        tint = when (isFavorite) {
                            true -> {
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
private fun CryptoNews(assets: List<Asset>) {

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
private fun FavoritesRow(
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

        IconButton(onClick = {
//            onViewModelEvent(CryptoEvent.UpdateRequested)
        }) {
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

@Composable
private fun CryptoItem(
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
                        coin = coin.id
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
private fun PreviewCryptoRow() {
    MainichiTheme() {
        CryptoItem(coin = fakeAssets.first(), url = "", onViewModelEvent = {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCryptoMain() {
    MainichiTheme(
    ) {

//        AllAssets(
//            assets = fakeAssets,
//            filteredAssets = emptyList(),
//            onViewModelEvent = {}
//        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewSearchBar() {
    MainichiTheme(
    ) {
        SearchBar(
            text = "Bitfgc",
            onFilterChanged = { /*TODO*/ },
            onTextChanged = {}
        )
    }
}