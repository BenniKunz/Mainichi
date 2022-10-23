package com.bknz.mainichi.feature.crypto.overview

import androidx.paging.PagingData
import com.bknz.mainichi.core.model.Asset
import kotlinx.coroutines.flow.Flow

sealed class CryptoUiState {

    object Loading : CryptoUiState()

    data class Content(
//        val isRefreshing: Boolean = false,
        val userName: String? = null,
        val pager: Flow<PagingData<Asset>>
    ) : CryptoUiState()
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
        data class NavigateToCoinScreen(val coin: String) : Navigation()
        object NavigateToSettingsScreen : Navigation()
        object NavigateToMenuScreen : Navigation()
    }
}