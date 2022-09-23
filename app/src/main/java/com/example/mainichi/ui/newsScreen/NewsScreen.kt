package com.example.mainichi.ui.newsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mainichi.R
import com.example.mainichi.ui.LoadingStateProgressIndicator
import com.example.mainichi.api.news.Article

@Composable
fun NewsScreen(
    viewModel: NewsScreenViewModel,
    onNavigate: (NewsEffect) -> Unit,
) {

    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onNavigate) {

        viewModel.effect.collect {
            when (it) {
                is NewsEffect.Navigation -> {
                    onNavigate(it)
                }
            }
        }
    }

    NewsScreen(
        state = state,
        onViewModelEvent = { event ->
            viewModel.setEvent(event)
        })
}

@Composable
fun NewsScreen(
    state: NewsUiState,
    onViewModelEvent: (NewsEvent) -> Unit
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
                        onViewModelEvent(NewsEvent.NavigateToMenu)
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
                        onViewModelEvent(NewsEvent.NavigateToSettings)
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
        when (state) {
            is NewsUiState.LoadingState -> LoadingStateProgressIndicator(
                color = MaterialTheme.colors.onBackground,
                size = 50
            )
            is NewsUiState.ContentState ->
                DisplayArticles(
                    articles = state.articles
                )
        }
    }
}

@Composable
fun DisplayArticles(
    articles: List<Article>
) {

    Column() {

        Row(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                text = "Newsfeed",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onSurface
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 0.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colors.background),
        ) {

            articles.forEach { article ->

                item {

                    var cardState by remember { mutableStateOf(false) }
                    NewsCard(
                        article = article,
                        expanded = cardState,
                        onCardClicked = { cardState = !cardState })
                }
            }
        }
    }
}