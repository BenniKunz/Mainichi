package com.example.mainichi.data

import com.example.mainichi.core.model.Asset
import com.example.mainichi.data.database.DbFavoriteAsset
import kotlinx.coroutines.flow.Flow

interface AssetRepository {

    suspend fun getAssets(): List<Asset>

    suspend fun getAsset(name: String): Asset?

    fun observeAssets(): Flow<List<Asset>>

    fun observeFavoriteAssets(): Flow<List<DbFavoriteAsset>>

    suspend fun changeFavorites(coin: String, clicked: Boolean)
}