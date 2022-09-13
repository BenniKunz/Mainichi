package com.example.mainichi.ui.coinScreen

import com.example.mainichi.core.model.Asset

sealed class CoinUiState {

    data class UiState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val asset: Asset? = null
    ) : CoinUiState()
}

sealed class CoinEvent {

    data class FavoriteClicked(val coin : String, val clicked : Boolean) : CoinEvent()
    object NavigateUp : CoinEvent()

}

sealed class CoinEffect {

    object NavigateUp : CoinEffect()
}