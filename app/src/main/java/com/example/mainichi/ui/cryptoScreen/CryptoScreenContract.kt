package com.example.mainichi.ui.cryptoScreen

import com.example.mainichi.ui.uiElements.Asset

sealed class CryptoUiState {

    object LoadingState : CryptoUiState()

    data class ContentState(
        val assets: List<Asset>
    ) : CryptoUiState() {
    }
}

sealed class CryptoEvent {

    data class FavoriteClicked(val coin: String, val setFavorite: Boolean) : CryptoEvent()
    data class CoinClicked(val coin: String) : CryptoEvent()
    object UpdateRequested : CryptoEvent()

}

sealed class CryptoEffect {

    data class NavigateToCoinScreen(val coin : String) : CryptoEffect()
}