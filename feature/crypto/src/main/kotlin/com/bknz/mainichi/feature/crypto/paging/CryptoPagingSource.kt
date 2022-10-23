package com.bknz.mainichi.feature.crypto.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bknz.mainichi.api.crypto.CryptoAPI
import com.bknz.mainichi.api.crypto.asAsset
import com.bknz.mainichi.core.model.Asset
import com.bknz.mainichi.data.database.dao.FavoriteAssetDao
import java.io.IOException
import java.util.Locale

class CryptoPagingSource constructor(
    private val cryptoAPI: CryptoAPI,
    private val filter: String? = null,
    private val favoriteAssetDao: FavoriteAssetDao
) : PagingSource<Int, Asset>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Asset> {

        return try {
            val page = params.key ?: 1

            val limit = params.loadSize

            val response = cryptoAPI.getAssets(count = limit, page = page)

            //TODO API Filter -> let user choose asset
            val filtered = response.filter {
                if (filter == null) {
                    true
                } else {
                    it.name.lowercase(Locale.getDefault())
                        .contains(filter.lowercase(Locale.getDefault()))
                }
            }.map { apiAsset ->
                apiAsset.asAsset(
                    isFavorite = favoriteAssetDao.observeFavoriteAssetsStatus(
                        assetName = apiAsset.name
                    )
                )
            }

            return LoadResult.Page(
                data = filtered,
                prevKey = null,
                nextKey = if (response.isNotEmpty()) {
                    page + 1
                } else {
                    null
                }
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Asset>): Int? {
        TODO("Not yet implemented")
    }
}