package com.example.mainichi.ui.cryptoScreen

import android.util.Log
import com.example.mainichi.helper.api.crypto.CryptoAPI
import com.example.mainichi.helper.api.crypto.asAsset
import com.example.mainichi.helper.db.AppDatabase
import com.example.mainichi.helper.db.DbFavoriteAsset
import com.example.mainichi.ui.uiElements.Asset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepository @Inject constructor(
    private val cryptoAPI: CryptoAPI,
    private val database: AppDatabase
) {
    private val repositoryScope = CoroutineScope(context = Dispatchers.IO)

    suspend fun getAssets(): List<Asset> {

        return cryptoAPI.getAllCryptoAssets().map { apiAsset ->

            apiAsset.asAsset(checkIfFavorite(name = apiAsset.name))

        }
    }

    suspend fun getAsset(name : String) : Asset? {

        return getAssets().find { it.name == name }
    }

//    suspend fun getAsset(coin : String): Asset {
//        return cryptoAPI.getAsset(coin).asAsset(
//            isFavorite = checkIfFavorite(name = coin)
//        )
//    }

//    suspend fun getPriceForAsset(coin : String): Double {
//
//        return cryptoAPI.getPriceForAsset(
//            coin = coin,
//            curr = "eur").values.first().eur
//
//    }

    private suspend fun checkIfFavorite(name: String): Boolean {

        if (getAllFavoriteAssets().find { favoriteAsset ->
                favoriteAsset.name == name
            } != null) {
            return true
        }
        return false
    }

    private suspend fun getAllFavoriteAssets(): List<DbFavoriteAsset> {

        return database.favoriteAssetDao().getAllFavoriteAssets()
    }

    suspend fun changeFavorites(coin: String, clicked: Boolean): List<Asset> {

        repositoryScope.launch {

            if (clicked) {
                database.favoriteAssetDao().insertFavoriteAsset(DbFavoriteAsset(name = coin))

            } else {
                database.favoriteAssetDao().deleteFavoriteAsset(coin)
            }
        }
        return getAssets()
    }
}

