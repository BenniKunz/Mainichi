package com.example.mainichi.helper.repository

import androidx.compose.runtime.Composable
import com.example.mainichi.helper.api.crypto.APIAsset
import com.example.mainichi.helper.api.crypto.CryptoAPI
import com.example.mainichi.helper.db.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepository @Inject constructor(
    private val api: CryptoAPI,
    private val database: AppDatabase
) {
    private val repositoryScope = CoroutineScope(context = Dispatchers.IO)

    suspend fun getAllFavoriteAssets(): List<DbFavoriteAsset> {

        return database.favoriteAssetDao().getAllFavoriteAssets()
    }

    fun getAllAssets(): Flow<List<APIAsset>> {

        repositoryScope.launch {

            refresh()
        }

        return database.assetDao().observeAssets().map { it.map { dbAsset -> dbAsset.toAPIAsset() } }
    }

    private fun refresh() {

        val assets = database.assetDao().getAllAssets()

//        if(assets.is)

    }
}

