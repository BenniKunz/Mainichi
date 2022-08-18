package bknz.example.mainichi.helper.api.crypto


import androidx.compose.runtime.Composable
import com.example.mainichi.helper.db.DbAsset
import com.example.mainichi.ui.uiElements.Asset
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@JsonClass(generateAdapter = true)
//data class APIAsset(
//    @Json(name = "ath")
//    val ath: Double,
//    @Json(name = "ath_change_percentage")
//    val athChangePercentage: Double,
//    @Json(name = "ath_date")
//    val athDate: String,
//    @Json(name = "atl")
//    val atl: Double,
//    @Json(name = "atl_change_percentage")
//    val atlChangePercentage: Double,
//    @Json(name = "atl_date")
//    val atlDate: String,
//    @Json(name = "circulating_supply")
//    val circulatingSupply: Double,
//    @Json(name = "current_price")
//    val currentPrice: Double,
//    @Json(name = "fully_diluted_valuation")
//    val fullyDilutedValuation: Long?,
//    @Json(name = "high_24h")
//    val high24h: Double,
//    @Json(name = "id")
//    val id: String,
//    @Json(name = "image")
//    val image: String,
//    @Json(name = "last_updated")
//    val lastUpdated: String,
//    @Json(name = "low_24h")
//    val low24h: Double,
//    @Json(name = "market_cap")
//    val marketCap: Long,
//    @Json(name = "market_cap_change_24h")
//    val marketCapChange24h: Float,
//    @Json(name = "market_cap_change_percentage_24h")
//    val marketCapChangePercentage24h: Double,
//    @Json(name = "market_cap_rank")
//    val marketCapRank: Int,
//    @Json(name = "max_supply")
//    val maxSupply: Double?,
//    @Json(name = "name")
//    val name: String,
//    @Json(name = "price_change_24h")
//    val priceChange24h: Double,
//    @Json(name = "price_change_percentage_24h")
//    val priceChangePercentage24h: Double,
//    @Json(name = "price_change_percentage_24h_in_currency")
//    val priceChangePercentage24hInCurrency: Double,
//    @Json(name = "roi")
//    val roi: ROI? = ROI("",0.0,0.0),
//    @Json(name = "symbol")
//    val symbol: String,
//    @Json(name = "total_supply")
//    val totalSupply: Double?,
//    @Json(name = "total_volume")
//    val totalVolume: Double,
//
//    var favorite : Boolean = false
//)

@Serializable
data class APIAsset(
    val ath: Double,
    val ath_change_percentage: Double? = null,
    val ath_date: String,
    val atl: Double,
    val atl_change_percentage: Double,
    val atl_date: String,
    val circulating_supply: Double,
    val current_price: Double,
    val fully_diluted_valuation: Long?,
    val high_24h: Double,
    val id: String,
    val image: String,
    val last_updated: String,
    val low_24h: Double,
    val market_cap: Long,
    val market_cap_change_24h: Float,
    val market_cap_change_percentage_24h: Double,
    val market_cap_rank: Int,
    val max_supply: Double? = null,
    val name: String,
    val price_change_24h: Double,
    val price_change_percentage_24h: Double,
    val price_change_percentage_24h_in_currency: Double,
    val roi: ROI? = ROI("",0.0,0.0),
    val symbol: String,
    val total_supply: Double?,
    val total_volume: Double,
)

fun APIAsset.asAsset(isFavorite : Boolean = false) = Asset(
    name = this.name,
    currentPrice = this.current_price,
    image = this.image,
    symbol = this.symbol,
    isFavorite = isFavorite,
    high24h = this.high_24h,
    low24h = this.low_24h,
    marketCap = this.market_cap,
    ath = this.ath,
    atl = this.atl
)