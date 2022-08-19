package com.example.mainichi.ui.cryptoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.ui.entities.Asset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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

            _uiState.update { uiState ->

                CryptoUiState.UiState(
                    isLoading = false,
                    assets = assetRepo.changeFavorites(
                        coin = coin,
                        clicked = clicked,
                    ),
                    filteredAssets = uiState.filteredAssets.toMutableList().apply {
                        val newFavorite = this.find { it.name == coin }
                        remove(newFavorite)
                        newFavorite?.let { add(newFavorite.copy(isFavorite = clicked)) }
                    }.sortedByDescending { it.marketCap }
                )
            }
        }
    }

    init {

        viewModelScope.launch {

            loadData()
        }

        viewModelScope.launch {

            handleEvents()
        }
    }

    private suspend fun handleEvents() {
        _event.collect { event ->

            when (event) {
                is CryptoEvent.FavoriteClicked -> changeFavorites(
                    coin = event.coin,
                    clicked = event.setFavorite
                )
                is CryptoEvent.CoinClicked -> {

                    setEffect {

                        CryptoEffect.NavigateToCoinScreen(coin = event.coin)

                    }
                }

                is CryptoEvent.UpdateRequested -> {

                    loadData()
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
            }
        }
    }

    private suspend fun loadData() {

        _uiState.update {
            CryptoUiState.UiState(
                isLoading = false,
                assets = assetRepo.getAssets()
            )
        }
    }
}