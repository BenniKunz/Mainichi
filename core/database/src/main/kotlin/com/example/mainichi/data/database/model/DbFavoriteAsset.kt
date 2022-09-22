package com.example.mainichi.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteAssets")
data class DbFavoriteAsset(
    @PrimaryKey val name : String)

