package com.bknz.mainichi.data

import androidx.paging.PagingData
import com.bknz.mainichi.core.model.Asset
import com.bknz.mainichi.data.database.model.DbFavoriteAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AssetRepository {

    suspend fun getAsset(id: String): Asset

    fun observeFavoriteAssets(): Flow<List<DbFavoriteAsset>>

    suspend fun changeFavorites(coin: String, clicked: Boolean)
}