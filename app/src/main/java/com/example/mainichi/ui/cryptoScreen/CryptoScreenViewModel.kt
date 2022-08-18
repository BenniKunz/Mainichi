package com.example.mainichi.ui.cryptoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoScreenViewModel @Inject constructor(
    private val assetRepo: AssetRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CryptoUiState> =
        MutableStateFlow(CryptoUiState.LoadingState)
    val uiState: StateFlow<CryptoUiState> = _uiState.asStateFlow()

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

            _uiState.update {

                CryptoUiState.ContentState(
                    assets = assetRepo.changeFavorites(
                        coin = coin,
                        clicked = clicked)
                )
            }
        }
    }

    init {

        viewModelScope.launch {

            loadData()
        }

        viewModelScope.launch {

            _event.collect { event ->

                when (event) {
                    is CryptoEvent.FavoriteClicked -> changeFavorites(
                        coin = event.coin,
                        clicked = event.setFavorite
                    )
                    is CryptoEvent.CoinClicked -> setEffect { CryptoEffect.NavigateToCoinScreen(coin = event.coin) }

                    is CryptoEvent.UpdateRequested -> {

//                        _uiState.update { CryptoUiState.LoadingState }

                        loadData()
                    }
                }
            }
        }
    }

    private suspend fun loadData() {
        _uiState.update {
            CryptoUiState.ContentState(
                assets = assetRepo.getAssets()
            )
        }
    }
}