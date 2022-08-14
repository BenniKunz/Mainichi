package com.example.mainichi.ui.newsScreen

import com.example.mainichi.helper.api.news.Article

sealed class NewsUiState {

    object LoadingState : NewsUiState()

    data class ContentState(val articles: List<Article>) : NewsUiState()
}

sealed class NewsEvent {

//    data class SpotClicked() : NewsEvent()

}

sealed class NewsEffect {

    object NavigateToHomeScreen : NewsEffect()
}