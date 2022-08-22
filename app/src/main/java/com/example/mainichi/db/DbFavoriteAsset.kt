package com.example.mainichi.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteAssets")
data class DbFavoriteAsset(
    @PrimaryKey val name : String)

