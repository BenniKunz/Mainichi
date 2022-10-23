package com.bknz.mainichi.api.crypto
import com.bknz.mainichi.core.model.Asset
import kotlinx.coroutines.flow.Flow


@kotlinx.serialization.Serializable
data class APIAsset(
    val ath: Double,
    val ath_change_percentage: Double? = null,
    val ath_date: String,
    val atl: Double,
    val atl_change_percentage: Double,
    val atl_date: String,
    val circulating_supply: Double,
    val current_price: Double,
    val fully_diluted_valuation: Double?,
    val high_24h: Double?,
    val id: String,
    val image: String,
    val last_updated: String,
    val low_24h: Double?,
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

fun APIAsset.asAsset(isFavorite: Flow<Boolean>) = Asset(
    id = this.id,
    name = this.name,
    currentPrice = this.current_price,
    image = this.image,
    symbol = this.symbol,
    isFavorite = isFavorite,
    high24h = this.high_24h ?: 0.0,
    low24h = this.low_24h ?: 0.0,
    marketCap = this.market_cap,
    ath = this.ath,
    atl = this.atl,
    isSelected = false
)