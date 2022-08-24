package com.example.mainichi.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAssetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteAsset(asset: DbFavoriteAsset)

    @Query("DELETE FROM favoriteAssets WHERE name = :name")
    suspend fun deleteFavoriteAsset(name: String)

    @Query("SELECT * FROM favoriteAssets")
    fun observeFavoriteAssets() : Flow<List<DbFavoriteAsset>>
}