package com.example.mainichi.data.database

import androidx.room.*
import com.example.mainichi.data.database.DbFavoriteAsset
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