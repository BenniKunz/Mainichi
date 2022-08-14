package com.example.mainichi.helper.api.crypto

import retrofit2.http.GET

public interface CryptoAPI {

    //Coin API
//    @GET("v1/assets")
//    suspend fun getAllCryptoAssets() : List<CryptoAsset>
//
//    @GET("/v1/assets?filter_asset_id=BTC;ETH;ADA;SOL;XRP;USDT;DOGE;SHIB")
//    suspend fun getSpecificCryptoAssets() : List<CryptoAsset>
//
//    @GET("v1/assets/icons/200")
//    suspend fun getAssetIcons() : List<CryptoIcon>


    //Coin Gecko

    @GET("api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false&price_change_percentage=24h")
    suspend fun getAllCryptoAssets() : List<APIAsset>

}