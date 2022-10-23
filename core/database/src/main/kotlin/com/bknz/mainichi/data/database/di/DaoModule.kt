package com.bknz.mainichi.data.database.di

import com.bknz.mainichi.data.database.dao.ArticleDao
import com.bknz.mainichi.data.database.AppDatabase
import com.bknz.mainichi.data.database.AssetDao
import com.bknz.mainichi.data.database.dao.FavoriteAssetDao
import com.bknz.mainichi.data.database.dao.NotificationsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun provideAssetDao(database: AppDatabase): AssetDao = database.assetDao()

    @Provides
    @Singleton
    fun provideFavoriteAssetDao(database: AppDatabase): FavoriteAssetDao = database.favoriteAssetDao()

    @Provides
    @Singleton
    fun provideNotificationsAssetDao(database: AppDatabase): NotificationsDao = database.notificationsDao()

    @Provides
    @Singleton
    fun provideArticleAssetDao(database: AppDatabase): ArticleDao = database.articleDao()

}