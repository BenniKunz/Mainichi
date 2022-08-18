package com.example.mainichi.helper.api.crypto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface CryptoAPI {

    //Coin Gecko
    @GET("api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false&price_change_percentage=24h")
    suspend fun getAllCryptoAssets() : List<APIAsset>

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

    @GET("api/v3/simple/price")
    suspend fun getPriceForAsset(
        @Query("ids") coin : String,
        @Query("vs_currencies") curr : String = "eur"
    ) : Map<String, PriceInfo>
}