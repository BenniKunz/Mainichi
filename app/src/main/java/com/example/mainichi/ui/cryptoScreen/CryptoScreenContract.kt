package com.example.mainichi.ui.cryptoScreen

import com.example.mainichi.helper.api.crypto.APIAsset

sealed class CryptoUiState {

    object LoadingState : CryptoUiState()

    data class ContentState(
        val assets: List<APIAsset>
    ) : CryptoUiState()
}

sealed class CryptoEvent {

    data class FavoriteClicked(val coin: String, val clicked: Boolean) : CryptoEvent()
    data class CoinClicked(val coin: String) : CryptoEvent()
    object UpdateRequested : CryptoEvent()

}

sealed class CryptoEffect {

    data class NavigateToCoinScreen(val coin : String) : CryptoEffect()
}