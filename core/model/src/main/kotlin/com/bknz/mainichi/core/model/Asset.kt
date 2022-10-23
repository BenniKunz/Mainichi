package com.bknz.mainichi.core.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class Asset(
    val id: String,
    val name: String,
    val currentPrice: Double = 0.0,
    val image: String,
    val symbol: String,
    val isFavorite: Flow<Boolean> = flowOf(false),
    val high24h: Double = 0.0,
    val low24h: Double = 0.0,
    val marketCap: Long = 0,
    val ath: Double = 0.0,
    val atl: Double = 0.0,
    val isSelected: Boolean = false
)
