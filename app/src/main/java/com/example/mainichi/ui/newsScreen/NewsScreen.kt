package bknz.example.mainichi.ui.newsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mainichi.helper.LoadingStateProgressIndicator
import com.example.mainichi.helper.api.news.Article
import kotlinx.coroutines.flow.collect

@Composable
fun NewsScreen(
    viewModel: NewsScreenViewModel,
    onNavigate: (NewsEffect) -> Unit,
    paddingValues: PaddingValues
) {

    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = viewModel, key2 = onNavigate) {

        viewModel.effect.collect {
            when (it) {
                NewsEffect.NavigateToHomeScreen -> TODO()
            }
        }
    }

    NewsScreen(state = state, paddingValues = paddingValues)
}

@Composable
fun NewsScreen(
    state: NewsUiState,
    paddingValues: PaddingValues
) {
    when (state) {
        is NewsUiState.LoadingState -> LoadingStateProgressIndicator(
            color = MaterialTheme.colors.onBackground,
            size = 50
        )
        is NewsUiState.ContentState ->
            DisplayArticles(
                articles = state.articles,
                paddingValues = paddingValues
            )
    }
}

@Composable
fun DisplayArticles(
    articles: List<Article>,
    paddingValues: PaddingValues
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
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(color = MaterialTheme.colors.background),
        ) {

            articles.forEach { article ->

                item {

                    var cardState by remember { mutableStateOf(false) }
                    NewsCard(
                        article = article,
                        expanded = cardState,
                        onCardClicked = { cardState = !cardState})
                }
            }
        }
    }
}