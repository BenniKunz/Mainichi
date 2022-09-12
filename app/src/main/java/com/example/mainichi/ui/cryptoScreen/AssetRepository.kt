package com.example.mainichi.ui.cryptoScreen

import com.example.mainichi.api.crypto.CryptoAPI
import com.example.mainichi.api.crypto.asAsset
import com.example.mainichi.data.database.AppDatabase
import com.example.mainichi.data.database.DbFavoriteAsset
import com.example.mainichi.ui.entities.Asset
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class AssetRepository @Inject constructor(
    private val cryptoAPI: CryptoAPI,
    private val database: AppDatabase
) {
    private val repositoryScope = CoroutineScope(context = Dispatchers.IO)

    suspend fun getAssets(): List<Asset> {

        return cryptoAPI.getAllCryptoAssets().map { apiAsset ->

            apiAsset.asAsset()

        }
    }

    fun observeAssets() = flow {
        while(true) {
            emit(getAssets())
            delay(10.seconds)
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

//    private suspend fun checkIfFavorite(name: String): Boolean {
//
//        if (getAllFavoriteAssets().find { favoriteAsset ->
//                favoriteAsset.name == name
//            } != null) {
//            return true
//        }
//        return false
//    }

//    private suspend fun getAllFavoriteAssets(): List<DbFavoriteAsset> {
//
//        return database.favoriteAssetDao().getAllFavoriteAssets()
//    }

    fun observeFavoriteAssets(): Flow<List<DbFavoriteAsset>> {

        return database.favoriteAssetDao().observeFavoriteAssets()
    }

    suspend fun changeFavorites(coin: String, clicked: Boolean) {

        repositoryScope.launch {

            withContext(Dispatchers.IO) {
                if (clicked) {
                    database.favoriteAssetDao().insertFavoriteAsset(DbFavoriteAsset(name = coin))

                } else {
                    database.favoriteAssetDao().deleteFavoriteAsset(coin)
                }
            }
        }
    }
}

