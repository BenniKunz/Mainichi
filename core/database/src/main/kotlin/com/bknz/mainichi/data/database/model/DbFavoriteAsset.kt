package com.bknz.mainichi.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteAssets")
data class DbFavoriteAsset(
    @PrimaryKey val name: String,
    val isFavorite: Boolean
)