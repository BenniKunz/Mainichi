package com.example.mainichi.ui.coinScreen

import com.example.mainichi.helper.api.crypto.APIAsset

sealed class CoinUiState {

    object LoadingState : CoinUiState()

    object ErrorState : CoinUiState()

    data class ContentState(
        val coin: APIAsset
    ) : CoinUiState()
}

sealed class CoinEvent {

//    data class FavoriteClicked(val coin : String, val clicked : Boolean) : CoinEvent()

}

sealed class CoinEffect {

    object NavigateBack : CoinEffect()
}