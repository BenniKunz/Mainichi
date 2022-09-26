package com.bknz.mainichi.data.di

import com.bknz.mainichi.data.*
import com.bknz.mainichi.data.ArticleRepository
import com.bknz.mainichi.data.CryptoAssetRepository
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

    @Binds
    fun bindUserRepository(userRepository: FirebaseUserRepository) : UserRepository

}