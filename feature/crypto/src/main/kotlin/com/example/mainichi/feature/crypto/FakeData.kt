package com.example.mainichi.feature.crypto

import com.example.mainichi.core.model.Asset


val fakeAssets = listOf(
    Asset(
        name = "Bitcoin",
        currentPrice = 20000.0,
        image = "",
        symbol = "BTC",
        isFavorite = true,
        high24h = 21000.5,
        low24h = 19000.0,
        marketCap = 3000000,
        ath = 60000.0,
        atl = 0.5,
        isSelected = false
    ),
    Asset(
        name = "Ethereum",
        currentPrice = 2005.3,
        image = "",
        symbol = "ETH",
        isFavorite = true,
        high24h = 2100.5,
        low24h = 1840.0,
        marketCap = 3000000,
        ath = 4800.4,
        atl = 0.5,
        isSelected = false
    )

)