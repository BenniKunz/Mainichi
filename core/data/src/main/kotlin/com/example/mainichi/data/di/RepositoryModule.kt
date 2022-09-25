package com.example.mainichi.data.di

import com.example.mainichi.data.ArticleRepository
import com.example.mainichi.data.AssetRepository
import com.example.mainichi.data.CryptoAssetRepository
import com.example.mainichi.data.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindAssetRepository(assetRepository: CryptoAssetRepository) : AssetRepository

    @Binds
    fun bindArticleRepository(newsRepository: ArticleRepository) : NewsRepository

}