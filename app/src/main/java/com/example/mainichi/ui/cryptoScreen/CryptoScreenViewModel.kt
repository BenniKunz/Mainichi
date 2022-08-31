package com.example.mainichi.ui.cryptoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.ui.entities.Asset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CryptoScreenViewModel @Inject constructor(
    private val assetRepo: AssetRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CryptoUiState.UiState> =
        MutableStateFlow(CryptoUiState.UiState(isLoading = true))
    val uiState: StateFlow<CryptoUiState.UiState> = _uiState.asStateFlow()

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

            observeFavoriteAssets()
        }

        viewModelScope.launch {

            handleEvents()
        }
    }

    var favoriteObserver: Job? = null

    private suspend fun observeFavoriteAssets() {
        assetRepo.observeFavoriteAssets().collect { favoriteAssets ->

            //val assets = assetRepo.getAssets()

            favoriteObserver?.cancel()
            favoriteObserver = viewModelScope.launch {
                assetRepo.observeAssets().collect { assets ->

                    _uiState.update { uiState ->
                        uiState.copy(isLoading = false)
                    }

                    _uiState.update { uiState ->

                        uiState.copy(
                            assets = assets.map { asset ->
                                if (favoriteAssets.find { favorite -> favorite.name == asset.name } != null) {
                                    asset.copy(isFavorite = true)
                                } else {
                                    if (asset.isFavorite) {
                                        asset.copy(isFavorite = false)
                                    } else {
                                        asset
                                    }
                                }
                            },
                            filteredAssets = uiState.filteredAssets.map { filteredAsset ->
                                if (favoriteAssets.find { favorite -> favorite.name == filteredAsset.name } != null) {
                                    filteredAsset.copy(isFavorite = true)
                                } else {
                                    if (filteredAsset.isFavorite) {
                                        filteredAsset.copy(isFavorite = false)
                                    } else {
                                        filteredAsset
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

//    val state2 =  combine(assetRepo.observeFavoriteAssets(), assetRepo.observeAssets()) { favorites, assets ->
//        assets.map { asset ->  UiAsset(favorite = favorites.any { it.name == asset.name } }
//    }.stateIn(viewModelScope, SharingStarted.Lazily, initialValue = emptyList())

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

                        if (event.text.isNotBlank()) {

                            _uiState.update { uiState ->

                                val filteredAssets = mutableListOf<Asset>()
                                for (asset in uiState.assets) {
                                    if (asset.name.lowercase(Locale.getDefault())
                                            .contains(event.text.lowercase(Locale.getDefault()))
                                    ) {
                                        filteredAssets.add(asset)
                                    }
                                }

                                CryptoUiState.UiState(
                                    isLoading = false,
                                    assets = uiState.assets,
                                    filteredAssets = filteredAssets
                                )
                            }
                        } else {

                            _uiState.update { uiState ->

                                CryptoUiState.UiState(
                                    isLoading = false,
                                    assets = uiState.assets,
                                    filteredAssets = emptyList()
                                )
                            }
                        }
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

    private suspend fun updateData() {

        _uiState.update { uiState ->
            uiState.copy(isRefreshing = true)
        }

        delay(2000)

        _uiState.update { uiState ->

            val favorites = uiState.assets.filter { asset -> asset.isFavorite }

            uiState.copy(
                assets = assetRepo.getAssets().map { asset ->
                    if (favorites.find { asset.name == it.name } != null) {
                        asset.copy(isFavorite = true)
                    } else {
                        asset
                    }
                }
            )
        }

        _uiState.update { uiState ->
            uiState.copy(isRefreshing = false)
        }
    }
}