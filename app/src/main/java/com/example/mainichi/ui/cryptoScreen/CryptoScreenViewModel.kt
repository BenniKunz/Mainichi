package com.example.mainichi.ui.cryptoScreen

import android.app.Application
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.mainichi.MainichiApplication
import com.example.mainichi.helper.api.APIConstants
import com.example.mainichi.helper.api.crypto.*
import com.example.mainichi.helper.db.AppDatabase
import com.example.mainichi.helper.db.AssetDao
import com.example.mainichi.helper.db.DbFavoriteAsset
import com.example.mainichi.helper.repository.AssetRepository
import com.example.mainichi.helper.worker.TestWorker
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject

@HiltViewModel
class CryptoScreenViewModel @Inject constructor(
    private val database: AppDatabase,
    private val cryptoAPI: CryptoAPI,
    private val assetRepo: AssetRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<CryptoUiState> = MutableStateFlow(CryptoUiState.LoadingState)
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

    private var assets: List<APIAsset> = emptyList()

    private fun changeFavorites(coin: String, clicked: Boolean) {

        viewModelScope.launch {

            if (clicked) {
                database.favoriteAssetDao().insertFavoriteAsset(DbFavoriteAsset(name = coin))
//                database.assetDao().insertFavoriteAsset(DbFavoriteAsset(name = coin, favorite = true))
                Log.d("Favorites: ", "Inserted favorite($coin) with value: $clicked")
            } else {
                database.favoriteAssetDao().deleteFavoriteAsset(coin)
//                database.assetDao().deleteAsset(asset = DbFavoriteAsset(coin, false))
                Log.d("Favorites: ", "Deleted favorite ($coin")
            }

            val num = database.favoriteAssetDao().getAllFavoriteAssets()
            Log.d("Favorites: ", "Number of favorites: ${num.size}")

            val favoriteAsset = assets.find { coin == it.name }

            assets = assets.map { asset ->
                if(asset == favoriteAsset) {
                    asset.copy(favorite = clicked)
                } else {
                    asset
                }
            }

            _uiState.update { CryptoUiState.ContentState(assets = assets) }

        }
    }

//    private val client = OkHttpClient.Builder().apply {
//            addInterceptor(CryptoInterceptor())
//    }.build()


//    private val repo by lazy {
//        AssetRepository(
//            api = cryptoAPI,
//            dbAsset = database.assetDao(),
//            dbFavoriteAsset = database.favoriteAssetDao()
//        )
//    }

    init {

        viewModelScope.launch {

            assets = cryptoAPI.getAllCryptoAssets()
            Log.d("API called", "Crypto API Called!!!")

            var favorites = assetRepo.getAllFavoriteAssets()
            Log.d("Favorites:", favorites.size.toString())

            favorites.forEach { dbFavoritAsset ->

                val asset = assets.find { dbFavoritAsset.name == it.name }

                if (asset != null) {
                    Log.d("Favorites: ", "Found favorite ${dbFavoritAsset.name}")
                    asset.favorite = true
                }
            }

            database.assetDao().insertAssets(assets.map { it.toDbAsset() })


            _uiState.update { CryptoUiState.ContentState(assets = assets) }
        }

        viewModelScope.launch {

            _event.collect { event ->

                when(event) {
                    is CryptoEvent.FavoriteClicked -> changeFavorites(
                        coin = event.coin,
                        clicked = event.clicked
                    )
                    is CryptoEvent.CoinClicked -> setEffect { CryptoEffect.NavigateToCoinScreen(coin = event.coin) }

                    is CryptoEvent.UpdateRequested -> {

                        _uiState.update { CryptoUiState.LoadingState }

                        assets = cryptoAPI.getAllCryptoAssets()
                        Log.d("API called", "Crypto API Called!!!")

                        var favorites = assetRepo.getAllFavoriteAssets()
                        Log.d("Favorites:", favorites.size.toString())

                        favorites.forEach { dbFavoritAsset ->

                            val asset = assets.find { dbFavoritAsset.name == it.name }

                            if (asset != null) {
                                Log.d("Favorites: ", "Found favorite ${dbFavoritAsset.name}")
                                asset.favorite = true
                            }
                        }

                        database.assetDao().insertAssets(assets.map { it.toDbAsset() })

                        _uiState.update { CryptoUiState.ContentState(assets = assets) }
                    }
                }
            }
        }
    }
}