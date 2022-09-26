package com.bknz.mainichi.core.model

data class Asset(
    val name : String,
    val currentPrice: Double = 0.0,
    val image : String,
    val symbol: String,
    val isFavorite: Boolean = false,
    val high24h: Double = 0.0,
    val low24h: Double = 0.0,
    val marketCap: Long = 0,
    val ath: Double = 0.0,
    val atl: Double = 0.0,
    val isSelected : Boolean = false
)
