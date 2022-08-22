package com.example.mainichi

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import androidx.room.Room
import androidx.work.WorkManager
import com.example.mainichi.api.APIConstants
import com.example.mainichi.api.crypto.CryptoAPI
import com.example.mainichi.api.news.NewsAPI
import com.example.mainichi.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
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
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpBuilder(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()


    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext, AppDatabase::class.java,
            "database.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideJson() = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJsonConverterFactory(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    @APICrypto
    fun retrofitBuilderCrypto(
        converter: Converter.Factory,
        client: OkHttpClient
    ): Retrofit.Builder =
        Retrofit.Builder().baseUrl(APIConstants.BASE_URL_COIN_GECKO)
            .addConverterFactory(converter)
            .client(client)

    @Provides
    @Singleton
    @APINews
    fun retrofitBuilderNews(
        converter: Converter.Factory,
        client: OkHttpClient): Retrofit.Builder =
        Retrofit.Builder().baseUrl(APIConstants.BASE_URL_NEWS)
            .addConverterFactory(converter)
            .client(client)

    @Provides
    @Singleton
    @APICrypto
    fun provideRetrofitCrypto(@APICrypto builder: Retrofit.Builder): Retrofit =
        builder.build()

    @Provides
    @Singleton
    @APINews
    fun provideRetrofitNews(@APINews builder: Retrofit.Builder): Retrofit =
        builder.build()

    @Provides
    @Singleton
    fun provideCryptoAPI(@APICrypto retrofit: Retrofit): CryptoAPI = retrofit.create()

    @Provides
    @Singleton
    fun provideNewsAPI(@APINews retrofit: Retrofit): NewsAPI = retrofit.create()

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class APINews

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class APICrypto