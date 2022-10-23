package com.bknz.mainichi.data

import android.util.Log
import com.bknz.mainichi.api.crypto.CryptoAPI
import com.bknz.mainichi.api.crypto.asAsset
import com.bknz.mainichi.core.model.Asset
import com.bknz.mainichi.data.database.model.DbFavoriteAsset
import com.bknz.mainichi.data.database.dao.FavoriteAssetDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CryptoAssetRepository @Inject constructor(
    private val cryptoAPI: CryptoAPI,
    private val favoriteAssetDao: FavoriteAssetDao
) : AssetRepository {
    private val repositoryScope = CoroutineScope(context = Dispatchers.IO)

    override suspend fun getAsset(id: String): Asset {

        Log.d("API Test", "handle for API: $id")
        return cryptoAPI.getAssets(
            ids = id
        ).first().asAsset(
            isFavorite = favoriteAssetDao.observeFavoriteAssetsStatus(assetName = id)
        )
    }

    override fun observeFavoriteAssets(): Flow<List<DbFavoriteAsset>> {

        return favoriteAssetDao.observeFavoriteAssets().map { dbFavoriteAssets ->
            dbFavoriteAssets.map { dbFavoriteAsset ->
                dbFavoriteAsset
            }
        }
    }

    override suspend fun changeFavorites(coin: String, clicked: Boolean) {

        repositoryScope.launch {

            withContext(Dispatchers.IO) {
                if (clicked) {
                    favoriteAssetDao.insertFavoriteAsset(
                        DbFavoriteAsset(
                            name = coin,
                            isFavorite = true
                        )
                    )

                } else {
                    favoriteAssetDao.deleteFavoriteAsset(coin)
                }
            }
        }
    }
}

