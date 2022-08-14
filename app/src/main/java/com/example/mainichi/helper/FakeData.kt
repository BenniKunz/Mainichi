package com.example.mainichi.helper

import com.example.mainichi.helper.api.crypto.APIAsset
import com.example.mainichi.helper.api.crypto.ROI
import com.squareup.moshi.Json

val fakeAPIAsset = APIAsset(
    ath = 60000.0,
    athChangePercentage = 10.0,
    athDate = "03/10/2014",
    atl = 0.02,
    atlChangePercentage = 10.0,
    atlDate ="01/ß1/2011",
    circulatingSupply = 200000.3,
    currentPrice = 200000.3567,
    fullyDilutedValuation = 10000L,
    high24h = 21.000,
    id = "Bitcoin",
    image = "",
    lastUpdated ="",
    low24h = 20031.232,
    marketCap =800000000L,
    marketCapChange24h = 10.23F,
    marketCapChangePercentage24h = 20.03,
    marketCapRank = 1,
    maxSupply = 20.034,
    name =  "Bitcoin",
    priceChange24h = 13.032,
    priceChangePercentage24h =  10.03,
    priceChangePercentage24hInCurrency =  2000.02,
    roi =  ROI("",0.0,0.0),
    symbol = "",
    totalSupply = 3000000.9,
    totalVolume = 4000000.232,

    favorite = false
)
