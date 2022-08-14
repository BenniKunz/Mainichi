package com.example.mainichi.helper.db

import androidx.room.*

@Dao
interface FavoriteAssetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteAsset(asset: DbFavoriteAsset)

    @Query("DELETE FROM favoriteAssets WHERE name = :name")
    suspend fun deleteFavoriteAsset(name: String)

    @Query("SELECT * FROM favoriteAssets")
    suspend fun getAllFavoriteAssets() : List<DbFavoriteAsset>
}