package com.example.mainichi.helper.api.crypto


import com.example.mainichi.helper.db.DbAsset
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class APIAsset(
    @Json(name = "ath")
    val ath: Double,
    @Json(name = "ath_change_percentage")
    val athChangePercentage: Double,
    @Json(name = "ath_date")
    val athDate: String,
    @Json(name = "atl")
    val atl: Double,
    @Json(name = "atl_change_percentage")
    val atlChangePercentage: Double,
    @Json(name = "atl_date")
    val atlDate: String,
    @Json(name = "circulating_supply")
    val circulatingSupply: Double,
    @Json(name = "current_price")
    val currentPrice: Double,
    @Json(name = "fully_diluted_valuation")
    val fullyDilutedValuation: Long?,
    @Json(name = "high_24h")
    val high24h: Double,
    @Json(name = "id")
    val id: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "last_updated")
    val lastUpdated: String,
    @Json(name = "low_24h")
    val low24h: Double,
    @Json(name = "market_cap")
    val marketCap: Long,
    @Json(name = "market_cap_change_24h")
    val marketCapChange24h: Float,
    @Json(name = "market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double,
    @Json(name = "market_cap_rank")
    val marketCapRank: Int,
    @Json(name = "max_supply")
    val maxSupply: Double?,
    @Json(name = "name")
    val name: String,
    @Json(name = "price_change_24h")
    val priceChange24h: Double,
    @Json(name = "price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @Json(name = "price_change_percentage_24h_in_currency")
    val priceChangePercentage24hInCurrency: Double,
    @Json(name = "roi")
    val roi: ROI? = ROI("",0.0,0.0),
    @Json(name = "symbol")
    val symbol: String,
    @Json(name = "total_supply")
    val totalSupply: Double?,
    @Json(name = "total_volume")
    val totalVolume: Double,

    var favorite : Boolean = false
)

fun APIAsset.toDbAsset() = DbAsset(
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
    marketCapRank = this.marketCapRank,
    maxSupply = this.maxSupply,
    name = this.name,
    priceChange24h = this.priceChange24h,
    priceChangePercentage24h = this.priceChangePercentage24h,
    priceChangePercentage24hInCurrency = this.priceChangePercentage24hInCurrency,
    roiCurrency = roi?.currency ?: "",
    roiPercentage = roi?.percentage ?: 0.0,
    roiTimes = roi?.times ?: 0.0,
    symbol = this.symbol,
    totalSupply = this.totalSupply,
    totalVolume = this.totalVolume
)