package com.example.mainichi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        DbArticle::class,
        DbAsset::class,
        DbFavoriteAsset::class,
        DbNotification::class],
    exportSchema = true,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
    abstract fun assetDao(): AssetDao
    abstract fun favoriteAssetDao(): FavoriteAssetDao
    abstract fun notificationsDao(): NotificationsDao
}