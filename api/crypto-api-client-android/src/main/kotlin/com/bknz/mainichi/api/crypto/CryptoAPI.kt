package com.bknz.mainichi.api.crypto

import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoAPI {

    //Coin Gecko
    @GET("api/v3/coins/markets")
    suspend fun getAssets(
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") ids : String = "",
        @Query("per_page") count: Int = 25,
        @Query("page") page: Int = 1,
        @Query("price_change_percentage") priceChangePercentage: String = "24h"
    ): List<APIAsset>

//    @GET("api/v3/coins/{coin}")
//    suspend fun getAsset(
//        @Path("coin") coin : String,
//        @Query("localization") localization: Boolean = false,
//        @Query("tickers") tickers: Boolean = false,
//        @Query("market_data") marketData: Boolean = true,
//        @Query("community_data") communityData: Boolean = false,
//        @Query("developer_data") developerData: Boolean = false,
//        @Query("sparkline") sparkline: Boolean = false,
//    ) : SingleAsset

//    @GET("api/v3/simple/price")
//    suspend fun getPriceForAsset(
//        @Query("ids") coin : String,
//        @Query("vs_currencies") curr : String = "eur"
//    ) : Map<String, PriceInfo>
}