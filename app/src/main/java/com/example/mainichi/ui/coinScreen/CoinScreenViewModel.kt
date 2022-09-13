package com.example.mainichi.ui.coinScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.data.AssetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val assetRepository: AssetRepository
) :
    ViewModel() {

    private val handle: String? = savedStateHandle["coinID"]

    private val _uiState: MutableStateFlow<CoinUiState.UiState> =
        MutableStateFlow(CoinUiState.UiState(isLoading = true))
    val uiState: StateFlow<CoinUiState.UiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<CoinEvent> = MutableSharedFlow()

    fun setEvent(event: CoinEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<CoinEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> CoinEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {
        viewModelScope.launch {

            val asset = assetRepository.getAsset(name = handle ?: "")

            assetRepository.observeFavoriteAssets().collect { favoriteAssets ->

                _uiState.update { uiState ->

                    when (asset) {
                        null -> CoinUiState.UiState(
                            isLoading = false,
                            isError = true,
                            asset = asset
                        )
                        else -> CoinUiState.UiState(
                            isLoading = false,
                            isError = false,
                            asset = if(favoriteAssets.find{ favorite -> favorite.name == asset.name} != null) {
                                asset.copy(isFavorite = true)
                            } else {
                                asset
                            }
                        )
                    }
                }

            }
        }

    viewModelScope.launch {
        _event.collect() { event ->
            when (event) {
                is CoinEvent.FavoriteClicked -> {

                    assetRepository.changeFavorites(event.coin, event.clicked)

                    _uiState.update { uiState ->

                        CoinUiState.UiState(
                            isLoading = false,
                            isError = false,
                            asset = uiState.asset?.copy(isFavorite = event.clicked)
                        )
                    }
                }
                is CoinEvent.NavigateUp -> {
                    setEffect {
                        CoinEffect.NavigateUp
                    }
                }
            }
        }
    }
}
}