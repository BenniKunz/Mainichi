package com.bknz.mainichi.feature.coin.coinScreen

import com.bknz.mainichi.core.model.Asset

internal sealed class CoinUiState {

    data class UiState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val asset: Asset? = null
    ) : CoinUiState()
}

internal sealed class CoinEvent {

    data class FavoriteClicked(val coin : String, val clicked : Boolean) : CoinEvent()
    object NavigateUp : CoinEvent()

}

internal sealed class CoinEffect {

    object NavigateUp : CoinEffect()
}