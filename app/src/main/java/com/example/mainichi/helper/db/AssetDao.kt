package com.example.mainichi.helper.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: DbAsset)

    @Update
    suspend fun updateAsset(asset: DbAsset)

    @Query("SELECT * FROM assets")
    fun getAllAssets(): List<DbAsset>

    @Query("SELECT * FROM assets")
    fun observeAssets(): Flow<List<DbAsset>>

    @Query("SELECT * FROM assets WHERE favorite = :isFavorite")
    suspend fun getAllFavorites(isFavorite: Boolean): List<DbAsset>

    @Query("SELECT * FROM assets WHERE name = :coin")
    suspend fun getAsset(coin : String) : DbAsset

    @Query("UPDATE assets set favorite = :isFavorite WHERE name = :name")
    suspend fun updateAsset(name: String, isFavorite : Boolean) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(asset:List<DbAsset>)

    @Query("DELETE FROM assets")
    suspend fun deleteAll()

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertFavoriteAsset(asset: DbFavoriteAsset)
//
//    @Update
//    suspend fun updateAsset(asset: DbFavoriteAsset)
//
//    @Query("SELECT * FROM favoriteAssets")
//    suspend fun getAllFavorites(): List<DbFavoriteAsset>
//
//    @Query("SELECT * FROM favoriteAssets")
//    suspend fun getAllFavorites(): List<DbFavoriteAsset>
//
//    @Delete
//    suspend fun deleteAsset(asset : DbFavoriteAsset)
//
//    @Query("DELETE FROM favoriteAssets")
//    suspend fun deleteAllFavoriteAssets()
//
//    @Query("select count(*) from favoriteAssets")
//    suspend fun count() : Long
}