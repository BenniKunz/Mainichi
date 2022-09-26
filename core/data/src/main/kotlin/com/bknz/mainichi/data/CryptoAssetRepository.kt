package com.bknz.mainichi.data

import com.bknz.mainichi.api.crypto.CryptoAPI
import com.bknz.mainichi.api.crypto.asAsset
import com.bknz.mainichi.core.model.Asset
import com.bknz.mainichi.data.database.DbFavoriteAsset
import com.bknz.mainichi.data.database.FavoriteAssetDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
internal class CryptoAssetRepository @Inject constructor(
    private val cryptoAPI: CryptoAPI,
    private val favoriteAssetDao: FavoriteAssetDao
) : AssetRepository {
    private val repositoryScope = CoroutineScope(context = Dispatchers.IO)

    override suspend fun getAssets(): List<Asset> {

        return cryptoAPI.getAllCryptoAssets().map { apiAsset ->

            apiAsset.asAsset()

        }
    }

    override fun observeAssets() = flow {
        while(true) {
            emit(getAssets())
            delay(10.seconds)
        }
    }

    override suspend fun getAsset(name : String) : Asset? {

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

    override fun observeFavoriteAssets(): Flow<List<DbFavoriteAsset>> {

        return favoriteAssetDao.observeFavoriteAssets()
    }

    override suspend fun changeFavorites(coin: String, clicked: Boolean) {

        repositoryScope.launch {

            withContext(Dispatchers.IO) {
                if (clicked) {
                    favoriteAssetDao.insertFavoriteAsset(DbFavoriteAsset(name = coin))

                } else {
                    favoriteAssetDao.deleteFavoriteAsset(coin)
                }
            }
        }
    }
}

