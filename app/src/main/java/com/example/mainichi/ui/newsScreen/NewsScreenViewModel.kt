package com.example.mainichi.ui.newsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.api.news.NewsAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val articleRepo: ArticleRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<NewsUiState> = MutableStateFlow(NewsUiState.LoadingState)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<NewsEvent> = MutableSharedFlow()

    fun setEvent(event: NewsEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<NewsEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> NewsEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {
        viewModelScope.launch {

//            val headLines = newsAPI.getTopHeadlines()

            articleRepo.getAllArticles().collect { articleList ->

                _uiState.update {
                    NewsUiState.ContentState(
                        articles = articleList
                    )
                }
            }
        }

        viewModelScope.launch {
            handleEvents()
        }
    }

    private suspend fun handleEvents() {
        _event.collect { newsEvent ->

            when(newsEvent) {
                NewsEvent.NavigateToMenu -> {
                    setEffect { NewsEffect.Navigation.NavigateToMenu }
                }
                NewsEvent.NavigateToSettings -> {
                    setEffect { NewsEffect.Navigation.NavigateToSettings }
                }
            }
        }
    }
}