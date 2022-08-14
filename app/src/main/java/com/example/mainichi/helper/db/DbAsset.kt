package com.example.mainichi.helper.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mainichi.helper.api.crypto.APIAsset
import com.example.mainichi.helper.api.crypto.ROI

//@Entity(tableName = "favoriteAssets")
//data class DbFavoriteAsset(
//    @PrimaryKey val name: String,
//    val favorite: Boolean
//)

@Entity(tableName = "assets")
data class DbAsset(
    val ath: Double,
    val athChangePercentage: Double,
    val athDate: String,
    val atl: Double,
    val atlChangePercentage: Double,
    val atlDate: String,
    val circulatingSupply: Double,
    val currentPrice: Double,
    val fullyDilutedValuation: Long?,
    val high24h: Double,
    val id: String,
    val image: String,
    val lastUpdated: String,
    val low24h: Double,
    val marketCap: Long,
    val marketCapChange24h: Float,
    val marketCapChangePercentage24h: Double,
    val marketCapRank: Int,
    val maxSupply: Double?,
    @PrimaryKey val name: String,
    val priceChange24h: Double,
    val priceChangePercentage24h: Double,
    val priceChangePercentage24hInCurrency: Double,
    val roiCurrency: String,
    val roiPercentage: Double,
    val roiTimes: Double,
    val symbol: String,
    val totalSupply: Double?,
    val totalVolume: Double,

    var favorite: Boolean = false
)

fun DbAsset.toAPIAsset() = APIAsset(
    ath = this.ath,
    athChangePercentage = this.athChangePercentage,
    athDate = this.athDate,
    atl = this.atl,
    atlChangePercentage = this.atlChangePercentage,
    atlDate = this.atlDate,
    circulatingSupply = this.circulatingSupply,
    currentPrice = this.currentPrice,
    fullyDilutedValuation = this.fullyDilutedValuation,
    high24h = this.high24h,
    id = this.id,
    image = this.image,
    lastUpdated = this.lastUpdated,
    low24h = this.low24h,
    marketCap = this.marketCap,
    marketCapChange24h = this.marketCapChange24h,
    marketCapChangePercentage24h = this.marketCapChangePercentage24h,
    marketCapRank = this.marketCapRank, maxSupply = this.maxSupply,
    name = this.name,
    priceChange24h = this.priceChange24h,
    priceChangePercentage24h = this.priceChangePercentage24h,
    priceChangePercentage24hInCurrency = this.priceChangePercentage24hInCurrency,
    roi = ROI(this.roiCurrency, this.roiPercentage, this.roiTimes),
    symbol = this.symbol,
    totalSupply = this.totalSupply,
    totalVolume = this.totalVolume
)