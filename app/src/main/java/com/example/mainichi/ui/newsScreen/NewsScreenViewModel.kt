package com.example.mainichi.ui.newsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.APICrypto
import com.example.mainichi.MainichiApplication
import com.example.mainichi.helper.api.APIConstants
import com.example.mainichi.helper.api.crypto.CryptoAPI
import com.example.mainichi.helper.api.news.Article
import com.example.mainichi.helper.api.news.NewsAPI
import com.example.mainichi.helper.repository.ArticleRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Provides
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val newsAPI : NewsAPI,
    private val articleRepo : ArticleRepository
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
    }
}