package com.example.mainichi.helper

import com.example.mainichi.helper.api.crypto.APIAsset
import com.example.mainichi.helper.api.crypto.ROI
import com.example.mainichi.ui.entities.Asset
import com.squareup.moshi.Json


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
        atl = 0.5
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
        atl = 0.5
    )

)