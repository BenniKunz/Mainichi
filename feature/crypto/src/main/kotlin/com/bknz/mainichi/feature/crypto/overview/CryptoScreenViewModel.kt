package com.bknz.mainichi.feature.crypto.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.bknz.mainichi.api.crypto.CryptoAPI
import com.bknz.mainichi.feature.crypto.paging.CryptoPagingSource
import com.bknz.mainichi.data.AssetRepository
import com.bknz.mainichi.data.UserRepository
import com.bknz.mainichi.data.database.dao.FavoriteAssetDao
import com.bknz.mainichi.feature.crypto.overview.CryptoUiState.Content
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoScreenViewModel @Inject constructor(
    private val assetRepo: AssetRepository,
    private val userRepository: UserRepository,
    private val cryptoAPI: CryptoAPI,
    private val favoriteAssetDao: FavoriteAssetDao
) : ViewModel() {

    private val filter = MutableStateFlow("")

    val state =
        combine(
            flow = userRepository.userData,
            flow2 = filter
        ) { userData, filter ->

            Content(
                userName = userData.name,
                pager = Pager(
                    config = PagingConfig(
                        pageSize = 50,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = {
                        CryptoPagingSource(
                            cryptoAPI = cryptoAPI,
                            filter = filter.takeUnless { filter.isEmpty() },
                            favoriteAssetDao = favoriteAssetDao
                        )
                    }
                ).flow.cachedIn(scope = viewModelScope)
            )

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = CryptoUiState.Loading
        )

    private val _event: MutableSharedFlow<CryptoEvent> = MutableSharedFlow()

    fun setEvent(event: CryptoEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<CryptoEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> CryptoEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    private fun changeFavorites(coin: String, clicked: Boolean) {

        viewModelScope.launch {

            assetRepo.changeFavorites(
                coin = coin,
                clicked = clicked,
            )
        }
    }

    init {

        viewModelScope.launch {

            handleEvents()
        }
    }

    private suspend fun handleEvents() {
        _event
            .debounce(timeoutMillis = 300)
            .collect { event ->

                when (event) {
                    is CryptoEvent.FavoriteClicked -> changeFavorites(
                        coin = event.coin,
                        clicked = event.setFavorite
                    )
                    is CryptoEvent.CoinClicked -> {

                        setEffect {

                            CryptoEffect.Navigation.NavigateToCoinScreen(coin = event.coin)

                        }
                    }

                    is CryptoEvent.UpdateRequested -> {

                        updateData()
                    }
                    is CryptoEvent.FilterChanged -> {

                        filter(asset = event.text)

                    }
                    CryptoEvent.NavigateToSettingsScreen -> {

                        setEffect {

                            CryptoEffect.Navigation.NavigateToSettingsScreen

                        }
                    }
                    CryptoEvent.NavigateToMenu -> {
                        setEffect {

                            CryptoEffect.Navigation.NavigateToMenuScreen

                        }
                    }
                }
            }
    }

    private fun filter(asset: String) {
        filter.update {
            asset
        }
    }

    private suspend fun updateData() {

//        _uiState.update { uiState ->
//            uiState.copy(isRefreshing = true)
//        }
//
//        delay(2000)
//
//        _uiState.update { uiState ->
//
//            val favorites = uiState.assets.filter { asset -> asset.isFavorite }
//
//            uiState.copy(
//                assets = assetRepo.getAssets().map { asset ->
//                    if (favorites.find { asset.name == it.name } != null) {
//                        asset.copy(isFavorite = true)
//                    } else {
//                        asset
//                    }
//                }
//            )
//        }
//
//        _uiState.update { uiState ->
//            uiState.copy(isRefreshing = false)
//        }
    }
}