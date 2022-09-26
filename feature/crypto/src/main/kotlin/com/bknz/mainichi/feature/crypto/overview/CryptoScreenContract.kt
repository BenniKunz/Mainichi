package com.bknz.mainichi.feature.crypto.overview

import com.bknz.mainichi.core.model.Asset

sealed class CryptoUiState {

    data class UiState(
        val isLoading: Boolean,
        val isRefreshing: Boolean = false,
        val assets: List<Asset> = emptyList(),
        val filteredAssets: List<Asset> = emptyList()
    ) : CryptoUiState() {
    }
}

sealed class CryptoEvent {

    data class FavoriteClicked(val coin: String, val setFavorite: Boolean) : CryptoEvent()
    data class CoinClicked(val coin: String) : CryptoEvent()
    data class FilterChanged(val text: String) : CryptoEvent()
    object UpdateRequested : CryptoEvent()
    object NavigateToSettingsScreen : CryptoEvent()
    object NavigateToMenu : CryptoEvent()

}

sealed class CryptoEffect {

    sealed class Navigation() : CryptoEffect() {
        data class NavigateToCoinScreen(val coin : String) : Navigation()
        object NavigateToSettingsScreen : Navigation()
        object NavigateToMenuScreen : Navigation()
    }
}