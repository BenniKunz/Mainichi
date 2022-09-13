package com.example.mainichi.data.di

import com.example.mainichi.data.AssetRepository
import com.example.mainichi.data.CryptoAssetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindAssetRepository(assetRepository: CryptoAssetRepository) : AssetRepository

}