package com.bknz.mainichi.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bknz.mainichi.data.database.dao.ArticleDao
import com.bknz.mainichi.data.database.dao.NotificationsDao
import com.bknz.mainichi.data.database.model.DbArticle
import com.bknz.mainichi.data.database.model.DbNotification

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