package com.bknz.mainichi.api.crypto

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CryptoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request : Request = chain.request()
            .newBuilder()
            .addHeader(name = "X-CoinAPI-Key", value = "707EA9F3-019D-479C-900B-C615CBE2023E")
            .build()
        return chain.proceed(request)
    }
}
//API Key: 707EA9F3-019D-479C-900B-C615CBE2023E    E7B4D551-2C8A-4FED-A3D5-C2DB80084661