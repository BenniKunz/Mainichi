package com.example.mainichi.ui.coinScreen

import com.example.mainichi.ui.uiElements.Asset

sealed class CoinUiState {

    object LoadingState : CoinUiState()

    object ErrorState : CoinUiState()

    data class ContentState(
        val asset: Asset
    ) : CoinUiState()
}

sealed class CoinEvent {

//    data class FavoriteClicked(val coin : String, val clicked : Boolean) : CoinEvent()

}

sealed class CoinEffect {

    object NavigateBack : CoinEffect()
}