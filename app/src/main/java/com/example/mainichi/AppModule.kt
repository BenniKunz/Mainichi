package com.example.mainichi

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.room.Room
import androidx.work.WorkManager
import com.example.mainichi.helper.api.APIConstants
import com.example.mainichi.helper.api.crypto.CryptoAPI
import com.example.mainichi.helper.api.news.NewsAPI
import com.example.mainichi.helper.db.AppDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext appContext: Context): AppDatabase = Room.databaseBuilder(
        appContext, AppDatabase::class.java,
        "database.db"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory =
        MoshiConverterFactory.create(
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    )

    @Provides
    @Singleton
    @APICrypto
    fun retrofitBuilderCrypto(converter: Converter.Factory): Retrofit.Builder =
        Retrofit.Builder().baseUrl(APIConstants.BASE_URL_COIN_GECKO)
            .addConverterFactory(converter)

    @Provides
    @Singleton
    @APINews
    fun retrofitBuilderNews(converter: Converter.Factory): Retrofit.Builder =
        Retrofit.Builder().baseUrl(APIConstants.BASE_URL_NEWS)
            .addConverterFactory(converter)

    @Provides
    @Singleton
    @APICrypto
    fun provideRetrofitCrypto(@APICrypto builder : Retrofit.Builder) : Retrofit =
        builder.build()

    @Provides
    @Singleton
    @APINews
    fun provideRetrofitNews(@APINews builder : Retrofit.Builder) : Retrofit =
        builder.build()

    @Provides
    @Singleton
    fun provideCryptoAPI(@APICrypto retrofit: Retrofit) : CryptoAPI = retrofit.create()

    @Provides
    @Singleton
    fun provideNewsAPI(@APINews retrofit: Retrofit) : NewsAPI = retrofit.create()

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