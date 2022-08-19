package com.example.mainichi.ui.entities

data class Asset(
    val name : String,
    val currentPrice: Double,
    val image : String,
    val symbol: String,
    val isFavorite: Boolean,
    val high24h: Double,
    val low24h: Double,
    val marketCap: Long,
    val ath: Double,
    val atl: Double
)
