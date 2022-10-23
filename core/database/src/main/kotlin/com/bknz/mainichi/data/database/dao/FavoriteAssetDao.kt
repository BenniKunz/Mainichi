package com.bknz.mainichi.data.database.dao

import androidx.room.*
import com.bknz.mainichi.data.database.model.DbFavoriteAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAssetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteAsset(asset: DbFavoriteAsset)

    @Query("DELETE FROM favoriteAssets WHERE name = :name")
    suspend fun deleteFavoriteAsset(name: String)

    @Query("SELECT * FROM favoriteAssets")
    fun observeFavoriteAssets() : Flow<List<DbFavoriteAsset>>

    @Query("SELECT isFavorite FROM favoriteAssets WHERE name = :assetName")
    fun observeFavoriteAssetsStatus(assetName : String) : Flow<Boolean>
}