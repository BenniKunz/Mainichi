package com.example.mainichi.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DbArticle::class, DbAsset::class, DbFavoriteAsset::class],
    exportSchema = true,
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun assetDao(): AssetDao
    abstract fun favoriteAssetDao() : FavoriteAssetDao
}