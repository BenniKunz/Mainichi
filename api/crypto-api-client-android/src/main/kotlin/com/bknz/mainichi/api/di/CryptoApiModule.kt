package com.bknz.mainichi.api.di

import com.bknz.mainichi.api.crypto.CryptoAPI
import com.bknz.mainichi.api.news.NewsAPI
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoApiModule {

    private const val BASE_URL_COIN_GECKO = "https://api.coingecko.com/"
    private const val BASE_URL_NEWS = "https://newsapi.org/v2/"

    @Provides
    @Singleton
    internal fun provideOkHttpBuilder(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    @Provides
    @Singleton
    internal fun provideJson() = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    internal fun provideJsonConverterFactory(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    @APICrypto
    internal fun retrofitBuilderCrypto(
        converter: Converter.Factory, client: OkHttpClient
    ): Retrofit.Builder =
        Retrofit.Builder().baseUrl(BASE_URL_COIN_GECKO)
            .addConverterFactory(converter)
            .client(client)

    @Provides
    @Singleton
    @APICrypto
    internal fun provideRetrofitCrypto(@APICrypto builder: Retrofit.Builder): Retrofit =
        builder.build()


    @Provides
    @Singleton
    fun provideCryptoAPI(@APICrypto retrofit: Retrofit) = retrofit.create<CryptoAPI>()

    @Provides
    @Singleton
    @APINews
    fun retrofitBuilderNews(
        converter: Converter.Factory,
        client: OkHttpClient
    ): Retrofit.Builder =
        Retrofit.Builder().baseUrl(BASE_URL_NEWS)
            .addConverterFactory(converter)
            .client(client)

    @Provides
    @Singleton
    @APINews
    fun provideRetrofitNews(@APINews builder: Retrofit.Builder): Retrofit =
        builder.build()

    @Provides
    @Singleton
    fun provideNewsAPI(@APINews retrofit: Retrofit): NewsAPI = retrofit.create()

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class APINews

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class APICrypto