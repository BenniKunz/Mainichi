package com.example.mainichi.ui.newsScreen

import com.example.mainichi.api.news.Article

sealed class NewsUiState {

    object LoadingState : NewsUiState()

    data class ContentState(val articles: List<Article>) : NewsUiState()
}

sealed class NewsEvent {

    object NavigateToMenu : NewsEvent()
    object NavigateToSettings : NewsEvent()

}

sealed class NewsEffect {

    sealed class Navigation : NewsEffect() {
        object NavigateToMenu : Navigation()
        object NavigateToSettings : Navigation()
    }
}